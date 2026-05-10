package de.hsrm.mi.web.projekt.benutzer.ui;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class BenutzerFormular {


    @NotBlank @Size(min = 3,max = 60)
    private String name = "";

    @Email
    private String email = "";
    private String adresse = "";
    private String rolle = "";
    private String passwort = "";
    private String passwortWiederholung = "";
    private boolean aktiviert = false;
    public String getName() {
      return name;
    }
    public void setName(String name) {
      this.name = name;
    }
    public String getEmail() {
      return email;
    }
    public void setEmail(String email) {
      this.email = email;
    }
    public String getAdresse() {
      return adresse;
    }
    public void setAdresse(String adresse) {
      this.adresse = adresse;
    }
    public String getRolle() {
      return rolle;
    }
    public void setRolle(String rolle) {
      this.rolle = rolle;
    }
    public String getPasswort() {
      return passwort;
    }
    public void setPasswort(String passwort) {
      this.passwort = passwort;
    }
    public String getPasswortWiederholung() {
      return passwortWiederholung;
    }
    public void setPasswortWiederholung(String passwortWiederholung) {
      this.passwortWiederholung = passwortWiederholung;
    }
    public boolean isAktiviert() {
      return aktiviert;
    }
    public void setAktiviert(boolean aktiviert) {
      this.aktiviert = aktiviert;
    }
    @Override
    public String toString() {
      return "BenutzerFormular [name=" + name + ", email=" + email + ", adresse=" + adresse + ", rolle=" + rolle
          + ", passwort=" + "[PROTECTED]" + ", passwortWiederholung=" + "[PROTECTED]" + ", aktiviert=" + aktiviert
          + "]";
    }


}
