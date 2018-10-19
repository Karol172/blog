package com.karol172.blog.validator;

import com.karol172.blog.annotations.LoginValid;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class LoginValidator
        implements ConstraintValidator<LoginValid, String>
{
    private static final String LOGIN_PATTERN = "^[A-Za-z]+[A-Za-z0-9_-]*$";

    public void initialize(LoginValid constraintAnnotation) {}

    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext)
    {
        return validateLogin(s);
    }

    private boolean validateLogin(String login)
    {
        return Pattern.compile("^[A-Za-z]+[A-Za-z0-9_-]*$").matcher(login).matches();
    }
}
