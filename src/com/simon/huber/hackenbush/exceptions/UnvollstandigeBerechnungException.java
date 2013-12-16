package com.simon.huber.hackenbush.exceptions;

import com.simon.huber.hackenbush.dataobjects.hilfsobjekte.HilfsKante;

public class UnvollstandigeBerechnungException extends Exception {

    private static final long serialVersionUID = 1L;

    public UnvollstandigeBerechnungException(HilfsKante k) {
	super("Die Kante " + k.getPunkt()[0].getX() + "/" + k.getPunkt()[0].getY() + " - " + k.getPunkt()[1].getX() + "/" + k.getPunkt()[1].getY() + " hat keinen Wert!");
    }

}
