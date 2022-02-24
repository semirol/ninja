package com.bananna.ninja.service;

import com.bananna.ninja.entity.FrameSyncRoom;
import com.bananna.ninja.entity.Room;
import com.bananna.ninja.entity.StateSyncRoom;
import org.springframework.stereotype.Component;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.concurrent.*;

@Component
public class RoomServiceImpl implements RoomService{

    private static Map<String, Room> roomMap = new ConcurrentHashMap<>();

    private static Map<Integer, WeakReference<Room>> roomMapByPlayerId = new ConcurrentHashMap<>();

    private static Executor executor = new ScheduledThreadPoolExecutor(10);

    @Override
    public int enterRoom(String roomNumber, int playerId) {
        Room room;
        if (roomMap.containsKey(roomNumber)){
            room = roomMap.get(roomNumber);
        }
        else{
            room = new StateSyncRoom(roomNumber, executor);
            roomMap.put(roomNumber, room);
        }
        roomMapByPlayerId.put(playerId, new WeakReference<>(room));
        return room.addPlayer(playerId);
    }

    @Override
    public Room getRoomByPlayerId(int playerId) {
        return roomMapByPlayerId.get(playerId).get();
    }

    @Override
    public void deleteRoom(String roomNumber) {
        roomMap.remove(roomNumber);
    }

}
