package com.zonkil.roomoccupancymanager.service;

import com.zonkil.roomoccupancymanager.domain.Guest;
import com.zonkil.roomoccupancymanager.domain.Guests;

import java.math.BigDecimal;
import java.util.List;

public class GuestsGuestService implements GuestService {

	private final Guests guests;

	public GuestsGuestService(Guests guests) {
		this.guests = guests;
	}

	@Override
	public int countGuests(GuestType guestType) {
		switch (guestType) {
		case PREMIUM:
			return guests.getPremiumGuestsNumber();
		case ECONOMY:
			return guests.getEconomyGuestsNumber();
		}
		throw new IllegalArgumentException("Unknown guest type: " + guestType);
	}

	@Override
	public BigDecimal calculateProfit(GuestType guestType, int guestLimit) {
		if (guestLimit < 0) {
			return BigDecimal.ZERO;
		}
		switch (guestType) {
		case PREMIUM:
			return calculate(guests.getPremiumGuests(), guestLimit);
		case ECONOMY:
			return calculate(guests.getEconomyGuests(), guestLimit);
		}
		throw new IllegalArgumentException("Unknown guest type: " + guestType);
	}

	private BigDecimal calculate(List<Guest> guests, int limit) {
		return guests.stream().limit(limit).map(Guest::getMoneyAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
	}
}
