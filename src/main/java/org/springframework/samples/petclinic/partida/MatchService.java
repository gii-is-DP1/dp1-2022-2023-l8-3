package org.springframework.samples.petclinic.partida;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.disco.Disco;
import org.springframework.samples.petclinic.disco.DishService;
import org.springframework.samples.petclinic.jugador.Jugador;
import org.springframework.stereotype.Service;


@Service
public class MatchService {
	
	private MatchRepository matchRepository;
	private DishService diskService;
	
	
	
	@Autowired
	public MatchService(MatchRepository matchRepository, DishService diskService) {
		this.matchRepository = matchRepository;
		this.diskService = diskService;
	}
	public Collection<Match> getMatchesWithoutPlayer2() throws DataAccessException{
	    return matchRepository.findMatchesWhitoutPlayer2();
	}
	
	public Collection<Match> getMatches() throws DataAccessException {
		return matchRepository.findAll();
	}
	
	public Match saveMatch(Match match) {
		matchRepository.save(match);
		for(Disco disco: match.getDiscos()) {
		    diskService.saveDisk(disco);
		}
		return match;
	}
	
	public Match getMatchById(Integer id) {
		return matchRepository.findById(id).get();
	}
	
	public List<Match> getMatchesByGameWinner(GameWinner gameWinner){
		return matchRepository.findMatchesByGameWinner(gameWinner);
	}

	public Collection<Match> getMatchesOfAPlayer(Integer id){
		return matchRepository.findMatchesOfAPlayer(id);
	}
	
	public Boolean canIplay(Jugador player) {
	    Boolean res = true;
	    int contador = 0;
	    List<Match> todas = matchRepository.findAll();
	    for(Match partida:todas) {
	        if(partida.getInicioPartida().toLocalDate().equals(LocalDate.now())&&(partida.getJugador1()==player||partida.getJugador2()==player)) {
	            contador++;
	        }
	        if(contador>=5) {
	            res = false;
	            break;
	        }
	        System.out.println(contador +" & "+ partida.getInicioPartida().toLocalDate() +" & "+ LocalDate.now() + " & Boolean de fecha: "+ (partida.getInicioPartida().toLocalDate().equals(LocalDate.now())) + " & Boolean de player: " + (partida.getJugador1()==player||partida.getJugador2()==player));  
	    }
	    
	    return res;
	}
}
