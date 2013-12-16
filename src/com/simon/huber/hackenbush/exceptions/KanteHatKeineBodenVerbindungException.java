package com.simon.huber.hackenbush.exceptions;

import com.simon.huber.hackenbush.dataobjects.Kante;
import com.simon.huber.hackenbush.dataobjects.Punkt;
import com.simon.huber.hackenbush.dataobjects.hilfsobjekte.HilfsKante;

public class KanteHatKeineBodenVerbindungException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public KanteHatKeineBodenVerbindungException(Kante k, Punkt p1, Punkt p2) {
		super("Die Kante " 
			+ " Spieler: " + k.getSpieler() 
			+ " von P: X" + p1.getX() + "/Y:" + p1.getY() 
			+ " zu P: X" + p2.getX() + "/Y:" + p2.getY() 
			+ " hat keine Bodenverbindung!");
	}
	public KanteHatKeineBodenVerbindungException(HilfsKante k) {
		super("Die Kante " 
			+ " Spieler: " + k.getKante().getSpieler() 
			+ " von P: X" + k.getPunkt()[0].getX() + "/Y:" + k.getPunkt()[0].getY() 
			+ " zu P: X" + k.getPunkt()[1].getX() + "/Y:" + k.getPunkt()[1].getY() 
			+ " hat keine Bodenverbindung!");
	}

}
