package com.simon.huber.hackenbush.throwable;

/**
 * Rückgabe Nachricht für das erfolgreiche Erstellen einer Kante
 * @author Simon Huber
 *
 */
public class EinfuegenAbgeschlossen extends Throwable {

    private static final long serialVersionUID = 1L;

    public EinfuegenAbgeschlossen() {
	super("Einfügen der Kante ist abgeschlossen");
    }
}
