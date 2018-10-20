package com.karol172.blog.controllers;

import com.karol172.blog.form.ChangePasswordForm;
import com.karol172.blog.form.ForgottenPasswordForm;
import com.karol172.blog.service.DataPageService;
import com.karol172.blog.service.ForgottenPasswordService;
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
public class ForgottenPasswordController {

  @Autowired
  private DataPageService dataPageService;

  @Autowired
  private ForgottenPasswordService forgottenPasswordService;
  
  @RequestMapping(value="/forgotten/password", method= RequestMethod.GET)
  public String forgottenPasswordForm(Model model, HttpServletRequest request) {
    this.dataPageService.getDataPage(model, request);
    this.forgottenPasswordService.getForgottenPasswordForm(model);
    return "forgottenPasswordForm";
  }
  
  @RequestMapping(value="/forgotten/password/send", method=RequestMethod.POST)
  public String reportForgottenPassword(@ModelAttribute("forgottenPasswordForm") @Valid ForgottenPasswordForm form,
                                        BindingResult result, Model model, HttpServletRequest request) {
    this.dataPageService.getDataPage(model, request);
    this.forgottenPasswordService.reportForgottenPassword(form, result, model, request);
    return "forgottenPasswordForm";
  }
  
  @RequestMapping(value="/forgotten/password/change/{changeCode}", method=RequestMethod.GET)
  public String changePasswordForm(@PathVariable("changeCode") String changeCode, Model model, HttpServletRequest request) {
    this.forgottenPasswordService.checkChangeCode(changeCode, model);
    this.dataPageService.getDataPage(model, request);
    return "changePasswordForm";
  }
  
  @RequestMapping(value="/forgotten/password/try_change", method=RequestMethod.POST)
  public String changePassword(@ModelAttribute("changePasswordForm") ChangePasswordForm changePasswordForm, BindingResult result,
                               Model model, HttpServletRequest request) {
    this.forgottenPasswordService.tryChangePassword(changePasswordForm, model, result);
    this.dataPageService.getDataPage(model, request);
    return "changePasswordForm";
  }
}
