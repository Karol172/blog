package com.karol172.blog.model;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class Permission
{
  @Id
  @GeneratedValue
  private Long id;
  @Column(nullable=false, unique=true)
  private String name;
  private String description;
  @ManyToMany(mappedBy="permissions")
  private List<UserGroup> userGroups;
  
  public Permission() {}
  
  public Permission(String name, String description)
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
  
  public List<UserGroup> getUserGroups()
  {
    return this.userGroups;
  }
  
  public void setUserGroups(List<UserGroup> userGroups)
  {
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
  
  public boolean equals(Object o)
  {
    if (this == o) {
      return true;
    }
    if ((o == null) || (getClass() != o.getClass())) {
      return false;
    }
    Permission that = (Permission)o;
    return that.name.equals(this.name);
  }
}
