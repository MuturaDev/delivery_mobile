package com.bg.deliveryapp.sectionedrecyclerview;


/**
 * Created by apple on 11/7/16.
 */
public class Child {

    String name;
    int image_icon;

    public Child(String name,int image_icon) {
        this.name = name;
        this.image_icon = image_icon;
    }

    public String getName() {
        return name;
    }

    public int getImage_icon() {
        return image_icon;
    }

    public void setName(String name) {
        this.name = name;
    }
}