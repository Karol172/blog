package com.karol172.blog.repository;

import com.karol172.blog.model.User;
import com.karol172.blog.model.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByIsActivated (Boolean isActivated);

    User findFirstByUsername (String username);

    User findFirstByUsernameAndEmailAndFirstNameAndLastNameAndAvatarPathAndIsActivatedAndUserGroup
            (String username, String email, String firstName, String lastName, String avatarPath, Boolean isActivated, UserGroup userGroup);

    List<User> findByOrderByUsername ();

    User findFirstById (long id);
}
