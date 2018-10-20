package com.karol172.blog.model;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

@Entity
public class Category {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable=false, unique=true)
    private String name;

    private String description;

    @OneToMany(mappedBy="category", cascade=CascadeType.ALL)
    private Set<Article> articles = new HashSet();

    public Category() {}

    public Category(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Article> getArticles() {
        return this.articles;
    }

    public void setArticles(Set<Article> articles) {
        this.articles = articles;
  }
}