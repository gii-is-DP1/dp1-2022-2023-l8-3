package org.springframework.samples.petclinic.disco;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DishService {

	private DishRepository dishRepo;

	@Autowired
	public DishService(DishRepository dishRepository) {
		this.dishRepo = dishRepository;
	}

	@Transactional
	public int dishCount() {
		return (int) dishRepo.count();
	}
}
