package com.karol172.blog.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class ForgottenPasswordForm implements Serializable {

    @NotEmpty(message = "To pole nie może być puste")
    @NotNull(message = "To pole nie może być puste")
    private String usernameOrEmail;

    public ForgottenPasswordForm() { }

    public String getUsernameOrEmail() {
        return usernameOrEmail;
    }

    public void setUsernameOrEmail(String usernameOrEmail) {
        this.usernameOrEmail = usernameOrEmail;
    }
}
