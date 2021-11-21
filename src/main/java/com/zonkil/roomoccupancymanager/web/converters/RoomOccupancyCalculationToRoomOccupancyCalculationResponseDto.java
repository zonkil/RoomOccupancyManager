package com.zonkil.roomoccupancymanager.web.converters;

import com.zonkil.roomoccupancymanager.domain.RoomOccupancyCalculation;
import com.zonkil.roomoccupancymanager.web.dto.RoomOccupancyCalculationResponseDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

@Service
public class RoomOccupancyCalculationToRoomOccupancyCalculationResponseDto
		implements Converter<RoomOccupancyCalculation, RoomOccupancyCalculationResponseDto> {

	@Override
	public RoomOccupancyCalculationResponseDto convert(RoomOccupancyCalculation source) {
		return RoomOccupancyCalculationResponseDto.builder()
												  .premiumOccupancy(source.getPremiumOccupancy())
												  .premiumProfit(source.getPremiumTotalMoney())
												  .economyOccupancy(source.getEconomyOccupancy())
												  .economyProfit(source.getEconomyTotalMoney())
												  .build();
	}
}
