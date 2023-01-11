package org.springframework.samples.petclinic.statistics;


import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.samples.petclinic.jugador.Jugador;
import org.springframework.samples.petclinic.jugador.PlayerService;
import org.springframework.samples.petclinic.partida.MatchService;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/statistics/achievements")
public class AchievementController {
	
	private static final String ACHIEVEMENTS_LISTING_VIEW = "/achievements/achievementsListing";
	private static final String ACHIEVEMENTS_LISTING_VIEW_ADMIN = "/achievements/admin/achievementsListing";
	private static final String ACHIEVEMENTS_FORM = "/achievements/createOrUpdateAchievementForm";
	
	private AchievementService achievementService;
	private UserService userService;
	private PlayerService playerService;
	private MatchService matchService;
	
	@Autowired
	public AchievementController(AchievementService achievementService, UserService userService, PlayerService playerService, MatchService matchService) {
		this.achievementService = achievementService;
		this.userService = userService;
		this.playerService = playerService;
		this.matchService = matchService;
	}
	
	@GetMapping(value = "/{page}")
	public ModelAndView showAchievements(@PathVariable("page") int page) {
		if(page<1) 
			return new ModelAndView("redirect:/statistics/achievements/1");

		ModelAndView result = new ModelAndView(ACHIEVEMENTS_LISTING_VIEW);

		Pageable pageable = PageRequest.of(page-1, 10);
		Page<Achievement>achievements = achievementService.getPublicAchievementsPageable(pageable);

		Integer numberOfPages = achievements.getTotalPages();
		Integer thisPage = page;

		if(thisPage > numberOfPages && numberOfPages != 0) 
			return new ModelAndView("redirect:/statistics/achievements/"+numberOfPages);

		result.addObject("numberOfPages", numberOfPages);
		result.addObject("thisPage", thisPage);		
		result.addObject("url", "/statistics/achievements/");		
		result.addObject("achievements", achievements.getContent());
		
		return result;
	}
	
	@GetMapping(value = "/currentPlayer/{page}")
	public ModelAndView showCurrentPlayerAchievements(@PathVariable("page") int page) {
		if(page<1) 
			return new ModelAndView("redirect:/statistics/achievements/currentPlayer/1");

		ModelAndView result;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userService.findUser(auth.getName()).get();
		String username = user.getUsername();
		Jugador player = playerService.findPlayerByUsername(username);

		if(matchService.getMatchesOfAPlayer(player.getId()).size()==0) {

			result = new ModelAndView("welcome");
			result.addObject("message", "Para desbloquear los logros debes jugar una partida");
		} else {
			result = new ModelAndView(ACHIEVEMENTS_LISTING_VIEW);
			Pageable pageable = PageRequest.of(page-1, 10);
			Page<Achievement>achievements = playerService.findAchievementsOfUser(username, pageable);
			Integer numberOfPages = achievements.getTotalPages();
			Integer thisPage = page;

			if((thisPage > numberOfPages) && numberOfPages != 0) 
				return new ModelAndView("redirect:/statistics/achievements/currentPlayer/"+numberOfPages);

			result.addObject("numberOfPages", numberOfPages);
			result.addObject("thisPage", thisPage);	
			result.addObject("url", "/statistics/achievements/currentPlayer/");		
			result.addObject("achievements", achievements.getContent());
		}
		
		return result;
	}
	
	@GetMapping(value = "/admin/{page}")
	public ModelAndView showAchievementsAdmin(@PathVariable("page") int page) {
		if(page<1) 
			return new ModelAndView("redirect:/statistics/achievements/admin/1");
		
		ModelAndView result = new ModelAndView(ACHIEVEMENTS_LISTING_VIEW_ADMIN);
		
		Pageable pageable = PageRequest.of(page-1, 10);
		Page<Achievement> achievements = achievementService.getAchievementsPageable(pageable);
			
		Integer numberOfPages = achievements.getTotalPages();
		Integer thisPage = page;
		
		if(thisPage > numberOfPages && numberOfPages != 0) 
			return new ModelAndView("redirect:/statistics/achievements/admin/"+numberOfPages);

		result.addObject("numberOfPages", numberOfPages);
		result.addObject("thisPage", thisPage);		
		result.addObject("achievements", achievements.getContent());
		
		return result;
	}
	
	@GetMapping(value = "/admin/new")
	public ModelAndView newAchievement(Map<String, Object> model) {
		ModelAndView result = new ModelAndView(ACHIEVEMENTS_FORM);
		model.put("achievement", new Achievement());
		result.addObject("metrics", List.of(Metrics.values()));
		result.addObject("difficulty", List.of(AchievementDifficulty.values()));
		result.addObject("visibility", List.of(Visibility.values()));
		return result;
	}
	
	@PostMapping("/admin/new")
	public ModelAndView saveAchievement(@Valid Achievement achievement, BindingResult br, Map<String, Object> model) {
		ModelAndView result;
		if(Boolean.TRUE.equals(br.hasErrors())) {
			log.error("Input error");
			result = new ModelAndView(ACHIEVEMENTS_FORM);
			model.put("achievement", achievement);
			result.addObject("achievement", achievement);
		} else {
			if(Boolean.FALSE.equals(itRepeatsTheMetricAndLimit(achievement))) {
				achievementService.saveAchievement(achievement);
				result = showAchievements(1);
				log.info("Achievement created");
				result.addObject("message", "The achievement was added succesfully");
			} else {
				result = new ModelAndView(ACHIEVEMENTS_FORM);
				model.put("achievement", achievement);
				result.addObject("message", "La metrica y limite coinciden con un logro existente");
			}
		}
		return result;
	}
	
	@GetMapping(value = "/admin/{id}/edit")
	public ModelAndView editAchievement(@PathVariable int id, RedirectAttributes ra) {
		ModelAndView result;
		Achievement achievement = achievementService.getAchievementById(id);
		if(achievement.getVisibility().equals(Visibility.EN_BORRADOR)) {
			result = new ModelAndView(ACHIEVEMENTS_FORM);
			result.addObject("achievement", achievement);
			result.addObject("metrics", List.of(Metrics.values()));
			result.addObject("difficulty", List.of(AchievementDifficulty.values()));
			result.addObject("visibility", List.of(Visibility.values()));
		} else {
			result = new ModelAndView(ACHIEVEMENTS_LISTING_VIEW_ADMIN);
			result.setViewName("redirect:/statistics/achievements/admin/1");
			ra.addFlashAttribute("message", "Cannot edit an achievement that has already been published");
		}
		
		return result;
	}
	
	@PostMapping("/admin/{id}/edit")
	public ModelAndView saveAchievement(@PathVariable int id, @Valid Achievement achievement, BindingResult br, Map<String, Object> model) {
		ModelAndView result = showAchievementsAdmin(1);
		
		if(Boolean.TRUE.equals(br.hasErrors())) {
			log.error("Input error");
			result = new ModelAndView(ACHIEVEMENTS_FORM);
			model.put("achievement", achievement);
			result.addObject("achievement", achievement);
		} else {
			Achievement achievementToBeUpdated = achievementService.getAchievementById(id);
			Boolean itRepeats = itRepeatsTheMetricAndLimit(achievement);
			Boolean theyAreEquals = theyAreEquals(achievement, achievementToBeUpdated);
			BeanUtils.copyProperties(achievement, achievementToBeUpdated, "id");
			
			if(Boolean.FALSE.equals(itRepeats) || Boolean.TRUE.equals(theyAreEquals)) {
				achievementService.saveAchievement(achievementToBeUpdated);
				result = showAchievements(1);
				log.info("Achievement updated");
				result.addObject("message", "The achievement was updated succesfully");
			} else {
				result = new ModelAndView(ACHIEVEMENTS_FORM);
				model.put("achievement", achievement);
				result.addObject("message", "La metrica y limite coinciden con un logro existente");
			}
			

		}
		return result;
	}
	
	private Boolean itRepeatsTheMetricAndLimit(Achievement achievement) {
		Boolean isRepeated = false;
		Integer i = 0;
		List<Achievement> achievements = (List<Achievement>) achievementService.getAchievements();
		while(Boolean.FALSE.equals(isRepeated) && i < achievements.size()) {
			isRepeated = achievement.getMetrics().equals(achievements.get(i).getMetrics()) && achievement.getThreshold().equals(achievements.get(i).getThreshold());
			i++;
		}
		return isRepeated;
	}
	
	private Boolean theyAreEquals(Achievement ach1, Achievement ach2) {
		return ach1.getName().equals(ach2.getName()) && ach1.getDescription().equals(ach2.getDescription()) && ach1.getMetrics().equals(ach2.getMetrics())
				&& ach1.getThreshold().equals(ach2.getThreshold()) && ach1.getDifficulty().equals(ach2.getDifficulty());
	}
	
	@GetMapping("/admin/{id}/delete")
	public ModelAndView deleteAchievement(@PathVariable int id) {
		Achievement achievement = achievementService.getAchievementById(id);
		achievementService.deleteAchievement(achievement);
		log.info("Achievement deleted");
		ModelAndView result = showAchievements(1);
		result.addObject("message", "The achievement was deleted succesfully");
		return result;
	}
}
