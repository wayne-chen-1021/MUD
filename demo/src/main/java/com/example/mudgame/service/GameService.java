package com.example.mudgame.service;

import com.example.mudgame.model.Item;
import com.example.mudgame.model.Monster;
import com.example.mudgame.model.Player;
import com.example.mudgame.model.Room;
import com.example.mudgame.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

import java.util.Optional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GameService {
    @Autowired
    private PlayerRepository playerRepository;

    private Player player;
    private Map<String, Room> rooms = new HashMap<>();

    @PostConstruct
    public void init() {
        // 初始化怪物
        Monster goblin = new Monster("哥布林", 20, 10);
        Monster troll = new Monster("巨魔", 30, 20);

        // 初始化房間
        Room room1 = new Room("room1", "你位於一間昏暗的起始房間。");
        Room room2 = new Room("room2", "你來到一間佈滿蜘蛛網的石室。");
        Room room3 = new Room("room3", "這是一間有著藥水閃光的神秘實驗室。");
        Room room4 = new Room("room4", "你來到了最後決戰之地。");

        room1.addExit("north", "room2");

        room2.addExit("south", "room1");
        room2.addExit("east", "room3");
        room2.setMonster(goblin);

        room3.addExit("west", "room2");
        room3.addExit("north", "room4");
        room3.setItem(new Item("potion", "healing", 20));

        room4.addExit("south", "room3");
        room4.setMonster(troll);

        rooms.put(room1.getId(), room1);
        rooms.put(room2.getId(), room2);
        rooms.put(room3.getId(), room3);
        rooms.put(room4.getId(), room4);

    }

    // 歡迎玩家
    public String setPlayerName(String name) {
        Optional<Player> existing = playerRepository.findByName(name);
    
        if (existing.isPresent()) {
            player = existing.get();
            return "歡迎回來，" + player.getName() + "！你目前在 " +
                   rooms.get(player.getCurrentRoom()).getDescription();
        }
    
        // 新玩家
        player = new Player(name);
        player.setHealth(100);
        player.setMaxHealth(100);
        player.setAttackPower(10);
        player.setCurrentRoom("room1");
        player.setSkills(new ArrayList<>(List.of("fireball")));
        player.setInventory(new ArrayList<>());
    
        // 儲存到資料庫
        playerRepository.save(player);
    
        return "歡迎你，" + name + "！冒險即將開始...\n" +
               rooms.get("room1").getDescription();
    }
    
    
    // 移動
    public String handleMove(String direction) {
        Room currentRoom = rooms.get(player.getCurrentRoom());
        String nextRoomId = currentRoom.getExit(direction.toLowerCase());
    
        if (nextRoomId == null) {
            return "你無法往 " + direction + " 移動。";
        }
    
        Room nextRoom = rooms.get(nextRoomId);
        player.moveTo(nextRoomId);
        // 更新玩家位置
        playerRepository.save(player);
        return "你往 " + direction + " 移動。\n" + nextRoom.getDescription();
    }
    
    // 攻擊
    public String attack(String targetName) {
        Room currentRoom = rooms.get(player.getCurrentRoom());
        Monster monster = currentRoom.getMonster();
    
        if (monster == null) {
            return "這裡沒有怪物可供攻擊。";
        }
        if (!monster.getName().equalsIgnoreCase(targetName)) {
            return "這裡沒有叫做 " + targetName + " 的怪物。";
        }
    
        int damage = player.getAttackPower();
        monster.takeDamage(damage);
        StringBuilder result = new StringBuilder();
    
        result.append("你攻擊了 ")
              .append(monster.getName())
              .append("，造成 ")
              .append(damage)
              .append(" 點傷害。\n");
    
        if (!monster.isAlive()) {
            currentRoom.setMonster(null); // 移除怪物
            result.append("你擊敗了 ").append(monster.getName()).append("！");
            return result.toString();
        }
        // 怪物反擊
        int monsterDamage = monster.getAttackPower();
        player.takeDamage(monsterDamage);
        result.append(monster.getName())
              .append(" 反擊了你，造成 ")
              .append(monsterDamage)
              .append(" 點傷害。\n");
    
        if (!player.isAlive()) {
            result.append("你已經倒下了！遊戲結束。\n請輸入 restart 以重新開始遊戲\n");
        } else {
            result.append("你目前剩下 ")
                    .append(player.getHealth())
                    .append(" 點血量。");
        }
    
        return result.toString();
    }
    
    public String useSkill(String skillName, String targetName) {
        if (!player.hasSkill(skillName)) {
            return "你不會這個技能。";
        }
    
        Room currentRoom = rooms.get(player.getCurrentRoom());
        Monster monster = currentRoom.getMonster();
    
        if (monster == null) {
            return "這裡沒有怪物可以攻擊。";
        }
        if (!monster.getName().equalsIgnoreCase(targetName)) {
            return "這裡沒有叫做 " + targetName + " 的怪物。";
        }
    
        if (skillName.equalsIgnoreCase("fireball")) {
            int damage = 20;
            monster.takeDamage(damage);
    
            StringBuilder result = new StringBuilder();
            result.append("你對 ").append(monster.getName())
                  .append(" 施放了火球術，造成 ").append(damage).append(" 點傷害！\n");
    
            if (!monster.isAlive()) {
                currentRoom.setMonster(null);
                result.append("你擊敗了 ").append(monster.getName()).append("！");
            } else {
                // 怪物反擊
                int counter = monster.getAttackPower();
                player.takeDamage(counter);
                result.append(monster.getName()).append(" 反擊了你，造成 ")
                      .append(counter).append(" 點傷害。\n");
    
                if (!player.isAlive()) {
                    result.append("你已經倒下了！遊戲結束。");
                } else {
                    result.append("你目前剩下 ").append(player.getHealth()).append(" 點血量。");
                }
            }

            return result.toString();
        }
    
        return "這個技能尚未實作。";
    }
    

    // 使用道具
    public String useItem(String itemName) {
        Item item = player.getItemByName(itemName);

        if (item == null) {
            return "你沒有這項道具。";
        }

        if (item.getType().equalsIgnoreCase("healing")) {
            player.heal(item.getValue());
            player.removeItemByName(itemName);

            return "你使用了【" + item.getChineseName(item.getName()) + "】回復了 " + item.getValue() + " 點血量。你目前血量為 " + player.getHealth() + "。";
        }

        return "這個道具無法使用或類型不支援。";
    }

    // 拾取道具
    public String pickupItem() {
        Room currentRoom = rooms.get(player.getCurrentRoom());
    
        if (!currentRoom.hasItem()) {
            return "這裡沒有任何道具可以撿起。";
        }
    
        Item item = currentRoom.getItem();
        player.addItem(item);
        currentRoom.setItem(null);

        return "撿起了" + item.getChineseName(item.getName()) + "放進背包。";
    }
    
    // 重新開始遊戲
        public String exitGame() {
        // 重置玩家狀態
        player = null; // 清除當前玩家
        return "遊戲已結束。感謝遊玩！\n請重新輸入玩家名稱以繼續冒險。";
    }

    // 查看當前房間
    public String look() {
        StringBuilder description = new StringBuilder();
        Room currentRoom = rooms.get(player.getCurrentRoom());
    
        if (currentRoom == null) {
            return "無法找到當前房間的資訊！";
        }
    
        description.append("房間描述: ").append(currentRoom.getDescription()).append("\n");
        // 顯示怪物
        if (currentRoom.getMonster() != null) {
            description.append("怪物: ").append(currentRoom.getMonster().getName()).append("\n");
        } else {
            description.append("怪物: 無\n");
        }
        // 顯示道具
        if (currentRoom.getItem() != null) {
            description.append("道具: ").append(currentRoom.getItem().getName()).append("\n");
        } else {
            description.append("道具: 無\n");
        }
        // 可行進的方向
        description.append("可行進的方向: ");
        if (currentRoom.getExits().isEmpty()) {
            description.append("無\n");
        } else {
            description.append(String.join(", ", currentRoom.getExits().keySet())).append("\n");
        }
        return description.toString();
    }
}
