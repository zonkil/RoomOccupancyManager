package com.zonkil.roomoccupancymanager.service;

import com.zonkil.roomoccupancymanager.domain.AvailableRooms;
import com.zonkil.roomoccupancymanager.domain.RoomOccupancyCalculation;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;

public class GuestsRoomOccupancyService implements RoomOccupancyServiceV2 {

	private final GuestService guestService;

	public GuestsRoomOccupancyService(GuestService guestService) {
		this.guestService = guestService;
	}

	public RoomOccupancyCalculation calculateRoomOccupancy(AvailableRooms availableRooms) {

		int premiumGuestCount = guestService.countGuests(GuestType.PREMIUM);
		int economyGuestCount = guestService.countGuests(GuestType.ECONOMY);
		if (premiumGuestCount == 0 && economyGuestCount == 0) {
			return RoomOccupancyCalculation.ZERO;
		}

		var upgradeData = calculateUpgrade(availableRooms, premiumGuestCount, economyGuestCount);

		BigDecimal premiumProfit = calculatePremiumProfit(availableRooms, upgradeData);
		BigDecimal economyProfit = calculateEconomyProfit(availableRooms, upgradeData);

		int premiumOccupancy = Math.min(availableRooms.getNumberOfPremiumRooms(),
				premiumGuestCount + upgradeData.numberOfGuestToPromote);
		int economyOccupancy = Math.min(availableRooms.getNumberOfEconomyRooms(),
				economyGuestCount - upgradeData.numberOfGuestToPromote);

		return RoomOccupancyCalculation.builder()
									   .premiumOccupancy(premiumOccupancy)
									   .premiumTotalMoney(premiumProfit)
									   .economyOccupancy(economyOccupancy)
									   .economyTotalMoney(economyProfit)
									   .build();
	}

	private UpgradeData calculateUpgrade(AvailableRooms availableRooms, int premiumGuestCount, int economyGuestCount) {
		BigDecimal upgradedGuestsProfit = BigDecimal.ZERO;
		int numberOfGuestToPromote = availableRooms.getNumberOfPremiumRooms() - premiumGuestCount;
		if (numberOfGuestToPromote > 0 && economyGuestCount > availableRooms.getNumberOfEconomyRooms()) {
			upgradedGuestsProfit = guestService.calculateProfit(GuestType.ECONOMY, numberOfGuestToPromote);
		}
		else {
			numberOfGuestToPromote = 0;
		}

		return UpgradeData.of(upgradedGuestsProfit, numberOfGuestToPromote);
	}

	private BigDecimal calculatePremiumProfit(AvailableRooms availableRooms, UpgradeData upgradeData) {
		BigDecimal premiumProfit = guestService.calculateProfit(GuestType.PREMIUM,
				availableRooms.getNumberOfPremiumRooms());
		return premiumProfit.add(upgradeData.upgradedGuestsProfit);
	}

	private BigDecimal calculateEconomyProfit(AvailableRooms availableRooms, UpgradeData upgradeData) {
		BigDecimal economyProfitWithUpgraded = guestService.calculateProfit(GuestType.ECONOMY,
				availableRooms.getNumberOfEconomyRooms() + upgradeData.numberOfGuestToPromote);
		return economyProfitWithUpgraded.subtract(upgradeData.upgradedGuestsProfit);
	}

	@AllArgsConstructor(staticName = "of")
	private static class UpgradeData {
		private BigDecimal upgradedGuestsProfit;
		private int numberOfGuestToPromote;
	}
}
