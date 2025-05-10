package com.example.mudgame.model;


public class Item {

    private String name;
    private String type; // e.g., "healing"
    private int value;   // 數值，例如治療量

    public Item(String name, String type, int value) {
        this.name = name;
        this.type = type;
        this.value = value;
    }

    public String getName() {
        return name;
    }
    public String getChineseName(String name) {
        if (name.equals("potion")) {
            return "藥水";
        }
        return "未知道具";
    }
    public String getType() {
        return type;
    }

    public int getValue() {
        return value;
    }
}
