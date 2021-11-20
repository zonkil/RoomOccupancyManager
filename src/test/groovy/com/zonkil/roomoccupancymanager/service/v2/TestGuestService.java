package com.zonkil.roomoccupancymanager.service.v2;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static java.util.function.Predicate.not;

public class TestGuestService implements GuestService {

	private final List<BigDecimal> guests;

	public TestGuestService(List<BigDecimal> guests) {
		this.guests = Collections.unmodifiableList(guests);
	}

	@Override
	public long countGuests(GuestType guestType) {
		switch (guestType) {
		case PREMIUM:
			return guests.stream().filter(this::isPremiumGuest).count();
		case ECONOMY:
			return guests.stream().filter(not(this::isPremiumGuest)).count();
		}
		return 0;
	}

	@Override
	public BigDecimal calculateProfit(GuestType guestType, long guestLimit) {
		switch (guestType) {
		case PREMIUM:
			return guests.stream()
						 .filter(this::isPremiumGuest)
						 .sorted(Comparator.reverseOrder())
						 .limit(guestLimit)
						 .reduce(BigDecimal.ZERO, BigDecimal::add);
		case ECONOMY:
			return guests.stream()
						 .filter(not(this::isPremiumGuest))
						 .sorted(Comparator.reverseOrder())
						 .limit(guestLimit)
						 .reduce(BigDecimal.ZERO, BigDecimal::add);
		}
		return null;
	}

	private boolean isPremiumGuest(BigDecimal money) {
		return BigDecimal.valueOf(100).compareTo(money) <= 0;
	}
}
