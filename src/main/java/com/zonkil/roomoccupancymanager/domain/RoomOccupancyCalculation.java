package com.zonkil.roomoccupancymanager.domain;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class RoomOccupancyCalculation {
	public static final RoomOccupancyCalculation ZERO = RoomOccupancyCalculation.builder()
																				.premiumOccupancy(0)
																				.premiumTotalMoney(BigDecimal.ZERO)
																				.economyOccupancy(0)
																				.economyTotalMoney(BigDecimal.ZERO)
																				.build();

	long premiumOccupancy;
	BigDecimal premiumTotalMoney;
	long economyOccupancy;
	BigDecimal economyTotalMoney;
}
