package de.hsrm.mi.web.projekt.anzeige.ui;


public class AnzeigeException extends RuntimeException{
  public AnzeigeException(String message){
    super(message);
  }

  public AnzeigeException(String message, Throwable cause){
    super(message, cause);
  }

}
