package com.karol172.blog.model;

import java.time.LocalDateTime;
import javax.persistence.*;

@Entity
public class Comment {

  @Id
  @GeneratedValue
  private Long id;

  @Column(nullable=false)
  private String contents;

  @Column(nullable=false)
  private LocalDateTime additionDate;

  @Column(nullable=false)
  private Boolean isActive;

  private String author;

  @ManyToOne
  @JoinColumn(name="article_id", nullable=false)
  private Article article;

  @ManyToOne
  @JoinColumn(name="user_id")
  private User authorUser;
  
  public Comment() {}
  
  public Comment(String contents, LocalDateTime additionDate, Boolean isActive, String author, Article article) {
    this.contents = contents;
    this.additionDate = additionDate;
    this.isActive = isActive;
    this.author = author;
    this.article = article;
  }
  
  public Comment(String contents, LocalDateTime additionDate, Boolean isActive, User author, Article article) {
    this.contents = contents;
    this.additionDate = additionDate;
    this.isActive = isActive;
    this.authorUser = author;
    this.author = author.getUsername();
    this.article = article;
  }
  
  public Long getId() {
    return this.id;
  }
  
  public void setId(Long id) {
    this.id = id;
  }
  
  public String getContents() {
    return this.contents;
  }
  
  public void setContents(String contents) {
    this.contents = contents;
  }
  
  public LocalDateTime getAdditionDate() {
    return this.additionDate;
  }
  
  public void setAdditionDate(LocalDateTime additionDate) {
    this.additionDate = additionDate;
  }
  
  public Boolean getActive() {
    return this.isActive;
  }
  
  public void setActive(Boolean active) {
    this.isActive = active;
  }
  
  public String getAuthor() {
    return this.author;
  }
  
  public void setAuthor(String author) {
    this.author = author;
  }
  
  public Article getArticle() {
    return this.article;
  }
  
  public void setArticle(Article article) {
    this.article = article;
  }
  
  public User getAuthorUser() {
    return this.authorUser;
  }
  
  public void setAuthorUser(User authorUser) {
    this.authorUser = authorUser;
  }
}