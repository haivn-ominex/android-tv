package com.ominext.haivn.appandroidtv.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MyItem implements Serializable {
    @SerializedName("selfLink")
    private String selfLink;

    @SerializedName("webContentLink")
    private String webContentLink;

    @SerializedName("thumbnailLink")
    private String thumbnailLink;

    @SerializedName("title")
    private String title;

    @SerializedName("downloadUrl")
    private String downloadUrl;

    @SerializedName("alternateLink")
    private String alternateLink;

    public String getAlternateLink() {
        return alternateLink;
    }

    public void setAlternateLink(String alternateLink) {
        this.alternateLink = alternateLink;
    }

    public String getSelfLink() {
        return selfLink;
    }

    public void setSelfLink(String selfLink) {
        this.selfLink = selfLink;
    }

    public String getWebContentLink() {
        return webContentLink;
    }

    public void setWebContentLink(String webContentLink) {
        this.webContentLink = webContentLink;
    }

    public String getThumbnailLink() {
        return thumbnailLink;
    }

    public void setThumbnailLink(String thumbnailLink) {
        this.thumbnailLink = thumbnailLink;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }
}
