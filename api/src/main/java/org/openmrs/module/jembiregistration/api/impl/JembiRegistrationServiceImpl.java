/**
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */
package org.openmrs.module.jembiregistration.api.impl;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.openmrs.Location;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifier;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.module.jembiregistration.api.JembiRegistrationService;
import org.openmrs.module.jembiregistration.api.db.JembiRegistrationDAO;

/**
 * It is a default implementation of {@link JembiRegistrationService}.
 */
public class JembiRegistrationServiceImpl extends BaseOpenmrsService implements JembiRegistrationService {
	
	protected final Log log = LogFactory.getLog(this.getClass());
	
	private JembiRegistrationDAO dao;
	
	/**
     * @param dao the dao to set
     */
    public void setDao(JembiRegistrationDAO dao) {
	    this.dao = dao;
    }
    
    /**
     * @return the dao
     */
    public JembiRegistrationDAO getDao() {
	    return dao;
    }
    
    public boolean printPatientBarCode(Patient patient){
    	
    	DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Context.getLocale());
    	
		try {
			// handle null case
			if (patient == null) {
				throw new APIException("No patient passed to printPatientBarCode method");
			}
			
			// TODO: potentially pull this formatting code into a configurable template?
			// build the command to send to the printer -- written in ZPL
			StringBuilder data = new StringBuilder();
			data.append("^XA"); 
			data.append("^CI28");   // specify Unicode encoding	
			
			/* Name (Only print first and last name */			
			data.append("^FO140,40^AVN^FD" + (patient.getPersonName().getGivenName() != null ? patient.getPersonName().getGivenName() : "") + " " 
					+ (patient.getPersonName().getFamilyName() != null ? patient.getPersonName().getFamilyName() : "") + "^FS");
			
			/* Birthdate */
			data.append("^FO140,350^ATN^FD" + df.format(patient.getBirthdate()) + " " + (patient.getBirthdateEstimated() ? "(*)" : " ") + "^FS"); 
			data.append("^FO140,400^ATN^FD" + Context.getMessageSourceService().getMessage("patientregistration.gender." + patient.getGender()) + "^FS"); 	
			
			/* Print the bar code, based on the primary identifier */
			PatientIdentifier primaryIdentifier = patient.getPatientIdentifier();
			if (primaryIdentifier != null) {
				data.append("^FO790,250^ATN^BY4^BCN,150^FD" + primaryIdentifier.getIdentifier() + "^FS");    // print barcode & identifier
			}
			
			/* Quanity and print command */
			data.append("^PQ" + 1);
			data.append("^XZ");
    	
		} catch (Exception e){
			
		}
		
		return true;
    }
}