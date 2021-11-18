package com.zonkil.roomoccupancymanager.service;

import com.zonkil.roomoccupancymanager.domain.Guest;
import com.zonkil.roomoccupancymanager.domain.Guests;
import com.zonkil.roomoccupancymanager.domain.RoomOccupancyCalculation;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DefaultRoomOccupancyService implements RoomOccupancyService {

	@Override
	public RoomOccupancyCalculation calculateRoomOccupancy(int numberOfPremiumRooms, int numberOfEconomyRooms,
			List<BigDecimal> allGuests) {

		Guests guests = Guests.ofAllGuests(allGuests.stream().map(Guest::new).collect(Collectors.toList()));

		//is upgrade
		if (numberOfPremiumRooms > guests.getPremiumGuestsNumber()
				&& guests.getEconomyGuestsNumber() > numberOfEconomyRooms) {
			var emptyPremiumRoomsCount = numberOfPremiumRooms - guests.getPremiumGuestsNumber();
			guests = guests.promote(emptyPremiumRoomsCount);
		}

		var premiumOccupancy = occupancy(numberOfPremiumRooms, guests.getPremiumGuestsNumber());
		var economyOccupancy = occupancy(numberOfEconomyRooms, guests.getEconomyGuestsNumber());
		var premiumMoney = calculateMoneyLimitedByRooms(guests.getPremiumGuests(), numberOfPremiumRooms);
		var economyMoney = calculateMoneyLimitedByRooms(guests.getEconomyGuests(), numberOfEconomyRooms);

		return RoomOccupancyCalculation.builder()
									   .premiumOccupancy(premiumOccupancy)
									   .premiumTotalMoney(premiumMoney)
									   .economyOccupancy(economyOccupancy)
									   .economyTotalMoney(economyMoney)
									   .build();
	}

	private int occupancy(int numberOfPremiumRooms, int numberOfGuests) {
		return Math.min(numberOfPremiumRooms, numberOfGuests);
	}

	private BigDecimal calculateMoneyLimitedByRooms(List<Guest> guestsList, int numberOfRooms) {
		return guestsList.stream()
						 .limit(numberOfRooms)
						 .map(Guest::getMoneyAmount)
						 .reduce(BigDecimal.ZERO, BigDecimal::add);
	}
}
