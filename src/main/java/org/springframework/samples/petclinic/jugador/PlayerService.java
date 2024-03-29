
package org.springframework.samples.petclinic.jugador;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.user.AuthoritiesService;
import org.springframework.samples.petclinic.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.samples.petclinic.comentario.Comentario;
import org.springframework.samples.petclinic.comentario.ComentarioRepository;
import org.springframework.samples.petclinic.partida.Match;
import org.springframework.samples.petclinic.partida.MatchRepository;
import org.springframework.samples.petclinic.statistics.Achievement;

@Service
public class PlayerService {

	private PlayerRepository playerRepo;
	private UserRepository userRepo;
	private AuthoritiesService authService;
	private MatchRepository matchRepo;
	private FriendRequestRepository friendRepo;
	private ComentarioRepository commentRepo;

	@Autowired
	public PlayerService(PlayerRepository playerRepository, UserRepository userRepo, AuthoritiesService authService,
			MatchRepository matchRepo, FriendRequestRepository friendRepo, ComentarioRepository commentRepo) {
		this.playerRepo = playerRepository;
		this.authService = authService;
		this.matchRepo = matchRepo;
		this.userRepo = userRepo;
		this.friendRepo = friendRepo;
		this.commentRepo = commentRepo;
	}

	@Transactional
	public int playerCount() {
		return (int) playerRepo.count();
	}

	@Transactional(readOnly = true)
	public Jugador findJugadorById(int id) throws DataAccessException {
		return playerRepo.findById(id);
	}

	@Transactional(readOnly = true)
	public List<Jugador> findAllJugadores() throws DataAccessException {
		return playerRepo.findAll();
	}
	@Transactional(readOnly = true)
	public Page<Jugador> findAllJugadoresPageable(Pageable pageable) throws DataAccessException {
		return playerRepo.findAllPageable(pageable);
	}

	@Transactional(readOnly = true)
	public Collection<Jugador> findPlayerByLastName(String lastName) throws DataAccessException {
		return playerRepo.findByLastName(lastName);
	}

	@Transactional(readOnly = true)
	public Jugador findPlayerByUsername(String username) throws DataAccessException {
		return playerRepo.findByUserName(username);
	}

	@Transactional(readOnly = true)
	public List<Jugador> findPlayerByKeyword(String keyword) {
		List<Jugador> result = new ArrayList<Jugador>();
		if (keyword != null) {
			result = playerRepo.findByKeyword(keyword.toUpperCase());
		}
		return result;
	}
	
	@Transactional(readOnly = true)
	public Page<Achievement> findAchievementsOfUser(String keyword, Pageable pageable) {			
		return playerRepo.findAchievementsOfUser(keyword.toUpperCase(),pageable);
	}

	@Transactional
	public void deletePlayer(Integer id) throws Exception {
		try {
			playerRepo.findById(id).get().getSentFriendRequests().clear();
			playerRepo.findById(id).get().getReceivedFriendRequests().clear();
			playerRepo.save(playerRepo.findById(id).get());
			
			for(Comentario c : commentRepo.findByPlayer(id)) {
				c.setJugador(null);
			}
			for(FriendRequest fr : friendRepo.findByPlayer(id)) {
				friendRepo.delete(fr);
			}
			for (Jugador j : playerRepo.findAll()) {
				for (Integer i = 0; i < j.playerFriends().size(); i++) {
					if (j.playerFriends().get(i) == playerRepo.findById(id).get())
						j.playerFriends().remove(j.playerFriends().get(i));
					playerRepo.save(j);
				}
			}
			for (Match m : matchRepo.findMatchsWithIdPlayer1(id)) {
				m.setJugador1(null);
				matchRepo.save(m);
			}
			for (Match m : matchRepo.findMatchsWithIdPlayer2(id)) {
				m.setJugador2(null);
				matchRepo.save(m);
			}

			playerRepo.delete(playerRepo.findById(id).get());

		} catch (Exception e) {
			throw new Exception("Error service delete");
		}
	}

	@Transactional
	public Jugador saveJugador(Jugador jugador) throws DataAccessException {
		playerRepo.save(jugador);
		jugador.getUser().setEnabled(true);
		userRepo.save(jugador.getUser());
		authService.saveAuthorities(jugador.getUser().getUsername(), "jugador");
		
		return jugador;
	}
	
	public void saveAchievement(Integer achievementId, Integer playerId) {
		playerRepo.saveAchievement(achievementId, playerId);
	}
	
	
}
