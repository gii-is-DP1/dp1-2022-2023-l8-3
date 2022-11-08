package org.springframework.samples.petclinic.stadistics;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ArchivementService {

    private ArchivementRepository archRepo;
    
    @Autowired
    public ArchivementService(ArchivementRepository archrepo) {
        this.archRepo = archrepo;
    }
    
    
    public Collection<Archivement> findAllArchivement() throws DataAccessException{
        return archRepo.findAll();
    }
    
    public void delete(Archivement arch) throws DataAccessException{
        archRepo.delete(arch);
    }
    
    public void edit(Archivement arch) throws DataAccessException{
        archRepo.save(arch);
    }
    
    public Archivement getElementById(Integer Id) {
        return archRepo.findById(Id).get();
    }
    
}
