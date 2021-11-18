package com.zonkil.roomoccupancymanager.service;

import com.zonkil.roomoccupancymanager.domain.AvailableRooms;
import com.zonkil.roomoccupancymanager.domain.Guests;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
public class EmptyPremiumRoomUpgradeService implements GuestUpgradeService {

	@Override
	public Guests upgradeGuestsIfNeeded(AvailableRooms availableRooms, Guests guests) {
		if (!shouldPromote(availableRooms, guests)) {
			return guests;
		}
		var numberGuestToPromote = availableRooms.getNumberOfPremiumRooms() - guests.getPremiumGuestsNumber();
		var guestToPromote = guests.getEconomyGuests()
								   .stream()
								   .limit(numberGuestToPromote)
								   .collect(Collectors.toList());
		var newPrem = new ArrayList<>(guests.getPremiumGuests());
		var newEcon = new ArrayList<>(guests.getEconomyGuests());
		newPrem.addAll(guestToPromote);
		newEcon.removeAll(guestToPromote);
		return new Guests(newPrem, newEcon);
	}

	private boolean shouldPromote(AvailableRooms availableRooms, Guests guests) {
		return isEmptyPremiumRoom(availableRooms, guests) && isNotEnoughEconomyRooms(availableRooms, guests);
	}

	private boolean isEmptyPremiumRoom(AvailableRooms availableRooms, Guests guests) {
		return availableRooms.getNumberOfPremiumRooms() > guests.getPremiumGuestsNumber();
	}

	private boolean isNotEnoughEconomyRooms(AvailableRooms availableRooms, Guests guests) {
		return guests.getEconomyGuestsNumber() > availableRooms.getNumberOfEconomyRooms();
	}
}
