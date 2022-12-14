package org.springframework.samples.petclinic.invitacion;

import java.util.List;

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
	@Transactional
	public void save(Invitacion i) {
		invitationRepo.save(i);
	}
	
	@Transactional
	public List<Invitacion> getInvitacionByInvitadoId(Integer id){
		return invitationRepo.findInvitacionByInvitadoId(id);
	}
	
	@Transactional
	public Invitacion getInvitacionById(Integer id) {
		return invitationRepo.findById(id).get();
	}
	
	@Transactional
	public void delete(Invitacion i) {
		invitationRepo.delete(i);
	}

}
