package com.zonkil.roomoccupancymanager.domain;

import lombok.Getter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class Guests {
	private static final BigDecimal THRESHOLD = BigDecimal.valueOf(100);

	private final List<BigDecimal> premiumGuests;
	private final List<BigDecimal> economyGuests;

	public static Guests ofAllGuests(List<BigDecimal> allGuests) {
		List<BigDecimal> prem = new ArrayList<>(allGuests.size() / 2 + 1);
		List<BigDecimal> econ = new ArrayList<>(allGuests.size() / 2);
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

	public static boolean isPremiumGuest(BigDecimal guest) {
		return guest.compareTo(THRESHOLD) >= 0;
	}

	public Guests(List<BigDecimal> premiumGuests, List<BigDecimal> economyGuests) {
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

	public Guests promote(int numberGuestToPromote) {
		var guestToPromote = getEconomyGuests().stream().limit(numberGuestToPromote).collect(Collectors.toList());
		var newPrem = new ArrayList<>(getPremiumGuests());
		var newEcon = new ArrayList<>(getEconomyGuests());
		newPrem.addAll(guestToPromote);
		newEcon.removeAll(guestToPromote);
		return new Guests(newPrem, newEcon);
	}
}
