package com.simon.huber.hackenbush.actionlisteners;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JOptionPane;

import com.simon.huber.hackenbush.HauptFenster;
import com.simon.huber.hackenbush.dataobjects.Punkt;
import com.simon.huber.hackenbush.dataobjects.hilfsobjekte.HilfsKante;
import com.simon.huber.hackenbush.exceptions.KanteExistiertBereitsException;
import com.simon.huber.hackenbush.exceptions.KanteHatKeineBodenVerbindungException;
import com.simon.huber.hackenbush.gui.HackenbushGraphics;
import com.simon.huber.hackenbush.gui.KanteEinfuegen;
import com.simon.huber.hackenbush.gui.KantenToolbar;
import com.simon.huber.hackenbush.spieler.Computer;
import com.simon.huber.hackenbush.throwable.EinfuegenAbgeschlossen;

/**
 * Mausverwaltungsklasse
 * 
 * Hier werden alle Mauseingaben verwaltet
 * 
 * @author Simon Huber
 * 
 */
public class HackenbushMouseAdapter extends MouseAdapter {

    private HackenbushGraphics hackenbushGraphics;

    /**
     * Benötigt für das Zeichnen neuer Kanten
     */
    private double[] lastMouseClickedPosition = null;

    /**
     * Enthält einen Einfügevorgang
     */
    private KanteEinfuegen einfuegen = null;

    public HackenbushMouseAdapter(HackenbushGraphics g) {
	hackenbushGraphics = g;
    }

    @Override
    public void mouseMoved(MouseEvent e) {
	super.mouseMoved(e);
	if (hackenbushGraphics.getSpiel() == null) {
	    return;
	}

	aktualierungMausPosition(e);

	// Wenn die Maus auf einer Kante steht, wird hier die Kante gesucht
	HilfsKante hk = hackenbushGraphics.getSpiel().getFigur().getKantedurchKoordinaten(hackenbushGraphics.getLastMousePoint(), hackenbushGraphics.getGenauigkeit());
	if (hk != null) {
	    // Wenn eine Kante gefunden wurde, wird diese makiert
	    hackenbushGraphics.setLastMarkedEdge(hk);
	    hackenbushGraphics.repaint();
	    return;
	}
	if (hackenbushGraphics.getLastMarkedEdge() != null) {
	    hackenbushGraphics.setLastMarkedEdge(null);
	    // Keine Kante ist nun mehr makiert
	}
	hackenbushGraphics.repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
	super.mouseClicked(e);
	hackenbushGraphics.requestFocus();
	if (hackenbushGraphics.getSpiel() == null) {
	    return;
	}

	aktualierungMausPosition(e);
	lastMouseClickedPosition = hackenbushGraphics.getLastMousePoint();
	HauptFenster.getLogger().info("Mouse clicked: " + hackenbushGraphics.getLastMousePoint()[0] + "/" + hackenbushGraphics.getLastMousePoint()[1]);

	// Kanten können nur makiert werden, wenn zur Zeit keine Kante eingefügt
	// wird
	if (einfuegen == null) {
	    kanteMakieren();
	} else if (hackenbushGraphics.isEditMode()) {
	    // Abfragen ob editMode = true und deshalb eine neue Strecke
	    // eingefügt
	    // werden soll
	    kanteZeichnen(e);
	}
	hackenbushGraphics.repaint();
    }

    private void kanteMakieren() {
	HilfsKante hk = hackenbushGraphics.getSpiel().getFigur().getKantedurchKoordinaten(hackenbushGraphics.getLastMousePoint(), hackenbushGraphics.getGenauigkeit());
	// hk enthält eine angeklickte Kante, oder null
	if (hk != null) {
	    // Wenn Spiel gestarted ist, können nur noch eigene Kanten
	    // makiert werden
	    if (hackenbushGraphics.getSpiel().getAmZug() != -1) {
		// Spieler -1, da im Spiel der Spieler 1 = 0 ist
		if ((hk.getKante().getSpieler() - 1) != hackenbushGraphics.getSpiel().getAmZug()) {
		    return;
		}
		// kein Eingreifen durch Menschen möglich
		if (hackenbushGraphics.getSpiel().getTeilnehmer()[hackenbushGraphics.getSpiel().getAmZug()] instanceof Computer) {
		    return;
		}
	    }
	    // Zuweisung der geklickten Kante
	    if (hk.equals(hackenbushGraphics.getLastClickedEdge())) {
		hackenbushGraphics.setLastClickedEdge(null);
	    } else {
		hackenbushGraphics.setLastClickedEdge(hk);
		// Testausgabe des Kantenwerts, falls vorhanden
		if (hk.getKante().getWert() != null) {
		    HauptFenster.getLogger().info("Kantenwert: " + hk.getKante().getWert().getAnzeige());
		}
	    }
	    hackenbushGraphics.repaint();
	    return;
	} else if (hackenbushGraphics.getLastClickedEdge() != null) {
	    hackenbushGraphics.setLastClickedEdge(null);
	}
    }

    private void aktualierungMausPosition(MouseEvent e) {
	// Aktualisierung der Mausposition
	hackenbushGraphics.setLastMousePoint(new double[2]);
	hackenbushGraphics.getLastMousePoint()[0] = e.getX();
	hackenbushGraphics.getLastMousePoint()[1] = e.getY();
	hackenbushGraphics.setLastMousePoint(hackenbushGraphics.correctMousePosition(hackenbushGraphics.getLastMousePoint()));
    }

    /**
     * Ist fürs einfügen der Kante zuständig
     * 
     * @param e
     *            Mausevent
     */
    private void kanteZeichnen(MouseEvent e) {
	if (lastMouseClickedPosition != null) {
	    HauptFenster.getLogger().info("Zeichnen Kantenmodus 1");
	    boolean catched = false;
	    try {
		einfuegen.weiterenPunktEinfuegen(new Punkt((int) lastMouseClickedPosition[0], (int) lastMouseClickedPosition[1]));
	    } catch (EinfuegenAbgeschlossen e1) {
		// Einfügen abgeschlossen
		catched = true;
	    } catch (KanteExistiertBereitsException e1) {
		JOptionPane.showMessageDialog(hackenbushGraphics, "Einfügen der Kante fehlgeschlagen: " + e1.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
		catched = true;
	    } catch (KanteHatKeineBodenVerbindungException e1) {
		JOptionPane.showMessageDialog(hackenbushGraphics, "Kante hatte keine Verbindung zum Boden und wurde deshalb entfernt.", "Fehler", JOptionPane.ERROR_MESSAGE);
		catched = true;
	    }
	    if (catched == true) {
		// Einfügen ist entweder abgeschlossen oder fehlgeschlagen
		einfuegen = null;
		hackenbushGraphics.getSpiel().getFigur().entfernenVonFliegendenKantenStart();
		KantenToolbar.resetStartButton();
	    }
	}
    }

    /**
     * Das aktuelle Objekt, das das Einfügen der Kanten übernimmt
     * 
     * @return KanteEinfuegen
     */
    public KanteEinfuegen getEinfuegen() {
	return einfuegen;
    }

    /**
     * Wird von der KantenToolbar aufgerufen und "beginnt" somit einen neuen
     * Einfügevorgang
     * 
     * @param einfuegen
     *            {@link KanteEinfuegen}
     */
    public void setEinfuegen(KanteEinfuegen einfuegen) {
	this.einfuegen = einfuegen;
    }
}
