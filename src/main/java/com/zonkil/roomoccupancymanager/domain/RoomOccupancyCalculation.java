package com.zonkil.roomoccupancymanager.domain;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class RoomOccupancyCalculation {
	private int premiumOccupancy;
	private BigDecimal premiumTotalMoney;
	private int economyOccupancy;
	private BigDecimal economyTotalMoney;
}
