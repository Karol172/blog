package com.karol172.blog.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class User
  implements Serializable
{
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
  @ManyToMany(mappedBy="authors", cascade={javax.persistence.CascadeType.MERGE, javax.persistence.CascadeType.PERSIST})
  private Set<Article> articles = new HashSet();
  @OneToMany(mappedBy="authorUser", cascade={javax.persistence.CascadeType.PERSIST, javax.persistence.CascadeType.MERGE})
  private List<Comment> comments;
  @OneToMany(mappedBy="user", cascade={javax.persistence.CascadeType.ALL})
  private List<ActivationAccount> activationAccounts;
  @OneToMany(mappedBy="user", cascade={javax.persistence.CascadeType.ALL})
  private List<ForgottenPassword> forgottenPasswords;
  @OneToMany(mappedBy="sender", cascade={javax.persistence.CascadeType.MERGE, javax.persistence.CascadeType.PERSIST})
  private Set<File> sendedFiles = new HashSet();
  
  public User() {}
  
  public User(String username, String password, String email, String firstName, String lastName, String avatarPath, Boolean isActivated, UserGroup userGroup)
  {
    this.username = username;
    this.password = password;
    this.email = email;
    this.firstName = firstName;
    this.lastName = lastName;
    this.avatarPath = avatarPath;
    this.isActivated = isActivated;
    this.userGroup = userGroup;
  }
  
  public Long getId()
  {
    return this.id;
  }
  
  public void setId(Long id)
  {
    this.id = id;
  }
  
  public String getUsername()
  {
    return this.username;
  }
  
  public void setUsername(String username)
  {
    this.username = username;
  }
  
  public String getPassword()
  {
    return this.password;
  }
  
  public void setPassword(String password)
  {
    this.password = password;
  }
  
  public String getEmail()
  {
    return this.email;
  }
  
  public void setEmail(String email)
  {
    this.email = email;
  }
  
  public String getFirstName()
  {
    return this.firstName;
  }
  
  public void setFirstName(String firstName)
  {
    this.firstName = firstName;
  }
  
  public String getLastName()
  {
    return this.lastName;
  }
  
  public void setLastName(String lastName)
  {
    this.lastName = lastName;
  }
  
  public String getAvatarPath()
  {
    return this.avatarPath;
  }
  
  public void setAvatarPath(String avatarPath)
  {
    this.avatarPath = avatarPath;
  }
  
  public Boolean getActivated()
  {
    return this.isActivated;
  }
  
  public void setActivated(Boolean activated)
  {
    this.isActivated = activated;
  }
  
  public UserGroup getUserGroup()
  {
    return this.userGroup;
  }
  
  public void setUserGroup(UserGroup userGroup)
  {
    this.userGroup = userGroup;
  }
  
  public Set<Article> getArticles()
  {
    return this.articles;
  }
  
  public void setArticles(Set<Article> articles)
  {
    this.articles = articles;
  }
  
  public List<Comment> getComments()
  {
    return this.comments;
  }
  
  public void setComments(List<Comment> comments)
  {
    this.comments = comments;
  }
  
  public List<ActivationAccount> getActivationAccounts()
  {
    return this.activationAccounts;
  }
  
  public void setActivationAccounts(List<ActivationAccount> activationAccounts)
  {
    this.activationAccounts = activationAccounts;
  }
  
  public List<ForgottenPassword> getForgottenPasswords()
  {
    return this.forgottenPasswords;
  }
  
  public void setForgottenPasswords(List<ForgottenPassword> forgottenPasswords)
  {
    this.forgottenPasswords = forgottenPasswords;
  }
  
  public Set<File> getSendedFiles()
  {
    return this.sendedFiles;
  }
  
  public void setSendedFiles(Set<File> sendedFiles)
  {
    this.sendedFiles = sendedFiles;
  }
  
  public User addArticle(Article article)
  {
    if (!this.articles.contains(article)) {
      this.articles.add(article);
    }
    if (!article.getAuthors().contains(this)) {
      article.addAuthor(this);
    }
    return this;
  }
  
  public User removeArticle(Article article)
  {
    if (this.articles.contains(article)) {
      this.articles.add(article);
    }
    if (article.getAuthors().contains(this)) {
      article.addAuthor(this);
    }
    return this;
  }
  
  public boolean hasPermission(String permissionName)
  {
    if (this.userGroup.getPermissions().contains(new Permission(permissionName, null))) {
      return true;
    }
    return false;
  }
}
