package org.springframework.samples.petclinic.invitacion;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.samples.petclinic.model.NamedEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "invitaciones")
public class Invitacion extends NamedEntity{

}
