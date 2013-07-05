package org.openmrs.module.jembiregistration.extension.html;

import org.openmrs.module.web.extension.LinkExt;

public class PrintBarCodeExt extends LinkExt{

	@Override
	public String getLabel(){
		return "jembiregistration.printBarCode";
	}
	
	@Override
	public String getRequiredPrivilege() {
		// TODO Auto-generated method stub
		return "View Patients";
	}

	@Override
	public String getUrl() {
		// TODO Auto-generated method stub
		return null;
	}

}
