
package org.openmrs.module.jembiregistration.extension.html;
 
import org.openmrs.module.web.extension.LinkExt;
 
public class RegisterPatientGutterListExt extends LinkExt {
 
    String urlg = "";
    String label = "";
 
    public String getLabel() {
        label = "jembiregistration.registrationgutterlabel";
        return this.label;
    }
 
    public String getUrl() {
        urlg = "admin/patients/shortPatientForm.form";
        return this.urlg;
    }
 
    /**
     * Returns the required privilege in order to see this section. Can be a
     * comma delimited list of privileges. If the default empty string is
     * returned, only an authenticated user is required
     *
     * @return Privilege string
     */
    public String getRequiredPrivilege() {
        return "";
    }
 
}