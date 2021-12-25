package com.bol.assignment.game.entity;

import com.bol.assignment.game.core.PlayerType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table
@Setter
@Getter
public class GameStatus {
    @Id
    @GeneratedValue
    private long id;
    @Column
    private PlayerType activePlayer;
    @OneToMany(
            mappedBy = "gameStatus",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Pit> pits = new ArrayList<>();
    @Transient
    private String reward;
    @Transient
    private PlayerType winner;
}

