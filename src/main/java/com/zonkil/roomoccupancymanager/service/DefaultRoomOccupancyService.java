package com.zonkil.roomoccupancymanager.service;

import com.zonkil.roomoccupancymanager.domain.AvailableRooms;
import com.zonkil.roomoccupancymanager.domain.RoomOccupancyCalculation;
import com.zonkil.roomoccupancymanager.service.v2.GuestService;
import com.zonkil.roomoccupancymanager.service.v2.GuestType;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class DefaultRoomOccupancyService implements RoomOccupancyService {

	private final GuestServiceStrategy guestServices;

	public DefaultRoomOccupancyService(GuestServiceStrategy guestServices) {
		this.guestServices = guestServices;
	}

	public RoomOccupancyCalculation calculateRoomOccupancy(AvailableRooms availableRooms) {
		var guestService = guestServices.get();
		return calculateRoomOccupancyInternal(availableRooms, guestService);
	}

	private RoomOccupancyCalculation calculateRoomOccupancyInternal(AvailableRooms availableRooms,
			GuestService guestService) {

		long premiumGuestCount = guestService.countGuests(GuestType.PREMIUM);
		long economyGuestCount = guestService.countGuests(GuestType.ECONOMY);
		if (premiumGuestCount == 0 && economyGuestCount == 0) {
			return RoomOccupancyCalculation.ZERO;
		}

		var upgradeData = calculateUpgrade(availableRooms, premiumGuestCount, economyGuestCount, guestService);

		BigDecimal premiumProfit = calculatePremiumProfit(availableRooms, upgradeData, guestService);
		BigDecimal economyProfit = calculateEconomyProfit(availableRooms, upgradeData, guestService);

		long premiumOccupancy = Math.min(availableRooms.getNumberOfPremiumRooms(),
				premiumGuestCount + upgradeData.numberOfGuestToPromote);
		long economyOccupancy = Math.min(availableRooms.getNumberOfEconomyRooms(),
				economyGuestCount - upgradeData.numberOfGuestToPromote);

		return RoomOccupancyCalculation.builder()
									   .premiumOccupancy(premiumOccupancy)
									   .premiumTotalMoney(premiumProfit)
									   .economyOccupancy(economyOccupancy)
									   .economyTotalMoney(economyProfit)
									   .build();
	}

	private UpgradeData calculateUpgrade(AvailableRooms availableRooms, long premiumGuestCount, long economyGuestCount,
			GuestService guestService) {
		BigDecimal upgradedGuestsProfit = BigDecimal.ZERO;
		long numberOfGuestToPromote = availableRooms.getNumberOfPremiumRooms() - premiumGuestCount;
		if (isUpgradePossible(numberOfGuestToPromote, economyGuestCount, availableRooms)) {
			upgradedGuestsProfit = guestService.calculateProfit(GuestType.ECONOMY, numberOfGuestToPromote);
		}
		else {
			numberOfGuestToPromote = 0;
		}

		return UpgradeData.of(upgradedGuestsProfit, numberOfGuestToPromote);
	}

	private boolean isUpgradePossible(long numberOfGuestToPromote, long economyGuestCount,
			AvailableRooms availableRooms) {
		return numberOfGuestToPromote > 0 && economyGuestCount > availableRooms.getNumberOfEconomyRooms();
	}

	private BigDecimal calculatePremiumProfit(AvailableRooms availableRooms, UpgradeData upgradeData,
			GuestService guestService) {
		BigDecimal premiumProfit = guestService.calculateProfit(GuestType.PREMIUM,
				availableRooms.getNumberOfPremiumRooms());
		return premiumProfit.add(upgradeData.upgradedGuestsProfit);
	}

	private BigDecimal calculateEconomyProfit(AvailableRooms availableRooms, UpgradeData upgradeData,
			GuestService guestService) {
		BigDecimal economyProfitWithUpgraded = guestService.calculateProfit(GuestType.ECONOMY,
				availableRooms.getNumberOfEconomyRooms() + upgradeData.numberOfGuestToPromote);
		return economyProfitWithUpgraded.subtract(upgradeData.upgradedGuestsProfit);
	}

	@AllArgsConstructor(staticName = "of")
	private static class UpgradeData {
		private BigDecimal upgradedGuestsProfit;
		private long numberOfGuestToPromote;
	}
}
