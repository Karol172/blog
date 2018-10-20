package com.karol172.blog.service;

import com.karol172.blog.dto.ArticleDto;
import com.karol172.blog.dto.CommentDto;
import com.karol172.blog.form.CommentForm;
import com.karol172.blog.model.Article;
import com.karol172.blog.model.Comment;
import com.karol172.blog.model.User;
import com.karol172.blog.repository.ArticleRepository;
import com.karol172.blog.repository.CommentRepository;
import com.karol172.blog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    public void getCommentForm (Model model, HttpServletRequest request) {
        User user = userRepository.findFirstByUsername(request.getRemoteUser());
        model.addAttribute("commentForm", new CommentForm(LocalDateTime.now(), user != null ? user.getId(): null));
    }

    public void getCommentForm (Model model, long id) {
        model.addAttribute("commentForm", new CommentForm(commentRepository.findFirstById(id)));
    }

    public void getArticles (int page, Model model) {
        model.addAttribute("successInfo", null);
        model.addAttribute("failureInfo", null);
        List<Article> articleList = articleRepository.findByOrderByPublicationDateDesc();
        int articlesPageNumber = getItemPageNumber(articleList);
        int pageNumber = page > articlesPageNumber ? articlesPageNumber : page < 0 ? 0 : page;
        List<ArticleDto> articles = new ArrayList<>();
        getItemsDividedIntoPages(articleList, page, 30).forEach(k->articles.add(new ArticleDto((Article)k)));
        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("numberPages", articlesPageNumber);
        model.addAttribute("headerArticle", articles);
    }

    public void getCommentsOfArticle (long articleId, int page, Model model) {
        Article article = articleRepository.findFirstById(articleId);
        if (article != null) {
            List<Comment> list = commentRepository.findByArticleOrderByAdditionDateDesc(article);
            List<CommentDto> commentList = new ArrayList<>();
            int commentsPageNumber = getItemPageNumber(list);
            getItemsDividedIntoPages(list, page, 30).forEach(k->commentList.add(new CommentDto((Comment)k)));
            int pageNumber = page > commentsPageNumber ? commentsPageNumber : page < 0 ? 0 : page;
            model.addAttribute("currentPage", pageNumber);
            model.addAttribute("numberPages", commentsPageNumber);
            model.addAttribute("headerComments", commentList);
            model.addAttribute("articleId", articleId);
        }
        else
            model.addAttribute("failureInfo", "Nie znaleziono artykułu.");
    }

    public void changeStatus (long id, boolean status, int page, Model model) {
        model.addAttribute("successInfo", null);
        model.addAttribute("failureInfo", null);
        Comment comment = commentRepository.findFirstById(id);
        if (comment != null && commentRepository.findFirstByIdAndIsActive(id, status) == null) {
            comment.setActive(!comment.getActive());
            commentRepository.save(comment);
            model.addAttribute("successInfo", "Pomyślnie " + (status ? "odblokowano" : "zablokowano") + " komentarz.");
        }
        getCommentsOfArticle(comment != null ? comment.getArticle().getId() : -1, page, model);
    }

    public void removeComment (long id, int page, long articleId, Model model) {
        model.addAttribute("successInfo", null);
        model.addAttribute("failureInfo", null);
        Comment comment = commentRepository.findFirstById(id);
        if (comment != null) {
            comment.getArticle().getComments().remove(comment);
            commentRepository.deleteById(comment.getId());
            model.addAttribute("successInfo", "Pomyślnie usunięto komentarz.");
        }
        getCommentsOfArticle(articleId, page, model);
    }

    public void add (CommentForm commentForm, BindingResult bindingResult, Model model, HttpServletRequest request) {
        model.addAttribute("successInfo", null);
        model.addAttribute("failureInfo", null);

        User authorUser = userRepository.findFirstByUsername(commentForm.getAuthor());
        Article article = articleRepository.findFirstById(commentForm.getArticle());

        if (authorUser != null && commentForm.getUser() == null)
            bindingResult.rejectValue("author", "comment", "Nie możesz podpisać się jak autor o takiej nazwie.");

        if (commentForm.getUser() != null)
            authorUser = userRepository.findFirstById(commentForm.getUser());

        if (article != null && !bindingResult.hasErrors() && commentRepository
                .findFirstByContentsAndAdditionDateAndArticleAndAuthor(commentForm.getContents(), commentForm.getAdditionDate(),
                        articleRepository.findFirstById(commentForm.getArticle()),
                        (authorUser != null ? authorUser.getUsername() : commentForm.getAuthor())) == null) {
            Comment comment;
                if (authorUser != null)
                    comment = new Comment(commentForm.getContents(),
                            commentForm.getAdditionDate(), true, authorUser, article);
                else
                    comment = new Comment(commentForm.getContents(),
                            commentForm.getAdditionDate(), true, commentForm.getAuthor(), article);

            commentRepository.save(comment);
            model.addAttribute("successInfo", "Pomyślnie dodano komentarz.");
            getCommentForm(model, request);
        }
    }

    public void change (CommentForm commentForm, BindingResult bindingResult, Model model) {
        model.addAttribute("successInfo", null);
        User authorUser = userRepository.findFirstByUsername(commentForm.getAuthor());
        Article article = articleRepository.findFirstById(commentForm.getArticle());

        if (commentForm.getId() != null) {
            Comment comment = commentRepository.findFirstById(commentForm.getId());

            if (commentForm.getUser() != null)
                authorUser = userRepository.findFirstById(commentForm.getUser());

            if (comment != null && article != null && !bindingResult.hasErrors() && commentRepository
                    .findFirstByContentsAndAdditionDateAndArticleAndAuthor(commentForm.getContents(), commentForm.getAdditionDate(),
                            articleRepository.findFirstById(commentForm.getArticle()),
                            (authorUser != null ? authorUser.getUsername() : commentForm.getAuthor())) == null) {
                if (comment != null)
                    comment.setContents(commentForm.getContents());
                commentRepository.save(comment);
                model.addAttribute("successInfo", "Pomyślnie wyedytwano komentarz.");
            }
        }
    }

    public void getComments (Model model, long idArticle) {
        Article article = articleRepository.findFirstById(idArticle);
        if (article != null){
            List<CommentDto> commentDtoList = new ArrayList<>();
            commentRepository.findByArticleAndIsActiveOrderByAdditionDateDesc(article, true)
                    .forEach(c->commentDtoList.add(new CommentDto(c)));
            model.addAttribute("comments", commentDtoList);
        }
    }

    private List<? extends Object> getItemsDividedIntoPages (List<? extends Object> list, int page, int itemsPerPage) {
        if (page == 0)
            return null;

        int beginIndex = (page - 1) * itemsPerPage < list.size()
                ? (page - 1) * itemsPerPage : list.size() > 0 ? list.size() - 1 : 0;
        int endIndex = (page - 1) * itemsPerPage + itemsPerPage - 1 < list.size()
                ? (page - 1) * itemsPerPage + itemsPerPage : list.size();
        return list.subList(beginIndex, endIndex);
    }

    private int getItemPageNumber (List<? extends Object> list) {
        return (int)Math.ceil((double) list.size() / 30);
    }
}
