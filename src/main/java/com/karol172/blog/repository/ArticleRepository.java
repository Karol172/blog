package com.karol172.blog.repository;

import com.karol172.blog.model.Article;
import com.karol172.blog.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    List<Article> findByCategoryOrderByPublicationDateDesc (Category category);

    List<Article> findByIsActiveAndPublicationDateLessThanEqualOrderByPublicationDateDesc (boolean isActive,
                                                                               LocalDateTime publicationDate);

    List<Article> findByOrderByPublicationDateDesc ();

    List<Article> findByCategoryAndIsActiveAndPublicationDateLessThanEqualOrderByPublicationDateDesc(Category category,
                                                                     boolean isActive, LocalDateTime publicationTime);

    Article findFirstById (Long id);

    Article findFirstByTitleAndContentsAndContentsMoreAndCategoryAndAdditionDateAndPublicationDateAndIsActiveAndCanComment
            (String title, String contents, String ContentsMore, Category category, LocalDateTime additionDate,
             LocalDateTime publicationDate, Boolean isActive, Boolean active);

}