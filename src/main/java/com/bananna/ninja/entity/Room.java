package com.bananna.ninja.entity;

public interface Room {
    void  addBattleOperation(BattleOperation battleOperation);
    int addPlayer(int playerId);
    void playerReady(int playerId);
}
