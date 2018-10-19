package com.karol172.blog.form;

import com.karol172.blog.configuration.AppConfiguration;

import java.io.Serializable;

public class SettingsForm implements Serializable {

    private int articlesPerPage;

    private String titlePage;

    public SettingsForm() {
    }

    public SettingsForm(AppConfiguration appConfiguration) {
        this.articlesPerPage = appConfiguration.getArticlesPerPage();
        this.titlePage = appConfiguration.getTitlePage();
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
