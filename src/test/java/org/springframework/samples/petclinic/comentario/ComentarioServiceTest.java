package org.springframework.samples.petclinic.comentario;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
class ComentarioServiceTest {

	@Autowired
	private ComentarioService comentarioService;

	@Test
	void test() {
	}

}
