package org.springframework.samples.petclinic.user;

import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.Email;

import org.springframework.samples.petclinic.jugador.Jugador;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;
	
	@Column(name="username")
	String username;
	
	@Email
	String email;
	
	String password;
	
	boolean enabled;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "user")
	private Set<Authorities> authorities;
	
	@OneToOne(cascade = CascadeType.ALL, mappedBy = "user")
	private Jugador jugador;
	
	public User() {
		
	}
	
	public User(String username) {
		this.username = username;
	}
	
	public User(String username,String password) {
		this.username = username;
		this.password = password;
	}
	
}
