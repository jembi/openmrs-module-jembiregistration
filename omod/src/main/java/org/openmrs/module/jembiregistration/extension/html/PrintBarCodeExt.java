package org.openmrs.module.jembiregistration.extension.html;

import org.openmrs.module.web.extension.LinkExt;

public class PrintBarCodeExt extends LinkExt{

    String urlg = "";
    String label = "";
 
	@Override
	public String getLabel() {
		this.label="Print Bar Code";
		return this.label;
	}


	@Override
	public String getRequiredPrivilege() {
		return "";
	}

	@Override
	public String getUrl() {
		Integer patientId = Integer.valueOf(getParameterMap().get("patientId"));
		this.urlg = "moduleServlet/jembiregistration/printBarCodeServlet?patientId=" + patientId ;
		return this.urlg;
	}


}
