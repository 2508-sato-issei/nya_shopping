package com.example.nya_shopping.model;

public enum Category {
    BUSINESS_MACHINES("事務機器");

    private final String label;

    Category(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
