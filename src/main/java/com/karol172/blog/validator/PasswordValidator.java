package com.karol172.blog.validator;

import com.karol172.blog.annotations.PasswordValid;
import java.util.regex.Pattern;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<PasswordValid, String> {
  private static final String PASSWORD_PATTERN = "^[A-Za-z0-9!@#$%^&*()_+-=,.<>/?|{}]+$";
  
  public void initialize(PasswordValid constraintAnnotation) {}
  
  public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
    return validatePassword(s);
  }
  
  private boolean validatePassword(String password) {
    return Pattern.compile("^[A-Za-z0-9!@#$%^&*()_+-=,.<>/?|{}]+$").matcher(password).matches();
  }
}
