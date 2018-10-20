package com.karol172.blog.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.*;

@Entity
public class UserGroup {

  @Id
  @GeneratedValue
  private Long id;

  @Column(nullable=false, unique=true)
  private String name;

  private String description;

  @ManyToMany
  @JoinTable(name="perrmissions_group", joinColumns={@JoinColumn(name="permission_id", referencedColumnName="id")},
          inverseJoinColumns={@JoinColumn(name="user_group_id", referencedColumnName="id")})
  private Set<Permission> permissions = new HashSet<>();

  @OneToMany(mappedBy="userGroup", cascade={CascadeType.MERGE, CascadeType.PERSIST})
  private Set<User> users = new HashSet<>();
  
  public UserGroup() {}
  
  public UserGroup(String name, String description) {
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
  
  public Set<Permission> getPermissions() {
    return this.permissions;
  }
  
  public void setPermissions(Set<Permission> permissions) {
    this.permissions = permissions;
  }
  
  public Set<User> getUsers() {
    return this.users;
  }
  
  public void setUsers(Set<User> users) {
    this.users = users;
  }

  public void addPermission (Permission permission) {
    if (permission != null) {
      if (!this.permissions.contains(permission))
        this.permissions.add(permission);
      if (!permission.getUserGroups().contains(this))
        permission.addUserGroup(this);
    }
  }

  public void removePermission (Permission permission) {
    if (permission != null) {
      if (this.permissions.contains(permission))
        this.permissions.remove(permission);
      if (permission.getUserGroups().contains(this))
        permission.removeUserGroup(this);
    }
  }
}
