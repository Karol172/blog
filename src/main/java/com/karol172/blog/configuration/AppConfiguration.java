package com.karol172.blog.configuration;


public class AppConfiguration {

    private int articlesPerPage;

    private String titlePage;

    public AppConfiguration () { }

    public AppConfiguration (int articlesPerPage, String titlePage) {
        this.articlesPerPage = articlesPerPage;
        this.titlePage = titlePage;
    }

    public int getArticlesPerPage() {
        return articlesPerPage;
    }

    public void setArticlesPerPage(int articlesPerPage) {
        this.articlesPerPage = articlesPerPage;
    }

    public String getTitlePage() {
        return titlePage;
    }

    public void setTitlePage(String titlePage) {
        this.titlePage = titlePage;
    }
}
