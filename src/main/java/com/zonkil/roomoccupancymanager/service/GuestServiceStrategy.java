package com.zonkil.roomoccupancymanager.service;

import com.zonkil.roomoccupancymanager.service.v2.DbGuestService;
import com.zonkil.roomoccupancymanager.service.v2.GuestService;
import org.springframework.stereotype.Service;

@Service
public class GuestServiceStrategy {

	private final DbGuestService dbGuestService;
	private final DataProviderGuestService dataProviderGuestService;
	private final GuestDataProvider guestDataProvider;

	public GuestServiceStrategy(DbGuestService dbGuestService, DataProviderGuestService dataProviderGuestService,
			GuestDataProvider guestDataProvider) {
		this.dbGuestService = dbGuestService;
		this.dataProviderGuestService = dataProviderGuestService;
		this.guestDataProvider = guestDataProvider;
	}

	public GuestService get() {
		if (guestDataProvider.isInitialized()) {
			return dataProviderGuestService;
		}
		return dbGuestService;
	}
}
