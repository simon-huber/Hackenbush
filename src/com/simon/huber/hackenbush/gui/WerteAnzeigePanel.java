package com.simon.huber.hackenbush.gui;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.simon.huber.hackenbush.dataobjects.Bruch;

/**
 * Ist für die Werteanzeige zuständig
 * @author Simon Huber
 *
 */
public class WerteAnzeigePanel extends JPanel {

    private static final long serialVersionUID = 1L;

    /**
     * Beschriftungslabel für das JLabel anzeige_wert_gesamt
     */
    private JLabel label_wert_gesamt = new JLabel("Aktueller Spielwert: ");
    /**
     * Anzeigefeld für den aktuellen Spielwert der Figur
     */
    private JLabel anzeige_wert_gesamt = new JLabel();

    /**
     * Beschriftungslabel für das JLabel anzeige_wert_gesamt
     */
    private JLabel label_wert_kante = new JLabel("Kantenwert: ");
    /**
     * Anzeigefeld für den aktuellen Spielwert der Figur
     */
    private JLabel anzeige_wert_kante = new JLabel();

    public WerteAnzeigePanel() {
	erstellen();
    }

    /**
     * Befüllen des Labels
     */
    private void erstellen() {
	add(label_wert_gesamt);
	label_wert_gesamt.setFocusable(false);
	add(anzeige_wert_gesamt);
	anzeige_wert_gesamt.setFocusable(false);
	add(label_wert_kante);
	label_wert_kante.setFocusable(false);
	add(anzeige_wert_kante);
	anzeige_wert_kante.setFocusable(false);
	setVisible(true);
	repaint();
    }

    /**
     * Setzt den Text des Anzeigelabels
     * 
     * @param bruch
     *            Spielwert der gesamten Figur
     */
    public void setSpielwert(Bruch bruch) {
	anzeige_wert_gesamt.setText(bruch.getAnzeige());
    }

    /**
     * Setzt den Text des Anzeigelabels
     * 
     * @param bruch
     *            Spielwert der aktuellen Kante
     */
    public void setKantenwert(Bruch bruch) {
	if (bruch != null) {
	    anzeige_wert_kante.setText(bruch.getAnzeige());
	} else {
	    anzeige_wert_kante.setText("");
	}
    }

}
