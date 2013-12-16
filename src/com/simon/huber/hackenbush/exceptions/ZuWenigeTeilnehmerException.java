package com.simon.huber.hackenbush.exceptions;

public class ZuWenigeTeilnehmerException extends Exception {

    private static final long serialVersionUID = 1L;

    public ZuWenigeTeilnehmerException() {
	super("Zu wenige Teilnehmer initialisiert!");
    }
}
