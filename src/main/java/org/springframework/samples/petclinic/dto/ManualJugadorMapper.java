package org.springframework.samples.petclinic.dto;

import org.springframework.beans.BeanUtils;
import org.springframework.samples.petclinic.jugador.Jugador;
import org.springframework.stereotype.Component;

@Component
public class ManualJugadorMapper {

	public Jugador convertJugadorDTOToEntity(JugadorDTO jugador) {
		Jugador res = new Jugador();
		BeanUtils.copyProperties(jugador, res);
		return res;
	}
	
}
