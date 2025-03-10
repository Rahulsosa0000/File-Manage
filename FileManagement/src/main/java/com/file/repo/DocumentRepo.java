package com.file.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.file.model.Document;


@Repository
public interface DocumentRepo extends JpaRepository<Document, Long> {
	
}


