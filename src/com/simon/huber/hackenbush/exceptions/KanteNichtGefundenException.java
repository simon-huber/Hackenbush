package com.simon.huber.hackenbush.exceptions;

import com.simon.huber.hackenbush.dataobjects.Kante;
import com.simon.huber.hackenbush.dataobjects.Punkt;
import com.simon.huber.hackenbush.tools.HackenbushConfig;

public class KanteNichtGefundenException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public KanteNichtGefundenException(Kante k, Punkt p1, Punkt p2) {
		super(HackenbushConfig.getString("KanteNichtGefundenException.message1") 
			+ " Spieler: " + k.getSpieler() 
			+ " von P: X" + p1.getX() + "/Y:" + p1.getY() 
			+ " zu P: X" + p2.getX() + "/Y:" + p2.getY() 
			+ HackenbushConfig.getString("KanteNichtGefundenException.message2"));
	}

}
