package org.springframework.samples.petclinic.comentario;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.samples.petclinic.model.NamedEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "comentarios")
public class Comentario extends NamedEntity{

}
