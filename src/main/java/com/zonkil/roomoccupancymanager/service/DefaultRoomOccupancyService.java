package com.zonkil.roomoccupancymanager.service;

import com.zonkil.roomoccupancymanager.domain.AvailableRooms;
import com.zonkil.roomoccupancymanager.domain.DefaultGuestsFactory;
import com.zonkil.roomoccupancymanager.domain.Guest;
import com.zonkil.roomoccupancymanager.domain.RoomOccupancyCalculation;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class DefaultRoomOccupancyService implements RoomOccupancyService {

	private final GuestUpgradeService guestUpgradeService;
	private final DefaultGuestsFactory defaultGuestsFactory;
	private final GuestDataProvider guestDataProvider;

	public DefaultRoomOccupancyService(GuestUpgradeService guestUpgradeService, GuestDataProvider guestDataProvider,
			DefaultGuestsFactory defaultGuestsFactory) {
		this.guestUpgradeService = guestUpgradeService;
		this.defaultGuestsFactory = defaultGuestsFactory;
		this.guestDataProvider = guestDataProvider;
	}

	@Override
	public RoomOccupancyCalculation calculateRoomOccupancy(AvailableRooms availableRooms) {

		var guestData = guestDataProvider.getGuestData();
		var guests = defaultGuestsFactory.createGuests(guestData);
		guests = guestUpgradeService.upgradeGuestsIfNeeded(availableRooms, guests);

		var premiumOccupancy = occupancy(availableRooms.getNumberOfPremiumRooms(), guests.getPremiumGuestsNumber());
		var economyOccupancy = occupancy(availableRooms.getNumberOfEconomyRooms(), guests.getEconomyGuestsNumber());
		var premiumMoney = calculateMoneyLimitedByRooms(guests.getPremiumGuests(),
				availableRooms.getNumberOfPremiumRooms());
		var economyMoney = calculateMoneyLimitedByRooms(guests.getEconomyGuests(),
				availableRooms.getNumberOfEconomyRooms());

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
