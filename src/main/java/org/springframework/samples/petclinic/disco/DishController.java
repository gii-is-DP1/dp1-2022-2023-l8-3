package org.springframework.samples.petclinic.disco;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class DishController {

	private DishService dishService;

	@Autowired
	public DishController(DishService dishService) {
		this.dishService = dishService;
	}
}
