package com.bananna.ninja.service;

import com.bananna.ninja.entity.Player;

public interface PlayerService {

    Player getByPlayerId(String playerId);
}
