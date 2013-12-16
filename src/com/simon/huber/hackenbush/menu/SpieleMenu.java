package com.simon.huber.hackenbush.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import com.simon.huber.hackenbush.HauptFenster;
import com.simon.huber.hackenbush.gui.StartGameFrame;

/**
 * Erstellt ein Spiele-Menü, über das man ein neues Spiel starten kann.
 * @author Simon Huber
 *
 */
public class SpieleMenu extends JMenu {

    private static final long serialVersionUID = 1L;

    /**
     * Referenz auf das Hauptfenster
     */
    private HauptFenster frame;
    
    /**
     * MenuItem zum Starten eines neuen Spiels
     */
    private JMenuItem starten;

    public SpieleMenu(HauptFenster frame) {
	super("Spiel");
	this.frame = frame;
	create();
    }

    /**
     * Stellt ein neues Menü zusammen und befüllt dieses
     */
    public void create() {
	setEnabled(false);
	starten = new JMenuItem("Spiel starten");
	starten.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent arg0) {
		new StartGameFrame(frame);
	    }
	});
	add(starten);

	JMenuItem tipp = new JMenuItem("Beste Kante Blau");
	tipp.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent arg0) {
		frame.getHackenbushGraphics().setLastClickedEdge(frame.getHackenbushGraphics().getSpiel().getFigur().getBesteKante(1));
		frame.getHackenbushGraphics().repaint();
	    }
	});
	add(tipp);

	JMenuItem tipprot = new JMenuItem("Beste Kante Rot");
	tipprot.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent arg0) {
		frame.getHackenbushGraphics().setLastClickedEdge(frame.getHackenbushGraphics().getSpiel().getFigur().getBesteKante(2));
		frame.getHackenbushGraphics().repaint();
	    }
	});
	add(tipprot);
    }

    /**
     * Bestimmt, ob das MenuItem "Spiel starten" aktiviert ist
     * @param enabled
     */
    public void setStartGameEnabled(boolean enabled) {
	starten.setEnabled(enabled);
    }

}
