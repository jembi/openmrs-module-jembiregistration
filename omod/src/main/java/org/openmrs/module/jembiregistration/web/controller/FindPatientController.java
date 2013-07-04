package org.openmrs.module.jembiregistration.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class FindPatientController {
 
 @RequestMapping(method=RequestMethod.GET)
 public String showThePage(){
	 return "findPatient";
 }
 
}