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

    public String getType() {
        return type;
    }

    public int getValue() {
        return value;
    }
}
