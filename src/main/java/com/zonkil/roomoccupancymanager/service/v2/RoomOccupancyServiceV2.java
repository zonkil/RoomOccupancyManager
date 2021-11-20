package com.zonkil.roomoccupancymanager.service.v2;

import com.zonkil.roomoccupancymanager.domain.AvailableRooms;
import com.zonkil.roomoccupancymanager.domain.RoomOccupancyCalculation;

public interface RoomOccupancyServiceV2 {
	RoomOccupancyCalculation calculateRoomOccupancy(AvailableRooms availableRooms);
}
