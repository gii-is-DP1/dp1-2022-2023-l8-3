package org.springframework.samples.petclinic.stadistics;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.samples.petclinic.model.NamedEntity;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="archivement")
public class Archivement extends NamedEntity{

    @Column(name="threshold")
    private Double threshold;
    
    @Column(name="description")
    private String description;
    
    @Column(name="badge_image")
    private String badgeImage;
    
    
}
