package com.project.tracker.sortingEnums;

import lombok.Getter;

@Getter
public enum AuditLogSorting {
    SORT_BY_ID("id"),
    SORT_BY_ACTION_TYPE("actionType"),
    SORT_BY_ENTITY_TYPE("actionType"),
    SORT_BY_ACTOR_NAME("actorName"),
    SORT_BY_TIMESTAMP("timestamp");

    private final String field;
    AuditLogSorting(String field){
        this.field = field;
    }
}
