# Scope and Objectives of the Project: 

The project involves the design, implementation, security analysis, and hardening of a Spring Security-based IAM system. The project covers the achievement of the following goals on an incremental basis: 

Create a secure IAM system using Spring Security-based authentication and authorization. 

Apply the principle of Least Privileges based on Role-Based Access Control (RBAC) and eliminate unauthorized access. 

Conduct testing for IAM-based vulnerabilities such as privilege escalation, session hijack, and brute-force attack to confirm system vulnerabilities. 

Review auth logs and security events to detect threats and unauthorized access. 

Implement security hardening features such as Multi-Factor Authentication (MFA), account lockout policies, and enhanced password security. 

Re-validate the system after deploying the mitigation measures to measure security improvements. 

  

The project entails a total security audit of the IAM processes involving attack simulation, audit of logs and security rules for authentication and authorization.

#Methodology 


## Design and Deployment of an IAM System  

- Implement a Spring Security IAM system with the following extra features Authentication and Authorization. [1] 

- Define user roles and user permissions based on the Role-Based Access Control (RBAC) standards. [1] [5] 

- Enforce a robust authentication process e.g. Password policy and implement Multi-Factor Authentication (MFA). [1] 

## Simulated Attack Scenarios 

- Conduct security testing via simulation of normal IAM based attack vectors such as brute-force login, session hijacking, and privilege escalation. 

- Monitor login history to detect unauthorized login efforts and vulnerabilities 

## Security Hardening and Policy Enforcement 

- Implement account lockout policies, stronger password policies, and access control lists (ACLs) to mitigate security threats. 

- Secure Authentication through Mandatory Multifactor Authentication for Privileged Accounts. [3] 

## Analysis and Documentation 

- Compare system security before and after the application of security hardening measures Suggest some extra security improvements 

- Provide recommendations for further security improvements.

#System Architecture Diagram 

Below is a high-level system architecture diagram illustrating how authentication, authorization, and security controls interact in the IAM system: 

<img width="630" height="283" alt="image" src="https://github.com/user-attachments/assets/716cca87-ed81-4fad-9c69-e7f9d42f66cb" />

Figure 1: System Architecture of IAM System using Spring Security 

It offers secure authentication workflow, fine-grained access control, and real-time monitoring of security events. 

## Project Flowchart: 
 
<img width="663" height="314" alt="image" src="https://github.com/user-attachments/assets/4c1467cc-6dcb-4bfc-a795-e936e4f71977" />

Figure 2: Flowchart of IAM System using Spring Security 


## Attack Tree Based on Our System: 
 
<img width="614" height="310" alt="image" src="https://github.com/user-attachments/assets/971a0058-aa31-4c96-9d99-472c068fcb03" />

Figure 3: Attack Tree of IAM System 

Entity Relationship Diagram: 

<img width="612" height="420" alt="image" src="https://github.com/user-attachments/assets/5eb03f26-570a-4f64-9853-098087bf2f1c" />

Figure 4: Entity Relationship of the IAM System 

 
