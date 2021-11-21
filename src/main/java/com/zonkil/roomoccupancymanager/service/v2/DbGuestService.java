package com.zonkil.roomoccupancymanager.service.v2;

import com.zonkil.roomoccupancymanager.persistance.repositories.GuestRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class DbGuestService implements GuestService {

	private final GuestRepository guestRepository;
	private final BigDecimal threshold;

	public DbGuestService(GuestRepository guestRepository, @Value("${premium-guest.threshold}") BigDecimal threshold) {
		this.guestRepository = guestRepository;
		this.threshold = threshold;
	}

	@Override
	public long countGuests(GuestType guestType) {
		switch (guestType) {
		case PREMIUM:
			return (int) guestRepository.countAllByWillingnessToPayIsGreaterThanEqual(threshold);
		case ECONOMY:
			return (int) guestRepository.countAllByWillingnessToPayIsLessThan(threshold);
		}
		return 0;
	}

	@Override
	public BigDecimal calculateProfit(GuestType guestType, long guestLimit) {
		switch (guestType) {
		case PREMIUM:
			return guestRepository.calculateProfitFromPremiumRooms(threshold, guestLimit);
		case ECONOMY:
			return guestRepository.calculateProfitFromEconomyRooms(threshold, guestLimit);
		}
		return BigDecimal.ZERO;
	}
}
