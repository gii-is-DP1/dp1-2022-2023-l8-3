package org.springframework.samples.petclinic.invitacion;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.samples.petclinic.jugador.Jugador;
import org.springframework.samples.petclinic.model.BaseEntity;
import org.springframework.samples.petclinic.partida.Match;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "invitaciones")
public class Invitacion extends BaseEntity{
    
	@NotNull
    @DateTimeFormat(pattern = "yyyy/MM/dd")
    private LocalDate fechaHora;
    
    @NotNull
    @Enumerated(EnumType.STRING)
    private resultadoInvitacion resultado;
    
	@Enumerated(EnumType.STRING)
    @Column(name="tipo_invitacion", nullable=false)
    private tipoInvitacion tipo;
    
    @ManyToOne
    @JoinColumn(name = "partida_id")
    private Match match;
    
    @ManyToOne
    @JoinColumn(name = "jugador_id")
    private Jugador jugador;

}
