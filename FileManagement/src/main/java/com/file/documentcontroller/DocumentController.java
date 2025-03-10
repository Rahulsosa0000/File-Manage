package com.file.documentcontroller;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.file.entity.Document;
import com.file.services.DocumentService;

@RestController
@RequestMapping("/api/documents")
@CrossOrigin(origins = "*")
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    
    
    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("authorName") String authorName) { // Changed authorId -> authorName

        try {
            Document document = documentService.saveFile(file, authorName);
            Map<String, String> response = new HashMap<>();
            response.put("message", "File uploaded successfully!");
            response.put("fileName", document.getFileName());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", e.getMessage()));
        }
    }



    
    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadDocument(@PathVariable Long id) {
        try {
            Optional<Document> documentOpt = documentService.getFile(id);
            if (documentOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            String fileName = documentOpt.get().getFileName();
            byte[] fileContent = documentService.decryptFile(fileName);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(fileContent);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(("Error: " + e.getMessage()).getBytes());
        }
    }
    
    
    @GetMapping
    public ResponseEntity<List<Document>> getAllDocuments() {
        List<Document> documents = documentService.getAllDocuments();
        return ResponseEntity.ok(documents);
    }
 
    //  Fetch document by ID
    @GetMapping("/{id}")
    public ResponseEntity<Document> getDocumentById(@PathVariable Long id) {
        return documentService.getFile(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
 
    //  Update document
    @PutMapping("/update/{id}")
    public ResponseEntity<Document> updateDocument(
            @PathVariable Long id,
            @RequestParam("author") String author,
            @RequestParam("addedDate") LocalDateTime addedDate,
            @RequestParam(value = "file", required = false) MultipartFile file) {

        Document updatedDoc = documentService.updateDocument(id, author, addedDate, file);
        return ResponseEntity.ok(updatedDoc);
    }
 
    //  Delete document
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteDocument(@PathVariable Long id) {
        documentService.deleteDocument(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Document deleted successfully!");
        return ResponseEntity.ok(response);
    }
}
