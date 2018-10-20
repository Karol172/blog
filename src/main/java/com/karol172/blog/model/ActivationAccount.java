package com.karol172.blog.model;

import java.time.LocalDateTime;
import javax.persistence.*;

@Entity
public class ActivationAccount {

  @Id
  @GeneratedValue
  private Long id;

  @OneToOne
  @JoinColumn(name="user_id", nullable=false)
  private User user;

  @Column(nullable=false)
  private LocalDateTime dateRegistration;

  private LocalDateTime dateActivation;

  @Column
  private String activationCode;
  
  public ActivationAccount() {}
  
  public ActivationAccount(User user, LocalDateTime dateRegistration, String activationCode) {
    this.user = user;
    this.dateRegistration = dateRegistration;
    this.activationCode = activationCode;
  }
  
  public Long getId() {
    return this.id;
  }
  
  public void setId(Long id) {
    this.id = id;
  }
  
  public User getUser() {
    return this.user;
  }
  
  public void setUser(User user) {
    this.user = user;
  }
  
  public LocalDateTime getDateRegistration() {
    return this.dateRegistration;
  }
  
  public void setDateRegistration(LocalDateTime dateRegistration) {
    this.dateRegistration = dateRegistration;
  }
  
  public LocalDateTime getDateActivation() {
    return this.dateActivation;
  }
  
  public void setDateActivation(LocalDateTime dateActivation) {
    this.dateActivation = dateActivation;
  }
  
  public String getActivationCode() {
    return this.activationCode;
  }
  
  public void setActivationCode(String activationCode) {
    this.activationCode = activationCode;
  }
}
