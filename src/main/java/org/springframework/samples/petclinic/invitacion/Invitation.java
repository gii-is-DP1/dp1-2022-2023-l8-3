package org.springframework.samples.petclinic.invitacion;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.samples.petclinic.jugador.Jugador;
import org.springframework.samples.petclinic.model.BaseEntity;
import org.springframework.samples.petclinic.model.NamedEntity;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.partida.Match;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "invitaciones")
public class Invitacion extends BaseEntity{
    
    @Column(name="fechahora")
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDate fechaHora;
    
    @Column(name="resultado")
    private resultadoInvitacion resultado;
    
    @Column(name="tipo_invitacion")
    private tipoInvitacion tipo;
    
    @ManyToOne
    @JoinColumn(name = "partida_id")
    private Match match;
    
    @ManyToOne
    @JoinColumn(name = "jugador_id")
    private Jugador jugador;

}