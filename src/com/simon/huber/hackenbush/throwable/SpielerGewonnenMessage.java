package com.simon.huber.hackenbush.throwable;

/**
 * Klasse ist eine Message, die geworfen wird, falls ein Spieler gewonnen hat.
 * @author Simon Huber
 *
 */
public class SpielerGewonnenMessage extends Throwable {

    private static final long serialVersionUID = 1L;

    public SpielerGewonnenMessage(int spieler) {
	super("Spieler " + (spieler == 1 ? "Blau" : "Rot") + " hat gewonnen!");
    }
}
