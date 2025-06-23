package com.project.tracker.sortingEnums;

import lombok.Getter;

@Getter
public enum DeveloperSorting {
    SORT_BY_ID("id"),
    SORT_BY_NAME("name");

    private final String field;
    DeveloperSorting(String field){
        this.field = field;
    }
}
