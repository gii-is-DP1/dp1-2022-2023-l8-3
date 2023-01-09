package org.springframework.samples.petclinic.dto;

import java.io.Serializable;
import java.util.List;

import org.springframework.samples.petclinic.jugador.Jugador;
import org.springframework.samples.petclinic.statistics.AchievementDifficulty;
import org.springframework.samples.petclinic.statistics.Metrics;
import org.springframework.samples.petclinic.statistics.Visibility;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AchievementDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		
	private Integer id;
	private String name;
	private String description;
	private Metrics metrics;
	private Double threshold;
	private Visibility visibility;
	private AchievementDifficulty difficulty;
	private List<Jugador> players;
	
}
