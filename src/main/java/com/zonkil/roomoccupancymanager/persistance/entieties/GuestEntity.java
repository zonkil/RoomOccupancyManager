package com.zonkil.roomoccupancymanager.persistance.entieties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@NoArgsConstructor
@Data
@Entity
@Table(name = "guest")
public class GuestEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "willingnes_to_pay")
	private BigDecimal willingnessToPay;

	public GuestEntity(BigDecimal willingnessToPay) {
		this.willingnessToPay = willingnessToPay;
	}
}
