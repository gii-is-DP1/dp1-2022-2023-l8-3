package org.springframework.samples.petclinic.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MatchDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		
	private Integer disco1, disco2, disco3, disco4, disco5, disco6, disco7;
	private Integer[] deDisco;
	
}
