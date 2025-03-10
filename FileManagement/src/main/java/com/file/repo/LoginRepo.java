package com.file.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.file.model.Users;

@Repository
public interface LoginRepo extends JpaRepository<Users, Long> {
    Users findByUsername(String username);
}