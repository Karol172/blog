package com.karol172.blog.service;

import com.karol172.blog.form.ChangePasswordForm;
import com.karol172.blog.form.ForgottenPasswordForm;
import com.karol172.blog.model.ActivationAccount;
import com.karol172.blog.model.ForgottenPassword;
import com.karol172.blog.model.User;
import com.karol172.blog.repository.ForgottenPasswordRepository;
import com.karol172.blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.thymeleaf.context.Context;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Service
public class ForgottenPasswordService {

    @Autowired
    private EmailSenderService emailSenderService;

    @Autowired
    private ForgottenPasswordRepository forgottenPasswordRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;
    
    public void getForgottenPasswordForm(Model model) {
        model.addAttribute("forgottenPasswordForm", new ForgottenPasswordForm());
    }

    public void reportForgottenPassword(ForgottenPasswordForm form, BindingResult result, Model model, HttpServletRequest request) {
        model.addAttribute("successInfo", null);
        User user = userRepository.findFirstByUsername(form.getUsernameOrEmail());
        if (user == null)
            user = userRepository.findFirstByEmail(form.getUsernameOrEmail());
        if (user == null || (user != null && !user.getActivated()))
            result.rejectValue("usernameOrEmail", "forgottenPasswordForm", "Nie znaleziono użytkownika.");

        if (!result.hasErrors()) {
            ForgottenPassword forgottenPassword = new ForgottenPassword(passwordEncoder.encode(user.getUsername())
                    .replace("/", ""), user);
            forgottenPasswordRepository.save(forgottenPassword);
            model.addAttribute("successInfo", "Na podaną skrzynkę pocztową zostały wysłane dalsze instrukcje w celu zmiany hasła");
            sendEmail(forgottenPassword, request.getHeader("host"));
        }
    }

    public void checkChangeCode(String changeCode, Model model) {
        ForgottenPassword forgottenPassword = forgottenPasswordRepository
                .findFirstByCodeForgottenPasswordAndDateChangeIsNull(changeCode);
        if (forgottenPassword != null)
            model.addAttribute("changePasswordForm", new ChangePasswordForm(changeCode));
    }

    public void tryChangePassword(ChangePasswordForm changePasswordForm, Model model, BindingResult result) {
        model.addAttribute("successInfo", null);
        model.addAttribute("failureInfo", null);
        ForgottenPassword forgottenPassword = forgottenPasswordRepository
                .findFirstByCodeForgottenPasswordAndDateChangeIsNull(changePasswordForm.getChangeCode());

        if (forgottenPassword != null && !result.hasErrors()) {
            forgottenPasswordRepository.findByUserAndDateChangeIsNull(forgottenPassword.getUser()).forEach( f -> {
                f.setDateChange(LocalDateTime.now());
                forgottenPasswordRepository.save(f);
            });
            forgottenPassword.getUser().setPassword(passwordEncoder.encode(changePasswordForm.getPassword()));
            userRepository.save(forgottenPassword.getUser());
            model.addAttribute("successInfo", "Pomyślnie dokonano zmiany hasła");
        }
        else
            model.addAttribute("failureInfo", "Wystąpił błąd");
    }

    private void sendEmail(ForgottenPassword forgottenPassword, String url) {
        Context context = new Context();
        context.setVariable("title", "Przypomnienie hasła");
        context.setVariable("content", "Witaj, " + forgottenPassword.getUser().getUsername() + "!\n" +
                "Właśnie dokonano próby przypomnienia hasła. W celu dokończenia tego procesu kliknij w poniższy link.");
        context.setVariable("link", "http://" + url + "/forgotten/password/change/" + forgottenPassword.getCodeForgottenPassword());
        emailSenderService.sendEmail(forgottenPassword.getUser().getEmail(), "Przypomnienie hasła", context);
    }
}
