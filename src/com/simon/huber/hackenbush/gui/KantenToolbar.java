package com.simon.huber.hackenbush.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

/**
 * Ist für das einfügen von neuen Kanten zuständig
 * @author Simon Huber
 *
 */
public class KantenToolbar extends JPanel {

    private static final long serialVersionUID = 1L;
    private HackenbushGraphics graphics;

    // Buttons
    private static JRadioButton spielerblau = null;
    private static JRadioButton spielerrot = null;
    private static JComboBox<String> kantentypbox = null;
    private static JButton start = null;

    public KantenToolbar(HackenbushGraphics g) {
	graphics = g;
	initToolbar();
	// Damit KeyListener immer im Editor ankommt
	this.setFocusable(false);
    }

    /**
     * Befüllt die Toolbaar
     */
    private void initToolbar() {
	
	JLabel spieler = new JLabel("Spieler:");
	this.add(spieler);
	
	ButtonGroup spielergroup = new ButtonGroup();
	
	spielerblau = new JRadioButton("Blau");
	spielerblau.setSelected(true);
	spielerrot = new JRadioButton("Rot");
	spielergroup.add(spielerblau);
	spielergroup.add(spielerrot);
	
	spielerblau.setFocusable(false);
	this.add(spielerblau);
	
	spielerrot.setFocusable(false);
	this.add(spielerrot);
	
	JLabel kantentyp = new JLabel("Kantenart:");
	this.add(kantentyp);
	
	final String[] content = new String[2];
	content[0] = "Normale Kanten";
	content[1] = "Kanten mit Verbindungspunkt";
	kantentypbox = new JComboBox<>(content);
	kantentypbox.setFocusable(false);
	this.add(kantentypbox);
	
	start = new JButton("Neue Kante");
	start.setFocusable(false);
	start.setBackground(Color.GREEN);
	start.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent arg0) {
		if (start.getText().equals("Neue Kante")) {
		    if (kantentypbox.getSelectedIndex() == 0) {
			graphics.getMouseAdapter().setEinfuegen(new KanteEinfuegen(spielerblau.isSelected() ? 1 : 2, graphics.getSpiel().getFigur()));
		    } else {
			graphics.getMouseAdapter().setEinfuegen(new KanteEinfuegenMitVerPunkt(spielerblau.isSelected() ? 1 : 2, graphics.getSpiel().getFigur()));
		    }
		    spielerblau.setEnabled(false);
		    spielerrot.setEnabled(false);
		    kantentypbox.setEnabled(false);
		    start.setText("Abbrechen");
		} else {
		    graphics.getMouseAdapter().setEinfuegen(null);
		    resetStartButton();
		}
	    }
	});
	this.add(start);
	
	spielerblau.setEnabled(false);
	spielerrot.setEnabled(false);
	kantentypbox.setEnabled(false);
	start.setEnabled(false);
	
	repaint();
    }

    /**
     * Aktiviert nach dem Einfügen einer Kante wieder alle Schaltflächen
     */
    public static void resetStartButton() {
	spielerblau.setEnabled(true);
	spielerrot.setEnabled(true);
	kantentypbox.setEnabled(true);
	start.setEnabled(true);
	start.setText("Neue Kante");
    }
}
