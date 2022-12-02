package org.springframework.samples.petclinic.partida;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.disco.Disco;
import org.springframework.samples.petclinic.disco.DishService;
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
}
