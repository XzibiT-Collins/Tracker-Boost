package com.project.tracker.sortingEnums;

import lombok.Getter;

@Getter
public enum TaskSorting {
    SORT_BY_DUE_DATE("dueDate"),
    SORT_BY_STATUS("status"),
    SORT_BY_TITLE("title");

    private final String field;
    TaskSorting(String field){
        this.field = field;
    }
}
