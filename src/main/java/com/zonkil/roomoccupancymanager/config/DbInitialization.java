package com.zonkil.roomoccupancymanager.config;

import com.zonkil.roomoccupancymanager.persistance.entieties.GuestEntity;
import com.zonkil.roomoccupancymanager.persistance.repositories.GuestRepository;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.stream.Collectors;

@Service
public class DbInitialization {

	private final GuestRepository guestRepository;

	public DbInitialization(GuestRepository guestRepository) {
		this.guestRepository = guestRepository;
	}

	@PostConstruct
	public void init() {
		double[] tmp = { 23.0, 45.0, 155.0, 374.0, 22.0, 99.99, 100.0, 101.0, 115.0, 209.0 };
		var guests = Arrays.stream(tmp)
						   .mapToObj(price -> new GuestEntity(BigDecimal.valueOf(price)))
						   .collect(Collectors.toList());

		guestRepository.saveAll(guests);
	}
}
