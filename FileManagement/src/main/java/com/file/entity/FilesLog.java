package com.file.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "files_log")
public class FilesLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String docName;
    private String addedDate;
    private String lastModified;
    private String downloadCount;
    private String size;

    @ManyToOne
    @JoinColumn(name = "uploaded_by", referencedColumnName = "id") // Foreign key with Users
    private Users author;

    @ManyToOne
    @JoinColumn(name = "last_modified_by", referencedColumnName = "id") // Foreign key with Users
    private Users lastModifiedBy;

    @ManyToOne
    @JoinColumn(name = "downloaded_by", referencedColumnName = "id") // Foreign key with Users
    private Users downloadedBy;

    public FilesLog() {
    }

    public FilesLog(Users author, String docName, String addedDate, String lastModified, Users lastModifiedBy,
                    String downloadCount, String size, Users downloadedBy) {
        this.author = author;
        this.docName = docName;
        this.addedDate = addedDate;
        this.lastModified = lastModified;
        this.lastModifiedBy = lastModifiedBy;
        this.downloadCount = downloadCount;
        this.size = size;
        this.downloadedBy = downloadedBy;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDocName() {
		return docName;
	}

	public void setDocName(String docName) {
		this.docName = docName;
	}

	public String getAddedDate() {
		return addedDate;
	}

	public void setAddedDate(String addedDate) {
		this.addedDate = addedDate;
	}

	public String getLastModified() {
		return lastModified;
	}

	public void setLastModified(String lastModified) {
		this.lastModified = lastModified;
	}

	public String getDownloadCount() {
		return downloadCount;
	}

	public void setDownloadCount(String downloadCount) {
		this.downloadCount = downloadCount;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public Users getAuthor() {
		return author;
	}

	public void setAuthor(Users author) {
		this.author = author;
	}

	public Users getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(Users lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public Users getDownloadedBy() {
		return downloadedBy;
	}

	public void setDownloadedBy(Users downloadedBy) {
		this.downloadedBy = downloadedBy;
	}

    
    
}
