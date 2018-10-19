package com.karol172.blog.repository;

import com.karol172.blog.model.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepostiory extends JpaRepository<File, Long> {

    File findFirstById (Long id);

    File findFirstByFileName (String fileName);

    List<File> findByOrderByAdditionDateDesc ();
}
