package com.karol172.blog.service;

import com.karol172.blog.form.RegistrationUserForm;
import com.karol172.blog.model.ActivationAccount;
import com.karol172.blog.model.User;
import com.karol172.blog.repository.ActivationAccountRepository;
import com.karol172.blog.repository.UserGroupRepository;
import com.karol172.blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.thymeleaf.context.Context;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
public class RegistrationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserGroupRepository userGroupRepository;

    @Autowired
    private ActivationAccountRepository activationAccountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailSenderService emailSenderService;

    public void getRegistrationUserForm(Model model) {
        model.addAttribute("registrationUserForm", new RegistrationUserForm());
    }

    public void tryRegisterNewUser(RegistrationUserForm registrationUserForm, BindingResult result, Model model,
                                   HttpServletRequest request) {
        model.addAttribute("successInfo", null);

        User u = userRepository.findFirstByUsernameAndEmailAndFirstNameAndLastNameAndAvatarPathAndIsActivatedAndUserGroup
                (registrationUserForm.getUsername(), registrationUserForm.getEmail(),
                        registrationUserForm.getFirstName(), registrationUserForm.getLastName(),
                        "avatar__none.jpg", false, userGroupRepository.findFirstByName("Użytkownicy"));

        if (u != null && passwordEncoder.matches(registrationUserForm.getPassword(), u.getPassword()))
            model.addAttribute("successInfo", "Pomyślnie zarejestrowano nowego użytkownika. " +
                    "Na podany e-mail została wysłana wiadomość w celu aktywacji konta.");
        else {
            int errorsNumber = 0;
            if (!userRepository.findAll().stream().filter((User user) -> user.getUsername().toLowerCase()
                    .equals(registrationUserForm.getUsername().toLowerCase())).collect(Collectors.toList()).isEmpty()) {
                result.rejectValue("username", "user", "Użytkownik o takiej nazwie już istnieje. Wprowadź inny login");
                errorsNumber++;
            }
            if (!userRepository.findAll().stream().filter((User user) -> user.getEmail().toLowerCase()
                    .equals(registrationUserForm.getEmail().toLowerCase())).collect(Collectors.toList()).isEmpty()) {
                result.rejectValue("email", "user", "Użytkownik o takim mail'u znajduje się już w bazie danych.");
                errorsNumber++;
            }
            if (!registrationUserForm.getPassword().equals(registrationUserForm.getRepeatedPassword())) {
                result.rejectValue("repeatedPassword", "user", "Wprowadzone hasła się różnią.");
                errorsNumber++;
            }
            if (errorsNumber == 0 && !result.hasErrors()) {
                User user = new User(registrationUserForm.getUsername(),
                        passwordEncoder.encode(registrationUserForm.getPassword()), registrationUserForm.getEmail(),
                        registrationUserForm.getFirstName(), registrationUserForm.getLastName(), "avatar__none.jpg",
                        false, userGroupRepository.findFirstByName("Użytkownicy"));
                userRepository.save(user);
                ActivationAccount activationAccount =
                        new ActivationAccount(user, LocalDateTime.now(),
                                passwordEncoder.encode(user.getUsername()).replace("/", ""));
                activationAccountRepository.save(activationAccount);
                model.addAttribute("successInfo", "Pomyślnie zarejestrowano nowego użytkownika. " +
                        "Na podany e-mail została wysłana wiadomość w celu aktywacji konta.");
                sendActivationEmail(activationAccount, request.getHeader("host"));
            }
        }
    }

    public void tryActivateAccount (String activationCode, Model model) {
        ActivationAccount activationAccount = activationAccountRepository.findFirstByActivationCode(activationCode);
        model.addAttribute("successInfo", null);
        model.addAttribute("failureInfo", null);

        if (activationAccount != null && activationAccount.getDateActivation() == null &&!activationAccount.getUser().getActivated()) {
            activationAccount.setDateActivation(LocalDateTime.now());
            activationAccount.getUser().setActivated(true);
            activationAccountRepository.save(activationAccount);
            userRepository.save(activationAccount.getUser());
            model.addAttribute("successInfo", "Pomyślnie dokonano aktywacji. Możesz się już zalogować.");
        }
        else if (activationAccount != null && activationAccount.getDateActivation() != null && activationAccount.getUser().getActivated())
            model.addAttribute("successInfo", "Pomyślnie dokonano aktywacji. Możesz się już zalogować.");
        else
            model.addAttribute("failureInfo", "Podany link jest błędny.");
    }

    private void sendActivationEmail(ActivationAccount activationAccount, String url) {
        Context context = new Context();
        context.setVariable("title", "Aktywacja konta");
        context.setVariable("content", "Witaj, " + activationAccount.getUser().getUsername() + "!\n" +
                "Właśnie dokonano rejestracji konta w naszym serwisie. W celu aktywacji konta kliknij w poniższy link.");
        context.setVariable("link", "http://" + url + "/registration/activation/" + activationAccount.getActivationCode());
        emailSenderService.sendEmail(activationAccount.getUser().getEmail(), "Aktywacja konta", context);
    }
}