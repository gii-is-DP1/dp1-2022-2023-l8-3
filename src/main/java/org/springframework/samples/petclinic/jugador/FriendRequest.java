package org.springframework.samples.petclinic.jugador;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;

import org.springframework.samples.petclinic.model.BaseEntity;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class FriendRequest extends BaseEntity {

	@ManyToOne(fetch = FetchType.LAZY)
	private Jugador jugador1;

	@ManyToOne(fetch = FetchType.LAZY)
	private Jugador jugador2;

	@Column(name = "resultado")
	private Boolean resultado;

	public FriendRequest() {
		
	}
	
	public FriendRequest(Jugador jugador1, Jugador jugador2) {
		this.jugador1 = jugador1;
		this.jugador2 = jugador2;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, jugador1, jugador2, resultado);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FriendRequest other = (FriendRequest) obj;
		return Objects.equals(id, other.id) && Objects.equals(jugador1, other.jugador1)
				&& Objects.equals(jugador2, other.jugador2) && Objects.equals(resultado, other.resultado);
	}

}
