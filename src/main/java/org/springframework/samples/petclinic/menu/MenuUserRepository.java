package org.springframework.samples.petclinic.menu;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.user.User;

public interface MenuUserRepository extends  CrudRepository<User, String> {
	
	@Query("SELECT user FROM User user WHERE user.username =:username")
	public Optional<User> findByUsername(@Param("username") String username);

}
