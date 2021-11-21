package com.zonkil.roomoccupancymanager.service.v2;

import com.zonkil.roomoccupancymanager.domain.AvailableRooms;
import com.zonkil.roomoccupancymanager.domain.RoomOccupancyCalculation;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class DefaultRoomOccupancyCalculationV2Service implements RoomOccupancyServiceV2 {
	private final GuestService guestService;

	public DefaultRoomOccupancyCalculationV2Service(@Qualifier("DbGuestService") GuestService guestService) {
		this.guestService = guestService;
	}

	public RoomOccupancyCalculation calculateRoomOccupancy(AvailableRooms availableRooms) {

		long premiumGuestCount = guestService.countGuests(GuestType.PREMIUM);
		long economyGuestCount = guestService.countGuests(GuestType.ECONOMY);
		if (premiumGuestCount == 0 && economyGuestCount == 0) {
			return RoomOccupancyCalculation.ZERO;
		}

		var upgradeData = calculateUpgrade(availableRooms, premiumGuestCount, economyGuestCount);

		BigDecimal premiumProfit = calculatePremiumProfit(availableRooms, upgradeData);
		BigDecimal economyProfit = calculateEconomyProfit(availableRooms, upgradeData);

		long premiumOccupancy = Math.min(availableRooms.getNumberOfPremiumRooms(),
				premiumGuestCount + upgradeData.numberOfGuestToPromote);
		long economyOccupancy = Math.min(availableRooms.getNumberOfEconomyRooms(),
				economyGuestCount - upgradeData.numberOfGuestToPromote);

		return RoomOccupancyCalculation.builder()
									   .premiumOccupancy((int) premiumOccupancy)
									   .premiumTotalMoney(premiumProfit)
									   .economyOccupancy((int) economyOccupancy)
									   .economyTotalMoney(economyProfit)
									   .build();
	}

	private UpgradeData calculateUpgrade(AvailableRooms availableRooms, long premiumGuestCount,
			long economyGuestCount) {
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

	private boolean isUpgradePossible(long numberOfGuestToPromote, long economyGuestCount, AvailableRooms availableRooms) {
		return numberOfGuestToPromote > 0 && economyGuestCount > availableRooms.getNumberOfEconomyRooms();
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
		private long numberOfGuestToPromote;
	}
}
