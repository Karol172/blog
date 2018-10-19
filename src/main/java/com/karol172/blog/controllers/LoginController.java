package com.karol172.blog.controllers;

import com.karol172.blog.service.DataPageService;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LoginController
{
  @Autowired
  private DataPageService dataPageService;
  
  @RequestMapping(value={"/login"}, method={org.springframework.web.bind.annotation.RequestMethod.GET})
  public String login(Model model, HttpServletRequest request)
  {
    this.dataPageService.getDataPage(model, request);
    return "loginForm";
  }
}
