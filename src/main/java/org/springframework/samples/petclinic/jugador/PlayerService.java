
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
import org.springframework.samples.petclinic.disco.Disco;
import org.springframework.samples.petclinic.disco.DishRepository;
import org.springframework.samples.petclinic.partida.Match;
import org.springframework.samples.petclinic.partida.MatchRepository;
import org.springframework.samples.petclinic.partida.MatchService;
import org.springframework.samples.petclinic.user.Authorities;
import org.springframework.samples.petclinic.user.AuthoritiesRepository;

@Service
public class PlayerService {
	
	
	private PlayerRepository playerRepo;
	private UserRepository userRepo;
	private AuthoritiesService authService;
	private MatchRepository matchRepo;
	private DishRepository dishRepo;
	
	@Autowired
	public PlayerService(PlayerRepository playerRepository, UserRepository userRepo, AuthoritiesService authService,MatchRepository matchRepo, DishRepository dishRepo) {
		this.playerRepo = playerRepository;
		this.authService = authService;
		this.matchRepo = matchRepo;
		this.userRepo = userRepo;
		this.dishRepo = dishRepo;
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
		return playerRepo.findByUserName(username);
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
	


	@Transactional
	public void deletePlayer(Integer id) throws Exception{
		try {
			playerRepo.findById(id).get().getListaAmigos().clear();
			playerRepo.save(playerRepo.findById(id).get());
			for(Jugador j:playerRepo.findAll()) {
				for(Integer i=0;i<j.getListaAmigos().size();i++) {
					if(j.getListaAmigos().get(i)==playerRepo.findById(id).get())
						j.getListaAmigos().remove(j.getListaAmigos().get(i));
						playerRepo.save(j);
				}
			}
			
			for(Match m:matchRepo.findMatchsWithIdPlayer1(id)) {
				for(Disco d:dishRepo.findDiscosWithMatchId(m.getId())) {
					dishRepo.delete(d);
				}
				matchRepo.delete(m);
			}
			for(Match m:matchRepo.findMatchsWithIdPlayer2(id)) {
				for(Disco d:dishRepo.findDiscosWithMatchId(m.getId())) {
					dishRepo.delete(d);
				}
				matchRepo.delete(m);
			}
			
			playerRepo.delete(playerRepo.findById(id).get());
			
		} catch (Exception e) {
			throw new Exception("Error service delete");
		}
	}
	
	@Transactional
	public void saveJugador(Jugador jugador) throws DataAccessException{
		
		playerRepo.save(jugador);
		jugador.getUser().setEnabled(true);
		userRepo.save(jugador.getUser());
		authService.saveAuthorities(jugador.getUser().getUsername(),"jugador");
		
	}

	
	
}
