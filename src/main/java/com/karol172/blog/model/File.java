package com.karol172.blog.model;

import java.time.LocalDateTime;
import javax.persistence.*;

@Entity
public class File {

  @Id
  @GeneratedValue
  private Long id;

  @Column(nullable=false, unique=true)
  private String fileName;

  @Column(nullable=false)
  private LocalDateTime additionDate;

  @ManyToOne
  @JoinColumn(name="user_id", nullable=false)
  private User sender;
  
  public File() {}
  
  public File(String fileName, LocalDateTime additionDate, User sender) {
    this.fileName = fileName;
    this.additionDate = additionDate;
    this.sender = sender;
  }
  
  public Long getId() {
    return this.id;
  }
  
  public void setId(Long id) {
    this.id = id;
  }
  
  public String getFileName() {
    return this.fileName;
  }
  
  public void setFileName(String fileName) {
    this.fileName = fileName;
  }
  
  public LocalDateTime getAdditionDate() {
    return this.additionDate;
  }
  
  public void setAdditionDate(LocalDateTime additionDate) {
    this.additionDate = additionDate;
  }
  
  public User getSender() {
    return this.sender;
  }
  
  public void setSender(User sender) {
    this.sender = sender;
  }
}