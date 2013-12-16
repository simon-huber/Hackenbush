package com.simon.huber.hackenbush.exceptions;

import com.simon.huber.hackenbush.dataobjects.Kante;
import com.simon.huber.hackenbush.dataobjects.Punkt;
import com.simon.huber.hackenbush.tools.HackenbushConfig;

/**
 * Wird geworfen, falls versucht wird eine Kante zu entfernen, die nicht mehr existiert.
 * @author Simon Huber
 *
 */
public class KanteBereitsEntferntException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public KanteBereitsEntferntException(Kante k, Punkt p1, Punkt p2) {
		super(HackenbushConfig.getString("KanteBereitsEntferntException.message1") 
			+ " Spieler: " + k.getSpieler() 
			+ " von P: X" + p1.getX() + "/Y:" + p1.getY() 
			+ " zu P: X" + p2.getX() + "/Y:" + p2.getY() 
			+ HackenbushConfig.getString("KanteBereitsEntferntException.message2"));
	}

}
