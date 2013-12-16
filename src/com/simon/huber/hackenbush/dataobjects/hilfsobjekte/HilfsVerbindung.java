package com.simon.huber.hackenbush.dataobjects.hilfsobjekte;

import java.util.Arrays;

import com.simon.huber.hackenbush.dataobjects.Punkt;
import com.simon.huber.hackenbush.dataobjects.Verbindung;

public class HilfsVerbindung {
    private Punkt[] punkt = new Punkt[2];
    private Verbindung verbindung;
    
    public HilfsVerbindung(Punkt p1, Punkt p2, Verbindung ver) {
	punkt[0] = p1;
	punkt[1] = p2;
	verbindung = ver;
    }
    
    public HilfsVerbindung(HilfsVerbindung k) {
	this.verbindung = k.verbindung;
	this.punkt = k.punkt;
    }
    
    public HilfsVerbindung punkteUmdrehen() {
	Punkt[] p = punkt;
	punkt = new Punkt[2];
	punkt[1] = p[0];
	punkt[0] = p[1];
	return this;
    }
    
    public Verbindung getVerbindung() {
	return verbindung;
    }
   
    public Punkt[] getPunkt() {
	return punkt;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + Arrays.hashCode(punkt);
	result = prime * result + ((verbindung == null) ? 0 : verbindung.hashCode());
	return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (!(obj instanceof HilfsVerbindung))
	    return false;
	HilfsVerbindung other = (HilfsVerbindung) obj;
	if (!Arrays.equals(punkt, other.punkt))
	    return false;
	if (verbindung == null) {
	    if (other.verbindung != null)
		return false;
	} else if (!verbindung.equals(other.verbindung))
	    return false;
	return true;
    }

    
    
}
