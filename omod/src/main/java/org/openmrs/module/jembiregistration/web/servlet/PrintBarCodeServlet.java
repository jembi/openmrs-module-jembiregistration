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
package org.openmrs.module.jembiregistration.web.servlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.jembiregistration.api.JembiRegistrationService;

public class PrintBarcodeServlet extends HttpServlet {

	private static final long serialVersionUID = 8755613099797407344L;

	protected Log log = LogFactory.getLog(getClass());

	JembiRegistrationService rs = Context
			.getService(JembiRegistrationService.class);

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) {

		Integer patientId = Integer.parseInt(request.getParameter("patientId"));
		Patient patient = Context.getPatientService().getPatient(patientId);
		try {
			rs.printPatientBarcode(patient);
		} catch (Exception e) {
			log.error(e);
		}
	}
}
