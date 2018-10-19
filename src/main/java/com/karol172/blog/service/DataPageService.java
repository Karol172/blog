package com.karol172.blog.service;

import com.karol172.blog.configuration.AppConfiguration;
import com.karol172.blog.dto.CategoryDto;
import com.karol172.blog.dto.LinkDto;
import com.karol172.blog.form.SettingsForm;
import com.karol172.blog.model.Permission;
import com.karol172.blog.model.User;
import com.karol172.blog.repository.CategoryRepository;
import com.karol172.blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;
import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class DataPageService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    @Qualifier("appConfiguration")
    private AppConfiguration appConfiguration;


    public void getDataPage(Model model, HttpServletRequest request) {

        model.addAttribute("categories", getCategories());
        model.addAttribute("titlePage", appConfiguration.getTitlePage());
        User user = userRepository
                .findFirstByUsername(request.getRemoteUser());
        if (user != null) {
            model.addAttribute("articleMenu", createArticleMenu(user));
            model.addAttribute("administrationMenu", createAdministrationMenu(user));
        }
    }

    public void get403(Model model) {
        model.addAttribute("failureInfo", "Brak dostępu!");
    }

    public void getSettingsForm (Model model) {
        model.addAttribute("successInfo", null);
        model.addAttribute("failureInfo", null);
        model.addAttribute("settingsForm", new SettingsForm(appConfiguration));
    }

    public void editSettings (Model model, BindingResult result, SettingsForm settingsForm) {
        model.addAttribute("successInfo", null);
        model.addAttribute("failureInfo", null);
        if (!result.hasErrors()) {
            //File configXml = new File(getClass().getClassLoader().getResource("static/xml/configuration.xml").getFile());

            AppConfiguration appConfiguration = new AppConfiguration();
            appConfiguration.setTitlePage(settingsForm.getTitlePage());
            appConfiguration.setArticlesPerPage(settingsForm.getArticlesPerPage());

            XMLEncoder encoder = null;
            try{
                encoder = new XMLEncoder(new BufferedOutputStream(
                        new FileOutputStream(getClass().getClassLoader().getResource("static/xml/configuration.xml").getFile())));
            } catch(FileNotFoundException fileNotFound){
                fileNotFound.printStackTrace();
                model.addAttribute("failureInfo", "Wystąpił błąd");
            }
            encoder.writeObject(appConfiguration);
            encoder.close();

            model.addAttribute("successInfo", "Pomyślnie dokonano zmian");
        }
    }

    private List<LinkDto> createArticleMenu (User user) {
        List<LinkDto> linkDtoList = new ArrayList<>();
        if (user.hasPermission("Artykuły")) {
            linkDtoList.add(new LinkDto("/admin/articles", "Przeglądaj"));
            linkDtoList.add(new LinkDto("/admin/article/add", "Dodaj"));
        }
        if (user.hasPermission("Kategorie"))
            linkDtoList.add(new LinkDto("/admin/categories", "Kategorie"));

        return linkDtoList;
    }

    private List<LinkDto> createAdministrationMenu (User user) {
        List<LinkDto> linkDtoList = new ArrayList<>();
        if (user.hasPermission("Pliki"))
            linkDtoList.add(new LinkDto("/admin/files", "Pliki"));
        if (user.hasPermission("Komentarze"))
            linkDtoList.add(new LinkDto("/admin/comments/1", "Komentarze"));
        if (user.hasPermission("Grupy Użytkowników"))
            linkDtoList.add(new LinkDto("/admin/usersgroups", "Grupy"));
        if (user.hasPermission("Użytkownicy"))
            linkDtoList.add(new LinkDto("/admin/users/1", "Użytkownicy"));
        if (user.hasPermission("Ustawienia"))
            linkDtoList.add(new LinkDto("/admin/settings", "Ustawienia"));

        return linkDtoList;
    }

    private List<CategoryDto> getCategories () {
        List<CategoryDto> categoryDtoList = new ArrayList<>();
        categoryRepository.findByOrderByNameAsc().forEach(k->categoryDtoList.add(new CategoryDto(k)));
        return categoryDtoList;
    }

}
