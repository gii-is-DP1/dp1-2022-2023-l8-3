package org.springframework.samples.petclinic.web;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@RequestMapping("/error")
@Controller
public class MyErrorController{
	
	public String handleError(HttpServletRequest request) {
		// get error status
		Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
		if (status != null) {
			int statusCode = Integer.parseInt(status.toString());
			// display specific error page
			if (statusCode == HttpStatus.NOT_FOUND.value()) {
				return "error-404";
			} else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
				return "error-500";
			} else if (statusCode == HttpStatus.FORBIDDEN.value()) {
				return "error-403";
			}
		}
		// display generic error
		return "error";
	}

    @GetMapping("/error-403")
    public ModelAndView error403(){
        return new ModelAndView("/error/error-403");
    }

	@GetMapping("/error-404")
    public ModelAndView error404(){
        return new ModelAndView("/error/error-403");
    }

    @GetMapping("/error-500")
    public ModelAndView error500(){
        return new ModelAndView("/error/error-500");
    }

}
