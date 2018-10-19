package com.karol172.blog.dto;

import com.karol172.blog.model.Article;
import com.karol172.blog.model.Comment;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

public class CommentDto implements Serializable, Comparable {

    private Long id;

    @NotNull
    @NotEmpty
    private String contents;

    @NotNull
    @NotEmpty
    private LocalDateTime additionDate;

    private Boolean isActive;

    @NotNull
    @NotEmpty
    private String author;

    @NotNull
    @NotEmpty
    private ArticleDto article;

    private UserDto authorUser;

    public CommentDto() {
    }

    public CommentDto(Comment comment) {
        id = comment.getId();
        contents = comment.getContents();
        additionDate = comment.getAdditionDate();
        isActive = comment.getActive();
        author = comment.getAuthor();
        Article article = comment.getArticle();
        this.article = new ArticleDto(article.getId(), article.getTitle(), article.getContents(), article.getContentsMore(), article.getAdditionDate(),
                article.getPublicationDate(), article.getActive(), article.getCanComment(), new CategoryDto(article.getCategory().getId(),
                article.getCategory().getName(), article.getCategory().getDescription()));
        authorUser = new UserDto(comment.getAuthorUser());
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public LocalDateTime getAdditionDate() {
        return additionDate;
    }

    public void setAdditionDate(LocalDateTime additionDate) {
        this.additionDate = additionDate;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public ArticleDto getArticle() {
        return article;
    }

    public void setArticle(ArticleDto article) {
        this.article = article;
    }

    public UserDto getAuthorUser() {
        return authorUser;
    }

    public void setAuthorUser(UserDto authorUser) {
        this.authorUser = authorUser;
    }

    @Override
    public int compareTo(Object o) {
        if (o == null)
            return -1;
        if (o.getClass() != getClass())
            return -1;

        return -this.additionDate.compareTo(((CommentDto) o).getAdditionDate());
    }
}
