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
    	String template = "^XA~TA000~JSN^LT0^MNW^MTD^PON^PMN^LH0,0^JMA^PR2,2~SD30^JUS^LRN^CI0^XZ"+
    					  "^XA" +
    					  "^MMT" +
    					  "^PW799" +
    					  "^LL0240" +
    					  "^LS24" +
    					  "^FT583,85^A0N,28,28^FH\\^FD*PN*^FS" +
    					  "^FT583,119^A0N,28,28^FH\\^FD*BDL*: *BD*^FS" +
    					  "^FT583,153^A0N,28,28^FH\\^FDNID: *NID*^FS" +
    					  "^FT583,187^A0N,28,28^FH\\^FD*GL*: *G*^FS" +
    					  "^BY4,3,160^FT65,182^BCN,,Y,N" +
    					  "^FD>;12345678>69^FS" +
    					  "^PQ1,0,1,Y^XZ";
    					  		
    	DateFormat df = new SimpleDateFormat("yyyy-MM-dd", Context.getLocale());
    	//log.fatal("printing Bar Code");
    	
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
			
			String[] cmd = new String[3];
			
			/*cmd[0] = "cmd.exe" ;
            cmd[1] = "/c" ;
            cmd[2] = "C:\\Users\\moasis\\printbarcode.bat";
			
			try{
				log.fatal(data.toString());
				File srcFile = new File("C:\\Users\\moasis\\barcode.zpl");
				//File destDir = new File("\\\\MOASIS-PILOT\\ZebraPrinter\\");

				FileUtils.writeStringToFile(srcFile,data.toString());
				//FileUtils.copyFileToDirectory(srcFile, destDir);
				Process proc = Runtime.getRuntime().exec("cmd /c C:\\Users\\moasis\\printbarcode.bat");

				// any error message?
				log.fatal("Barcode process exited with value: " + proc.exitValue() + " | " + proc.waitFor());
				
			} catch (IOException io){
				io.printStackTrace();
			}*/
			
			  try {
		           
		           PrintService psZebra = null;
		           String sPrinterName = null;
		           PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
		           
		           log.fatal("Printers found:");
		           for (int i = 0; i < services.length; i++) {
		               
		               PrintServiceAttribute attr = services[i].getAttribute(PrinterName.class);
		               sPrinterName = ((PrinterName) attr).getValue();
		               log.fatal(sPrinterName);
		               if (sPrinterName.toLowerCase().indexOf("textbarcodeprinter") >= 0) {
		            	   
		                   psZebra = services[i];
		                   break;
		               }
		           }
		           
		           if (psZebra == null) {
		               log.fatal("Zebra printer is not found.");
		               
		           }
		           DocPrintJob job = psZebra.createPrintJob();

		           String s = "^XA^FO100,40^BY3^B3,,30^FD123ABC^XZ";

		           byte[] by = s.getBytes();
		           DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
		           Doc doc = new SimpleDoc(by, flavor, null);
		           job.print(doc, null);
		           
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