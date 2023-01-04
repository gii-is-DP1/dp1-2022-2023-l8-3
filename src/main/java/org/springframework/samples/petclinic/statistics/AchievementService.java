package org.springframework.samples.petclinic.statistics;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
	
	public Page<Achievement> getAchievementsPageable(Pageable pageable) throws DataAccessException {
		return achievementRepository.findAllPageable(pageable);
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
	
	public List<Achievement> getPublicAchievements() throws DataAccessException {
        Collection<Achievement> todos=  achievementRepository.findAll();
        return todos.stream().filter(x -> x.getVisibility() == Visibility.PUBLICADO).collect(Collectors.toList());
    }

	public Page<Achievement> getPublicAchievementsPageable(Pageable pageable) throws DataAccessException {
        return achievementRepository.findAllPageableByVisibility(pageable, Visibility.PUBLICADO);
    }

}
