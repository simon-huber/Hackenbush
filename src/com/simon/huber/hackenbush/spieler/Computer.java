package com.simon.huber.hackenbush.spieler;

import java.util.Timer;
import java.util.TimerTask;

import com.simon.huber.hackenbush.Spiel;
import com.simon.huber.hackenbush.dataobjects.hilfsobjekte.HilfsKante;
import com.simon.huber.hackenbush.gui.HackenbushGraphics;

/**
 * Computer ist ein Teilnehmer und handelt anders als ein Spieler
 * @author Simon Huber
 *
 */
public class Computer extends Teilnehmer {

    public Computer(Spiel spiel, int spielernr) {
	super(spiel, spielernr);
    }

    @Override
    public void beginneZug(final HackenbushGraphics component) {
	// Bestimmen der besten Kante
	final HilfsKante besteKante = getSpiel().getFigur().getBesteKante(getSpielernr());
	// Makierung der entsprechenden Kante
	component.setLastClickedEdge(besteKante);
	component.repaint();
	//Zeitversetzung, damit die Kantenmakierung sichtbar ist
	Timer timer = new Timer();
	timer.schedule(new TimerTask() {

	    @Override
	    public void run() {
		// Computer entfernt kante
		getSpiel().entferneKante(component, besteKante);
	    }
	}, 2000);
    }

}
