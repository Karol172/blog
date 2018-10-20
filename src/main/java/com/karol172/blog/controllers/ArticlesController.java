package com.karol172.blog.controllers;

import com.karol172.blog.service.ArticlesService;
import com.karol172.blog.service.CommentService;
import com.karol172.blog.service.DataPageService;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ArticlesController {

  @Autowired
  private ArticlesService articlesService;

  @Autowired
  private DataPageService dataPageService;

  @Autowired
  private CommentService commentService;
  
  @RequestMapping(value="/article/category/{categoryId}", method = RequestMethod.GET)
  public String articlesDividedIntoCategory(@PathVariable(name="categoryId") long categoryId, Model model, HttpServletRequest request) {
    this.dataPageService.getDataPage(model, request);
    this.articlesService.getArticlesOfCategory(model, categoryId, 1);
    return "articlesOfCategory";
  }
  
  @RequestMapping(value="/article/category/{categoryId}/{pageNumber}", method=RequestMethod.GET)
  public String articlesDividedIntoCategory(@PathVariable(name="categoryId") long categoryId, @PathVariable(name="pageNumber") int pageNumber,
                                            Model model, HttpServletRequest request) {
    this.articlesService.getArticlesOfCategory(model, categoryId, pageNumber);
    this.dataPageService.getDataPage(model, request);
    return "articlesOfCategory";
  }

  @RequestMapping(value="/article/more/{id}", method=RequestMethod.GET)
  public String article(@PathVariable("id") long id, Model model, HttpServletRequest request) {
    this.articlesService.getArticle(model, id);
    this.dataPageService.getDataPage(model, request);
    this.commentService.getCommentForm(model, request);
    this.commentService.getComments(model, id);
    return "article";
  }
}
