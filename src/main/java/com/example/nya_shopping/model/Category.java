package com.example.nya_shopping.model;

public enum Category {
    OA_EQUIPMENT("OA機器"),
    PC_AND_PERIPHERALS("PC・周辺機器"),
    OFFICE_FURNITURE("オフィス家具"),
    STATIONERY_AND_SUPPLIES("文房具・消耗品"),
    OFFICE_SUPPLIES("オフィス用品（雑貨）");

    private final String label;

    Category(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static String getLabel(String name) {
        for (Category c : values()) {
            if (c.name().equals(name)) {
                return c.getLabel();
            }
        }
        return "";
    }
}
