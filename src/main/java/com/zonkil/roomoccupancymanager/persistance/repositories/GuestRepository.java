package com.zonkil.roomoccupancymanager.persistance.repositories;

import com.zonkil.roomoccupancymanager.persistance.entieties.GuestEntity;
import org.springframework.data.repository.CrudRepository;

public interface GuestRepository extends CrudRepository<GuestEntity, Long> {}
