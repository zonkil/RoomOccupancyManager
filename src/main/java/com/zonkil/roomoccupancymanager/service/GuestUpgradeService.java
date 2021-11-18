package com.zonkil.roomoccupancymanager.service;

import com.zonkil.roomoccupancymanager.domain.AvailableRooms;
import com.zonkil.roomoccupancymanager.domain.Guests;

public interface GuestUpgradeService {
	Guests upgradeGuestsIfNeeded(AvailableRooms availableRooms, Guests guests);
}
