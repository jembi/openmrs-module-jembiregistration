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
package org.openmrs.module.jembiregistration.extension.html;

import org.openmrs.api.context.Context;
import org.openmrs.module.Extension;

public class BarcodeExt extends Extension {

	@Override
	public MEDIA_TYPE getMediaType() {
		return Extension.MEDIA_TYPE.html;
	}

	public String getOverrideContent(String test) {

		Integer patientId = Integer.valueOf(getParameterMap().get("patientId"));

		StringBuffer headerHTML = new StringBuffer();

		String servletResource = "moduleServlet/jembiregistration/printBarcodeServlet?patientId="
				+ patientId.toString();

		headerHTML.append("<script type=\"text/javascript\">");
		headerHTML.append("function printBarcode() {");
		headerHTML.append("	jQuery.get('" + servletResource
				+ "', function(data) {");
		headerHTML.append("		});	");
		headerHTML.append("}");
		headerHTML.append("</script>");

		headerHTML.append("<table><tr><td>");
		headerHTML.append("<button onClick=\"printBarcode();\">");
		headerHTML.append(Context.getMessageSourceService().getMessage(
				"jembiregistration.printButtonLabel"));
		headerHTML.append("</button>");
		headerHTML.append("</td></tr></table>");

		return headerHTML.toString();
	}

}
