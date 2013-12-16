package com.simon.huber.hackenbush.exceptions;

import com.simon.huber.hackenbush.dataobjects.Punkt;
import com.simon.huber.hackenbush.tools.HackenbushConfig;

public class PunktNichtGefundenException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public PunktNichtGefundenException(Punkt p) {
		super(HackenbushConfig.getString("PunktNichtGefundenException.message1") + " X: " + p.getX() + "/ Y: " + p.getY() + HackenbushConfig.getString("PunktNichtGefundenException.message2"));
	}
	
	public PunktNichtGefundenException() {
		super("Punkt nicht gefunden!");
	}

}
