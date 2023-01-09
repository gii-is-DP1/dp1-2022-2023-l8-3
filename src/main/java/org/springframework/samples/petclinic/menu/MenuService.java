package org.springframework.samples.petclinic.menu;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.jugador.Jugador;
import org.springframework.samples.petclinic.jugador.PlayerRepository;
import org.springframework.samples.petclinic.user.User;
import org.springframework.samples.petclinic.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {
	
	private MenuPlayerRepository menuPlayerRepo;
	private MenuUserRepository menuUserRepo;

	@Autowired
	public MenuService(MenuPlayerRepository menuPlayerRepo,MenuUserRepository menuUserRepo) {
		this.menuPlayerRepo = menuPlayerRepo;
		this.menuUserRepo=menuUserRepo;
	}
	
	
	@Transactional(readOnly = true)
	public Jugador findPlayerByUsername(String username) throws DataAccessException {
		return menuPlayerRepo.findByUserName(username);
	}
	
	public Optional<User> findUser(String username) {
		return menuUserRepo.findByUsername(username);
	}

}
