package de.hsrm.mi.web.projekt.entities.benutzer;

import de.hsrm.mi.web.projekt.benutzer.validators.GutesPasswort;
import de.hsrm.mi.web.projekt.entities.anzeige.Anzeige;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Version;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Collection;
import java.util.HashSet;
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

  @Version
  private long version;


@OneToMany(mappedBy = "anbieter", cascade = CascadeType.REMOVE, orphanRemoval = true)
private Collection<Anzeige> anzeigen = new HashSet<>();


@ManyToMany(mappedBy = "besteller")
private Collection<Anzeige> bestellungen = new HashSet<>();

  public Benutzer() { // to be removed later
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
   public long getVersion() {
    return version;
  }



  public void setVersion(long version) {
    this.version = version;
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



  public Collection<Anzeige> getAnzeigen() {
    return anzeigen;
  }



  public void setAnzeigen(Collection<Anzeige> anzeigen) {
    this.anzeigen = anzeigen;
  }



  public Collection<Anzeige> getBestellungen() {
    return bestellungen;
  }



  public void setBestellungen(Collection<Anzeige> bestellungen) {
    this.bestellungen = bestellungen;
  }






}
