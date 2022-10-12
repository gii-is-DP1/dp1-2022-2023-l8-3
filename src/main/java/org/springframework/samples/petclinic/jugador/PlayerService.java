package org.springframework.samples.petclinic.jugador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PlayerService {
	@Autowired
	private PlayerRepository playerRepo;
	
	@Transactional
	public int playerCount() {
		return (int)	playerRepo.count();
	}
	
	
}
