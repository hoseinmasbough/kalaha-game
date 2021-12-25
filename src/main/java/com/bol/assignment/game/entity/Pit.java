package com.bol.assignment.game.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table
@Setter
@Getter
public class Pit {
    @Id
    @GeneratedValue
    private long id;
    @Column
    private int index;
    @Column
    private int stones;
    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false)
    private GameStatus gameStatus;

    public Pit() {

    }

    public Pit(int index, int stones, GameStatus gameStatus) {
        this.index = index;
        this.stones = stones;
        this.gameStatus = gameStatus;
    }

    public void addStones(int stones) {
        this.stones += stones;
    }

    public void sow() {
        this.stones++;
    }

    public int takeStones() {
        int count = stones;
        this.stones = 0;
        return count;
    }

    public boolean isEmpty() {
        return this.stones == 0;
    }

}
