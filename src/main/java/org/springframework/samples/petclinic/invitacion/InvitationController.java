package org.springframework.samples.petclinic.invitacion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class InvitationController {

	private InvitationService invitationService;

	@Autowired
	public InvitationController(InvitationService invitationService) {
		this.invitationService = invitationService;
	}

}
