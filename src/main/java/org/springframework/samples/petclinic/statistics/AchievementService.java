package org.springframework.samples.petclinic.statistics;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;


@Service
public class AchievementService {
	
	private AchievementRepository achievementRepository;
	
	@Autowired
	public AchievementService(AchievementRepository achievementRepository) {
		this.achievementRepository = achievementRepository;
	}
	
	public Collection<Achievement> getAchievements() throws DataAccessException {
		return achievementRepository.findAll();
	}
	
	public void saveAchievement(Achievement achievement) {
		achievementRepository.save(achievement);
	}
	
	public void deleteAchievement(Achievement achievement) {
		achievementRepository.delete(achievement);
	}
	
	public Achievement getAchievementById(Integer id) {
		return achievementRepository.findById(id).get();
	}

}
