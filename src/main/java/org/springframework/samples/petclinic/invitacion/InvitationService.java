package org.springframework.samples.petclinic.invitacion;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InvitationService {

	private InvitationRepository invitationRepo;

	@Autowired
	public InvitationService(InvitationRepository invitationRepository) {
		this.invitationRepo = invitationRepository;
	}

	@Transactional
	public int invitationCount() {
		return (int) invitationRepo.count();
	}

}
