package com.karol172.blog.repository;

import com.karol172.blog.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByOrderByNameAsc();

    Category findFirstByName (String name);

    Category findFirstById (Long id);

}
