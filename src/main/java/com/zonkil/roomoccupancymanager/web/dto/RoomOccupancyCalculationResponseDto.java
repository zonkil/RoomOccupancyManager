package com.zonkil.roomoccupancymanager.web.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class RoomOccupancyCalculationResponseDto {
	private int premiumOccupancy;
	private BigDecimal premiumTotalMoney;
	private int economyOccupancy;
	private BigDecimal economyTotalMoney;
}
