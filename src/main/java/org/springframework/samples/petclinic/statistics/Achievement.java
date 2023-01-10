package org.springframework.samples.petclinic.statistics;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.samples.petclinic.jugador.Jugador;
import org.springframework.samples.petclinic.model.NamedEntity;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "achievements")
public class Achievement extends NamedEntity {
	
	@NotEmpty
	private String description;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	private Metrics metrics;
	
	@Min(0)
	@NotNull
	private Double threshold;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	private Visibility visibility;
	
	@NotNull
	@Enumerated(EnumType.STRING)
	private AchievementDifficulty difficulty;
	
	@ManyToMany
	private List<Jugador> players;
	
	public Achievement() {
		
	}
	
	public Achievement(String description) {
		this.description = description;
	}
	
	public String getActualDescription() {
		return description.replaceAll("<THRESHOLD>", String.valueOf(threshold));
	}
}
