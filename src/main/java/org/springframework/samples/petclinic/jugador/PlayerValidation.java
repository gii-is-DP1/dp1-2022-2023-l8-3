package org.springframework.samples.petclinic.jugador;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlayerValidation {
	
	private static final String EMAIL_PATTERN = "^.+@.+\\..+$";
	
	public static Boolean isRegisteredEmail(Jugador jugador, Map<String, Object> model, List<Jugador> lista) {
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

	public static Boolean isValidEmail(Map<String, Object> model, Jugador player) {
		Boolean result = true;
		Pattern pattern = Pattern.compile(EMAIL_PATTERN);
		Matcher matcher = pattern.matcher(player.getUser().getEmail());

		if (!matcher.matches()) {
			model.put("emailIncorrecto2", true);
			result = false;
		}

		return result;
	}

	public static Boolean isCorrectPassword(Jugador player, Map<String, Object> model, Boolean correctPassword) {
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
	
	public static Boolean firstNameOrLastNameAreEmpty(Jugador jugador, Map<String, Object> model) {
		Boolean result = jugador.getFirstName().trim().equals("") || jugador.getLastName().trim().equals("");
		model.put("firstNameOrLastNameAreEmpty", result);
		return result;
	}
	
}
