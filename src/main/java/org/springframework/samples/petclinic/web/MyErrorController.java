package org.springframework.samples.petclinic.web;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MyErrorController implements ErrorController{
	
	@GetMapping("/error")
	public String handleError(HttpServletRequest request) {
		// get error status
		Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
		if (status != null) {
			int statusCode = Integer.parseInt(status.toString());
			// display specific error page
			if (statusCode == HttpStatus.NOT_FOUND.value()) {
				return "error/error-403";
			} else if (statusCode == HttpStatus.INTERNAL_SERVER_ERROR.value()) {
				return "error/error-500";
			} else if (statusCode == HttpStatus.FORBIDDEN.value()) {
				return "error/error-403";
			}
		}
		// display generic error
		return "error/error";
	}
	@Override
	  public String getErrorPath() {
	    return "/error";
	  }
}
