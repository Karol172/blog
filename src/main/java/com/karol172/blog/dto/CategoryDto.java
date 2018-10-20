package com.karol172.blog.dto;

import com.karol172.blog.model.Article;
import com.karol172.blog.model.Category;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class CategoryDto implements Serializable {

    private Long id;

    @NotEmpty(message = "To pole nie może być puste.")
    @NotNull(message = "To pole nie może być puste.")
    private String name;

    private String description;

    private Set<ArticleDto> articlesSet = new HashSet<>();

    public CategoryDto() { }

    public CategoryDto(long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public CategoryDto(Category category) {
        if (category != null) {
            this.id = category.getId();
            this.name = category.getName();
            this.description = category.getDescription();
            category.getArticles().forEach(k-> articlesSet.add(new ArticleDto(k)));
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<ArticleDto> getArticlesSet() {
        return articlesSet;
    }

    public void setArticlesSet(Set<Article> articlesSet) {
        articlesSet.forEach(k-> this.articlesSet.add(new ArticleDto(k)));
    }
}
