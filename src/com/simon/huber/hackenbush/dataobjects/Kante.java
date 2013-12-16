package com.simon.huber.hackenbush.dataobjects;

import java.io.Serializable;

/**
 * Stellt eine Kante dar
 * @author Simon Huber
 *
 */
public class Kante implements Serializable {
    
    private VerbindungsPunkt verbindungspunkt = null;
    
    private static final long serialVersionUID = 1L;
    /**
     * Spieler 1 oder 2, 0 entspricht Grundlinie Bestimmt zugleich die Farbe der
     * Linie
     */
    private int spieler;
    /**
     * Wenn der Spieler die Linie entfernt hat: false
     */
    private boolean entfernt = false;
    
    /**
     * Der Wert der Kante, muss erst berechnet werden
     */
    private Bruch wert = null;

    /**
     * Erzeugt eine neue Linie, die z.B. in der Adjazenzmatrix gespeichert wird
     * 
     * @param spieler
     *            Spieler 1 oder 2, bestimmt zugleich die Farbe der Linie
     * @param punkt Zwischenpunkt
     */
    public Kante(int spieler, VerbindungsPunkt punkt) {
	this.spieler = spieler;
	verbindungspunkt = punkt;
    }
    
    /**
     * Erzeugt eine neue Linie, die z.B. in der Adjazenzmatrix gespeichert wird
     * 
     * @param spieler
     *            Spieler 1 oder 2, bestimmt zugleich die Farbe der Linie
     */
    public Kante(int spieler) {
	this.spieler = spieler;
    }
    
    public Kante(Kante kante) {
	this.entfernt = kante.isEntfernt();
	this.spieler = kante.getSpieler();
	verbindungspunkt = kante.getVerbindungspunkt();
	wert = kante.getWert();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + (entfernt ? 1231 : 1237);
	result = prime * result + spieler;
	result = prime * result + ((verbindungspunkt == null) ? 0 : verbindungspunkt.hashCode());
	return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
	if (this == obj) {
	    return true;
	}
	if (obj == null) {
	    return false;
	}
	if (!(obj instanceof Kante)) {
	    return false;
	}
	Kante other = (Kante) obj;
	if (entfernt != other.entfernt) {
	    return false;
	}
	if (spieler != other.spieler) {
	    return false;
	}
	if (verbindungspunkt == null) {
	    if (other.verbindungspunkt != null) {
		return false;
	    }
	} else if (!verbindungspunkt.equals(other.verbindungspunkt)) {
	    return false;
	}
	return true;
    }

    /**
     * Gibt den Spieler zurueck Spieler 1 oder 2
     * 
     * @return Spieler
     */
    public int getSpieler() {
	return spieler;
    }

    /**
     * Gibt zurueck, ob die Linie bereits entfernt wurde.
     * 
     * @return entfernt oder nicht entfernt
     */
    public boolean isEntfernt() {
	return entfernt;
    }

    /**
     * Setzt den Status der Linie Entfernt oder nicht Entfernt
     * 
     * @param entfernt
     */
    public void setEntfernt(boolean entfernt) {
	this.entfernt = entfernt;
    }
    
    public VerbindungsPunkt getVerbindungspunkt() {
	return verbindungspunkt;
    }

    public Bruch getWert() {
	return wert;
    }

    public void setWert(Bruch wert) {
	this.wert = wert;
    }
}
