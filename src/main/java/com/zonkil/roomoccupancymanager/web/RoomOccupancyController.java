package com.zonkil.roomoccupancymanager.web;

import com.zonkil.roomoccupancymanager.service.RoomOccupancyService;
import com.zonkil.roomoccupancymanager.web.dto.RoomOccupancyCalculationResponseDto;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.util.List;

@Validated
@RestController
@RequestMapping(produces = { MediaType.APPLICATION_JSON_VALUE }, path = "/occupancy")
public class RoomOccupancyController {

	private final RoomOccupancyService roomOccupancyService;
	private final ConversionService conversionService;

	public RoomOccupancyController(RoomOccupancyService roomOccupancyService, ConversionService conversionService) {
		this.roomOccupancyService = roomOccupancyService;
		this.conversionService = conversionService;
	}

	@GetMapping
	public RoomOccupancyCalculationResponseDto calculateOccupancy(@RequestParam @Min(0) int numberOfPremiumRooms,
			@RequestParam @Min(0) int numberOfEconomyRooms, @RequestParam List<@Min(0) BigDecimal> allGuests) {

		var calculation = roomOccupancyService.calculateRoomOccupancy(numberOfPremiumRooms, numberOfEconomyRooms,
				allGuests);

		return conversionService.convert(calculation, RoomOccupancyCalculationResponseDto.class);
	}

}
