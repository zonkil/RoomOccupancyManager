package com.zonkil.roomoccupancymanager.service;

import com.zonkil.roomoccupancymanager.service.v2.GuestService;
import com.zonkil.roomoccupancymanager.service.v2.GuestType;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

import java.math.BigDecimal;
import java.util.Comparator;

import static java.util.function.Predicate.not;

@Service("DataProviderGuestService")
public class DataProviderGuestService implements GuestService {

	private final GuestDataProvider guestDataProvider;

	public DataProviderGuestService(GuestDataProvider guestDataProvider) {
		this.guestDataProvider = guestDataProvider;
	}

	@Override
	public long countGuests(GuestType guestType) {
		switch (guestType) {
		case PREMIUM:
			return guestDataProvider.getGuestData().stream().filter(this::isPremiumGuest).count();
		case ECONOMY:
			return guestDataProvider.getGuestData().stream().filter(not(this::isPremiumGuest)).count();
		}
		return 0;
	}

	@Override
	public BigDecimal calculateProfit(GuestType guestType, long guestLimit) {
		switch (guestType) {
		case PREMIUM:
			return guestDataProvider.getGuestData()
									.stream()
									.filter(this::isPremiumGuest)
									.sorted(Comparator.reverseOrder())
									.limit(guestLimit)
									.reduce(BigDecimal.ZERO, BigDecimal::add);
		case ECONOMY:
			return guestDataProvider.getGuestData()
									.stream()
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
