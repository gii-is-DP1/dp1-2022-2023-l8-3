
package org.springframework.samples.petclinic.jugador;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.user.AuthoritiesService;
import org.springframework.samples.petclinic.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.samples.petclinic.user.Authorities;
import org.springframework.samples.petclinic.user.AuthoritiesRepository;

@Service
public class PlayerService {
	
	
	private PlayerRepository playerRepo;
	private UserRepository userRepo;
	private AuthoritiesService authService;
	private AuthoritiesRepository authRepo;
	
	@Autowired
	public PlayerService(PlayerRepository playerRepository, UserRepository userRepo, AuthoritiesService authService,AuthoritiesRepository authRepo) {
		this.playerRepo = playerRepository;
		this.authService = authService;
		this.authRepo = authRepo;
		this.userRepo = userRepo;
	}	
	
	@Transactional
	public int playerCount() {
		return (int)	playerRepo.count();
	}
	
	@Transactional(readOnly = true)
	public Jugador findJugadorById(int id) throws DataAccessException {
		return playerRepo.findById(id);
	}
	
	@Transactional(readOnly = true)
	public Jugador findJugadorByUsername(String username) throws DataAccessException {
		return playerRepo.findByUsername(username);
	}
	
	@Transactional(readOnly = true)
	public List<Jugador> findAllJugadores() throws DataAccessException {
		return playerRepo.findAll();
	}
	
	@Transactional(readOnly = true)
	public Collection<Jugador> findPlayerByLastName(String lastName) throws DataAccessException {
		return playerRepo.findByLastName(lastName);
	}
	
	

	@Transactional(readOnly = true)
	public Jugador findJugadorByUserName(String userName) throws DataAccessException{
	    return playerRepo.findByUserName(userName);
	}
	
//	@Transactional(readOnly = true)
//	public Collection<Owner> findOwnerByLastName(String lastName) throws DataAccessException {
//		return playerRepo.findByLastName(lastName);
//	}

	@Transactional
	public void deletePlayer(Integer id) throws Exception{
		try {
			playerRepo.findById(id).get().getListaAmigos().clear();
			playerRepo.deleteById(id);
		} catch (Exception e) {
			throw new Exception("Error service delete");
		}
	}
	
	@Transactional
	public void saveJugador(Jugador jugador) throws DataAccessException{
		
		playerRepo.save(jugador);

		userRepo.save(jugador.getUser());
		authService.saveAuthorities(jugador.getUser().getUsername(),"jugador");
		
	}

	
	
}
