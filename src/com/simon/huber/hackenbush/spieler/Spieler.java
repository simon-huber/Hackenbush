package com.simon.huber.hackenbush.spieler;

import com.simon.huber.hackenbush.Spiel;
import com.simon.huber.hackenbush.gui.HackenbushGraphics;

/**
 * Ein Spieler ist ein Teilnehmer. Der Spieler hat keine spezifischen
 * Funktionen, da der "menschliche Spieler" oder allgemein der Teilnehmer den
 * Spielzug durchführt.
 * 
 * Wichtig ist jedoch einen "Computer" von einem "Spieler zu unterscheiden",
 * deshalb wird diese seperatete Klasse verwendet
 * 
 * @author Simon Huber
 * 
 */
public class Spieler extends Teilnehmer {

    public Spieler(Spiel spiel, int spielernr) {
	super(spiel, spielernr);
    }

    @Override
    public void beginneZug(HackenbushGraphics component) {
	// HackenbushGraphics zuständig, deshalb nichts tun
    }

}
