package com.karol172.blog.model;

import com.karol172.blog.form.ArticleForm;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Article
{
  @Id
  @GeneratedValue
  private Long id;
  @Column(nullable=false)
  private String title;
  @Column(columnDefinition="TEXT")
  private String contents;
  @Column(columnDefinition="TEXT")
  private String contentsMore;
  @Column(nullable=false)
  private LocalDateTime additionDate;
  @Column(nullable=false)
  private LocalDateTime publicationDate;
  @Column(nullable=false)
  private Boolean isActive;
  @Column(nullable=false)
  private Boolean canComment;
  @ManyToOne
  @JoinColumn(name="category_id", nullable=false)
  private Category category;
  @ManyToMany(cascade={javax.persistence.CascadeType.MERGE, javax.persistence.CascadeType.PERSIST})
  @JoinTable(name="authors_articles", joinColumns={@JoinColumn(name="article_id", referencedColumnName="id")}, inverseJoinColumns={@JoinColumn(name="user_id", referencedColumnName="id")})
  private Set<User> authors = new HashSet();
  @OneToMany(mappedBy="article", cascade={javax.persistence.CascadeType.ALL})
  private Set<Comment> comments;
  
  public Article() {}
  
  public Article(String title, String contents, String contentsMore, LocalDateTime additionDate, LocalDateTime publicationDate, Boolean isActive, Boolean canComment, Category category)
  {
    this();
    this.title = title;
    this.contents = contents;
    this.contentsMore = contentsMore;
    this.additionDate = additionDate;
    this.publicationDate = publicationDate;
    this.isActive = isActive;
    this.canComment = canComment;
    this.category = category;
  }
  
  public Article(ArticleForm articleForm, Category category)
  {
    this.title = articleForm.getTitle();
    this.contents = articleForm.getContents();
    this.contentsMore = articleForm.getContentsMore();
    this.additionDate = LocalDateTime.of(articleForm.getAdditionDate(), articleForm.getAdditionTime());
    this.publicationDate = LocalDateTime.of(articleForm.getPublicationDate(), articleForm.getPublicationTime());
    this.isActive = articleForm.getActive();
    this.canComment = articleForm.getCanComment();
    this.category = category;
  }
  
  public Long getId()
  {
    return this.id;
  }
  
  public void setId(Long id)
  {
    this.id = id;
  }
  
  public String getTitle()
  {
    return this.title;
  }
  
  public void setTitle(String title)
  {
    this.title = title;
  }
  
  public String getContents()
  {
    return this.contents;
  }
  
  public void setContents(String contents)
  {
    this.contents = contents;
  }
  
  public String getContentsMore()
  {
    return this.contentsMore;
  }
  
  public void setImagePath(String contentsMore)
  {
    this.contentsMore = contentsMore;
  }
  
  public void setContentsMore(String contentsMore)
  {
    this.contentsMore = contentsMore;
  }
  
  public LocalDateTime getAdditionDate()
  {
    return this.additionDate;
  }
  
  public void setAdditionDate(LocalDateTime additionDate)
  {
    this.additionDate = additionDate;
  }
  
  public LocalDateTime getPublicationDate()
  {
    return this.publicationDate;
  }
  
  public void setPublicationDate(LocalDateTime publicationDate)
  {
    this.publicationDate = publicationDate;
  }
  
  public Boolean getActive()
  {
    return this.isActive;
  }
  
  public void setActive(Boolean active)
  {
    this.isActive = active;
  }
  
  public Category getCategory()
  {
    return this.category;
  }
  
  public Boolean getCanComment()
  {
    return this.canComment;
  }
  
  public void setCanComment(Boolean canComment)
  {
    this.canComment = canComment;
  }
  
  public void setCategory(Category category)
  {
    this.category = category;
  }
  
  public Set<User> getAuthors()
  {
    return this.authors;
  }
  
  public void setAuthors(Set<User> authors)
  {
    this.authors = authors;
  }
  
  public Set<Comment> getComments()
  {
    return this.comments;
  }
  
  public void setComments(Set<Comment> comments)
  {
    this.comments = comments;
  }
  
  public Article addAuthor(User author)
  {
    if (!this.authors.contains(author)) {
      this.authors.add(author);
    }
    if (!author.getArticles().contains(this)) {
      author.addArticle(this);
    }
    return this;
  }
  
  public Article removeAuthor(User author)
  {
    if (this.authors.contains(author)) {
      this.authors.remove(author);
    }
    if (author.getArticles().contains(this)) {
      author.removeArticle(this);
    }
    return this;
  }
}
