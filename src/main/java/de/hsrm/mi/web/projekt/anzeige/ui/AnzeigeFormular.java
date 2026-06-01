package de.hsrm.mi.web.projekt.anzeige.ui;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public class AnzeigeFormular {


  private long id;

  @NotBlank @Size(min = 3, max = 40)
  private String titel;

  @NotBlank @Size(min = 17)
  private String beschreibung;

  @PositiveOrZero
  private int preis;

  @PositiveOrZero
  private int anzahl;

  @NotNull @Future
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
  private LocalDate ablaufdatum;




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

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }



}
