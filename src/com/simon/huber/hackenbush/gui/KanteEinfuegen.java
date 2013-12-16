package com.simon.huber.hackenbush.gui;

import com.simon.huber.hackenbush.dataobjects.HackenbushFigur;
import com.simon.huber.hackenbush.dataobjects.Kante;
import com.simon.huber.hackenbush.dataobjects.Punkt;
import com.simon.huber.hackenbush.dataobjects.hilfsobjekte.HilfsKante;
import com.simon.huber.hackenbush.exceptions.KanteExistiertBereitsException;
import com.simon.huber.hackenbush.exceptions.KanteHatKeineBodenVerbindungException;
import com.simon.huber.hackenbush.exceptions.PunktNichtGefundenException;
import com.simon.huber.hackenbush.throwable.EinfuegenAbgeschlossen;

public class KanteEinfuegen {

    protected int spieler;
    protected HackenbushFigur figur;
    protected Punkt punkt1;
    protected Punkt punkt2;

    public KanteEinfuegen(int spieler, HackenbushFigur figur) {
	this.spieler = spieler;
	this.figur = figur;
    }

    public void setPunkt1(Punkt punkt1) {
	this.punkt1 = punkt1;
    }

    public void setPunkt2(Punkt punkt2) {
	this.punkt2 = punkt2;
    }

    public Punkt getPunkt1() {
	return punkt1;
    }

    public Punkt getPunkt2() {
	return punkt2;
    }

    /**
     * Fügt einen Punkt zum Erstellungsprozess hinzu, falls alle Punkte bereits
     * gesetzt wurden, passiert nichts
     * 
     * @param p
     *            Einzufügender Punkt
     * @throws EinfuegenAbgeschlossen
     *             wird geworfen, wenn das Einfügen der Kante abgeschlossen ist
     * @throws KanteExistiertBereitsException
     *             falls beim Abschluss der Vorgangs festgestellt wird, dass die
     *             Kante schon existiert
     * @throws KanteHatKeineBodenVerbindungException 
     */
    public void weiterenPunktEinfuegen(Punkt p) throws EinfuegenAbgeschlossen, KanteExistiertBereitsException, KanteHatKeineBodenVerbindungException {
	korrigierePunktPosition(p);
	if(p.equals(punkt1) || p.equals(punkt2)) {
	    return;
	}
	
	if (punkt1 == null) {
	    punkt1 = p;
	} else if (punkt2 == null && !p.istAmBoden()) {
	    punkt2 = p;
	}
	if (punkt2 != null) {
	    try {
		abschliessen();
	    } catch (PunktNichtGefundenException e) {
	    }
	    throw new EinfuegenAbgeschlossen();
	}
    }

    /**
     * Schließt den Einfügevorgang ab
     * 
     * @throws PunktNichtGefundenException
     *             wird geworfen, falls ein Punkt noch nicht initialisiert wurde
     * @throws KanteExistiertBereitsException
     *             Falls die Kante bereits existiert
     * @throws KanteHatKeineBodenVerbindungException 
     */
    protected void abschliessen() throws PunktNichtGefundenException, KanteExistiertBereitsException, KanteHatKeineBodenVerbindungException {
	if (punkt1 == null || punkt2 == null) {
	    throw new PunktNichtGefundenException();
	}
	punkt1 = figur.einfuegenPunkt(punkt1);
	punkt2 = figur.einfuegenPunkt(punkt2);
	HilfsKante kante = new HilfsKante(punkt1, punkt2, new Kante(spieler));
	if (figur.istMitBodenVerbundenPunkt(punkt1) || figur.istMitBodenVerbundenPunkt(punkt2)) {
	    if (figur.checkKanteExist(kante)) {
		throw new KanteExistiertBereitsException(kante);
	    } else {
		figur.einfügenKante(punkt1, punkt2, spieler);
	    }
	} else {
	    throw new KanteHatKeineBodenVerbindungException(kante);
	}
    }

    /**
     * Gibt alle Punkte samt Verbindungspunkt zurück, damit diese in HackenbushGraphics gezeichnet werden können
     * @return Punkte, die beim Einfügevorgang angeklickt wurden
     */
    public Punkt[] getPunkte() {
	Punkt[] p = new Punkt[2];
	p[0] = punkt1;
	p[1] = punkt2;
	return p;
    }
    
    /**
     * Korrigiert die Position eines geklickten Punktes (Damit kein Punkt unter der Grundlinie ist)
     * @param p
     */
    protected void korrigierePunktPosition(Punkt p) {
	if (0 >= p.y) {
	    p.y = 0;
	} else {
	    Punkt temp = figur.getPunktdurchKoordinaten(p.x, p.y, 2);
	    if (temp != null) {
		p = temp;
	    }
	}
    }
}
