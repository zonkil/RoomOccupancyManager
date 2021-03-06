package com.zonkil.roomoccupancymanager.web;

import com.zonkil.roomoccupancymanager.domain.AvailableRooms;
import com.zonkil.roomoccupancymanager.service.GuestDataProvider;
import com.zonkil.roomoccupancymanager.service.RoomOccupancyService;
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
import java.math.BigDecimal;
import java.util.List;

@Validated
@RestController
@RequestMapping(produces = { MediaType.APPLICATION_JSON_VALUE }, path = "/occupancy")
@Tag(name = "RoomOccupancy", description = "RoomOccupancy API")
public class RoomOccupancyController {

	private final RoomOccupancyService roomOccupancyService;
	private final ConversionService conversionService;
	private final GuestDataProvider guestDataProvider;

	public RoomOccupancyController(RoomOccupancyService roomOccupancyService, ConversionService conversionService,
			GuestDataProvider guestDataProvider) {
		this.roomOccupancyService = roomOccupancyService;
		this.conversionService = conversionService;
		this.guestDataProvider = guestDataProvider;
	}

	@Operation(summary = "Calculate room occupancy based on available rooms and guest list", description = "Calculate room occupancy based on available rooms. If guest list is provided in parameter calculation will be done based on this data. If parameter guest is not provided, calculation will be done based on data in database")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(implementation = RoomOccupancyCalculationResponseDto.class))),
			@ApiResponse(responseCode = "400", description = "bad request") })
	@GetMapping
	public RoomOccupancyCalculationResponseDto calculateOccupancy(
			@Parameter(description = "Number of empty premium rooms", example = "3", required = true) @RequestParam @Min(0) int numberOfPremiumRooms,
			@Parameter(description = "Number of empty economy rooms", example = "3", required = true) @RequestParam @Min(0) int numberOfEconomyRooms,
			@Parameter(description = "List of people represented as a numbers. Each number is amount of money guest will pay for a room", example = "[23, 45, 155, 374, 22, 99.99, 100, 101, 115, 209]", required = false) @RequestParam(required = false, name = "guests") List<@Min(0) BigDecimal> allGuests) {

		if (allGuests != null) {
			guestDataProvider.initialize(allGuests);
		}
		var calculation = roomOccupancyService.calculateRoomOccupancy(
				AvailableRooms.of(numberOfPremiumRooms, numberOfEconomyRooms));

		return conversionService.convert(calculation, RoomOccupancyCalculationResponseDto.class);
	}

}
