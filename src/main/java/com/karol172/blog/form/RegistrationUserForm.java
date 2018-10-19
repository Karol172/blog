package com.karol172.blog.form;

import com.karol172.blog.annotations.LoginValid;
import com.karol172.blog.annotations.PasswordValid;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

public class RegistrationUserForm implements Serializable {

    @NotNull (message = "To pole nie może być puste.")
    @NotEmpty (message = "To pole nie może być puste.")
    @LoginValid
    private String username;

    @NotNull (message = "To pole nie może być puste.")
    @NotEmpty (message = "To pole nie może być puste.")
    @Email (message = "Wprowadzony adres e-mail ma błędny format")
    private String email;

    private String firstName;

    private String lastName;

    @NotNull (message = "To pole nie może być puste.")
    @NotEmpty (message = "To pole nie może być puste.")
    @PasswordValid
    @Size(min = 6, message = "Hasło musi posiadać co najmniej 6 znaków")
    private String password;

    @NotNull (message = "To pole nie może być puste.")
    @NotEmpty (message = "To pole nie może być puste.")
    private String repeatedPassword;

    public RegistrationUserForm() { }

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRepeatedPassword() {
        return repeatedPassword;
    }

    public void setRepeatedPassword(String repeatedPassword) {
        this.repeatedPassword = repeatedPassword;
    }

}
