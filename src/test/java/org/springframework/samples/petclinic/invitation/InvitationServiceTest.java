package org.springframework.samples.petclinic.invitation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.invitacion.InvitationService;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class InvitationServiceTest {

	@Autowired
	private InvitationService invitationService;

	@Test
	public void testCountWithInitialData() {
		int count = invitationService.invitationCount();
		assertEquals(count, 0);
	}

}
