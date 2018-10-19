package com.karol172.blog.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class LinkDto implements Serializable {
    @NotNull
    @NotEmpty
    private String link;

    @NotNull
    @NotEmpty
    private String name;

    public LinkDto(String link, String name) {
        this.link = link;
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
