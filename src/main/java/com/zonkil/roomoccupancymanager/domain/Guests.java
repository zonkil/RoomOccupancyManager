package com.zonkil.roomoccupancymanager.domain;

import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Getter
@ToString
public class Guests {
	private static final BigDecimal THRESHOLD = BigDecimal.valueOf(100);

	private final List<Guest> premiumGuests;
	private final List<Guest> economyGuests;

	public static Guests ofAllGuests(List<Guest> allGuests) {
		List<Guest> prem = new ArrayList<>(allGuests.size() / 2 + 1);
		List<Guest> econ = new ArrayList<>(allGuests.size() / 2);
		allGuests.forEach(guest -> {
			if (isPremiumGuest(guest)) {
				prem.add(guest);
			}
			else {
				econ.add(guest);
			}
		});

		return new Guests(prem, econ);
	}

	public static boolean isPremiumGuest(Guest guest) {
		return guest.getMoneyAmount().compareTo(THRESHOLD) >= 0;
	}

	public Guests(List<Guest> premiumGuests, List<Guest> economyGuests) {
		premiumGuests.sort(Comparator.reverseOrder());
		economyGuests.sort(Comparator.reverseOrder());
		this.premiumGuests = Collections.unmodifiableList(premiumGuests);
		this.economyGuests = Collections.unmodifiableList(economyGuests);
	}

	public int getPremiumGuestsNumber() {
		return getPremiumGuests().size();
	}

	public int getEconomyGuestsNumber() {
		return getEconomyGuests().size();
	}
}
