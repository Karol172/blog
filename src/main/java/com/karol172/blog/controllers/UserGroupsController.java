package com.karol172.blog.controllers;

import com.karol172.blog.dto.UserGroupForm;
import com.karol172.blog.service.DataPageService;
import com.karol172.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Controller
public class UserGroupsController {

    @Autowired
    private UserService userService;

    @Autowired
    private DataPageService dataPageService;

    @RequestMapping(value = "/admin/usersgroups", method = RequestMethod.GET)
    public String userGroupReview (Model model, HttpServletRequest request) {
        dataPageService.getDataPage(model, request);
        userService.getUsersGroups(model);
        return "userGroupReview";
    }

    @RequestMapping(value = "/admin/usersgroups/list/{userGroupId}/{page}", method = RequestMethod.GET)
    public String usersReview (@PathVariable("userGroupId") long userGroupId, @PathVariable("page") int page,
                               Model model, HttpServletRequest request) {
        dataPageService.getDataPage(model, request);
        userService.getUsersFromGroup(userGroupId, page, model);
        return "userGroupUsers";
    }

    @RequestMapping(value = "/admin/usersgroups/{userGroupId}/{page}/allocate/{userId}/{destinationUserGroupId}", method = RequestMethod.GET)
    public String allocateUser (@PathVariable("userGroupId") long userGroupId, @PathVariable("page") int page,
                                @PathVariable("userId") long userId, @PathVariable("destinationUserGroupId") long destinationId,
                                Model model, HttpServletRequest request) {
        dataPageService.getDataPage(model, request);
        StringBuilder result = new StringBuilder();
        userService.allocateUser(userId, destinationId, page, userGroupId, model, request, result);
        return result.toString();
    }

    @RequestMapping(value = "/admin/usersgroups/add", method = RequestMethod.GET)
    public String add (Model model, HttpServletRequest request) {
        dataPageService.getDataPage(model, request);
        userService.getUserGroupForm(model);
        return "userGroupForm";
    }

    @RequestMapping(value = "/admin/usersgroups/edit/{userGroupId}", method = RequestMethod.GET)
    public String editUserGroup (@PathVariable("userGroupId") long userGroupId, Model model, HttpServletRequest request) {
        dataPageService.getDataPage(model, request);
        userService.getUserGroupForm(model, userGroupId);
        return "userGroupForm";
    }

    @RequestMapping(value = "/admin/usersgroups/try_add", method = RequestMethod.POST)
    public String changeUserGroup (@ModelAttribute("userGroupForm") @Valid UserGroupForm userGroupForm,
                                   @ModelAttribute("newPermissions") List<String> newPermission,
                                   BindingResult bindingResult,Model model, HttpServletRequest request) {
        dataPageService.getDataPage(model, request);
        userService.changeUserGroup(userGroupForm, bindingResult, model, newPermission);
        return "userGroupForm";
    }

    @RequestMapping(value = "/admin/usersgroups/remove/{id}", method = RequestMethod.GET)
    public String removeUserGroup (@PathVariable("id") long id, Model model, HttpServletRequest request) {
        dataPageService.getDataPage(model, request);
        userService.removeUserGroup(model, id);
        userService.getUsersGroups(model);
        return "userGroupReview";
    }
}