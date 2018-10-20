package com.karol172.blog.model;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

@Entity
public class Permission {

  @Id
  @GeneratedValue
  private Long id;

  @Column(nullable=false, unique=true)
  private String name;

  private String description;

  @ManyToMany(mappedBy="permissions", fetch = FetchType.EAGER)
  private Set<UserGroup> userGroups = new HashSet<>();
  
  public Permission() {}
  
  public Permission(String name, String description) {
    this.name = name;
    this.description = description;
  }
  
  public Long getId() {
    return this.id;
  }
  
  public void setId(Long id) {
    this.id = id;
  }
  
  public String getName() {
    return this.name;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public String getDescription() {
    return this.description;
  }
  
  public void setDescription(String description) {
    this.description = description;
  }
  
  public Set<UserGroup> getUserGroups() {
    return this.userGroups;
  }
  
  public void setUserGroups(Set<UserGroup> userGroups) {
    this.userGroups = userGroups;
  }

  public void addUserGroup (UserGroup userGroup) {
    if (userGroup != null) {
      if (!this.userGroups.contains(userGroup))
        this.userGroups.add(userGroup);
      if (!userGroup.getPermissions().contains(this))
        userGroup.addPermission(this);
    }
  }

  public void removeUserGroup (UserGroup userGroup) {
    if (userGroup != null) {
      if (this.userGroups.contains(userGroup))
        this.userGroups.remove(userGroup);
      if (userGroup.getPermissions().contains(this))
        userGroup.removePermission(this);
    }
  }
}
