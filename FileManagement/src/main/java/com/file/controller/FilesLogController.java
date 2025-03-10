package com.file.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.file.model.FilesLog;
import com.file.repo.FilesLogRepo;

@RestController
@RequestMapping("/api/files")
@CrossOrigin("*")
public class FilesLogController {

    @Autowired
    private FilesLogRepo filesLogRepository;

    @PostMapping("/save")
    public ResponseEntity<String> saveFileLog(@RequestBody FilesLog filesLog) {
        filesLogRepository.save(filesLog);
        return ResponseEntity.ok("File Log Saved Successfully");
    }

    
    @GetMapping("/all")
    public List<FilesLog> getAllFiles() {
        return filesLogRepository.findAll();
    }
}
