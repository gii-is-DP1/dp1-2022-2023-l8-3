package org.springframework.samples.petclinic.jugador;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;

@Service
public class FriendRequestService {

	private FriendRequestRepository friendRequestRepository;

	@Autowired
	public FriendRequestService(FriendRequestRepository friendRequestRepository) {
		this.friendRequestRepository = friendRequestRepository;
	}

	public Collection<FriendRequest> getFriendRequests() throws DataAccessException {
		return friendRequestRepository.findAll();
	}

	public void saveFriendRequest(FriendRequest friendRequest) {
		friendRequestRepository.save(friendRequest);
	}

	public void deleteFriendRequest(FriendRequest friendRequest) {
		friendRequestRepository.delete(friendRequest);
	}

	public FriendRequest getFriendRequestById(Integer id) {
		return friendRequestRepository.findById(id).get();
	}

	public FriendRequest getFriendRequestByPlayers(Integer idJugador1, Integer idJugador2) {
		return friendRequestRepository.findFriendRequestByPlayers(idJugador1, idJugador2);
	}
	
	public FriendRequest getNoReplyFriendRequestByPlayers(Integer idJugador1, Integer idJugador2) {
		return friendRequestRepository.findNoReplyFriendRequestByPlayers(idJugador1, idJugador2);
	}
	
	public FriendRequest getFriendshipByPlayers(@Param("jugador1Id") int jugador1Id, @Param("jugador2Id") int jugador2Id) {
		return friendRequestRepository.findFriendshipByPlayers(jugador1Id, jugador2Id);
	}
	
	public List<FriendRequest> getFriendRequestByPlayer(Integer playerId) {
		return friendRequestRepository.findByPlayer(playerId);
	}

}
