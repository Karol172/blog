package com.karol172.blog.dto;

import com.karol172.blog.model.File;
import com.karol172.blog.model.User;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

public class FileDto implements Serializable {

    private Long id;

    @NotNull
    @NotEmpty
    private String fileName;

    @NotNull
    private LocalDateTime additionDate;

    @NotNull
    @NotEmpty
    private User sender;

    @NotNull
    @NotEmpty
    private String link;

    public FileDto() { }

    public FileDto(File file) {
        if (file != null) {
            this.id = file.getId();
            this.fileName = file.getFileName();
            this.additionDate = file.getAdditionDate();
            this.sender = file.getSender();
            this.link = "/images/" + this.fileName;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public LocalDateTime getAdditionDate() {
        return additionDate;
    }

    public void setAdditionDate(LocalDateTime additionDate) {
        this.additionDate = additionDate;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
