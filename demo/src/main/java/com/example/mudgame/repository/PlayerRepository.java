package com.example.mudgame.repository;

import com.example.mudgame.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PlayerRepository extends JpaRepository<Player, Long> {
    Optional<Player> findByName(String name);
    @Query("SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END " +
       "FROM Player p JOIN p.skills s " +
       "WHERE p.name = :name AND s = :skill")
    boolean existsSkillByPlayerName(@Param("name") String name, @Param("skill") String skill);
}
