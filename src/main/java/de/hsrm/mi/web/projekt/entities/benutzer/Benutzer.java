package de.hsrm.mi.web.projekt.entities.benutzer;

import de.hsrm.mi.web.projekt.benutzer.validators.GutesPasswort;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Objects;

@Entity
public class Benutzer {


  @Id
  @NotBlank
  private String loginName;

  @NotBlank @Size(min = 3, max = 60)
  private String name;

  @NotBlank
  @Email
  private String email;

  @NotBlank
  private String adresse;

  @NotBlank
  private String rolle;

  @GutesPasswort
  @NotBlank
  private String passwort;

  private boolean aktiviert;

  public Benutzer() {
  }



  public String getLoginName() {
    return loginName;
  }

  public void setLoginName(String loginName) {
    this.loginName = loginName;
  }

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

  public boolean isAktiviert() {
    return aktiviert;
  }

  public void setAktiviert(boolean aktiviert) {
    this.aktiviert = aktiviert;
  }

  @Override
  public String toString() {
    return "Benutzer [loginName=" + loginName + ", name=" + name + ", email=" + email + ", adresse=" + adresse
        + ", rolle=" + rolle + ", passwort=" + passwort + ", aktiviert=" + aktiviert + "]";
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    if (other == null || getClass() != other.getClass()) {
      return false;
    }
    Benutzer benutzer = (Benutzer) other;
    return Objects.equals(loginName, benutzer.loginName);
  }

  @Override
  public int hashCode() {
    return Objects.hash(loginName);
  }


}
