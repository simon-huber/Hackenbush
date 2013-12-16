package com.simon.huber.hackenbush.exceptions;

import com.simon.huber.hackenbush.dataobjects.Kante;
import com.simon.huber.hackenbush.dataobjects.Punkt;
import com.simon.huber.hackenbush.dataobjects.hilfsobjekte.HilfsKante;
import com.simon.huber.hackenbush.tools.HackenbushConfig;

/**
 * Wird beim Einfügen einer Kante geworfen, wenn diese bereits existiert
 * @author Simon Huber
 *
 */
public class KanteExistiertBereitsException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public KanteExistiertBereitsException(Kante k, Punkt p1, Punkt p2) {
		super(HackenbushConfig.getString("KanteExistiertBereitsException.message1") 
			+ " Spieler: " + k.getSpieler() 
			+ " von P: X" + p1.getX() + "/Y:" + p1.getY() 
			+ " zu P: X" + p2.getX() + "/Y:" + p2.getY() 
			+ HackenbushConfig.getString("KanteExistiertBereitsException.message2"));
	}
	public KanteExistiertBereitsException(HilfsKante k) {
		super(HackenbushConfig.getString("KanteExistiertBereitsException.message1") 
			+ " Spieler: " + k.getKante().getSpieler() 
			+ " von P: X" + k.getPunkt()[0].getX() + "/Y:" + k.getPunkt()[0].getY() 
			+ " zu P: X" + k.getPunkt()[1].getX() + "/Y:" + k.getPunkt()[1].getY() 
			+ HackenbushConfig.getString("KanteExistiertBereitsException.message2"));
	}

}
