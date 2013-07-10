package org.openmrs.module.jembiregistration.web.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.jembiregistration.api.JembiRegistrationService;

public class PrintBarCodeServlet extends HttpServlet{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8755613099797407344L;

	protected Log log = LogFactory.getLog(getClass());
	
	JembiRegistrationService rs = Context.getService(JembiRegistrationService.class);
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response){
		
		Integer patientId = Integer.parseInt( request.getParameter("patientId") );
		Patient patient = Context.getPatientService().getPatient(patientId);
		try{
			log.info("Trying to print Barcode");
			boolean b = rs.printPatientBarCode(patient);
		} catch (Exception e){
			log.error(e);
		}
			/*try{
				
				response.sendRedirect(response.encodeRedirectURL("patientDashboard.jsp"));
			
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
	}
	

	
}
