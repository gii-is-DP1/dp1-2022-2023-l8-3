package org.springframework.samples.petclinic.statistics;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
class AchievementServiceTests {

	@Autowired
	private AchievementService achievementService;
	
	@Test
	void testAchievements() {
		List<Achievement> achievements = (List<Achievement>) achievementService.getAchievements();
		for (Achievement achievement : achievements) {
			assertNotNull(achievement);
		}
	}
	
	@Test
	void testAchievementById() {
		Integer id = 1;
		assertNotNull(achievementService.getAchievementById(id));
	}
	
	@Test
	void testSaveAchievement() {
		Achievement achievement = new Achievement("prueba");
		achievement.setDescription("");
		achievement.setDifficulty(AchievementDifficulty.ORO);
		achievement.setId(1);
		achievement.setMetrics(Metrics.AMIGOS);
		achievement.setPlayers(new ArrayList<>());
		achievement.setThreshold(8.0);
		achievement.setVisibility(Visibility.PUBLICADO);
		achievement.setName("prueba");
		achievementService.saveAchievement(achievement);
		Integer id = achievement.getId();
		assertNotNull(achievementService.getAchievementById(id));
	}
	
	@Test
	void testDeleteAchievement() {
		Achievement achievement = new Achievement("prueba");
		achievement.setDescription("");
		achievement.setDifficulty(AchievementDifficulty.ORO);
		achievement.setId(1);
		achievement.setMetrics(Metrics.AMIGOS);
		achievement.setPlayers(new ArrayList<>());
		achievement.setThreshold(8.0);
		achievement.setVisibility(Visibility.PUBLICADO);
		achievement.setName("prueba");
		achievementService.saveAchievement(achievement);
		Integer id = achievement.getId();
		achievementService.deleteAchievement(achievement);
		assertThrows(NoSuchElementException.class, ()->achievementService.getAchievementById(id));
	}
	
}
