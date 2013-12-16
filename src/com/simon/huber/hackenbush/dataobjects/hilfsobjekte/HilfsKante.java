package com.simon.huber.hackenbush.dataobjects.hilfsobjekte;

import java.util.Arrays;

import com.simon.huber.hackenbush.dataobjects.Kante;
import com.simon.huber.hackenbush.dataobjects.Punkt;

/**
 * Verknüpft zwei Punkte mit einer Kante
 * @author Simon Huber
 *
 */
public class HilfsKante {
    Punkt[] punkt = new Punkt[2];
    Kante kante;
    
    public HilfsKante(Punkt p1, Punkt p2, Kante k) {
	punkt[0] = p1;
	punkt[1] = p2;
	kante = k;
    }
    
    public HilfsKante(HilfsKante k) {
	this.kante = k.kante;
	this.punkt = k.punkt;
    }
    
    public HilfsKante punkteUmdrehen() {
	Punkt[] p = punkt;
	punkt = new Punkt[2];
	punkt[1] = p[0];
	punkt[0] = p[1];
	return this;
    }
    
    public Kante getKante() {
	return kante;
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
	result = prime * result + ((kante == null) ? 0 : kante.hashCode());
	result = prime * result + Arrays.hashCode(punkt);
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
	if (!(obj instanceof HilfsKante))
	    return false;
	HilfsKante other = (HilfsKante) obj;
	if (kante == null) {
	    if (other.kante != null)
		return false;
	} else if (!kante.equals(other.kante))
	    return false;
	if (!Arrays.equals(punkt, other.punkt))
	    return false;
	return true;
    }
    
    
}
