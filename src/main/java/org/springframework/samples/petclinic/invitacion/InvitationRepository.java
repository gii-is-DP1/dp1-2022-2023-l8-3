package org.springframework.samples.petclinic.invitacion;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface InvitationRepository extends CrudRepository<Invitacion, Integer>{
	
	@Query("SELECT invitacion FROM Invitacion invitacion WHERE invitacion.jugador.id =:id")
    public List<Invitacion> findInvitacionByInvitadoId(@Param("id") Integer id);

}
