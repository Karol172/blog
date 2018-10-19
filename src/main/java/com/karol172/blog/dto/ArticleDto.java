package com.karol172.blog.dto;

import com.karol172.blog.model.Article;
import com.karol172.blog.model.Category;
import com.karol172.blog.model.Comment;
import com.karol172.blog.model.User;
import org.hibernate.mapping.Collection;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class ArticleDto implements Serializable, Comparable {

    private Long id;

    @NotEmpty(message = "To pole nie może być puste.")
    @NotNull(message = "To pole nie może być puste.")
    private String title;

    private String contents;

    private String contentsMore;

    @NotEmpty(message = "To pole nie może być puste.")
    @NotNull(message = "To pole nie może być puste.")
    private LocalDateTime additionDate;

    @NotEmpty(message = "To pole nie może być puste.")
    @NotNull(message = "To pole nie może być puste.")
    private LocalDateTime publicationDate;

    @NotEmpty(message = "To pole nie może być puste.")
    @NotNull(message = "To pole nie może być puste.")
    private Boolean isActive;

    @NotEmpty(message = "To pole nie może być puste.")
    @NotNull(message = "To pole nie może być puste.")
    private Boolean canComment;

    @NotEmpty(message = "To pole nie może być puste.")
    @NotNull(message = "To pole nie może być puste.")
    private CategoryDto category;

    private Set<UserDto> authors = new HashSet<>();

    private List<CommentDto> comments = new ArrayList<>();

    public ArticleDto() {
    }

    public ArticleDto(Article article) {
        this.id = article.getId();
        this.title = article.getTitle();
        this.contents = article.getContents();
        this.contentsMore = article.getContentsMore();
        this.additionDate = article.getAdditionDate();
        this.publicationDate = article.getPublicationDate();
        this.isActive = article.getActive();
        this.canComment = article.getCanComment();
        this.category = new CategoryDto(article.getCategory().getId(),
                article.getCategory().getName(), article.getCategory().getDescription());
        article.getAuthors().forEach(k->authors.add(new UserDto(k)));
        article.getComments().stream().filter(k->k.getActive()).collect(Collectors.toList())
                .forEach(k->this.comments.add(new CommentDto(k)));
        Collections.sort(this.comments);
    }

    public ArticleDto(Long id, String title, String contents, String contentsMore,  LocalDateTime additionDate,
                      LocalDateTime publicationDate, Boolean isActive,  Boolean canComment,  CategoryDto category) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.contentsMore = contentsMore;
        this.additionDate = additionDate;
        this.publicationDate = publicationDate;
        this.isActive = isActive;
        this.canComment = canComment;
        this.category = category;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getContentsMore() {
        return contentsMore;
    }

    public void setContentsMore(String contentsMore) {
        this.contentsMore = contentsMore;
    }

    public LocalDateTime getAdditionDate() {
        return additionDate;
    }

    public void setAdditionDate(Timestamp additionDate) {
        this.additionDate = additionDate.toLocalDateTime();
    }

    public LocalDateTime getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(Timestamp publicationDate) {
        this.publicationDate = publicationDate.toLocalDateTime();
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public void setAdditionDate(LocalDateTime additionDate) {
        this.additionDate = additionDate;
    }

    public CategoryDto getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = new CategoryDto(category);
    }

    public Set<UserDto> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<User> authors) {
        authors.forEach(k->this.authors.add(new UserDto(k)));
    }

    public List<CommentDto> getComments() {
        return comments;
    }

    public void setComments(List<CommentDto> comments) { this.comments = comments;
    }

    public void setPublicationDate(LocalDateTime publicationDate) {
        this.publicationDate = publicationDate;
    }

    public Boolean getCanComment() {
        return canComment;
    }

    public void setCanComment(Boolean canComment) {
        this.canComment = canComment;
    }

    public void setCategory(CategoryDto category) {
        this.category = category;
    }

    @Override
    public int compareTo(Object o) {
        if (o == null)
            return -1;
        if (o.getClass() != getClass())
            return -1;
        return -additionDate.compareTo(((ArticleDto) o).additionDate);
    }
}
