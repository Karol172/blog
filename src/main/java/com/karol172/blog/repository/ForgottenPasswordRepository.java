package com.karol172.blog.repository;

import com.karol172.blog.model.ForgottenPassword;
import com.karol172.blog.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ForgottenPasswordRepository extends JpaRepository<ForgottenPassword, Long> {

    List<ForgottenPassword> findByUserAndDateChangeIsNull (User user);

    ForgottenPassword findFirstByCodeForgottenPasswordAndDateChangeIsNull(String code) ;
}
