package com.karol172.blog.controllers;

import com.karol172.blog.form.RegistrationUserForm;
import com.karol172.blog.service.DataPageService;
import com.karol172.blog.service.RegistrationService;
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
public class RegistrationController {

  @Autowired
  private RegistrationService registrationService;

  @Autowired
  private DataPageService dataPageService;
  
  @RequestMapping(value="/registration", method= RequestMethod.GET)
  public String registrationForm(Model model, HttpServletRequest request) {
    this.registrationService.getRegistrationUserForm(model);
    this.dataPageService.getDataPage(model, request);
    return "registrationForm";
  }
  
  @RequestMapping(value="/registration/try_register", method=RequestMethod.POST)
  public String registerUser(@ModelAttribute("registrationUserForm") @Valid RegistrationUserForm registrationUserForm,
                             BindingResult result, Model model, HttpServletRequest request) {
    this.registrationService.tryRegisterNewUser(registrationUserForm, result, model, request);
    this.dataPageService.getDataPage(model, request);
    return "registrationForm";
  }
  
  @RequestMapping(value="/registration/activation/{activationCode}", method=RequestMethod.GET)
  public String activateAccount(@PathVariable("activationCode") String activationCode,
                                Model model, HttpServletRequest request) {
    this.registrationService.tryActivateAccount(activationCode, model);
    this.dataPageService.getDataPage(model, request);
    return "message";
  }
}
