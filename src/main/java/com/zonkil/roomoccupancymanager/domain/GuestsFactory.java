package com.zonkil.roomoccupancymanager.domain;

import java.math.BigDecimal;
import java.util.List;

public interface GuestsFactory {
	Guests createGuests(List<BigDecimal> allGuests);
}
