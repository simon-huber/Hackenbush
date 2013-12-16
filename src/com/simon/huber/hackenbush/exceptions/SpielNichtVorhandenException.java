package com.simon.huber.hackenbush.exceptions;

public class SpielNichtVorhandenException extends Exception {

    private static final long serialVersionUID = 1L;

    public SpielNichtVorhandenException() {
	super("Kein Spiel vorhanden!");
    }
}
