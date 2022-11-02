package org.springframework.samples.petclinic.statistics;

import javax.persistence.Entity;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import org.springframework.samples.petclinic.model.NamedEntity;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Achievement extends NamedEntity {
	@NotBlank
	private String name;
	@NotBlank
	private String description;
	private String badgeImage;
	@Min(0)
	private Double threshold;
	
	public String getActualDescription() {
		return description.replaceAll("<THRESHOLD>", String.valueOf(threshold));
	}
}
