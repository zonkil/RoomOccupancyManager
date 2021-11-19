package com.zonkil.roomoccupancymanager.domain;

import lombok.Getter;
import lombok.ToString;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Getter
@ToString
public class Guests {
	private final List<Guest> premiumGuests;
	private final List<Guest> economyGuests;

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
