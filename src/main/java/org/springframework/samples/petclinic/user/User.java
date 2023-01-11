package org.springframework.samples.petclinic.user;

import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.springframework.samples.petclinic.jugador.Jugador;
import org.springframework.samples.petclinic.model.BaseEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Audited
@Table(name = "users")
public class User extends BaseEntity{
	
	@NotEmpty
	@Size(min = 5, max = 50)
	String username;
	
	@NotEmpty
	String email;
	
	@NotEmpty
	String password;
	
	boolean enabled;
	
	@NotAudited
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
