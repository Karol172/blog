package com.karol172.blog.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("storage")
public class StorageProperties {

    private String imagesLocation = "images";

    public String getImagesLocation() {
        return imagesLocation;
    }

    public void setImagesLocation(String imagesLocation) {
        this.imagesLocation = imagesLocation;
    }
}