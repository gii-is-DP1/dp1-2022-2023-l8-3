package org.springframework.samples.petclinic.dish;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.disco.DishService;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class DishServiceTest {

	@Autowired
	private DishService dishService;

	@Test
	public void testCountWithInitialData() {
		int count = dishService.dishCount();
		assertEquals(14, count);
	}

}
