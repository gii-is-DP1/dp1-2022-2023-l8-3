package dto;

import org.springframework.beans.BeanUtils;
import org.springframework.samples.petclinic.partida.Match;
import org.springframework.stereotype.Component;

@Component
public class ManualMatchMapper {

	public Match convertMatchDTOToEntity(MatchDTO match) {
		Match res = new Match();
		BeanUtils.copyProperties(match, res);
		return res;
	}
	
}
