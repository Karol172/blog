package com.karol172.blog.form;

import com.karol172.blog.model.Article;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class ArticleForm implements Serializable {

    private Long id;

    @NotEmpty(message = "Tytuł nie może być pusty.")
    @NotNull(message = "Tytuł nie może być pusty.")
    private String title;

    private String contents;

    private String contentsMore;

    @DateTimeFormat(pattern = "yyyy-MM-dd", iso = DateTimeFormat.ISO.DATE)
    @NotNull(message = "Data publikacji nie może być pusta.")
    private LocalDate publicationDate;

    @DateTimeFormat(pattern = "HH:mm", iso = DateTimeFormat.ISO.TIME)
    @NotNull(message = "Data publikacji nie może być pusta.")
    private LocalTime publicationTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd", iso = DateTimeFormat.ISO.DATE)
    @NotNull(message = "Data dodania nie może być pusta.")
    private LocalDate additionDate;

    @DateTimeFormat(pattern = "HH:mm", iso = DateTimeFormat.ISO.TIME)
    @NotNull(message = "Data dodania nie może być pusta.")
    private LocalTime additionTime;

    @NotNull(message = "Pole aktywności artykułu nie może być puste.")
    private Boolean isActive;

    @NotNull(message = "Pole komentarzy nie może być puste")
    private Boolean canComment;

    @NotEmpty(message = "Artykuł musi posiadać kategorię.")
    @NotNull(message = "Artykuł musi posiadać kategorię.")
    private String category;

    @NotNull(message = "Artykuł musi mieć autora.")
    private Set<String> authors;

    public ArticleForm() {
        authors = new HashSet<>();
    }

    public ArticleForm(Long id, String title, String contents, String contentsMore, LocalDate publicationDate,
                       LocalTime publicationTime, LocalDate additionDate, LocalTime additionTime, Boolean isActive,
                       Boolean canComment, String category, Collection<String> authors) {
        this();
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.contentsMore = contentsMore;
        this.isActive = isActive;
        this.canComment = canComment;
        this.category = category;
        this.publicationDate = publicationDate;
        this.publicationTime = publicationTime;
        this.additionDate = additionDate;
        this.additionTime = additionTime;
        this.authors.addAll(authors);
    }

    public ArticleForm(Article article) {
        this();
        if (article != null) {
            this.id = article.getId();
            this.title = article.getTitle();
            this.contents = article.getContents();
            this.contentsMore = article.getContentsMore();
            this.isActive = article.getActive();
            this.canComment = article.getCanComment();
            this.category = article.getCategory().getName();
            this.publicationDate = article.getPublicationDate().toLocalDate();
            this.publicationTime = article.getPublicationDate().toLocalTime();
            this.additionDate = article.getAdditionDate().toLocalDate();
            this.additionTime = article.getAdditionDate().toLocalTime();
            article.getAuthors().forEach(k -> this.authors.add(k.getUsername()));
        }
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

    public LocalDate getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(LocalDate publicationDate) {
        this.publicationDate = publicationDate;
    }

    public LocalTime getPublicationTime() {
        return publicationTime;
    }

    public void setPublicationTime(LocalTime publicationTime) {
        this.publicationTime = publicationTime;
    }

    public String getContentsMore() {
        return contentsMore;
    }

    public void setContentsMore(String contentsMore) {
        this.contentsMore = contentsMore;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public Boolean getCanComment() {
        return canComment;
    }

    public void setCanComment(Boolean canComment) {
        this.canComment = canComment;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LocalDate getAdditionDate() {
        return additionDate;
    }

    public void setAdditionDate(LocalDate additionDate) {
        this.additionDate = additionDate;
    }

    public LocalTime getAdditionTime() {
        return additionTime;
    }

    public void setAdditionTime(LocalTime additionTime) {
        this.additionTime = additionTime;
    }

    public Set<String> getAuthors() {
        return authors;
    }

    public void setAuthors(Set<String> authors) {
        this.authors = authors;
    }
}
