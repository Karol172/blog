package com.karol172.blog.controllers;

import com.karol172.blog.dto.CategoryDto;
import com.karol172.blog.form.ArticleForm;
import com.karol172.blog.service.ArticlesManagmentService;
import com.karol172.blog.service.DataPageService;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ArticlesManagmentController {

  @Autowired
  private DataPageService dataPageService;

  @Autowired
  private ArticlesManagmentService articlesManagmentService;
  
  @RequestMapping(value="/admin/categories", method= RequestMethod.GET)
  public String categoriesReview(Model model, HttpServletRequest request) {
    this.dataPageService.getDataPage(model, request);
    return "categoriesReview";
  }
  
  @RequestMapping(value="/admin/categories/add", method=RequestMethod.GET)
  public String categoryAddForm(Model model, HttpServletRequest request) {
    this.dataPageService.getDataPage(model, request);
    this.articlesManagmentService.getCategoryForm(model);
    return "categoryForm";
  }
  
  @RequestMapping(value="/admin/categories/edit/{id}", method=RequestMethod.GET)
  public String categoryEditForm(@PathVariable("id") long id, Model model, HttpServletRequest request) {
    this.dataPageService.getDataPage(model, request);
    this.articlesManagmentService.getCategoryForm(model, id);
    return "categoryForm";
  }
  
  @RequestMapping(value="/admin/categories/try_add", method=RequestMethod.POST)
  public String categorySave(@ModelAttribute("categoryForm") @Valid CategoryDto categoryDto, BindingResult result,
                             Model model, HttpServletRequest request) {
    this.articlesManagmentService.tryChangeCategory(model, result, categoryDto);
    this.dataPageService.getDataPage(model, request);
    return "categoryForm";
  }
  
  @RequestMapping(value="/admin/categories/remove/{id}", method=RequestMethod.GET)
  public String categoryRemove(@PathVariable("id") long id, Model model, HttpServletRequest request) {
    this.articlesManagmentService.removeCategory(model, id);
    this.dataPageService.getDataPage(model, request);
    return "categoriesReview";
  }
  
  @RequestMapping(value="/admin/articles", method=RequestMethod.GET)
  public String articlesReview(Model model, HttpServletRequest request) {
    this.dataPageService.getDataPage(model, request);
    this.articlesManagmentService.getArticlesDivadedIntoCategories(model);
    return "articlesReview";
  }
  
  @RequestMapping(value="/admin/articles/add", method=RequestMethod.GET)
  public String articleAddForm(Model model, HttpServletRequest request) {
    this.dataPageService.getDataPage(model, request);
    this.articlesManagmentService.getArticleForm(model, request);
    return "articleForm";
  }
  
  @RequestMapping(value="/admin/article/edit/{id}", method=RequestMethod.GET)
  public String articleEditForm(@PathVariable("id") long id, Model model, HttpServletRequest request) {
    this.dataPageService.getDataPage(model, request);
    this.articlesManagmentService.getArticleForm(model, request, id);
    return "articleForm";
  }
  
  @RequestMapping(value="/admin/articles/try_add", method=RequestMethod.POST)
  public String articleSave(@ModelAttribute("articleForm") @Valid ArticleForm articleForm,
                            BindingResult result, Model model, HttpServletRequest request) {
    this.dataPageService.getDataPage(model, request);
    this.articlesManagmentService.try_add(articleForm, model, result, request);
    return "articleForm";
  }
  
  @RequestMapping(value="/admin/article/remove/{id}", method=RequestMethod.GET)
  public String articleRemove(@PathVariable("id") long id, Model model, HttpServletRequest request) {
    this.dataPageService.getDataPage(model, request);
    this.articlesManagmentService.removeArticle(id, model);
    this.articlesManagmentService.getArticlesDivadedIntoCategories(model);
    return "articlesReview";
  }
}