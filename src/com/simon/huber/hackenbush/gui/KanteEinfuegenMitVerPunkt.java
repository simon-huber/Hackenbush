package com.simon.huber.hackenbush.gui;

import com.simon.huber.hackenbush.dataobjects.HackenbushFigur;
import com.simon.huber.hackenbush.dataobjects.Kante;
import com.simon.huber.hackenbush.dataobjects.Punkt;
import com.simon.huber.hackenbush.dataobjects.VerbindungsPunkt;
import com.simon.huber.hackenbush.dataobjects.hilfsobjekte.HilfsKante;
import com.simon.huber.hackenbush.exceptions.KanteExistiertBereitsException;
import com.simon.huber.hackenbush.exceptions.KanteHatKeineBodenVerbindungException;
import com.simon.huber.hackenbush.exceptions.PunktNichtGefundenException;
import com.simon.huber.hackenbush.throwable.EinfuegenAbgeschlossen;

/**
 * Führt den Einfügevorgang für eine Kante mit Verbindungspunkt
 * @author Simon Huber
 *
 */
public class KanteEinfuegenMitVerPunkt extends KanteEinfuegen {

    /**
     * Verbindungspunkt
     */
    private VerbindungsPunkt verbindungspunkt;

    public KanteEinfuegenMitVerPunkt(int spieler, HackenbushFigur figur) {
	super(spieler, figur);
    }

    public Punkt getVerbindungspunkt() {
	return verbindungspunkt;
    }

    public void setVerbindungspunkt(VerbindungsPunkt verbindungspunkt) {
	this.verbindungspunkt = verbindungspunkt;
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
    @Override
    public void weiterenPunktEinfuegen(Punkt p) throws EinfuegenAbgeschlossen, KanteExistiertBereitsException, KanteHatKeineBodenVerbindungException {
	korrigierePunktPosition(p);
	if (p.equals(punkt1) || p.equals(punkt2)) {
	    return;
	}
	if (punkt1 == null) {
	    punkt1 = p;
	} else if (verbindungspunkt == null) {
	    verbindungspunkt = new VerbindungsPunkt(p.x, p.y);
	} else if (punkt2 == null && !p.istAmBoden()) {
	    punkt2 = p;
	}
	if (punkt2 != null) {
	    try {
		abschliessen();
	    } catch (PunktNichtGefundenException e) {
		e.printStackTrace();
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
    @Override
    protected void abschliessen() throws PunktNichtGefundenException, KanteExistiertBereitsException, KanteHatKeineBodenVerbindungException {
	if (punkt1 == null || punkt2 == null || verbindungspunkt == null) {
	    throw new PunktNichtGefundenException();
	}
	punkt1 = figur.einfuegenPunkt(punkt1);
	punkt2 = figur.einfuegenPunkt(punkt2);
	HilfsKante kante = new HilfsKante(punkt1, punkt2, new Kante(spieler, verbindungspunkt));
	if (figur.istMitBodenVerbundenPunkt(punkt1) || figur.istMitBodenVerbundenPunkt(punkt2)) {
	    if (figur.checkKanteExist(kante)) {
		throw new KanteExistiertBereitsException(kante);
	    } else {
		figur.einfügenKante(punkt1, punkt2, spieler, verbindungspunkt);
	    }
	} else {
	    throw new KanteHatKeineBodenVerbindungException(kante);
	}
    }

    @Override
    public Punkt[] getPunkte() {
	Punkt[] p = new Punkt[3];
	p[0] = punkt1;
	p[1] = verbindungspunkt;
	p[2] = punkt2;
	return p;
    }

}
