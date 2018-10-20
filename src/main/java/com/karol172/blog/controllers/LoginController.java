package com.karol172.blog.controllers;

import com.karol172.blog.service.DataPageService;
import javax.servlet.http.HttpServletRequest;

import com.karol172.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class LoginController {

  @Autowired
  private DataPageService dataPageService;

  @Autowired
  private UserService userService;
  
  @RequestMapping(value="/login", method= RequestMethod.GET)
  public String login(Model model, HttpServletRequest request) {
    this.dataPageService.getDataPage(model, request);
    return "loginForm";
  }

  @RequestMapping(value = "/admin/users/{page}", method = RequestMethod.GET)
  public String usersReview (@PathVariable("page") int page, Model model,
                             HttpServletRequest request) {
    dataPageService.getDataPage(model, request);
    userService.getUsers(page, model);
    return "usersReview";
  }

  @RequestMapping(value = "/admin/users/{page}/status/{userId}/{status}", method = RequestMethod.GET)
  public String activateUser (@PathVariable("page") int page, @PathVariable("userId") long userId,
                              @PathVariable("status") boolean status, Model model, HttpServletRequest request) {
    dataPageService.getDataPage(model, request);
    userService.activeUser(userId, status, page, model);
    return "usersReview";
  }

  @RequestMapping(value = "/admin/users/review/{userId}", method = RequestMethod.GET)
  public String userReview (@PathVariable("userId") long userId, Model model, HttpServletRequest request) {
    dataPageService.getDataPage(model, request);
    userService.getUser(userId, model);
    return "userReview";
  }

  @RequestMapping(value = "/admin/users/remove/{userId}/{page}", method = RequestMethod.GET)
  public String removeUser (@PathVariable("userId") long userId, @PathVariable("page") int page,
                            Model model, HttpServletRequest request) {
    dataPageService.getDataPage(model, request);
    userService.removeUser(userId, page, model, request);
    return "usersReview";
  }

}
