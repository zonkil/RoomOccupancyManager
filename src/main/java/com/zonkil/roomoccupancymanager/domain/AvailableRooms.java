package com.zonkil.roomoccupancymanager.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor(staticName = "of")
@Getter
@ToString
public class AvailableRooms {
	int numberOfPremiumRooms;
	int numberOfEconomyRooms;
}
