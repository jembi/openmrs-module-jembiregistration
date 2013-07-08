package org.openmrs.module.jembiregistration.extension.html;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.module.web.extension.LinkExt;

public class PrintBarCodeExt extends LinkExt{

    String urlg = "";
    String label = "";
    protected final Log log = LogFactory.getLog(this.getClass());
    
	@Override
	public String getLabel() {
		log.info("in the override method");
		this.label="Print Bar Code";
		return this.label;
	}


	@Override
	public String getRequiredPrivilege() {
		return "";
	}

	@Override
	public String getUrl() {
		log.info("in the override method");
		Integer patientId = Integer.valueOf(getParameterMap().get("patientId"));
		this.urlg = "moduleServlet/jembiregistration/printBarCodeServlet?patientId=" + patientId ;
		return this.urlg;
	}


}