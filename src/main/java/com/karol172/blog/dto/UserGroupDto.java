package com.karol172.blog.dto;

import com.karol172.blog.model.Permission;
import com.karol172.blog.model.UserGroup;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.*;

public class UserGroupDto implements Serializable {

    private Long id;

    @NotNull(message = "To pole nie może być puste.")
    @NotEmpty(message = "To pole nie może być puste.")
    private String name;

    private String description;

    private List<UserDto> users;

    public UserGroupDto () { }

    public UserGroupDto (Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public UserGroupDto(UserGroup userGroup) {
        if (userGroup != null) {
            this.id = userGroup.getId();
            this.name = userGroup.getName();
            this.description = userGroup.getDescription();
            users = new ArrayList<>();
            userGroup.getUsers().forEach(k-> users.add(new UserDto(k)));
            Collections.sort(users);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<UserDto> getUsers() {
        return users;
    }

    public void setUsers(List<UserDto> users) {
        this.users = users;
    }
}
