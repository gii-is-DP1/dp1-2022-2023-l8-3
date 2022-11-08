package org.springframework.samples.petclinic.stadistics;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.statistics.Achievement;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/stadistics/archivement")
public class ArchivementController {

    private static final String ARCHIVEMENTS_LISTING_VIEW = "/archivement/archivementListado";
    
    private ArchivementService archServ;
    
    @Autowired
    public ArchivementController(ArchivementService archserv) {
        this.archServ = archserv;
    }
    
    @GetMapping(value = "/")
    public ModelAndView showArchivement() {
        ModelAndView result = new ModelAndView(ARCHIVEMENTS_LISTING_VIEW);
        result.addObject("archivement", archServ.findAllArchivement());
        return result;
    }
    
    @GetMapping("/{id}/delete")
    public ModelAndView deleteAchievement(@PathVariable int id) {
        Archivement archivement = archServ.getElementById(id);
        archServ.delete(archivement);
        ModelAndView result = showArchivement();
        result.addObject("message", "The achievement was deleted succesfully");
        return result;
    }
    
    @GetMapping("/{id}/edit")
    public ModelAndView editArchivement(@PathVariable int id) {
        Archivement archivement = archServ.getElementById(id);
        
                
    }
}
