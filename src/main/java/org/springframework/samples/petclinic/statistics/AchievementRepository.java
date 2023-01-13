package org.springframework.samples.petclinic.statistics;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface AchievementRepository extends CrudRepository<Achievement, Integer> {
	List<Achievement> findAll();
	
	@Query("SELECT a FROM Achievement a")
	Page<Achievement> findAllPageable(Pageable pageable);
	
	@Query("SELECT a FROM Achievement a WHERE a.visibility = :visibility")
	Page<Achievement> findAllPageableByVisibility(Pageable pageable, Visibility visibility);
	
	@Modifying
	@Transactional
	@Query(value = "DELETE FROM achievements_players WHERE achievement_id = :achievementId AND players_id = :playerId", nativeQuery = true)
	public void deleteAchievement(@Param("achievementId") Integer achievementId, @Param("playerId") Integer playerId);

}
