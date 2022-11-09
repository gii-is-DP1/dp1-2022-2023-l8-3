package org.springframework.samples.petclinic.user;

import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.samples.petclinic.jugador.Jugador;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User{
	@Id
	String username;
	
	String email;
	
	String password;
	
	boolean enabled;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
	private Set<Authorities> authorities;
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
	//Es una lista ya que puede haber uno o ningun jugador asociado a un user
	private List<Jugador> jugador;
}
