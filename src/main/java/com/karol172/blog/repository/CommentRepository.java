package com.karol172.blog.repository;

import com.karol172.blog.model.Article;
import com.karol172.blog.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;;import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByArticleOrderByAdditionDateDesc (Article article);

    Comment findFirstByIdAndIsActive (long id, boolean active);

    Comment findFirstById (long id);

    Comment findFirstByContentsAndAdditionDateAndArticleAndAuthor (String contents,
                                                       LocalDateTime additionDate, Article article, String author);

    List<Comment> findByArticleAndIsActiveOrderByAdditionDateDesc (Article article, boolean isActive);
}
