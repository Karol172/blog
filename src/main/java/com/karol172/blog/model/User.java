package com.karol172.blog.model;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.*;

@Entity
public class User {

  @Id
  @GeneratedValue
  private Long id;

  @Column(nullable=false, unique=true)
  private String username;

  @Column(nullable=false, length=60)
  private String password;

  @Column(nullable=false, unique=true)
  private String email;

  private String firstName;

  private String lastName;

  private String avatarPath;

  @Column(nullable=false)
  private Boolean isActivated;

  @ManyToOne
  @JoinColumn(name="user_group_id", nullable=false)
  private UserGroup userGroup;

  @ManyToMany(mappedBy="authors", cascade={CascadeType.MERGE, CascadeType.PERSIST})
  private Set<Article> articles = new HashSet();

  @OneToMany(mappedBy="authorUser", cascade={CascadeType.PERSIST, CascadeType.MERGE})
  private Set<Comment> comments = new HashSet<>();

  @OneToOne(mappedBy="user", cascade=CascadeType.ALL)
  private ActivationAccount activationAccounts;

  @OneToMany(mappedBy="user", cascade=CascadeType.ALL)
  private Set<ForgottenPassword> forgottenPasswords = new HashSet<>();

  @OneToMany(mappedBy="sender", cascade={CascadeType.MERGE, CascadeType.PERSIST})
  private Set<File> sendedFiles = new HashSet();
  
  public User() {}
  
  public User(String username, String password, String email, String firstName,
              String lastName, String avatarPath, Boolean isActivated, UserGroup userGroup) {
    this.username = username;
    this.password = password;
    this.email = email;
    this.firstName = firstName;
    this.lastName = lastName;
    this.avatarPath = avatarPath;
    this.isActivated = isActivated;
    this.userGroup = userGroup;
  }
  
  public Long getId() {
    return this.id;
  }
  
  public void setId(Long id) {
    this.id = id;
  }
  
  public String getUsername() {
    return this.username;
  }
  
  public void setUsername(String username) {
    this.username = username;
  }
  
  public String getPassword() {
    return this.password;
  }
  
  public void setPassword(String password) {
    this.password = password;
  }
  
  public String getEmail() {
    return this.email;
  }
  
  public void setEmail(String email) {
    this.email = email;
  }
  
  public String getFirstName() {
    return this.firstName;
  }
  
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }
  
  public String getLastName() {
    return this.lastName;
  }
  
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }
  
  public String getAvatarPath() {
    return this.avatarPath;
  }
  
  public void setAvatarPath(String avatarPath) {
    this.avatarPath = avatarPath;
  }
  
  public Boolean getActivated() {
    return this.isActivated;
  }
  
  public void setActivated(Boolean activated) {
    this.isActivated = activated;
  }
  
  public UserGroup getUserGroup() {
    return this.userGroup;
  }
  
  public void setUserGroup(UserGroup userGroup) {
    this.userGroup = userGroup;
  }
  
  public Set<Article> getArticles() {
    return this.articles;
  }
  
  public void setArticles(Set<Article> articles) {
    this.articles = articles;
  }
  
  public Set<Comment> getComments() {
    return this.comments;
  }
  
  public void setComments(Set<Comment> comments) {
    this.comments = comments;
  }
  
  public ActivationAccount getActivationAccount() {
    return this.activationAccounts;
  }
  
  public void setActivationAccount(ActivationAccount activationAccounts) {
    this.activationAccounts = activationAccounts;
  }
  
  public Set<ForgottenPassword> getForgottenPasswords() {
    return this.forgottenPasswords;
  }
  
  public void setForgottenPasswords(Set<ForgottenPassword> forgottenPasswords) {
    this.forgottenPasswords = forgottenPasswords;
  }
  
  public Set<File> getSendedFiles() {
    return this.sendedFiles;
  }
  
  public void setSendedFiles(Set<File> sendedFiles) {
    this.sendedFiles = sendedFiles;
  }
  
  public void addArticle(Article article) {
    if (article != null) {
      if (!this.articles.contains(article))
        this.articles.add(article);

      if (!article.getAuthors().contains(this))
        article.addAuthor(this);
    }
  }
  
  public void removeArticle(Article article) {
    if (article != null) {
      if (this.articles.contains(article))
        this.articles.remove(article);

      if (article.getAuthors().contains(this))
        article.removeAuthor(this);
    }
  }
  
  public boolean hasPermission(String permissionName) {
    return !this.userGroup.getPermissions().stream().filter(p -> p.getName().equals(permissionName))
            .collect(Collectors.toSet()).isEmpty();
  }
}
