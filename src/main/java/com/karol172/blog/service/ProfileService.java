package com.karol172.blog.service;

import com.karol172.blog.controllers.FileController;
import com.karol172.blog.form.ChangePasswordAsUserForm;
import com.karol172.blog.form.ProfileForm;
import com.karol172.blog.model.User;
import com.karol172.blog.repository.UserRepository;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;

@Service
public class ProfileService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StorageService storageService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void getChangePasswordUserForm (Model model) {
        model.addAttribute("changePasswordUserForm", new ChangePasswordAsUserForm());
    }

    public void getProfileForm (Model model, HttpServletRequest request) {
        User user = userRepository.findFirstByUsername(request.getRemoteUser());
        model.addAttribute("profileForm", new ProfileForm(user));
        model.addAttribute("userAvatar",
                user.getAvatarPath() != null ? "/images/"+ user.getAvatarPath() : "../../../img/avatar__none.jpg");
    }

    public void editUserDetails(Model model, BindingResult result, ProfileForm profileForm) {
        model.addAttribute("successInfo", null);
        if (!result.hasErrors()) {
            User user = userRepository.findFirstByUsername(profileForm.getUsername());
            if (user != null) {
                user.setFirstName(profileForm.getFirstName());
                user.setLastName(profileForm.getLastName());
                if (!profileForm.getAvatar().isEmpty()) {
                    String filename = "avatar_" + user.getUsername() + '.' +
                            FilenameUtils.getExtension(profileForm.getAvatar().getOriginalFilename());
                    storageService.store(profileForm.getAvatar(), filename);
                    user.setAvatarPath(filename);
                }
                userRepository.save(user);
                model.addAttribute("userAvatar",
                        user.getAvatarPath() != null ? "/images/"+ user.getAvatarPath() : "../../../img/avatar__none.jpg");
                model.addAttribute("successInfo", "Pomyślnie dokonano zmian");
            }
        }
    }

    public void tryChangePassword (ChangePasswordAsUserForm changePasswordAsUserForm, Model model,
                                   BindingResult bindingResult, HttpServletRequest request) {
        model.addAttribute("successInfo", null);
        User user = userRepository.findFirstByUsername(request.getRemoteUser());
        if (user != null) {
            if (!passwordEncoder.matches(changePasswordAsUserForm.getCurrentPassword(), user.getPassword()))
                bindingResult.rejectValue("currentPassword", "user", "Podane hasło jest nieprawidłowe");
            else {
                user.setPassword(passwordEncoder.encode(changePasswordAsUserForm.getPassword()));
                userRepository.save(user);
                model.addAttribute("successInfo", "Pomyślnie dokonano zmiany hasła");
            }
        }
    }
}