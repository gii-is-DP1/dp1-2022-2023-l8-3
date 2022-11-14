package org.springframework.samples.petclinic.partida;

import java.util.Collection;

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
	public Collection<Match> getMatchWithotP2() throws DataAccessException{
	    return matchRepository.findMatchesWhitoutP2();
	}
	
	public Collection<Match> getMatches() throws DataAccessException {
		return matchRepository.findAll();
	}
	
	public void saveMatch(Match match) {
		matchRepository.save(match);
		for(Disco disco: match.getDiscos()) {
		    diskService.saveDisk(disco);
		}
	}
	
	public Match getMatchById(Integer id) {
		return matchRepository.findById(id).get();
	}
	
	public Collection<Match> getMatchesOfAPlayer(Integer id){
		return matchRepository.findMatchesOfAPlayer(id);
	}
}
