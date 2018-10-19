package com.karol172.blog.form;

import com.karol172.blog.annotations.LoginValid;
import com.karol172.blog.model.User;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class ProfileForm implements Serializable {

    private MultipartFile avatar;

    @NotNull(message = "To pole nie może być puste.")
    @NotEmpty(message = "To pole nie może być puste.")
    @LoginValid
    private String username;

    @NotNull (message = "To pole nie może być puste.")
    @NotEmpty (message = "To pole nie może być puste.")
    @Email(message = "Wprowadzony adres e-mail ma błędny format")
    private String email;

    private String firstName;

    private String lastName;

    public ProfileForm(User user) {
        if (user != null) {
            this.username = user.getUsername();
            this.email = user.getEmail();
            this.firstName = user.getFirstName();
            this.lastName = user.getLastName();
        }
    }

    public MultipartFile getAvatar() {
        return avatar;
    }

    public void setAvatar(MultipartFile avatar) {
        this.avatar = avatar;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
}
