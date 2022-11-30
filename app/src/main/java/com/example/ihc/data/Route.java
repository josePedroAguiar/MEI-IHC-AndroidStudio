package com.example.ihc.data;

public class Route {
    private  int order;
    String link;
    String image;
    public Route() {
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public String getLink() {
        return link;
    }

    public Route(String link) {
        this.link = link;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }
}
