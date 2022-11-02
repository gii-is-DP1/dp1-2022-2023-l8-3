package org.springframework.samples.petclinic.stadistics;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.jugador.Jugador;


public interface ArchivementRepository extends CrudRepository<Archivement, Integer>{
    @Query("SELECT archivement FROM Archivement archivement")
    public List<Archivement> findAll() throws DataAccessException;
    
}
