package org.springframework.samples.petclinic.jugador;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
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
		return friendRequestRepository.findByPlayers(idJugador1, idJugador2);
	}

}
