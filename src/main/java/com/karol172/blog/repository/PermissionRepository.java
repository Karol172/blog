package com.karol172.blog.repository;

import com.karol172.blog.model.Permission;
import com.karol172.blog.model.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {

    List<Permission> findByOrderByName ();

    Permission findFirstByName (String name);
}

