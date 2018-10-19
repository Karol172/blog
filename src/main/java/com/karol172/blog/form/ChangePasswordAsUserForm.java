package com.karol172.blog.form;

import com.karol172.blog.annotations.PasswordValid;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

public class ChangePasswordAsUserForm implements Serializable {

    @NotNull (message = "To pole nie może być puste.")
    @NotEmpty (message = "To pole nie może być puste.")
    private String currentPassword;

    @NotNull (message = "To pole nie może być puste.")
    @NotEmpty (message = "To pole nie może być puste.")
    @PasswordValid
    @Size(min = 6, message = "Hasło musi posiadać co najmniej 6 znaków")
    private String password;

    @NotNull (message = "To pole nie może być puste.")
    @NotEmpty (message = "To pole nie może być puste.")
    private String repeatedPassword;

    public ChangePasswordAsUserForm() { }

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

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }
}
