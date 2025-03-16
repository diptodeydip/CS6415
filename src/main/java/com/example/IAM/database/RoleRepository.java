package com.example.IAM.database;

import com.example.IAM.model.AppUser;
import com.example.IAM.model.Product;
import com.example.IAM.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    List<Role> findAll ();

    Optional<Role> findById(Long id);

    Optional<Role> findByName(String name);

}
