package com.file.services;

import java.io.File;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.file.entity.Document;
import com.file.repo.DocumentRepository;
import com.file.utils.FileEncrpt;

@Service
public class DocumentService {
    private final DocumentRepository documentRepository;
    private final String uploadDir = "D:/Documents/";
    private final String encryptionKey;

    public DocumentService(DocumentRepository documentRepository) throws Exception {
        this.documentRepository = documentRepository;
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Generate Encryption Key (Store it securely)
        SecretKey secretKey = FileEncrpt.generateKey();
        encryptionKey = java.util.Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }

    public Document saveFile(MultipartFile file, String author) throws Exception {
        // Generate a random file name
        String randomFileName = UUID.randomUUID().toString();
        File originalFile = new File(uploadDir + randomFileName);
        File encryptedFile = new File(uploadDir + "enc_" + randomFileName);

        // Save the uploaded file temporarily
        file.transferTo(originalFile);

        // Encrypt the file
        FileEncrpt.encryptFile(encryptionKey, originalFile, encryptedFile);

        // Delete original file after encryption
        originalFile.delete();

        // Save document details in DB
        Document document = new Document(
                randomFileName,    // Store only the random name
                file.getContentType(),
                file.getSize(),
                author,
                LocalDateTime.now()
        );

        return documentRepository.save(document);
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
}
