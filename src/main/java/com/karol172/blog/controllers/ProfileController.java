package com.karol172.blog.controllers;

import com.karol172.blog.form.ChangePasswordAsUserForm;
import com.karol172.blog.form.ProfileForm;
import com.karol172.blog.service.DataPageService;
import com.karol172.blog.service.ProfileService;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ProfileController {

  @Autowired
  private ProfileService profileService;

  @Autowired
  private DataPageService dataPageService;
  
  @RequestMapping(value="/user/profile", method= RequestMethod.GET)
  public String profileForm(Model model, HttpServletRequest request) {
    this.profileService.getProfileForm(model, request);
    this.dataPageService.getDataPage(model, request);
    return "profileForm";
  }
  
  @RequestMapping(value="/user/profile/edit", method=RequestMethod.POST)
  public String editProfile(@ModelAttribute("profileForm") ProfileForm profileForm, BindingResult result,
                            Model model, HttpServletRequest request) {
    this.profileService.editUserDetails(model, result, profileForm);
    this.dataPageService.getDataPage(model, request);
    return "profileForm";
  }
  
  @RequestMapping(value="/user/password", method=RequestMethod.GET)
  public String changePasswordForm(Model model, HttpServletRequest request) {
    this.profileService.getChangePasswordUserForm(model);
    this.dataPageService.getDataPage(model, request);
    return "changePasswordAsUserForm";
  }
  
  @RequestMapping(value="/user/password/change", method=RequestMethod.POST)
  public String changePassword(@ModelAttribute("changePasswordUserForm") @Valid ChangePasswordAsUserForm changePasswordAsUserForm,
                               BindingResult result, Model model, HttpServletRequest request) {
    this.profileService.tryChangePassword(changePasswordAsUserForm, model, result, request);
    this.dataPageService.getDataPage(model, request);
    return "changePasswordAsUserForm";
  }
}