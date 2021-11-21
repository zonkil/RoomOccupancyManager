package com.zonkil.roomoccupancymanager.persistance.repositories;

import com.zonkil.roomoccupancymanager.persistance.entieties.GuestEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.math.BigDecimal;

public interface GuestRepository extends CrudRepository<GuestEntity, Long> {

	long countAllByWillingnessToPayIsGreaterThanEqual(BigDecimal t);

	long countAllByWillingnessToPayIsLessThan(BigDecimal t);

	@Query(value = "SELECT sum(willingness_to_pay) FROM (SELECT g.willingness_to_pay FROM guest g where g.willingness_to_pay >= :threshold order by g.willingness_to_pay desc LIMIT :limit)", nativeQuery = true)
	BigDecimal calculateProfitFromPremiumRooms(BigDecimal threshold, long limit);

	@Query(value = "SELECT sum(willingness_to_pay) FROM (SELECT willingness_to_pay FROM guest where willingness_to_pay < :threshold order by willingness_to_pay desc LIMIT :limit)", nativeQuery = true)
	BigDecimal calculateProfitFromEconomyRooms(BigDecimal threshold, long limit);
}
