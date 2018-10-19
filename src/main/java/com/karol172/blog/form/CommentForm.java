package com.karol172.blog.form;

import com.karol172.blog.model.Comment;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

public class CommentForm implements Serializable {

    private Long id;

    @NotNull(message = "Pole nie może być puste")
    @NotEmpty(message = "Pole nie może byc puste")
    @Size(max = 255, message = "Pole nie może mieć więcej znaków niż 255.")
    private String contents;


    @NotNull(message = "Pole nie może być puste")
    @NotEmpty(message = "Pole nie może byc puste")
    private String author;

    private Long user;

    @NotNull(message = "Pole nie może być puste")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime additionDate;

    private Long article;

    public CommentForm() {
    }

    public CommentForm(LocalDateTime localDateTime, Long idUser) {
        this.user = idUser;
        this.additionDate = localDateTime;
    }

    public CommentForm(Comment comment) {
        this.id = comment.getId();
        this.contents = comment.getContents();
        this.additionDate = comment.getAdditionDate();
        this.author = comment.getAuthor();
        if (comment.getAuthorUser() != null)
            this.user = comment.getAuthorUser().getId();
        this.article = comment.getArticle().getId();
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public LocalDateTime getAdditionDate() {
        return additionDate;
    }

    public void setAdditionDate(LocalDateTime additionDate) {
        this.additionDate = additionDate;
    }

    public Long getArticle() {
        return article;
    }

    public void setArticle(Long article) {
        this.article = article;
    }

    public Long getUser() {
        return user;
    }

    public void setUser(Long user) {
        this.user = user;
    }
}
