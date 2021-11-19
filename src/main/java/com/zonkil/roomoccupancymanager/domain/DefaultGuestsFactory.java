package com.zonkil.roomoccupancymanager.domain;

import com.zonkil.roomoccupancymanager.service.GuestUpgradeService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class DefaultGuestsFactory implements GuestsFactory {

	private final GuestUpgradeService guestUpgradeService;

	public DefaultGuestsFactory(GuestUpgradeService guestUpgradeService) {
		this.guestUpgradeService = guestUpgradeService;
	}

	@Override
	public Guests createGuests(List<BigDecimal> allGuests) {
		List<Guest> prem = new ArrayList<>(allGuests.size() / 2 + 1);
		List<Guest> econ = new ArrayList<>(allGuests.size() / 2);
		allGuests.forEach(guest -> {
			var newGuest = new Guest(guest);
			if (guestUpgradeService.isPremiumGuest(newGuest)) {
				prem.add(newGuest);
			}
			else {
				econ.add(newGuest);
			}
		});

		return new Guests(prem, econ);
	}
}
