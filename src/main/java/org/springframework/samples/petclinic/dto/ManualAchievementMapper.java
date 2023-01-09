package org.springframework.samples.petclinic.dto;

import org.springframework.beans.BeanUtils;
import org.springframework.samples.petclinic.statistics.Achievement;
import org.springframework.stereotype.Component;

@Component
public class ManualAchievementMapper {

	public Achievement convertAchievementDTOToEntity(AchievementDTO achievement) {
		Achievement res = new Achievement();
		BeanUtils.copyProperties(achievement, res);
		return res;
	}
	
}
