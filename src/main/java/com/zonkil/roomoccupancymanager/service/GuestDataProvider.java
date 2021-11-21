package com.zonkil.roomoccupancymanager.service;

import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@RequestScope
public class GuestDataProvider {

	private List<BigDecimal> guestData;

	public synchronized void initialize(List<BigDecimal> guestData) {
		Objects.requireNonNull(guestData);
		if (this.guestData != null) {
			throw new IllegalStateException("Data provider already initialized");
		}
		this.guestData = Collections.unmodifiableList(guestData);
	}

	public synchronized List<BigDecimal> getGuestData() {
		if (guestData == null) {
			throw new IllegalStateException("Data provider not initialized");
		}
		return guestData;
	}

	public synchronized boolean isInitialized() {
		return guestData != null;
	}
}
