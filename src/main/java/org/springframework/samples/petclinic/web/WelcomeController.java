package org.springframework.samples.petclinic.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.dto.JugadorDTO;
import org.springframework.samples.petclinic.dto.ManualJugadorMapper;
import org.springframework.samples.petclinic.invitacion.Invitacion;
import org.springframework.samples.petclinic.invitacion.InvitationService;
import org.springframework.samples.petclinic.jugador.Jugador;
import org.springframework.samples.petclinic.jugador.PlayerService;
import org.springframework.samples.petclinic.menu.MenuService;
import org.springframework.samples.petclinic.partida.Match;
import org.springframework.samples.petclinic.partida.MatchService;
import org.springframework.samples.petclinic.statistics.Achievement;
import org.springframework.samples.petclinic.statistics.AchievementService;
import org.springframework.samples.petclinic.user.Authorities;
import org.springframework.samples.petclinic.user.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class WelcomeController {

	private static final String EMAIL_PATTERN = "^.+@.+\\..+$";
	private static final String CREATE_OR_UPDATE_PLAYER_VIEW = "jugadores/createOrUpdateJugadorForm";
	private static final int NUMBER_OF_PLAYERS_IN_GLOBAL_RANKING = 10;
	private InvitationService invitacionService;
	private MenuService menuService;
	private PlayerService playerService;
	private MatchService matchService;
	private AchievementService achievementService;

	@Autowired
	public WelcomeController(InvitationService invitacionService, MenuService menuService, PlayerService playerService,
			MatchService matchService, AchievementService achievementService) {
		this.playerService = playerService;
		this.menuService = menuService;
		this.invitacionService = invitacionService;
		this.matchService = matchService;
		this.achievementService = achievementService;
	}

	@GetMapping("/")
	public ModelAndView welcome() {
		ModelAndView result = new ModelAndView("welcome");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user1 = new User("testUser1", "testUser1");
		Set<Authorities> conj = new HashSet<Authorities>();
		Authorities authority = new Authorities();
		authority.setUser(user1);
		authority.setAuthority("admin");
		conj.add(authority);
		user1.setAuthorities(conj);
		result.addObject("players",
				playerService.findAllJugadores().stream()
						.sorted(Comparator.comparing(Jugador::getNumberOfGamesWon).reversed())
						.limit(NUMBER_OF_PLAYERS_IN_GLOBAL_RANKING).collect(Collectors.toList()));
		if (auth != null) {
			Boolean b = true;
			for (Authorities a : menuService.findUser(auth.getName()).orElse(user1).getAuthorities()) {
				if (a.getAuthority().equals("admin")) {
					b = false;
				}
			}
			if (Boolean.TRUE.equals(b)) {
				Jugador jugadorActual = menuService.findPlayerByUsername(auth.getName());
				checkAchievements(jugadorActual);
				Collection<Match> partidas = matchService.getMatches();
				for (Match partida : partidas) {
					if (partida.getFinPartida() == null
							&& (partida.getJugador1() == jugadorActual || partida.getJugador2() == jugadorActual)) {
						int id = partida.getId();
						result.addObject("matchId", id);
						if (partida.getJugador2() != null) {
							result.addObject("partidaPendiente", true);
						} else {
							result.addObject("jugador2NoUnido", true);
						}
					}
				}
				List<Invitacion> lista = invitacionService.getInvitacionByInvitadoId(jugadorActual.getId());
				if (lista.isEmpty()) {
					result.addObject("tengoInvitaciones", false);
				} else {
					result.addObject("tengoInvitaciones", true);
					result.addObject("mensaje",
							"Ha recibido una invitaci&oacute;n a una partida, acceda a trav&eacute;s de la campana del menu");
				}
			}
		} else {
			result.addObject("tengoInvitaciones", false);
			result.addObject("mensaje", "este mensaje no vale para nada");
		}
		return result;
	}

	public void checkAchievements(Jugador actualPlayer) {
		List<Achievement> achievements = new ArrayList<>(achievementService.getPublicAchievements());
		for (Achievement achievement : achievements) {
			switch (achievement.getMetrics()) {
			case JUGAR_PARTIDAS:
				if (Boolean.FALSE.equals(actualPlayer.getLogros().contains(achievement))
						&& actualPlayer.getNumberOfGames() >= achievement.getThreshold()) {
					playerService.saveAchievement(achievement.getId(), actualPlayer.getId());
				}
				break;
			case GANAR_PARTIDAS:
				if (Boolean.FALSE.equals(actualPlayer.getLogros().contains(achievement))
						&& actualPlayer.getNumberOfGamesWon() >= achievement.getThreshold()) {
					playerService.saveAchievement(achievement.getId(), actualPlayer.getId());
				}
				break;
			case COLOCAR_SARCINAS:
				if (Boolean.FALSE.equals(actualPlayer.getLogros().contains(achievement))
						&& actualPlayer.getNumberOfSarcinasPlaced() >= achievement.getThreshold()) {
					playerService.saveAchievement(achievement.getId(), actualPlayer.getId());
				}
				break;
			case AMIGOS:
				if (Boolean.FALSE.equals(actualPlayer.getLogros().contains(achievement))
						&& actualPlayer.getNumberOfFriends() >= achievement.getThreshold()) {
					playerService.saveAchievement(achievement.getId(), actualPlayer.getId());
				}
				break;
			}
		}
	}

	@GetMapping(value = "/registerNewJugador")
	public String getRegisterNewPlayer(Map<String, Object> model, Map<String, Object> model2) {
		Jugador jugador = new Jugador();
		model.put("jugador", jugador);
		Collection<Jugador> c = this.playerService.findAllJugadores();
		if (c.isEmpty()) {
			model2.put("sinJugadores", true);
		}
		return CREATE_OR_UPDATE_PLAYER_VIEW;
	}

	@PostMapping(value = "/registerNewJugador")
	public ModelAndView postRegisterNewPlayer(@Valid JugadorDTO jugadorDto, BindingResult br,
			Map<String, Object> model) {
		Boolean correctPassword = false;
		ModelAndView resul;
		ManualJugadorMapper m = new ManualJugadorMapper();
		Jugador jugador = m.convertJugadorDTOToEntity(jugadorDto);

		if (Boolean.TRUE.equals(br.hasErrors())) {
			log.error("Input error");
			resul = new ModelAndView(CREATE_OR_UPDATE_PLAYER_VIEW, br.getModel());
			model.put("jugador", jugador);
			resul.addObject("message", getErrorMessage(br));
		} else {
			List<Jugador> lista = playerService.findAllJugadores();
			if (Boolean.TRUE.equals(isRegisteredEmail(jugador, model, lista)) || Boolean.FALSE.equals(isValidEmail(model, jugador))
					|| Boolean.FALSE.equals(isCorrectPassword(jugador, model, correctPassword)) || firstNameOrLastNameAreEmpty(jugador, model)
					|| Boolean.TRUE.equals(usernameRegistered(jugador,model))) {
				resul = new ModelAndView(CREATE_OR_UPDATE_PLAYER_VIEW, br.getModel());
				model.put("jugador", jugador);
			} else {
				jugador.setEstadoOnline(false);
				this.playerService.saveJugador(jugador);
				log.info("Player created");
				resul = new ModelAndView("redirect:/login");
			}
		}
		return resul;
	}
	
	private Boolean usernameRegistered(Jugador jugador,Map<String, Object> model) {
		Boolean res=false;
		for (Jugador j:playerService.findAllJugadores()) {
			if(j.getUser().getUsername().equals(jugador.getUser().getUsername())) {
				res=true;
				model.put("usernameRegistered", true);
			}
		}
		return res;
	}

	private List<String> getErrorMessage(BindingResult br) {
		return br.getAllErrors()
			    .stream()
			    .map(error -> {
			      var defaultMessage = error.getDefaultMessage();
			      if (error instanceof FieldError) {
			        var fieldError = (FieldError) error;
			        return String.format("%s %s", fieldError.getField(), defaultMessage);
			      } else {
			        return defaultMessage;
			      }
			    })
			    .collect(Collectors.toList());
	}

	private Boolean isRegisteredEmail(Jugador jugador, Map<String, Object> model, List<Jugador> lista) {
		Boolean result = false;
		Integer i = 0;
		while (!result && i < lista.size()) {
			if (lista.get(i).getUser().getEmail().equals(jugador.getUser().getEmail())) {
				model.put("emailIncorrecto1", true);
				result = true;
			}
			i++;
		}
		return result;
	}

	private Boolean isValidEmail(Map<String, Object> model, Jugador player) {
		Boolean result = true;
		Pattern pattern = Pattern.compile(EMAIL_PATTERN);
		Matcher matcher = pattern.matcher(player.getUser().getEmail());

		if (!matcher.matches()) {
			model.put("emailIncorrecto2", true);
			result = false;
		}

		return result;
	}

	private Boolean isCorrectPassword(Jugador player, Map<String, Object> model, Boolean correctPassword) {
		Integer i = 0;

		if (player.getUser().getPassword().length() >= 10 && player.getUser().getPassword().length() <= 50) {
			while (Boolean.FALSE.equals(correctPassword) && i < 10) {
				if (player.getUser().getPassword().contains(i.toString())) {
					correctPassword = true;
				}
				i++;
			}
		}
		if (Boolean.FALSE.equals(correctPassword)) {
			model.put("contraseñaIncorrecta", true);
		}

		return correctPassword;
	}
	
	private Boolean firstNameOrLastNameAreEmpty(Jugador jugador, Map<String, Object> model) {
		Boolean result = jugador.getFirstName().trim().equals("") || jugador.getLastName().trim().equals("");
		model.put("firstNameOrLastNameAreEmpty", result);
		return result;
	}
}
