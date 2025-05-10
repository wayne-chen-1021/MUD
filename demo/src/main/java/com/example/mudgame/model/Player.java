package com.example.mudgame.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;


@Entity
@Table(name = "players")
public class Player {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private int health;
    private int maxHealth;
    private int attackPower;

    @Column(name = "current_room")
    private String currentRoom;

    // 忽略 skills 和 inventory 先不儲存進 DB，除非你另外建表（下面補充說明）
    @Transient
    private List<String> skills = new ArrayList<>();

    @Transient
    private List<Item> inventory = new ArrayList<>();

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
    public void setId(Long id) {
        this.id = id;
    }
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
}
