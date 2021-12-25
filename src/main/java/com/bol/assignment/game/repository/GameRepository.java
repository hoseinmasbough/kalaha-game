package com.bol.assignment.game.repository;

import com.bol.assignment.game.entity.GameStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameRepository extends JpaRepository<GameStatus, Long> {
}
