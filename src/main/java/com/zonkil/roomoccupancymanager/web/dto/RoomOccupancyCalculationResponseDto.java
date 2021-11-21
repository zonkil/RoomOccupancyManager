package com.zonkil.roomoccupancymanager.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class RoomOccupancyCalculationResponseDto {
	@Schema(description = "Number of occupied premium rooms", example = "3")
	private long premiumOccupancy;
	@Schema(description = "Profit from premium rooms", example = "738")
	private BigDecimal premiumTotalMoney;
	@Schema(description = "Number of occupied economy rooms", example = "3")
	private long economyOccupancy;
	@Schema(description = "Profit from economy rooms", example = "167.99")
	private BigDecimal economyTotalMoney;
}
