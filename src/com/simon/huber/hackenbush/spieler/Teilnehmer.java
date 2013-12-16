package com.simon.huber.hackenbush.spieler;

import com.simon.huber.hackenbush.Spiel;
import com.simon.huber.hackenbush.exceptions.KanteNichtGefundenException;
import com.simon.huber.hackenbush.exceptions.PunktNichtGefundenException;
import com.simon.huber.hackenbush.exceptions.ZuWenigeTeilnehmerException;
import com.simon.huber.hackenbush.gui.HackenbushGraphics;
import com.simon.huber.hackenbush.throwable.SpielerGewonnenMessage;

/**
 * Klasse Teilnehmer, von dieser erben Spieler und Computer. 
 * 
 * @author Simon Huber
 * 
 */
public abstract class Teilnehmer {

    private Spiel spiel;
    private int spielernr;

    public Teilnehmer(Spiel spiel, int spielernr) {
	this.spiel = spiel;
	this.spielernr = spielernr;
    }

    /**
     * Startet einen neuen Zug für den Teilnehmer (Muss überschrieben werden)
     * 
     * @throws SpielerGewonnenMessage
     * @throws KanteNichtGefundenException
     * @throws PunktNichtGefundenException
     * @throws ZuWenigeTeilnehmerException
     */
    public void beginneZug(HackenbushGraphics component) {

    }

    protected Spiel getSpiel() {
	return spiel;
    }

    public int getSpielernr() {
	return spielernr;
    }

}
