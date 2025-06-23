package com.project.tracker.sortingEnums;

import lombok.Getter;

@Getter
public enum ProjectSorting {
    SORT_BY_ID("id"),
    SORT_BY_NAME("projectName"),
    SORT_BY_DEADLINE("deadline"),
    SORT_BY_STATUS("status");

    private final String field;
    ProjectSorting(String field){
        this.field = field;
    }
}
