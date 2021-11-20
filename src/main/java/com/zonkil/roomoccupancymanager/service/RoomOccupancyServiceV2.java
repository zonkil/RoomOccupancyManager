package com.zonkil.roomoccupancymanager.service;

import com.zonkil.roomoccupancymanager.domain.AvailableRooms;
import com.zonkil.roomoccupancymanager.domain.RoomOccupancyCalculation;

//TODO: refactor name
public interface RoomOccupancyServiceV2 {
	RoomOccupancyCalculation calculateRoomOccupancy(AvailableRooms availableRooms);
}
