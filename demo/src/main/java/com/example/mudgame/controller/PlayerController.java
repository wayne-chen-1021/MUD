package com.example.mudgame.controller;

import com.example.mudgame.service.GameService;
import com.example.mudgame.dto.AttackRequest;
import com.example.mudgame.dto.DirectionRequest;
import com.example.mudgame.dto.NameRequest;
import com.example.mudgame.dto.SkillRequest;
import com.example.mudgame.dto.UseItemRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/player")
public class PlayerController {

    @Autowired
    private GameService gameService;

    @PutMapping("/position")
    public String movePlayer(@RequestBody DirectionRequest request) {
        return gameService.handleMove(request.getDirection());
    }
    @PostMapping("/attack")
    public String attack(@RequestBody AttackRequest request) {
        return gameService.attack(request.getTarget());
    }
    @PostMapping("/use-item")
    public String useItem(@RequestBody UseItemRequest request) {
        return gameService.useItem(request.getItemName());
    }
    @PostMapping("/pickup")
    public String pickup() {
        return gameService.pickupItem();
    }
    @PostMapping("/use-skill")
    public String useSkill(@RequestBody SkillRequest request) {
        return gameService.useSkill(request.getSkillName(), request.getTarget());
    }
    @PostMapping("/name")
    public String setPlayerName(@RequestBody NameRequest request) {
        return gameService.setPlayerName(request.getName());
    }

}
