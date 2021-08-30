package com.example.project.entity;

import java.util.ArrayList;
import java.util.List;

public class Filter {

    private List<String> filterList;

    private String searchString;

    public Filter(){
        filterList = new ArrayList<>();
    }

    public List<String> getFilterList() {
        return filterList;
    }

    public void setFilterList(List<String> filterList) {
        this.filterList = filterList;
    }

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }
}
