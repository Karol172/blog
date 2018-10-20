package com.karol172.blog.controllers;

import com.karol172.blog.form.CommentForm;
import com.karol172.blog.service.ArticlesService;
import com.karol172.blog.service.CommentService;
import com.karol172.blog.service.DataPageService;
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

@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private DataPageService dataPageService;

    @Autowired
    private ArticlesService articlesService;

    @RequestMapping(value = "/article/comment/add", method = RequestMethod.POST)
    public String addComment (@ModelAttribute("commentForm") @Valid CommentForm commentForm, BindingResult bindingResult,
                              Model model, HttpServletRequest request) {
        dataPageService.getDataPage(model, request);
        commentService.add(commentForm, bindingResult, model, request);
        articlesService.getArticle(model, Long.valueOf(commentForm.getArticle()));
        commentService.getComments(model, Long.valueOf(commentForm.getArticle()));
        commentService.getCommentForm(model, request);
        return "article";
    }

    @RequestMapping(value = "/article/comment/{id}", method=RequestMethod.GET)
    public String edit (@PathVariable("id") long id, Model model, HttpServletRequest request) {
        dataPageService.getDataPage(model, request);
        commentService.getCommentForm(model, id);
        return "commentForm";
    }

    @RequestMapping(value = "/article/comment/change", method = RequestMethod.POST)
    public String change (@ModelAttribute("commentForm") @Valid CommentForm commentForm, BindingResult result,
                          Model model, HttpServletRequest request) {
        dataPageService.getDataPage(model, request);
        commentService.change(commentForm, result, model);
        articlesService.getArticle(model, Long.valueOf(commentForm.getArticle()));
        commentService.getComments(model, Long.valueOf(commentForm.getArticle()));
        commentService.getCommentForm(model, request);
        return "article";
    }

    @RequestMapping(value = "/admin/comments/{page}", method = RequestMethod.GET)
    public String articlesReview (@PathVariable("page") int page, Model model, HttpServletRequest request) {
        dataPageService.getDataPage(model, request);
        commentService.getArticles(page, model);
        return "articleHeaderReview";
    }

    @RequestMapping(value = "/admin/comments/article/{articleId}/{page}", method = RequestMethod.GET)
    public String commentsReview (@PathVariable("articleId") long articleId, @PathVariable("page") int page,
                                  Model model, HttpServletRequest request) {
        dataPageService.getDataPage(model, request);
        commentService.getCommentsOfArticle(articleId, page, model);
        return "commentHeaderReview";
    }

    @RequestMapping(value = "/admin/comments/{page}/active/{comment}/{status}", method = RequestMethod.GET)
    public String commentChangeStatus (@PathVariable("page") int page, @PathVariable("comment") long commentId,
                                       @PathVariable("status") boolean status, Model model, HttpServletRequest request) {
        dataPageService.getDataPage(model, request);
        commentService.changeStatus(commentId, status, page, model);
        return "commentHeaderReview";
    }

    @RequestMapping(value = "/admin/comments/{page}/{articleId}/remove/{commentId}", method = RequestMethod.GET)
    public String removeComment (@PathVariable("page") int page, @PathVariable("articleId") long articleId, @PathVariable("commentId") long commentId,
                                 Model model, HttpServletRequest request) {
        dataPageService.getDataPage(model, request);
        commentService.removeComment(commentId, page, articleId, model);
        return "commentHeaderReview";
    }

}
