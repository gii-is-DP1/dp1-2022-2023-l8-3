package org.springframework.samples.petclinic.logro;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.samples.petclinic.model.NamedEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "logros")
public class Logro extends NamedEntity{

}
