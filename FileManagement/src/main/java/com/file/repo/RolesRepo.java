package com.file.repo;


import org.springframework.data.jpa.repository.JpaRepository;

import com.file.model.RoleType;
import com.file.model.Roles;

public interface RolesRepo extends JpaRepository<Roles, Long> {
    Roles findByRoleName(RoleType roleName); 
}
