package org.springframework.samples.petclinic.dto;

import java.io.Serializable;
import javax.validation.Valid;

import org.springframework.samples.petclinic.user.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JugadorDTO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		
	@Valid
	private String firstName;
	@Valid
	private String lastName;
	@Valid
	private User user;
	
}
