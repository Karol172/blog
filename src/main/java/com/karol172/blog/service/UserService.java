package com.karol172.blog.service;

import com.karol172.blog.dto.ArticleDto;
import com.karol172.blog.dto.UserDto;
import com.karol172.blog.dto.UserGroupDto;
import com.karol172.blog.dto.UserGroupForm;
import com.karol172.blog.model.User;
import com.karol172.blog.model.UserGroup;
import com.karol172.blog.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserGroupRepository userGroupRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private FileRepostiory fileRepository;

    public void getUsersGroups (Model model) {
        List<UserGroupForm> list = new ArrayList<>();
        userGroupRepository.findByOrderByNameAsc().forEach(k->list.add(new UserGroupForm(k, permissionRepository.findByOrderByName())));
        model.addAttribute("usersgroups", list);
    }

    public void removeUserGroup (Model model, long id) {
        model.addAttribute("successInfo", null);
        UserGroup userGroup = userGroupRepository.findFirstById(id);
        UserGroup users = userGroupRepository.findFirstByName("Użytkownicy");
        if (userGroup != null && users != null && !userGroup.getName().equals("Administratorzy")
                && !userGroup.getName().equals("Użytkownicy")) {
            userGroup.getUsers().forEach(u -> u.setUserGroup(users));
            userGroup.getPermissions().forEach(p->userGroup.removePermission(p));
            userGroupRepository.deleteById(id);
            model.addAttribute("successInfo", "Pomyślnie usunięto grupę.");
        }
    }

    public void getUserGroupForm (Model model) {
        model.addAttribute("userGroupForm", new UserGroupForm(permissionRepository.findByOrderByName()));
    }

    public void getUserGroupForm (Model model, long id) {
        model.addAttribute("successInfo", null);
        model.addAttribute("failureInfo", null);
        model.addAttribute("userGroupForm", null);
        UserGroup userGroup = userGroupRepository.findFirstById(id);
        if (userGroup != null)
            model.addAttribute("userGroupForm", new UserGroupForm(userGroup, permissionRepository.findByOrderByName()));
        else
            model.addAttribute("failureInfo", "Nie znaleziono grupy.");

    }

    public void changeUserGroup (UserGroupForm userGroupForm, BindingResult result, Model model, List<String> newPermissions) {
        model.addAttribute("successInfo", null);
        model.addAttribute("failureInfo", null);

        newPermissions.remove("none");
        userGroupForm.addPermissions(permissionRepository.findByOrderByName());
        newPermissions.forEach(k->userGroupForm.selectPermissions(k, true));

        UserGroup userGroupById = userGroupRepository.findFirstById(userGroupForm.getId());
        if (userGroupForm.getId() == null && userGroupRepository.findFirstByName(userGroupForm.getName()) != null)
            result.rejectValue("name", "userGroup", "Taka grupa użytkowników już istnieje.");
        else if (userGroupForm.getId() != null) {
            if (userGroupById != null && !userGroupById.getName().equals(userGroupForm.getName())
                    && userGroupRepository.findFirstByName(userGroupForm.getName()) != null)
                result.rejectValue("name", "userGroup", "Taka grupa użytkowników już istnieje.");
            else if (userGroupById == null) {
                model.addAttribute("failureInfo", "Nie znaleziono grupy");
                return;
            }
        }
        if (!result.hasErrors()) {
            UserGroup userGroup = null;
            if (userGroupById != null) {
                userGroup = userGroupById;
                userGroup.setName(userGroupForm.getName());
                userGroup.setDescription(userGroupForm.getDescription());
            }
            else
                userGroup = new UserGroup(userGroupForm.getName(), userGroupForm.getDescription());


            final UserGroup userGroupToSave = userGroup;
            if (!userGroupToSave.getName().equals("Administratorzy")) {
                userGroupToSave.getPermissions().stream().filter(k -> !newPermissions.contains(k.getName()))
                        .collect(Collectors.toList()).forEach(k -> userGroupToSave.removePermission(k));
                newPermissions.forEach(np -> userGroupToSave.addPermission(permissionRepository.findFirstByName(np)));
            }
            userGroupRepository.save(userGroupToSave);
            model.addAttribute("successInfo","Pomyślnie dokonano zmian.");
        }
    }

    public void getUsersFromGroup (long userGroupId, int page, Model model) {
        UserGroup userGroup = userGroupRepository.findFirstById(userGroupId);
        if (userGroup != null) {
            List<User> userList = userRepository.findByOrderByUsername();
            int usersPageNumber = getUsersPageNumber(userList);
            int pageNumber = page > usersPageNumber ? usersPageNumber : page < 0 ? 0 : page;
            model.addAttribute("currentPage", pageNumber);
            model.addAttribute("numberPages", usersPageNumber);
            model.addAttribute("users", getUsersDividedIntoPages(userList, pageNumber));
            model.addAttribute("userGroup", new UserGroupDto(userGroup));
        }
        else
            model.addAttribute("failureInfo", "Taka grupa nie istnieje.");
    }

    public void allocateUser (long userId, long groupId, int page, long groupPage, Model model, HttpServletRequest request, StringBuilder result) {
        model.addAttribute("successInfo", null);
        model.addAttribute("failureInfo", null);
        model.addAttribute("failure2Info", null);
        if (groupId == 0)
            groupId = userGroupRepository.findFirstByName("Użytkownicy").getId();
        UserGroup userGroup = userGroupRepository.findFirstById(groupId);
        if (userGroup != null) {
            User user = userRepository.findFirstById(userId);
            if (user != null) {
                if (user.getUserGroup().getName().equals("Administratorzy") && user.getUserGroup().getUsers().size() == 1)
                    model.addAttribute("failure2Info", "Nie można usunąć ostatniego administratora.");
                else {
                    user.setUserGroup(userGroup);
                    userRepository.save(user);
                    if (user.getUsername().equals(request.getRemoteUser())) {
                        User loggedUser = userRepository.findFirstByUsername(request.getRemoteUser());
                        if (loggedUser != null) {
                            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                            List<GrantedAuthority> authorities = new ArrayList<>();
                            user.getUserGroup().getPermissions().forEach(p -> authorities.add(new SimpleGrantedAuthority(p.getName())));
                            authorities.forEach(k->System.out.println(k.getAuthority()));
                            SecurityContextHolder.getContext().setAuthentication(
                                    new UsernamePasswordAuthenticationToken(auth.getPrincipal(), auth.getCredentials(), authorities));
                            result.append("redirect:/" );
                        }
                    }
                    model.addAttribute("successInfo", "Pomyślnie dokonano zmiany grupy");
                }
            }
        }
        getUsersFromGroup(groupPage, page, model);
        if (result.toString().length() == 0)
            result.append("userGroupUsers" );
    }

    public void getUsers (int page, Model model) {
        List<User> userList = userRepository.findAll();
        int usersPageNumber = getUsersPageNumber(userList);
        int pageNumber = page > usersPageNumber ? usersPageNumber : page < 0 ? 0 : page;
        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("numberPages", usersPageNumber);
        model.addAttribute("users", getUsersDividedIntoPages(userList, pageNumber));
    }

    public void activeUser(long userId, boolean status, int page, Model model) {
        User user = userRepository.findFirstById(userId);
        if (user != null && user.getActivationAccount() != null
                && user.getActivationAccount().getDateActivation() != null) {
            user.setActivated(status);
            userRepository.save(user);
        }
        getUsers(page, model);
    }

    public void removeUser(long userId, int page, Model model, HttpServletRequest request) {
        User user = userRepository.findFirstById(userId);
        if (user != null && user.getActivationAccount() != null
                && user.getActivationAccount().getDateActivation() != null) {
            if (user.getUserGroup().getName().equals("Administratorzy") && user.getUserGroup().getUsers().size() == 1)
                model.addAttribute("failureInfo", "Nie można usunąć ostatniego administratora");
            else if (user.getUsername().equals(request.getRemoteUser()))
                model.addAttribute("failureInfo", "Nie możesz usunąć swojego konta.");
            else {
                user.getComments().forEach(c -> {
                    c.setAuthorUser(null);
                    commentRepository.save(c);
                });

                user.getArticles().forEach(a -> {
                    a.removeAuthor(user);
                    articleRepository.save(a);
                });

                user.getSendedFiles().forEach(f -> {
                    f.setSender(null);
                    fileRepository.save(f);
                });
                userRepository.deleteById(user.getId());
                model.addAttribute("successInfo", "Pomyślnie usunięto użytkownika");
            }
            getUsers(page, model);
        }
    }

    public void getUser (long id, Model model) {
        User user = userRepository.findFirstById(id);
        if (user != null) {
            model.addAttribute("user", new UserDto(user));
            model.addAttribute("articles", getArticlesOfAuthor(user));
        }
    }

    private List<ArticleDto> getArticlesOfAuthor (User user) {
        List<ArticleDto> list = new ArrayList<>();
        user.getArticles().forEach(a->list.add(new ArticleDto(a)));
        Collections.sort(list);
        return list;
    }

    private List<UserDto> getUsersDividedIntoPages (List<User> userList, int page) {
        if (page == 0)
            return null;

        int usersPerPage = 15;
        List<UserDto> list = new ArrayList<>();
        int beginIndex = (page - 1) * usersPerPage < userList.size()
                ? (page - 1) * usersPerPage : userList.size() > 0 ? userList.size() - 1 : 0;
        int endIndex = (page - 1) * usersPerPage + usersPerPage - 1 < userList.size()
                ? (page - 1) * usersPerPage + usersPerPage : userList.size();
        userList.subList(beginIndex, endIndex).forEach(k->list.add(new UserDto(k)));
        return list;
    }

    private int getUsersPageNumber (List<User> userList) {
        return (int)Math.ceil((double) userList.size() / 15);
    }
}
