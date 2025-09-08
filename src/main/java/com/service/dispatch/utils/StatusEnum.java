package com.service.dispatch.utils;

public enum StatusEnum {
    // Status dispatch
        PENDING("Pending"),
        ACCEPTED("Accepted"),
        REJECTED("Rejected"),

    // Status driver
    ACCEPT("Accept"),
    REJECT("Reject");


    private final String value;

        StatusEnum(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
}
