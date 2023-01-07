package org.springframework.samples.petclinic.jugador;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface FriendRequestRepository extends CrudRepository<FriendRequest, Integer> {
	
	List<FriendRequest> findAll();
	
	@Query("SELECT fr FROM FriendRequest fr WHERE fr.jugador1.id =:jugador1Id AND fr.jugador2.id =:jugador2Id")
	public FriendRequest findFriendRequestByPlayers(@Param("jugador1Id") int jugador1Id, @Param("jugador2Id") int jugador2Id);
	
	@Query("SELECT fr FROM FriendRequest fr WHERE fr.jugador1.id =:jugador1Id AND fr.jugador2.id =:jugador2Id AND fr.resultado IS NULL")
	public FriendRequest findNoReplyFriendRequestByPlayers(@Param("jugador1Id") int jugador1Id, @Param("jugador2Id") int jugador2Id);
	
	@Query("SELECT fr FROM FriendRequest fr WHERE fr.jugador1.id = :playerId OR fr.jugador2.id = :playerId")
	public List<FriendRequest> findByPlayer(@Param("playerId") int playerId);
	
	
}
