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
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
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

    @Resource(name = "configuration")
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
            try {
                File fXmlFile = new File(getClass().getResource("/static/xml/configuration.xml").getFile());
                DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
                Document doc = docBuilder.parse(fXmlFile);
                doc.getDocumentElement().normalize();
                NodeList nList = doc.getElementsByTagName("configuration");
                Node node = nList.getLength() == 1 ? nList.item(0) : null;

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    element.getElementsByTagName("articlesPerPage").item(0).setTextContent(String.valueOf(settingsForm.getArticlesPerPage()));
                    element.getElementsByTagName("titlePage").item(0).setTextContent(settingsForm.getTitlePage());

                    TransformerFactory transformerFactory = TransformerFactory.newInstance();
                    Transformer transformer = transformerFactory.newTransformer();
                    DOMSource source = new DOMSource(doc);
                    StreamResult streamResult = new StreamResult(fXmlFile);
                    transformer.transform(source, streamResult);
                    model.addAttribute("successInfo", "Pomyślnie dokonano zmian");
                }

            } catch (Exception e ) {
                e.printStackTrace();
                model.addAttribute("failureInfo", "Wystąpił błąd");
            }
        }
    }

    private List<LinkDto> createArticleMenu (User user) {
        List<LinkDto> linkDtoList = new ArrayList<>();
        if (user.hasPermission("Artykuły")) {
            linkDtoList.add(new LinkDto("/admin/articles", "Przeglądaj"));
            linkDtoList.add(new LinkDto("/admin/articles/add", "Dodaj"));
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
