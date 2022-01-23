package com.bananna.ninja.service;

import com.bananna.ninja.entity.Player;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class PlayerServiceImpl implements PlayerService{

    private static Map<String, Player> playerMap = new ConcurrentHashMap<>();


    @Override
    public Player getByPlayerId(String playerId) {
        return playerMap.get(playerId);
    }
}
