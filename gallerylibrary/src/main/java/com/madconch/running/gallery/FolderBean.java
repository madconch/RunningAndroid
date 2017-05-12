package com.madconch.running.gallery;

import java.io.Serializable;
import java.util.ArrayList;

public class FolderBean implements Serializable{
    private String path;
    private ArrayList<String> childs = new ArrayList<>();

    public String getPath() {
        return path;
    }

    public FolderBean setPath(String path) {
        this.path = path;
        return this;
    }

    public ArrayList<String> getChilds() {
        return childs;
    }

    public FolderBean setChilds(ArrayList<String> childs) {
        this.childs = childs;
        return this;
    }
}