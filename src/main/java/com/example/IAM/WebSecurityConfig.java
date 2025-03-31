package com.example.IAM;

import com.example.IAM.service.CustomUserDetailsService;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests() //authorization check happens in bottom to top manner. matcher with arbitrary scope is placed in bottom.
                .antMatchers(  "/register", "/registerPage", "/css/**", "/favicon.ico").permitAll()
                .antMatchers("/feedback", "/submitFeedback")
                .access("hasAuthority('" + Commons.Provide_Feedback_PERMISSION + "') and hasAuthority('" + Commons.MFA_VERIFIED + "')")
                .antMatchers("/feedbacks")
                .access("hasAuthority('" + Commons.See_Feedback_PERMISSION + "') and hasAuthority('" + Commons.MFA_VERIFIED + "')")
                .antMatchers("/iam/**", "/user-requests", "/role-management")
                .access("hasAuthority('" + Commons.Admin_Panel_PERMISSION + "') and hasAuthority('" + Commons.MFA_VERIFIED + "')")
                .antMatchers("/addProductPage", "/addProduct")
                .access("hasAuthority('" + Commons.ADD_PRODUCT_PERMISSION + "') and hasAuthority('" + Commons.MFA_VERIFIED + "')")
                .antMatchers("/mfa", "/otp").authenticated()
                .antMatchers("/**").hasAuthority(Commons.MFA_VERIFIED)
                .anyRequest().authenticated() //means all the request url should be mfa verified, but some exceptions are mentioned above.
                .and()
                .formLogin()
                .loginPage("/loginPage")
                .loginProcessingUrl("/login")
                .failureUrl("/loginError")
                .defaultSuccessUrl("/mfa", true).permitAll()
                .and()
                .logout()
                .logoutSuccessUrl("/loginPage?logout=true").permitAll()// URL to redirect after logout (optional) .hasAuthority("ROLE_USER") .loginPage("/loginPage").loginProcessingUrl("/login")
                .and()
                .exceptionHandling(exception -> exception
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            // Invalidate session and logout
                            request.getSession().invalidate();
                            response.sendRedirect("/logout"); // Redirect to logout
                        }))
        ;
    }

    @Bean
    DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(customUserDetailsService);
        return daoAuthenticationProvider;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public Cache<String, Integer> caffeineCache() {
        return Caffeine.newBuilder()
                .expireAfterWrite(1, TimeUnit.MINUTES)
                .maximumSize(100)
                .build();
    }
}