package com.zonkil.roomoccupancymanager.web;

import com.zonkil.roomoccupancymanager.domain.AvailableRooms;
import com.zonkil.roomoccupancymanager.service.v2.RoomOccupancyServiceV2;
import com.zonkil.roomoccupancymanager.web.dto.RoomOccupancyCalculationResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;

@Validated
@RestController
@RequestMapping(produces = { MediaType.APPLICATION_JSON_VALUE }, path = "/v2")
@Tag(name = "RoomOccupancyV2", description = "RoomOccupancy API that use database as source of data")
public class RoomOccupancyV2Controller {

	private final RoomOccupancyServiceV2 roomOccupancyServiceV2;
	private final ConversionService conversionService;

	public RoomOccupancyV2Controller(RoomOccupancyServiceV2 roomOccupancyServiceV2,
			ConversionService conversionService) {
		this.roomOccupancyServiceV2 = roomOccupancyServiceV2;
		this.conversionService = conversionService;
	}

	@Operation(summary = "Calculate room occupancy based on available rooms and guest list", description = "Calculate room occupancy based on available rooms and guest list. Guest are sored in database.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = RoomOccupancyCalculationResponseDto.class))),
			@ApiResponse(responseCode = "400", description = "bad request") })
	@GetMapping("/occupancy")
	public RoomOccupancyCalculationResponseDto calculateOccupancyV2(
			@Parameter(description = "Number of empty premium rooms", example = "3") @RequestParam @Min(0) int numberOfPremiumRooms,
			@Parameter(description = "Number of empty economy rooms", example = "3") @RequestParam @Min(0) int numberOfEconomyRooms) {

		var calculation = roomOccupancyServiceV2.calculateRoomOccupancy(
				AvailableRooms.of(numberOfPremiumRooms, numberOfEconomyRooms));
		return conversionService.convert(calculation, RoomOccupancyCalculationResponseDto.class);
	}
}
