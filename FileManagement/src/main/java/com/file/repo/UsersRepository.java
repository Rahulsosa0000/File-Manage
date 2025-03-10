package com.file.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.file.entity.Users;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {
}

