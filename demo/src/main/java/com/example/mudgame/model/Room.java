package com.example.mudgame.model;

import java.util.HashMap;
import java.util.Map;

public class Room {
    private String id;
    private String description;
    private Map<String, String> exits; // e.g., "north" → "room2"

    private Monster monster;
    private Item item;

    public Room(String id, String description) {
        this.id = id;
        this.description = description;
        this.exits = new HashMap<>();
    }

    public void addExit(String direction, String targetRoomId) {
        exits.put(direction.toLowerCase(), targetRoomId);
    }

    public boolean hasItem() {
        return item != null;
    }

    // setter methods
    public void setMonster(Monster monster) {
        this.monster = monster;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    // Getter methods
    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getExit(String direction) {
        return exits.get(direction.toLowerCase());
    }

    public Map<String, String> getExits() {
        return exits;
    }

    public Monster getMonster() {
        return monster;
    }

    public Item getItem() {
        return item;
    }
}
