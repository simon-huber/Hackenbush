package com.simon.huber.hackenbush.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.simon.huber.hackenbush.resources.Settings;

/**
 * Panel, das den Fortschritt der Berechnung anzeigt
 * @author Simon Huber
 *
 */
public class FortschrittsPanel extends JFrame {

    private static final long serialVersionUID = 1L;
    
    /**
     * Zu berechnende Kanten
     */
    private int kanten_anzahl;
    
    /**
     * Fortschritt, der gleich der berechneten Kanten der angezeigten Figur ist
     */
    private int fortschritt = 0;
    
    /**
     * Anzahl der intern berechneten Kanten (Aufrufe der Methode, die einen Spielwert berechnet (im Singlethread: berechneFigur()))
     */
    private int berechneteKanten = 0;
    
    /**
     * Anzeige Labels
     */
    private JLabel text1 = new JLabel();
    private JLabel text_zeit = new JLabel("Zeit: 0 ms ");
    private JLabel text2 = new JLabel();
    
    /**
     * Buttons
     */
    private JButton button1 = new JButton("Abbruch");
    private JButton button2 = new JButton("Schließen");
    
    /**
     * Checkbox
     */
    private JCheckBox automatisch_beenden = new JCheckBox("Fenster automatisch schließen");

    private boolean abbruch = false;
    
    /**
     * Zeitmessung
     */
    private long time = System.currentTimeMillis();
    private Timer timer = new Timer();
    private Settings settings;
    
    /**
     * Runnable, die nach der Berechnung ausgeführt wird
     */
    private Runnable r;

    public FortschrittsPanel(Settings settings, int max, Runnable r) {
	if (max == 0) {
	    return;
	}
	this.settings = settings;
	kanten_anzahl = max;
	this.r = r;
	init();
	addWindowListener(new WindowAdapter() {
	    @Override
	    public void windowClosing(WindowEvent e) {
		abbruch = true;
	    }
	});
    }

    /**
     * Befüllen des Fensters
     */
    public void init() {
	
	BorderLayout layout = new BorderLayout();
	setLayout(layout);
	
	JPanel line1 = new JPanel();
	line1.add(text_zeit);
	line1.add(text1);
	line1.add(text2);
	
	JPanel line2 = new JPanel();
	line2.add(button1);
	
	button1.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent arg0) {
		abbruch();
	    }
	});
	button2.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		dispose();
	    }
	});
	button2.setEnabled(false);
	line2.add(button2);
	
	JPanel line3 = new JPanel();
	line3.add(automatisch_beenden);
	automatisch_beenden.setSelected(settings.isFortschritt_automatisch_beenden());
	automatisch_beenden.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent arg0) {
		settings.setFortschritt_automatisch_beenden(automatisch_beenden.isSelected());
	    }
	});
	// Hinzufügen der Panel
	getContentPane().add(line1, BorderLayout.NORTH);
	getContentPane().add(line2, BorderLayout.CENTER);
	getContentPane().add(line3, BorderLayout.SOUTH);

	setLocationRelativeTo(null);
	setSize(375, 150);
	setAlwaysOnTop(true);
	setName("Fortschritt");
	setTitle("Fortschritt der Berechnung");
	setVisible(true);
	
	updateTime();
	repaint();
    }

    /**
     * Inizialsiert den Timer, der die Zeit mitzählt
     */
    private void updateTime() {
	timer.schedule(new TimerTask() {

	    @Override
	    public void run() {
		text_zeit.setText("Zeit: " + (System.currentTimeMillis() - time) / 1000 + " s ");
		repaint();
	    }
	}, 0, 1000);
    }

    /**
     * Erhöht die Anzahl der berechneten Kanten der angezeigten Figur
     */
    public void addFortschritt() {
	text1.setText(" Aktueller Fortschritt: " + (++fortschritt) + "/" + kanten_anzahl);
	if (fortschritt == kanten_anzahl) {
	    abbruch();
	}
	repaint();
    }

    /**
     * Erhöht die Anzahl der intern berechneten Kanten
     */
    public void addKantenBerechnung() {
	text2.setText(" Intern berechnete Kanten: " + (++berechneteKanten));
	repaint();
    }

    /**
     * Gibt den Status zurück
     * @return abbruch
     */
    public boolean isAbbruch() {
	return abbruch;
    }

    /**
     * Bricht Berechnung ab
     */
    public void abbruch() {
	abbruch = true;
	timer.cancel();
	button1.setEnabled(false);
	button2.setEnabled(true);
	//debug
	System.out.println("Zeit: " + (System.currentTimeMillis() - time) + " ms");
	if (settings.isFortschritt_automatisch_beenden()) {
	    dispose();
	}
    }

    /**
     * Schließt Fenster und führt Runnable aus, falls vorhanden
     */
    @Override
    public void dispose() {
	super.dispose();
	if (r != null) {
	    r.run();
	}
    }

    /**
     * @return the kanten_anzahl
     */
    public int getKanten_anzahl() {
	return kanten_anzahl;
    }

    /**
     * @return the fortschritt
     */
    public int getFortschritt() {
	return fortschritt;
    }

}
