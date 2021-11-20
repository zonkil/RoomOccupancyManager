package com.zonkil.roomoccupancymanager.service.v2;

import java.math.BigDecimal;

public interface GuestService {

	long countGuests(GuestType guestType);

	BigDecimal calculateProfit(GuestType guestType, long guestLimit);
}
