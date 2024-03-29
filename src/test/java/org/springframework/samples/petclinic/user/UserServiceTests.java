package org.springframework.samples.petclinic.user;

import static org.junit.Assert.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
class UserServiceTests {

	@Autowired
	private UserService userService;
	
	@Test
	void testUser() {
		String username = "juamarher";
		assertNotNull(userService.findUser(username));
	}
	
}
