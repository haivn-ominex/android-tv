package com.ominext.haivn.appandroidtv.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ListItem {
    @SerializedName("items")
    private List<MyItem> items;

    @SerializedName("kind")
    private String kind;

    @SerializedName("etag")
    private String etag;

    @SerializedName("selfLink")
    private String selfLink;

    @SerializedName("incompleteSearch")
    private boolean incompleteSearch;

    public List<MyItem> getItems() {
        return items;
    }

    public void setItems(List<MyItem> items) {
        this.items = items;
    }
}
