package org.springframework.samples.petclinic.comentario;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ComentarioService {
	private ComentarioRepository cr;

	@Autowired
	public ComentarioService(ComentarioRepository cr){
		this.cr = cr;
	}
	
	public void saveComentario(Comentario c) {
		cr.save(c);
	}
	
	public List<Comentario> getByPlayer(Integer playerId) {
		return cr.findByPlayer(playerId);
	}
}
