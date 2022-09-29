package org.springframework.samples.petclinic.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.samples.petclinic.model.Person;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WelcomeController {
	
	
	  @GetMapping({"/","/welcome"})
	  public String welcome(Map<String, Object> model) {	    
		  List<Person> ls = new ArrayList<>();
		  Person p1 = new Person();
		  p1.setFirstName("Sergio");
		  p1.setLastName("Aguayo Orozco");
		  Person p2 = new Person();
		  p2.setFirstName("David");
		  p2.setLastName("Dana Cabello");
		  Person p3 = new Person();
		  p3.setFirstName("Jose Miguel");
		  p3.setLastName("Iborra Conejo");
		  Person p4 = new Person();
		  p4.setFirstName("Juan Jesús");
		  p4.setLastName("Martín Hernández");
		  Person p5 = new Person();
		  p5.setFirstName("Francisco Jesús");
		  p5.setLastName("Montero Martínez");
		  Person p6 = new Person();
		  p6.setFirstName("Manuel");
		  p6.setLastName("Ortega García");
		  ls.add(p1);
		  ls.add(p2);
		  ls.add(p3);
		  ls.add(p4);
		  ls.add(p5);
		  ls.add(p6);
		  model.put("persons", ls);
		  model.put("title", "My Project");
		  model.put("group", "L8-3");
		  return "welcome";
	  }
}