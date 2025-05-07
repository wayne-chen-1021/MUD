package com.example.mudgame.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

public class Player {

    private Long id;

    private String name;

    private int health;
    private int maxHealth;
    private int attackPower;
    private String currentRoom;

    private List<String> skills;

    private List<Item> inventory;

    public Player(String name) {
        this.name = name;
        this.maxHealth = 100;
        this.health = 100;
        this.attackPower = 10;
        this.currentRoom = "room1";
        this.inventory = new ArrayList<>();
        this.skills = new ArrayList<>();
    }

    public Player() {}

    public void moveTo(String roomId) {
        this.currentRoom = roomId;
    }

    public void takeDamage(int damage) {
        this.health -= damage;
        if (this.health < 0) this.health = 0;
    }

    public void heal(int amount) {
        this.health += amount;
        if (this.health > maxHealth) this.health = maxHealth;
    }

    public void addItem(Item item) {
        inventory.add(item);
    }

    public void addSkill(String skillName) {
        skills.add(skillName.toLowerCase());
    }

    public boolean hasSkill(String skillName) {
        return skills.contains(skillName.toLowerCase());
    }

    public boolean removeItemByName(String name) {
        return inventory.removeIf(i -> i.getName().equalsIgnoreCase(name));
    }

    public Item getItemByName(String name) {
        return inventory.stream()
            .filter(i -> i.getName().equalsIgnoreCase(name))
            .findFirst()
            .orElse(null);
    }

    public boolean isAlive() {
        return health > 0;
    }

    //Setter
    public void setHealth(int health) {
        this.health = health;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }
    public void setAttackPower(int attackPower) {
        this.attackPower = attackPower;
    }
    public void setCurrentRoom(String currentRoom) {
        this.currentRoom = currentRoom;
    }
    public void setInventory(List<Item> inventory) {
        this.inventory = inventory;
    }
    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    // Getter
    public Long getId() {
        return id;
    }
    public String getCurrentRoom() {
        return currentRoom;
    }

    public int getAttackPower() {
        return attackPower;
    }
    
    public int getMaxHealth() {
        return maxHealth;
    }

    public String getHealth() {
        return health + "/" + maxHealth;
    }

    public List<Item> getInventory() {
        return inventory;
    }

    public List<String> getSkills() {
        return skills;
    }
    public String getName() {
        return name;
    }

    @Override 
    public String toString() {
        String str = "";
        str += "玩家ID: " + id + "\n";
        str += "玩家名稱: " + name + "\n";
        str += "當前房間: " + currentRoom + "\n";
        str += "當前血量: " + health + "/" + maxHealth + "\n";
        str += "攻擊力: " + attackPower + "\n";
        str += "技能: " + String.join(", ", skills) + "\n";
        str += "物品: ";
        for (Item item : inventory) {
            str += item.getName() + " ";
        }
        str += "\n";
        return str;
    }
}
