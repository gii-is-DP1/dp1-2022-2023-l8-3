package org.springframework.samples.petclinic.statistics;


import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.jugador.PlayerService;
import org.springframework.samples.petclinic.user.User;
import org.springframework.samples.petclinic.user.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
	private static final String ACHIEVEMENTS_LISTING_VIEW_ADMIN = "/achievements/admin/achievementsListing";
	private static final String ACHIEVEMENTS_FORM = "/achievements/createOrUpdateAchievementForm";
	private AchievementService achievementService;
	private UserService userService;
	private PlayerService playerService;
	
	@Autowired
	public AchievementController(AchievementService achievementService, UserService userService, PlayerService playerService) {
		this.achievementService = achievementService;
		this.userService = userService;
		this.playerService = playerService;
	}
	
	@GetMapping(value = "/")
	public ModelAndView showAchievements() {
		ModelAndView result = new ModelAndView(ACHIEVEMENTS_LISTING_VIEW);
		result.addObject("achievements", achievementService.getAchievements());
		return result;
	}
	
	@GetMapping(value = "/currentPlayer")
	public ModelAndView showCurrentPlayerAchievements() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUser(auth.getName()).get();
		String username = user.getUsername();
		ModelAndView result = new ModelAndView(ACHIEVEMENTS_LISTING_VIEW);
		result.addObject("achievements", playerService.findPlayerByUsername(username).getLogros());
		return result;
	}
	
	@GetMapping(value = "/admin")
	public ModelAndView showAchievementsAdmin() {
		ModelAndView result = new ModelAndView(ACHIEVEMENTS_LISTING_VIEW_ADMIN);
		result.addObject("achievements", achievementService.getAchievements());
		return result;
	}
	
	@GetMapping(value = "/admin/{id}/edit")
	public ModelAndView editAchievement(@PathVariable int id) {
		ModelAndView result = new ModelAndView(ACHIEVEMENTS_FORM);
		Achievement achievement = achievementService.getAchievementById(id);
		result.addObject("achievement", achievement);
		result.addObject("metrics", List.of(Metrics.values()));
		result.addObject("difficulty", List.of(AchievementDifficulty.values()));
		return result;
	}
	
	@PostMapping("/admin/{id}/edit")
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
	
	@GetMapping(value = "/admin/new")
	public ModelAndView newAchievement() {
		Achievement achievement = new Achievement();
		ModelAndView result = new ModelAndView(ACHIEVEMENTS_FORM);
		result.addObject(achievement);
		result.addObject("metrics", List.of(Metrics.values()));
		result.addObject("difficulty", List.of(AchievementDifficulty.values()));
		return result;
	}
	
	@PostMapping("/admin/new")
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
	
	@GetMapping("/admin/{id}/delete")
	public ModelAndView deleteAchievement(@PathVariable int id) {
		Achievement achievement = achievementService.getAchievementById(id);
		achievementService.deleteAchievement(achievement);
		ModelAndView result = showAchievements();
		result.addObject("message", "The achievement was deleted succesfully");
		return result;
	}
}
