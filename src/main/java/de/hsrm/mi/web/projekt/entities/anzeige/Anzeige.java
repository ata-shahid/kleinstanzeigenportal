package de.hsrm.mi.web.projekt.entities.anzeige;

import de.hsrm.mi.web.projekt.entities.benutzer.Benutzer;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Version;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;

@Entity
public class Anzeige {

    @Id @GeneratedValue
    private long id;

    @Version
    private long version;

    @NotBlank @Size(min = 3, max = 40)
    private String titel;

    @NotBlank @Size(min = 17)
    private String beschreibung;

    @PositiveOrZero
    private int preis;

    @PositiveOrZero
    private int anzahl;

    @Future
    private LocalDate ablaufdatum;

    @ManyToOne
    private Benutzer anbieter;

    @ManyToMany
    private Collection<Benutzer> besteller = new HashSet<>();

    public long getId() {
      return id;
    }

    public void setId(long id) {
      this.id = id;
    }

    public long getVersion() {
      return version;
    }

    public void setVersion(long version) {
      this.version = version;
    }

    public String getTitel() {
      return titel;
    }

    public void setTitel(String titel) {
      this.titel = titel;
    }

    public String getBeschreibung() {
      return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
      this.beschreibung = beschreibung;
    }

    public int getPreis() {
      return preis;
    }

    public void setPreis(int preis) {
      this.preis = preis;
    }

    public int getAnzahl() {
      return anzahl;
    }

    public void setAnzahl(int anzahl) {
      this.anzahl = anzahl;
    }

    public LocalDate getAblaufdatum() {
      return ablaufdatum;
    }

    public void setAblaufdatum(LocalDate ablaufdatum) {
      this.ablaufdatum = ablaufdatum;
    }

    public Benutzer getAnbieter() {
      return anbieter;
    }

    public void setAnbieter(Benutzer anbieter) {
      this.anbieter = anbieter;
    }

    public Collection<Benutzer> getBesteller() {
      return besteller;
    }

    public void setBesteller(Collection<Benutzer> besteller) {
      this.besteller = besteller;
    }
}
