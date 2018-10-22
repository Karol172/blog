package com.karol172.blog.service;

import com.karol172.blog.configuration.AppConfiguration;
import com.karol172.blog.dto.ArticleDto;
import com.karol172.blog.form.CommentForm;
import com.karol172.blog.model.Article;
import com.karol172.blog.model.User;
import com.karol172.blog.repository.ArticleRepository;
import com.karol172.blog.repository.CategoryRepository;
import com.karol172.blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ArticlesService {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Resource(name = "configuration")
    private AppConfiguration appConfiguration;

    public void getArticlesOfCategory (Model model, long categoryId, int pageNumber) {
        int numberPages = getPagesNumber(categoryId);
        pageNumber = pageNumber < 1 ? (numberPages > 0 ? 1 : 0) : (pageNumber > numberPages ? numberPages : pageNumber);
        model.addAttribute("articles", getArticlesBelongingToCategory(categoryId, pageNumber));
        model.addAttribute("numberPages", getPagesNumber(categoryId));
        model.addAttribute("selectedCategory", categoryId);
        model.addAttribute("currentPage", pageNumber);
    }

    public void getAllArticles (Model model, int pageNumber) {
        int numberPages = getPagesNumber();
        int currentPage = pageNumber < 1 ? (numberPages > 0 ? 1 : 0) : (pageNumber > numberPages ? numberPages : pageNumber);
        model.addAttribute("articles", getArticlesDividedIntoPages(currentPage));
        model.addAttribute("numberPages", getPagesNumber());
        model.addAttribute("currentPage", currentPage);
    }

    public void getArticle (Model model, long id) {
        model.addAttribute("successInfo", null);
        model.addAttribute("failureInfo", null);
        model.addAttribute("commentSuccessInfo", null);
        Article article = articleRepository.findFirstById(id);
        if (article != null)
            model.addAttribute("article", new ArticleDto(article));
        else {
            model.addAttribute("article", null);
            model.addAttribute("failureInfo", "Nie znaleziono artykułu.");
        }
    }


    private List<Article> getArticlesDividedIntoPages (int pageNumber) {
        if (pageNumber == 0)
            return null;

        int articlesPerPage = appConfiguration.getArticlesPerPage();
        List<Article> list = articleRepository.findByIsActiveAndPublicationDateLessThanEqualOrderByPublicationDateDesc(
                true, LocalDateTime.now());
        int beginIndex = (pageNumber - 1) * articlesPerPage < list.size()
                ? (pageNumber - 1) * articlesPerPage : list.size() > 0 ? list.size() - 1 : 0;
        int endIndex = (pageNumber - 1) * articlesPerPage + articlesPerPage - 1 < list.size()
                ? (pageNumber - 1) * articlesPerPage + articlesPerPage : list.size();
        return list.subList(beginIndex, endIndex);
    }

    private List<ArticleDto> getArticlesBelongingToCategory (long categoryId, int pageNumber) {
        if (pageNumber == 0)
            return null;

        int articlesPerPage = appConfiguration.getArticlesPerPage();
        List<ArticleDto> list = new ArrayList<>();
        articleRepository.findByCategoryAndIsActiveAndPublicationDateLessThanEqualOrderByPublicationDateDesc
        (categoryRepository.findFirstById(categoryId), true, LocalDateTime.now()).forEach(k->list.add(new ArticleDto(k)));

        int beginIndex = (pageNumber - 1) * articlesPerPage < list.size()
                ? (pageNumber - 1) * articlesPerPage : list.size() > 0 ? list.size() - 1 : 0;
        int endIndex = (pageNumber - 1) * articlesPerPage + articlesPerPage - 1 < list.size()
                ? (pageNumber - 1) * articlesPerPage + articlesPerPage : list.size();
        return list.subList(beginIndex, endIndex);
    }

    private int getPagesNumber () {
        return (int)Math.ceil((double)articleRepository.findByIsActiveAndPublicationDateLessThanEqualOrderByPublicationDateDesc(
                true, LocalDateTime.now()).size()/appConfiguration.getArticlesPerPage());
    }

    private int getPagesNumber(long categoryId) {
        return (int)Math.ceil((double)articleRepository.findByCategoryAndIsActiveAndPublicationDateLessThanEqualOrderByPublicationDateDesc
                (categoryRepository.findFirstById(categoryId), true, LocalDateTime.now()).size()/appConfiguration.getArticlesPerPage());
    }
}
