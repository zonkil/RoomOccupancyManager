package com.zonkil.roomoccupancymanager.service;

import com.zonkil.roomoccupancymanager.domain.AvailableRooms;
import com.zonkil.roomoccupancymanager.domain.RoomOccupancyCalculation;

public interface RoomOccupancyService {
	RoomOccupancyCalculation calculateRoomOccupancy(AvailableRooms availableRooms);
}
