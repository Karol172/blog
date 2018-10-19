package com.karol172.blog.controllers;

import com.karol172.blog.service.ArticlesService;
import com.karol172.blog.service.DataPageService;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainPageController
{
  @Autowired
  private DataPageService dataPageService;
  @Autowired
  private ArticlesService articlesService;
  
  @RequestMapping(value={"/"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  public String mainPage(Model model, HttpServletRequest request)
  {
    this.dataPageService.getDataPage(model, request);
    this.articlesService.getAllArticles(model, 1);
    return "main";
  }
  
  @RequestMapping(value={"/pages/{pageNumber}"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  public String mainPage(@PathVariable(name="pageNumber") int pageNumber, Model model, HttpServletRequest request)
  {
    this.dataPageService.getDataPage(model, request);
    this.articlesService.getAllArticles(model, pageNumber);
    return "main";
  }
  
  @RequestMapping(value={"/403"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  public String error403(Model model, HttpServletRequest request)
  {
    this.dataPageService.getDataPage(model, request);
    this.dataPageService.get403(model);
    return "message";
  }
}
