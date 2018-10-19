package com.karol172.blog.service;

import com.karol172.blog.dto.ArticleDto;
import com.karol172.blog.dto.CategoryDto;
import com.karol172.blog.dto.FileDto;
import com.karol172.blog.form.ArticleForm;
import com.karol172.blog.model.*;
import com.karol172.blog.repository.ArticleRepository;
import com.karol172.blog.repository.CategoryRepository;
import com.karol172.blog.repository.FileRepostiory;
import com.karol172.blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
public class ArticlesManagmentService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FileRepostiory fileRepostiory;

    public void getArticleForm (Model model, HttpServletRequest request){
        model.addAttribute("successInfo", null);
        model.addAttribute("failureInfo", null);
        Collection<String> authors = new HashSet<>();
        authors.add(request.getRemoteUser());
        model.addAttribute("articleForm", new ArticleForm(null, null, null, null,
                LocalDate.now(), LocalTime.now(), LocalDate.now(), LocalTime.now(), true, true,
                null, authors));
        model.addAttribute("images", getRecentlyAddedImages());
    }

    public void getArticleForm (Model model, HttpServletRequest request, long idArticle){
        model.addAttribute("successInfo", null);
        model.addAttribute("failureInfo", null);
        Collection<String> authors = new HashSet<>();
        authors.add(request.getRemoteUser());
        List<FileDto> images = new ArrayList<>();

        ArticleForm articleForm = null;
        Article article = articleRepository.findFirstById(idArticle);
        if (article != null) {
            articleForm = new ArticleForm(article);
            articleForm.getAuthors().add(request.getRemoteUser());
            images.addAll(getRecentlyAddedImages());
        }
        else
            model.addAttribute("failureInfo", "Wystąpił bład");

        model.addAttribute("articleForm", articleForm);
        model.addAttribute("images", images);
    }

    public void getArticlesDivadedIntoCategories (Model model) {
        List<List<ArticleDto>> listArticles = new ArrayList<>();
        categoryRepository.findByOrderByNameAsc().forEach(category->{
            List<ArticleDto> list = new ArrayList<>();
            articleRepository.findByCategoryOrderByPublicationDateDesc(category)
                    .forEach(article->list.add(new ArticleDto(article)));
            listArticles.add(list); });
        model.addAttribute("articles", listArticles);
    }

    public void try_add (ArticleForm articleForm, Model model, BindingResult result, HttpServletRequest request) {
        if (articleForm.getAuthors().isEmpty())
            result.rejectValue("authors", "articleForm", "Artykuł musi mieć autora.");
        model.addAttribute("successInfo", null);
        model.addAttribute("failureInfo", null);
        final Article article = articleRepository.findFirstById(articleForm.getId());
        if (!result.hasErrors()) {
            if (article != null) {
                article.setTitle(articleForm.getTitle());
                article.setContents(articleForm.getContents());
                article.setContentsMore(articleForm.getContentsMore());
                article.setCategory(categoryRepository.findFirstByName(articleForm.getCategory()));
                article.setActive(articleForm.getActive());
                article.setCanComment(articleForm.getCanComment());
                article.setPublicationDate(LocalDateTime.of(articleForm.getPublicationDate(),
                        articleForm.getPublicationTime()));
                Set<User> authors = new HashSet<>();
                articleForm.getAuthors().forEach(a->authors.add(userRepository.findFirstByUsername(a)));
                article.setAuthors(authors);
                articleRepository.save(article);
                model.addAttribute("successInfo", "Pomyślnie zmodyfikowano artykuł");
            }
            else {
                Article articleAdd = new Article(articleForm, categoryRepository
                        .findFirstByName(articleForm.getCategory()));
                articleForm.getAuthors().forEach(k -> articleAdd.addAuthor(userRepository.findFirstByUsername(k)));
                if (!alreadyAdded(articleAdd)) {
                    articleRepository.save(articleAdd);
                }
                model.addAttribute("successInfo", "Pomyślnie dodano nowy artykuł");
            }
        }
        else {
            if (article != null) {
                Set<String> authors = new HashSet<>();
                article.getAuthors().forEach(a -> authors.add(a.getUsername()));
                articleForm.setAuthors(authors);
                articleForm.getAuthors().add(request.getRemoteUser());
            }
        }
    }

    public void removeArticle (long id, Model model) {
        model.addAttribute("successInfo", null);
        model.addAttribute("failureInfo", null);

        Article article = articleRepository.findFirstById(id);
        if (article != null) {
            article.getCategory().getArticles().remove(article);
            articleRepository.deleteById(article.getId());
            model.addAttribute("successInfo", "Pomyślnie usunięto artykuł.");
        }
    }

    public void getCategoryForm (Model model) {
        model.addAttribute("categoryForm", new CategoryDto());
    }

    public void getCategoryForm (Model model, long id) {
        model.addAttribute("failureInfo", null);
        Category category = categoryRepository.findFirstById(id);
        if (category != null)
            model.addAttribute("categoryForm", new CategoryDto(category));
        else {
            model.addAttribute("cattegoryForm", null);
            model.addAttribute("failureInfo", "Wystąpił błąd");
        }
    }

    public void tryChangeCategory (Model model, BindingResult bindingResult, CategoryDto categoryDto) {
        model.addAttribute("successInfo", null);
        model.addAttribute("failureInfo", null);
        Category category = categoryRepository.findFirstByName(categoryDto.getName());
        if (category != null && category.getId() != categoryDto.getId())
            bindingResult.rejectValue("name", "category", "Kategoria o takiej nazwie już istnieje");
        else if (category == null && categoryDto.getId() != null)
            category = categoryRepository.findFirstById(categoryDto.getId());

        if (category != null && category.getId() == categoryDto.getId() && !bindingResult.hasErrors()) {
            category.setName(categoryDto.getName());
            category.setDescription(categoryDto.getDescription());
        }
        else if (category == null)
            category = new Category(categoryDto.getName(), categoryDto.getDescription());

        if (!bindingResult.hasErrors()) {
            categoryRepository.save(category);
            model.addAttribute("successInfo", "Pomyślnie dodano zmian");
        }
    }


    public void removeCategory (Model model, long id) {
        model.addAttribute("successInfo", null);
        model.addAttribute("failureInfo", null);
        Optional<Category> category = categoryRepository.findById(id);
        if (category.isPresent()) {
            categoryRepository.delete(category.get());
            model.addAttribute("successInfo", "Pomyślnie usunięto kategorię i przypisane do niej artykuły");
        } else
            model.addAttribute("failureInfo", "Wystąpił błąd");
    }

    private boolean alreadyAdded (Article article) {
        Article temp = articleRepository.findFirstByTitleAndContentsAndContentsMoreAndCategoryAndAdditionDateAndPublicationDateAndIsActiveAndCanComment
                (article.getTitle(), article.getContents(), article.getContentsMore(), article.getCategory(),
                        article.getPublicationDate(), article.getAdditionDate(), article.getActive(), article.getCanComment());
        if (temp != null) {
            Set<User> authors = temp.getAuthors();
            authors.removeAll(article.getAuthors());

            Set<Comment> comments = temp.getComments();
            comments.removeAll(article.getComments());

            if (authors.isEmpty() && comments.isEmpty())
                return true;
        }
        return false;
    }

    private List<FileDto> getRecentlyAddedImages () {
        List<File> recentlyAddedImagesList = fileRepostiory.findByOrderByAdditionDateDesc();
        List<FileDto> list = new ArrayList<>();
        if (!recentlyAddedImagesList.isEmpty())
            recentlyAddedImagesList.subList(0, recentlyAddedImagesList.size() > 10 ? 10 : recentlyAddedImagesList.size())
            .forEach(k->list.add(new FileDto(k)));
        return list;
    }
}