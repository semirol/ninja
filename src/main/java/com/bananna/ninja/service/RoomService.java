package com.bananna.ninja.service;

import com.bananna.ninja.entity.FrameSyncRoom;
import com.bananna.ninja.entity.Room;


/**
 * 管理房间的服务，维护一个（房间id，房间状态）的map，并提供状态存取接口
 */
public interface RoomService {
    /**
     * 返回是否是p1
     * @param roomNumber
     * @param playerId
     * @return
     */
    int enterRoom(String roomNumber, int playerId);

    Room getRoomByPlayerId(int playerId);

    /**
     * 删除房间并释放内存。一般有room对象在游戏结束后调用，清除自身。
     * @param roomNumber
     */
    void deleteRoom(String roomNumber);

}
