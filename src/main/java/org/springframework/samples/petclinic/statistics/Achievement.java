package org.springframework.samples.petclinic.statistics;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import org.springframework.samples.petclinic.jugador.Jugador;
import org.springframework.samples.petclinic.model.NamedEntity;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "achievements")
public class Achievement extends NamedEntity {
	
	@NotBlank
	private String name;
	
	@NotBlank
	private String description;
	
	@Column(name="metrics")
	@Enumerated(EnumType.STRING)
	private Metrics metrics;
	
	@Min(0)
	private Double threshold;
	
	@Column(name="visibility")
	@Enumerated(EnumType.STRING)
	private Visibility visibility;
	
	@Column(name="difficulty")
	@Enumerated(EnumType.STRING)
	private AchievementDifficulty difficulty;
	
	@ManyToMany
	private List<Jugador> players;
	
	public String getActualDescription() {
		return description.replaceAll("<THRESHOLD>", String.valueOf(threshold));
	}
}
