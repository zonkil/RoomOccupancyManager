package com.zonkil.roomoccupancymanager.service.v2;

import com.zonkil.roomoccupancymanager.persistance.repositories.GuestRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class DbGuestService implements GuestService {

	private final GuestRepository guestRepository;

	public DbGuestService(GuestRepository guestRepository) {
		this.guestRepository = guestRepository;
	}

	@Override
	public long countGuests(GuestType guestType) {
		switch (guestType) {
		case PREMIUM:
			return (int) guestRepository.countAllByWillingnessToPayIsGreaterThanEqual(BigDecimal.valueOf(100.0));
		case ECONOMY:
			return (int) guestRepository.countAllByWillingnessToPayIsLessThan(BigDecimal.valueOf(100.0));
		}
		return 0;
	}

	@Override
	public BigDecimal calculateProfit(GuestType guestType, long guestLimit) {
		switch (guestType) {
		case PREMIUM:
			return guestRepository.calculateProfitFromPremiumRooms(guestLimit);
		case ECONOMY:
			return guestRepository.calculateProfitFromEconomyRooms(guestLimit);
		}
		return BigDecimal.ZERO;
	}
}
