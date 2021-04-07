package com.bg.deliveryapp.sectionedrecyclerview;


import com.intrusoft.sectionedrecyclerview.Section;

import java.util.Comparator;
import java.util.List;

/**
 * Created by apple on 11/7/16.
 */

public class SectionHeader implements Section<Child>, Comparable<SectionHeader> {

    List<Child> childList;
    String sectionText;
    int image_icon;
    int index;

    public SectionHeader(List<Child> childList, String sectionText, int index, int image_icon) {
        this.childList = childList;
        this.sectionText = sectionText;
        this.index = index;
        this.image_icon = image_icon;
    }

    @Override
    public List<Child> getChildItems() {
        return childList;
    }

    public String getSectionText() {
        return sectionText;
    }

    public int getImage_icon() {
        return image_icon;
    }

    @Override
    public int compareTo(SectionHeader another) {
        if (this.index > another.index) {
            return -1;
        } else {
            return 1;
        }
    }
}