package com.karol172.blog.model;

import java.time.LocalDateTime;
import javax.persistence.*;

@Entity
public class ForgottenPassword {

  @Id
  @GeneratedValue
  private Long id;

  private String codeForgottenPassword;

  private LocalDateTime dateChange;

  @ManyToOne
  @JoinColumn(name="user_id")
  private User user;
  
  public ForgottenPassword() {}
  
  public ForgottenPassword(String codeForgottenPassword, User user) {
    this.codeForgottenPassword = codeForgottenPassword;
    this.user = user;
  }
  
  public Long getId()
  {
    return this.id;
  }
  
  public void setId(Long id)
  {
    this.id = id;
  }
  
  public String getCodeForgottenPassword()
  {
    return this.codeForgottenPassword;
  }
  
  public void setCodeForgottenPassword(String codeForgottenPassword) {
    this.codeForgottenPassword = codeForgottenPassword;
  }
  
  public LocalDateTime getDateChange() {
    return this.dateChange;
  }
  
  public void setDateChange(LocalDateTime dateChange) {
    this.dateChange = dateChange;
  }
  
  public User getUser() {
    return this.user;
  }
  
  public void setUser(User user) {
    this.user = user;
  }
}
