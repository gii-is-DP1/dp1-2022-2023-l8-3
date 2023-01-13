/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.disco;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.springframework.samples.petclinic.model.BaseEntity;
import org.springframework.samples.petclinic.partida.Match;

import lombok.Getter;
import lombok.Setter;

/**
 * Simple business object representing a pet.
 *
 * @author Ken Krebs
 * @author Juergen Hoeller
 * @author Sam Brannen
 */
@Entity
@Getter
@Setter
@Table(name = "disco")
public class Disco extends BaseEntity{

	@Min(0)
	@Max(5)
	@Column(name = "num_bacteria_j1", nullable=false)    
	private Integer numBact1;
	
	@Min(0)
	@Max(5)
	@Column(name="num_bacteria_j2", nullable=false)
	private Integer numBact2;

	@Min(0)
	@Max(1)
	@Column(name = "num_sarcina_j1", nullable=false)        
    private Integer numSarc1;
    
	@Min(0)
	@Max(1)
    @Column(name="num_sarcina_j2", nullable=false)
    private Integer numSarc2;
    
	@Min(0)
    @Column(name="num_movimientos_j1", nullable=false)
    private Integer numMov1;
    
	@Min(0)
    @Column(name="num_movimientos_j2", nullable=false)
    private Integer numMov2;
    
    @ManyToOne
    @JoinColumn(name="id_match")
    private Match match;

    
    public Disco(Match match) {
    	super();
		this.numBact1 = 0;
		this.numBact2 = 0;
		this.numSarc1 = 0;
		this.numSarc2 = 0;
		this.numMov1 = 0;
		this.numMov2 = 0;
		this.match = match;
	}  
	 
    
    
    public Disco() {
    	super();
    }
    
    // ----------------------------------------------------------------------------------------------- //
	
    private Integer[] getBacterias() {
    	return new Integer[] {numBact1, numBact2};
    }
    
    private Integer[] getSarcinas() {
    	return new Integer[] {numSarc1, numSarc2};
    }
    
    public Integer getNumeroDeBacterias(Integer idJugador) {
		return getBacterias()[idJugador-1];
	}
    
    public Integer getNumeroDeSarcinas(Integer idJugador) {
    	return getSarcinas()[idJugador-1];
	}
    
    public void annadirBacterias(Integer idJugador, Integer numeroDeBacterias) {
    	if(idJugador == 0) {
			numBact1 += numeroDeBacterias;
			numMov1++;
		} else {
			numBact2 += numeroDeBacterias;
			numMov2++;
		}
	}
	
	public void eliminarBacterias(Integer idJugador, Integer numeroDeBacterias) {
		if(idJugador == 0) {
			numBact1 -= numeroDeBacterias;
		} else {
			numBact2 -= numeroDeBacterias;
		}
	}
	
	public void annadirSarcina(Integer idJugador) {
		if(idJugador == 0) {
			numSarc1++;
		} else {
			numSarc2++;
		}
	}
	
	// ----------------------------------------------------------------------------------------------- //
}
