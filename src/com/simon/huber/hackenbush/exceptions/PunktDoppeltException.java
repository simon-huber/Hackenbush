package com.simon.huber.hackenbush.exceptions;

import com.simon.huber.hackenbush.dataobjects.Punkt;
import com.simon.huber.hackenbush.tools.HackenbushConfig;

public class PunktDoppeltException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public PunktDoppeltException(Punkt p) {
		super(HackenbushConfig.getString("PunktDoppeltException.message1") + " X: " + p.getX() + "/ Y: " + p.getY() + HackenbushConfig.getString("PunktDoppeltException.message2"));
	}

}
