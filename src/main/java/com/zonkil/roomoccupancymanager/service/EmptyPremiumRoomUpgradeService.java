package com.zonkil.roomoccupancymanager.service;

import com.zonkil.roomoccupancymanager.domain.AvailableRooms;
import com.zonkil.roomoccupancymanager.domain.Guest;
import com.zonkil.roomoccupancymanager.domain.Guests;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
public class EmptyPremiumRoomUpgradeService implements GuestUpgradeService {

	private final BigDecimal THRESHOLD;

	public EmptyPremiumRoomUpgradeService(@Value("${premium-guest.threshold}") BigDecimal threshold) {
		THRESHOLD = threshold;
	}

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

	@Override
	public boolean isPremiumGuest(Guest guest) {
		return THRESHOLD.compareTo(guest.getMoneyAmount()) <= 0;
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
