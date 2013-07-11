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

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.PrintServiceAttribute;
import javax.print.attribute.standard.PrinterName;

import org.openmrs.Location;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifier;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.module.jembiregistration.api.JembiRegistrationService;
import org.openmrs.module.jembiregistration.api.db.JembiRegistrationDAO;

/**
 * It is a default implementation of {@link JembiRegistrationService}.
 */
public class JembiRegistrationServiceImpl extends BaseOpenmrsService implements JembiRegistrationService {
	
	protected final Log log = LogFactory.getLog(this.getClass());
	
	private final String LABEL_TEMPLATE = "^XA~TA000~JSN^LT0^MNW^MTD^PON^PMN^LH0,0^JMA^PR2,2~SD30^JUS^LRN^CI0^XZ"+
			  "^XA" +
			  "^MMT" +
			  "^PW799" +
			  "^LL0240" +
			  "^LS24" +
			  "^FT563,85^A0N,28,28^FH\\^FD*PN*^FS" +
			  "^FT563,119^A0N,28,28^FH\\^FD*BDL*: *BD*^FS" +
			  "^FT563,153^A0N,28,28^FH\\^FD*NIDL*: *NID*^FS" +
			  "^FT563,187^A0N,28,28^FH\\^FD*GL*: *G*^FS" +
			  "^BY2,3,160^FT65,182^BCN,,Y,N" +
			  "^FD>:*NID*^FS" +
			  "^PQ1,0,1,Y^XZ";
	
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
    	
    	String label = LABEL_TEMPLATE;
    					  		
    	DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Context.getLocale());
    	//log.fatal("printing Bar Code");
    	
		try {
			// handle null case
			if (patient == null) {
				throw new APIException("No patient passed to printPatientBarCode method");
			}
			
			String printerName = Context.getAdministrationService().getGlobalProperty("jembiregistration.PrinterName");
			Integer copies, idType;
			try {
				copies = Integer.parseInt(Context.getAdministrationService().getGlobalProperty("jembiregistration.LabelPrintCount"));
				idType = Integer.parseInt(Context.getAdministrationService().getGlobalProperty("jembiregistration.LabelIdType"));
			} catch (NumberFormatException n){
				
				log.error("Global property is not valid" + n.getMessage());
				
				copies = 1;
				idType = 0;
				
			}
			
			/* Name (Only print first and last name */
			String name = (patient.getPersonName().getGivenName() != null ? patient.getPersonName().getGivenName() : "")  + " " 
					+ (patient.getPersonName().getFamilyName() != null ? patient.getPersonName().getFamilyName() : "") ;
			/* Name (Only print first and last name */			
			
			/* Birthdate */
			String birthDateLabel = Context.getMessageSourceService().getMessage("jembiregistration.birthDateShort");
			String birthDate = (df.format(patient.getBirthdate()) + " " + (patient.getBirthdateEstimated() ? "(*)" : " ")); 
			
			/*Gender*/
			String genderLabel = Context.getMessageSourceService().getMessage("Patient.gender");
			String gender = (patient.getGender().equalsIgnoreCase("M") ? Context.getMessageSourceService().getMessage("Patient.gender.male") : Context.getMessageSourceService().getMessage("Patient.gender.female")); 	
			
			/* Primary identifier */
			
			PatientIdentifier primaryIdentifier = (idType==0) ? patient.getPatientIdentifier() : patient.getPatientIdentifier(Context.getPatientService().getPatientIdentifierType(idType));
				
			if (primaryIdentifier != null) {
				String nidLabel = primaryIdentifier.getIdentifierType().getName();
				String nid = primaryIdentifier.getIdentifier();
				
				label = label.replace("*NIDL*", nidLabel);
				label = label.replace("*NID*", nid );
			}	
			
			label = label.replace("*PN*", name);
			label = label.replace("*BDL*", birthDateLabel);
			label = label.replace("*BD*", birthDate);
			label = label.replace("*GL*", genderLabel);
			label = label.replace("*G*", gender);

		
			  try {
		           
		           PrintService psZebra = null;
		           String sPrinterName = null;
		           PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
		           
		           
		           
		           for (int i = 0; i < services.length; i++) {
		               
		               PrintServiceAttribute attr = services[i].getAttribute(PrinterName.class);
		               sPrinterName = ((PrinterName) attr).getValue();
		               log.fatal(sPrinterName);
		               if (sPrinterName.toLowerCase().indexOf(printerName.toLowerCase()) >= 0) {
		            	   
		                   psZebra = services[i];
		                   break;
		               }
		           }
		           
		           if (psZebra == null) {
		               log.error("Zebra printer is not found. Barcode could not be printed. Check your global properties to make sure printer names match");
		               
		           }
		           
		           for (int i=1; i<=copies; i++){
		        	   
		        	   DocPrintJob job = psZebra.createPrintJob();;

		        	   byte[] by = label.getBytes();
		        	   DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
		        	   Doc doc = new SimpleDoc(by, flavor, null);
		        	   job.print(doc, null);
		           
		           }
		           
		       } catch (PrintException e) {
		           e.printStackTrace();
		       } 
		   //}
		} catch (Exception e){
			e.printStackTrace();
		}
		
		return true;
    }
}