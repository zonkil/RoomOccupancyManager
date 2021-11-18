package com.zonkil.roomoccupancymanager.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
public class Guest implements Comparable<Guest> {
	private BigDecimal moneyAmount;

	@Override
	public int compareTo(Guest guest) {
		return moneyAmount.compareTo(guest.getMoneyAmount());
	}
}
