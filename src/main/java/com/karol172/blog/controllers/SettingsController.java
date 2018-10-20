package com.karol172.blog.controllers;

import com.karol172.blog.form.SettingsForm;
import com.karol172.blog.service.DataPageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
public class SettingsController {

    @Autowired
    private DataPageService dataPageService;

    @RequestMapping(value = "/admin/settings", method = RequestMethod.GET)
    public String settingsForm (Model model, HttpServletRequest request) {
        dataPageService.getDataPage(model, request);
        dataPageService.getSettingsForm(model);
        return "settingsForm";
    }

    @RequestMapping(value = "/admin/settings/edit", method = RequestMethod.POST)
    public String changeSettings (@ModelAttribute("settingsForm") @Valid SettingsForm settingsForm, BindingResult result,
                                  Model model, HttpServletRequest request) {
        dataPageService.getDataPage(model, request);
        dataPageService.editSettings(model, result, settingsForm);
        return "settingsForm";
    }
}
