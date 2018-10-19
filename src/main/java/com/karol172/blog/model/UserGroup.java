package com.karol172.blog.model;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

@Entity
public class UserGroup
{
  @Id
  @GeneratedValue
  private Long id;
  @Column(nullable=false, unique=true)
  private String name;
  private String description;
  @ManyToMany
  @JoinTable(name="perrmissions_group", joinColumns={@javax.persistence.JoinColumn(name="permission_id", referencedColumnName="id")}, inverseJoinColumns={@javax.persistence.JoinColumn(name="user_group_id", referencedColumnName="id")})
  private List<Permission> permissions;
  @OneToMany(mappedBy="userGroup", cascade={javax.persistence.CascadeType.MERGE, javax.persistence.CascadeType.PERSIST})
  private List<User> users;
  
  public UserGroup() {}
  
  public UserGroup(String name, String description)
  {
    this.name = name;
    this.description = description;
  }
  
  public Long getId()
  {
    return this.id;
  }
  
  public void setId(Long id)
  {
    this.id = id;
  }
  
  public String getName()
  {
    return this.name;
  }
  
  public void setName(String name)
  {
    this.name = name;
  }
  
  public String getDescription()
  {
    return this.description;
  }
  
  public void setDescription(String description)
  {
    this.description = description;
  }
  
  public List<Permission> getPermissions()
  {
    return this.permissions;
  }
  
  public void setPermissions(List<Permission> permissions)
  {
    this.permissions = permissions;
  }
  
  public List<User> getUsers()
  {
    return this.users;
  }
  
  public void setUsers(List<User> users)
  {
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
}
