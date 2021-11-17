package com.zonkil.roomoccupancymanager.service;

import com.zonkil.roomoccupancymanager.domain.RoomOccupancyCalculation;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.function.Predicate.not;

@Service
public class DefaultRoomOccupancyService implements RoomOccupancyService {

	private static final BigDecimal THRESHOLD = BigDecimal.valueOf(100);

	@Override
	public RoomOccupancyCalculation calculateRoomOccupancy(int numberOfPremiumRooms, int numberOfEconomyRooms,
			List<BigDecimal> guests) {

		var premiumGuests = guests.stream()
								  .filter(this::isPremiumGuest)
								  .sorted(Comparator.reverseOrder())
								  .limit(numberOfPremiumRooms)
								  .collect(Collectors.toList());
		var economyGuests = guests.stream()
								  .filter(not(this::isPremiumGuest))
								  .sorted(Comparator.reverseOrder())
								  .collect(Collectors.toList());

		//is upgrade
		if (numberOfPremiumRooms > premiumGuests.size() && economyGuests.size() > numberOfEconomyRooms) {
			var guestToPromote = economyGuests.stream()
											  .limit(numberOfPremiumRooms - premiumGuests.size())
											  .collect(Collectors.toList());
			premiumGuests.addAll(guestToPromote);
			economyGuests.removeAll(guestToPromote);
		}

		return RoomOccupancyCalculation.builder()
									   .premiumOccupancy(numberOfPremiumRooms > premiumGuests.size() ?
											   premiumGuests.size() :
											   numberOfPremiumRooms)
									   .premiumTotalMoney(
											   premiumGuests.stream().reduce(BigDecimal.ZERO, BigDecimal::add))
									   .economyOccupancy(numberOfEconomyRooms > economyGuests.size() ?
											   economyGuests.size() :
											   numberOfEconomyRooms)
									   .economyTotalMoney(economyGuests.stream()
																	   .limit(numberOfEconomyRooms)
																	   .reduce(BigDecimal.ZERO, BigDecimal::add))
									   .build();
	}

	private boolean isPremiumGuest(BigDecimal guest) {
		return guest.compareTo(THRESHOLD) >= 0;
	}

}
