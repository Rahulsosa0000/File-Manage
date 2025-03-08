package com.file.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.file.entity.Document;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long> {
	
}


