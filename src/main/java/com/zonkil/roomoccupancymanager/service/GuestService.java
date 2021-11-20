package com.zonkil.roomoccupancymanager.service;

import java.math.BigDecimal;

public interface GuestService {

	int countGuests(GuestType guestType);

	BigDecimal calculateProfit(GuestType guestType, int guestLimit);
}
