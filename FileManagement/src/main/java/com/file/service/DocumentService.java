package com.file.service;


import java.io.File;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.file.model.Document;
import com.file.model.FilesLog;
import com.file.repo.DocumentRepo;
import com.file.repo.FilesLogRepo;
import com.file.utils.FileEncrpt;

@Service
public class DocumentService {
	
	@Autowired
	private FilesLogRepo filesLogRepository;
	
//	@Autowired
//	private UsersRepository userRepository;
	
    private final DocumentRepo documentRepository;
    private final String uploadDir = "D:/Documents/";
    private final String encryptionKey;

    public DocumentService(DocumentRepo documentRepository) throws Exception {
        this.documentRepository = documentRepository;
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        SecretKey secretKey = FileEncrpt.generateKey();
        encryptionKey = java.util.Base64.getEncoder().encodeToString(secretKey.getEncoded());
        System.out.println("Save this key: " + encryptionKey);
        
    }

    
    public Document saveFile(MultipartFile file, String authorName) throws Exception {
        
        String randomFileName = UUID.randomUUID().toString();
        File originalFile = new File(uploadDir + randomFileName);
        File encryptedFile = new File(uploadDir + "enc_" + randomFileName);

        
        file.transferTo(originalFile);

        
        FileEncrpt.encryptFile(encryptionKey, originalFile, encryptedFile);

       
        originalFile.delete();

       
        Document document = new Document(
                randomFileName,
                file.getContentType(),
                file.getSize(),
                authorName, 
                LocalDateTime.now()
        );
        document = documentRepository.save(document); 

        
        FilesLog filesLog = new FilesLog(
                null, 
                document.getFileName(),
                
                LocalDateTime.now().toString(),
                null, 
                null, 
                "0", 
                String.valueOf(file.getSize()),
                null
        );
        filesLogRepository.save(filesLog); 

        return document;
    }



    public Optional<Document> getFile(Long id) {
        return documentRepository.findById(id);
    }

    public byte[] decryptFile(String fileName) throws Exception {
        File encryptedFile = new File(uploadDir + "enc_" + fileName);
        File decryptedFile = new File(uploadDir + "dec_" + fileName);

        FileEncrpt.decryptFile(encryptionKey, encryptedFile, decryptedFile);
        byte[] fileContent = Files.readAllBytes(decryptedFile.toPath());

        decryptedFile.delete();

        return fileContent;
    }
    
    
    public List<Document> getAllDocuments() {
        return documentRepository.findAll();
    }
 
    public Document updateDocument(Long id, String author, LocalDateTime uploadAt, MultipartFile file) {
        Optional<Document> optionalDoc = documentRepository.findById(id);
        if (optionalDoc.isPresent()) {
            Document existingDoc = optionalDoc.get();
            existingDoc.setAuthor(author);
            existingDoc.setUploadedAt(uploadAt);

            if (file != null && !file.isEmpty()) {
                existingDoc.setFileName(file.getOriginalFilename());
                existingDoc.setFileType(file.getContentType());
                existingDoc.setFileSize(file.getSize());
            }

            return documentRepository.save(existingDoc);
        } else {
            throw new RuntimeException("Document not found with ID: " + id);
        }
    }
    
    
    public void deleteDocument(Long id) {
        documentRepository.deleteById(id);
    }
    
    
}