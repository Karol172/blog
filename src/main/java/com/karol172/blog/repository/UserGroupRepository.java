package com.karol172.blog.repository;

import com.karol172.blog.model.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserGroupRepository extends JpaRepository<UserGroup, Long> {

    UserGroup findFirstByName (String name);

    UserGroup findFirstById (Long id);

    UserGroup findFirstByIdAndNameAndDescription (Long id, String name, String description);

    List<UserGroup> findByOrderByNameAsc ();

}
