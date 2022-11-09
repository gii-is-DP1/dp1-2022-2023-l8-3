package org.springframework.samples.petclinic.partida;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;


@Service
public class MatchService {
	
	private MatchRepository matchRepository;
	
	@Autowired
	public MatchService(MatchRepository matchRepository) {
		this.matchRepository = matchRepository;
	}
	public Collection<Match> getMatchWithotP2() throws DataAccessException{
	    return matchRepository.findMatchesWhitoutP2();
	}
	
	public Collection<Match> getMatches() throws DataAccessException {
		return matchRepository.findAll();
	}
	
	public void saveMatch(Match match) {
		matchRepository.save(match);
	}
	
	public Match getMatchById(Integer id) {
		return matchRepository.findById(id).get();
	}
	
	public List<Match> getMatchesInProgressOrFinished(GameWinner gameWinner){
		return matchRepository.findMatchesInProgressOrFinished(gameWinner);
	}
}
