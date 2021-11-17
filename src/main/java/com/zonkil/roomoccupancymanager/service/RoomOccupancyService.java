package com.zonkil.roomoccupancymanager.service;

import com.zonkil.roomoccupancymanager.domain.RoomOccupancyCalculation;

import java.math.BigDecimal;
import java.util.List;

public interface RoomOccupancyService {
	RoomOccupancyCalculation calculateRoomOccupancy(int numberOfPremiumRooms, int numberOfEconomyRooms, List<BigDecimal> guests);
}
