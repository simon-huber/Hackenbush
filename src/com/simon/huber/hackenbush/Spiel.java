/**
 * 
 */
package com.simon.huber.hackenbush;

import java.io.Serializable;

import javax.swing.JOptionPane;

import com.simon.huber.hackenbush.dataobjects.HackenbushFigur;
import com.simon.huber.hackenbush.dataobjects.Punkt;
import com.simon.huber.hackenbush.dataobjects.hilfsobjekte.HilfsKante;
import com.simon.huber.hackenbush.exceptions.EinfachheitsRegelException;
import com.simon.huber.hackenbush.exceptions.KanteNichtGefundenException;
import com.simon.huber.hackenbush.exceptions.PunktNichtGefundenException;
import com.simon.huber.hackenbush.exceptions.UnvollstandigeBerechnungException;
import com.simon.huber.hackenbush.exceptions.ZuWenigeTeilnehmerException;
import com.simon.huber.hackenbush.gui.HackenbushGraphics;
import com.simon.huber.hackenbush.resources.Settings;
import com.simon.huber.hackenbush.spieler.Teilnehmer;
import com.simon.huber.hackenbush.throwable.SpielerGewonnenMessage;

/**
 * Spiel-Klasse
 * 
 * Diese Klasse enthält alles, was für die Abwicklung eines Spiels
 * nötig ist, entscheidet jedoch nicht welche Kanten entfernt werden sollen.
 * 
 * @author Simon Huber
 */
public class Spiel implements Serializable {

    private static final long serialVersionUID = -2820829974696897341L;

    /**
     * Name des Spiels
     */
    private String name;

    /**
     * Die Figur
     */
    private HackenbushFigur figur;

    /**
     * Settings (werden vom Fenster jedesmal neu gesetzt, da die Setttings
     * spielunabhängig sind)
     */
    private Settings settings;

    /**
     * Spieler, die noch nicht initialisiert wurden
     */
    private Teilnehmer[] teilnehmer = null;

    /**
     * -1 (nicht gestartet), 0, 1 (jeweils der Teilnehmer)
     */
    private int amZug = -1;

    /**
     * @param settings
     *            the settings to set
     */
    public void setSettings(Settings settings) {
	this.settings = settings;
    }

    /**
     * Konstruktor eines neuen Spiels
     * 
     * @throws PunktNichtGefundenException
     */
    public Spiel(String name, int breite) throws PunktNichtGefundenException {
	this.name = name;
	figur = new HackenbushFigur();
	teilnehmer = new Teilnehmer[2];

	/**
	 * Testscript für Extremtests  //bei Datentyp "long" Abrruch bei 57. Kante (Überlauf)
	 */
	for (int i = 0; i < 4; i++) {
	    Punkt b = new Punkt(1, i * 10);
	    b = figur.einfuegenPunkt(b);
	    if (i < 1) {
		continue;
	    }
	    if (i % 2 == 0) {
		figur.einfügenKante(figur.getPunktdurchKoordinaten(1, (i - 1) * 10, 0.1), b, 1);
	    } else {
		figur.einfügenKante(figur.getPunktdurchKoordinaten(1, (i - 1) * 10, 0.1), b, 2);
	    }
	}
	// for (int i = 0; i < 5; i++) {
	// Punkt b = new Punkt(10, i * 10);
	// b = figur.einfuegenPunkt(b);
	// if(i < 1) {
	// continue;
	// }
	// if (i % 2 == 0) {
	// figur.einfügenKante(figur.getPunktdurchKoordinaten(10, (i - 1) * 10,
	// 0.1), b, 1);
	// } else {
	// figur.einfügenKante(figur.getPunktdurchKoordinaten(10, (i - 1) * 10,
	// 0.1), b, 2);
	// }
	// }

    }

    /**
     * @return Name des Spiels
     */
    public String getName() {
	return name;
    }

    /**
     * Startet ein neues Spiel zwischen den beiden Teilnehmern
     * 
     * @throws ZuWenigeTeilnehmerException
     * @throws SpielerGewonnenMessage
     * @throws KanteNichtGefundenException
     * @throws PunktNichtGefundenException
     */
    public void startGame(final HackenbushGraphics component) throws ZuWenigeTeilnehmerException, PunktNichtGefundenException, KanteNichtGefundenException, SpielerGewonnenMessage {
	if (teilnehmer[0] != null && teilnehmer[1] != null) {
	    Runnable r = new Runnable() {

		@Override
		public void run() {
		    try {
			component.getWerte_panel().setSpielwert(figur.getSpielWert());
		    } catch (EinfachheitsRegelException | UnvollstandigeBerechnungException e) {
			e.printStackTrace();
		    }
		    setAmZug(0);
		    teilnehmer[0].beginneZug(component);
		    component.repaint();
		}
	    };
	    figur.starteWerteBerechnung(settings, r);
	} else {
	    throw new ZuWenigeTeilnehmerException();
	}
    }

    /**
     * Entfernt eine Kante und schließt Zug ab
     * 
     * @param kante
     * @throws PunktNichtGefundenException
     * @throws SpielerGewonnenMessage
     * @throws KanteNichtGefundenException
     * @throws ZuWenigeTeilnehmerException
     */
    public void entferneKante(final HackenbushGraphics component, HilfsKante kante) {

	if (kante != null) {
	    if (kante.getKante() == null) {
		JOptionPane.showMessageDialog(component,
			"Entfernen der Kante fehlgeschlagen: " + new KanteNichtGefundenException(kante.getKante(), kante.getPunkt()[0], kante.getPunkt()[1]).getMessage(), "Fehler",
			JOptionPane.ERROR_MESSAGE);
	    }
	} else {
	    return;
	}

	if (teilnehmer[0] == null && teilnehmer[1] == null) {
	    JOptionPane.showMessageDialog(component, "Entfernen der Kante fehlgeschlagen: " + new ZuWenigeTeilnehmerException().getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
	}

	if (getAmZug() == (kante.getKante().getSpieler() - 1)) {

	    try {
		figur.entferneKante(kante);
		component.setLastClickedEdge(null);
	    } catch (PunktNichtGefundenException e) {
		JOptionPane.showMessageDialog(component, "Entfernen der Kante fehlgeschlagen: " + e.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
		e.printStackTrace();
	    }

	    int nurnochKantenvon = figur.beinhaltetNurNochKantenVon();
	    int[] kantenzahl = figur.getKantenAnzahl();

	    if (nurnochKantenvon > 0 && nurnochKantenvon < 3) {
		setAmZug(-1);
		gewonnenPopup(component, nurnochKantenvon);
	    } else if ((kantenzahl[0] + kantenzahl[1]) == 0) {
		int gewinner = getAmZug();
		setAmZug(-1);
		gewonnenPopup(component, gewinner + 1);
	    }
	    starteNeuenZug(component);
	}
    }
    
    /**
     * Beginnt einen neuen Zug
     * @param component
     */
    private void starteNeuenZug(final HackenbushGraphics component) {
	switch (getAmZug()) {
	    case 0:
		Runnable r = new Runnable() {

		    @Override
		    public void run() {
			try {
			    component.getWerte_panel().setSpielwert(figur.getSpielWert());
			} catch (EinfachheitsRegelException | UnvollstandigeBerechnungException e) {
			    e.printStackTrace();
			}
			setAmZug(1);
			teilnehmer[getAmZug()].beginneZug(component);
			component.repaint();
		    }
		};
		figur.starteWerteBerechnung(settings, r);
		break;

	    case 1:
		Runnable r2 = new Runnable() {

		    @Override
		    public void run() {
			try {
			    component.getWerte_panel().setSpielwert(figur.getSpielWert());
			} catch (EinfachheitsRegelException | UnvollstandigeBerechnungException e) {
			    e.printStackTrace();
			}
			setAmZug(0);
			teilnehmer[getAmZug()].beginneZug(component);
			component.repaint();
		    }
		};
		figur.starteWerteBerechnung(settings, r2);
		break;

	    default:
		break;
	    }
    }

    /**
     * Zeigt das Gewinner-Popup an
     * 
     * @param component
     *            Component-Objekt
     * @param spieler
     *            Spieler, der gewonnen hat
     */
    private void gewonnenPopup(HackenbushGraphics component, int spieler) {
	JOptionPane.showMessageDialog(component, new SpielerGewonnenMessage(spieler).getMessage(), "Spielende!", JOptionPane.INFORMATION_MESSAGE);
	if (component.getFrame() instanceof HauptFenster) {
	    HauptFenster fenster = (HauptFenster) component.getFrame();
	    fenster.getSpielMenu().setStartGameEnabled(true);
	}
    }

    /**
     * @return Hackenbush-Figur
     */
    public HackenbushFigur getFigur() {
	return figur;
    }

    /**
     * @return Spieler-Objekte
     */
    public Teilnehmer[] getTeilnehmer() {
	return teilnehmer;
    }

    /**
     * @return Spieler, der am Zug ist
     */
    public int getAmZug() {
	return amZug;
    }

    /**
     * Setzt einen Spieler auf den Status "amZug"
     * 
     * @param amZug
     *            Spieler der am Zug sein soll
     */
    public void setAmZug(int amZug) {
	this.amZug = amZug;
    }

}
