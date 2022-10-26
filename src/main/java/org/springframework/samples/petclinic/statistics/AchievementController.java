package org.springframework.samples.petclinic.statistics;


import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/statistics/achievements")
public class AchievementController {
	
	private static final String ACHIEVEMENTS_LISTING_VIEW = "/achievements/achievementsListing";
	private static final String ACHIEVEMENTS_FORM = "/achievements/createOrUpdateAchievementForm";
	private AchievementService achievementService;
	
	@Autowired
	public AchievementController(AchievementService achievementService) {
		this.achievementService = achievementService;
	}
	
	@GetMapping(value = "/")
	public ModelAndView showAchievements() {
		ModelAndView result = new ModelAndView(ACHIEVEMENTS_LISTING_VIEW);
		result.addObject("achievements", achievementService.getAchievements());
		return result;
	}
	
	@GetMapping(value = "/{id}/edit")
	public ModelAndView editAchievement(@PathVariable int id) {
		ModelAndView result = new ModelAndView(ACHIEVEMENTS_FORM);
		Achievement achievement = achievementService.getAchievementById(id);
		result.addObject(achievement);
		return result;
	}
	
	@PostMapping("/{id}/edit")
	public ModelAndView saveAchievement(@PathVariable int id, @Valid Achievement achievement, BindingResult br) {
		ModelAndView result;
		if(br.hasErrors()) {
			result = new ModelAndView(ACHIEVEMENTS_FORM, br.getModel());
		} else {
			Achievement achievementToBeUpdated = achievementService.getAchievementById(id);
			BeanUtils.copyProperties(achievement, achievementToBeUpdated, "id");
			achievementService.saveAchievement(achievementToBeUpdated);
			result = showAchievements();
			result.addObject("message", "The achievement was updated succesfully");
		}
		return result;
	}
	
	@GetMapping(value = "/new")
	public ModelAndView newAchievement() {
		Achievement achievement = new Achievement();
		ModelAndView result = new ModelAndView(ACHIEVEMENTS_FORM);
		result.addObject(achievement);
		return result;
	}
	
	@PostMapping("/new")
	public ModelAndView saveAchievement(@Valid Achievement achievement, BindingResult br) {
		ModelAndView result;
		if(br.hasErrors()) {
			result = new ModelAndView(ACHIEVEMENTS_FORM, br.getModel());
		} else {
			achievementService.saveAchievement(achievement);
			result = showAchievements();
			result.addObject("message", "The achievement was added succesfully");
		}
		return result;
	}
	
	@GetMapping("/{id}/delete")
	public ModelAndView deleteAchievement(@PathVariable int id) {
		Achievement achievement = achievementService.getAchievementById(id);
		achievementService.deleteAchievement(achievement);
		ModelAndView result = showAchievements();
		result.addObject("message", "The achievement was deleted succesfully");
		return result;
	}
}
