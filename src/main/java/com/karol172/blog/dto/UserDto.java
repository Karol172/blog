package com.karol172.blog.dto;

import com.karol172.blog.model.ActivationAccount;
import com.karol172.blog.model.User;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class UserDto implements Serializable, Comparable {

    private Long id;

    @NotNull(message = "To pole nie może być puste")
    @NotEmpty(message = "To pole nie może być puste")
    private String username;

    private String firstName;

    private String lastName;

    @NotNull(message = "To pole nie może być puste")
    @NotEmpty(message = "To pole nie może być puste")
    private String avatarPath;

    @NotNull(message = "To pole nie może być puste")
    @NotEmpty(message = "To pole nie może być puste")
    private Boolean isActive;

    @NotNull(message = "To pole nie może być puste")
    @NotEmpty(message = "To pole nie może być puste")
    private UserGroupDto userGroup;

    private ActivationAccount activationAccount;

    public UserDto() { }

    public UserDto (User user) {
        if (user != null) {
            id = user.getId();
            username = user.getUsername();
            firstName = user.getFirstName();
            lastName = user.getLastName();
            avatarPath = user.getAvatarPath();
            isActive = user.getActivated();
            userGroup = new UserGroupDto(user.getUserGroup().getId(),
                    user.getUserGroup().getName(), user.getUserGroup().getDescription());
            activationAccount = user.getActivationAccount();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAvatarPath() {
        return avatarPath;
    }

    public void setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public UserGroupDto getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(UserGroupDto userGroup) {
        this.userGroup = userGroup;
    }

    public ActivationAccount getActivationAccount() {
        return activationAccount;
    }

    public void setActivationAccount(ActivationAccount activationAccount) {
        this.activationAccount = activationAccount;
    }

    @Override
    public int compareTo(Object o) {
        if (o == null)
            return -1;
        if (o.getClass() != getClass())
            return -1;

        UserDto u = (UserDto)o;
        return username.compareTo(u.username);
    }
}
