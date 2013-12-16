package com.simon.huber.hackenbush.dataobjects;

import java.awt.Point;
import java.io.Serializable;


/**
 * Punkt, der Point erweitert und einen Copy-Konstruktor hat
 * @author Bibliothekar
 *
 */
public class Punkt extends Point implements Serializable {

    private static final long serialVersionUID = 1L;

    //Copy Konstruktor für eine Tiefenkopie

    public Punkt(Punkt p) {
	super(p.x, p.y);
    }
    
    public Punkt(int x, int y) {
	super(x, y);
    }

    public boolean istAmBoden() {
	return y == 0;
    }
}
