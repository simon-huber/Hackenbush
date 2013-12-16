package com.simon.huber.hackenbush;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.Graphics2D;
import java.awt.SplashScreen;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.simon.huber.hackenbush.gui.HackenbushGraphics;
import com.simon.huber.hackenbush.gui.ZoomToolbar;
import com.simon.huber.hackenbush.menu.EinstellungenMenu;
import com.simon.huber.hackenbush.tools.HackenbushConfig;
import com.simon.huber.hackenbush.tools.HackenbushFilter;
import com.simon.huber.hackenbush.tools.SpielManagementTools;

public class HackenbushFenster extends JFrame {
    /**
     * wird von JFrame gefordert
     */
    protected static final long serialVersionUID = 1160917263822092616L;

    /**
     * Logger des Programms (erreichbar durch Getter)
     */
    protected static Logger logger = Logger.getLogger("Hackenbush");

    /**
     * Menüeintrag Öffnen
     */
    protected JMenuItem Menu_itemOeffnen = null;

    /**
     * Menüeintrag Speichern
     */
    protected JMenuItem Menu_itemSpeichern = null;

    /**
     * Menüeintrag neues Spiel
     */
    protected JMenuItem Menu_itemNeuesSpiel = null;

    /**
     * Menüeintrag Beenden
     */
    protected JMenuItem Menu_itemBeenden = null;
    
    protected JPanel anzeige_panel = new JPanel();

    /**
     * Toolbar zum Wählen des Zooms
     */
    protected ZoomToolbar zoom = null;
    
    protected EinstellungenMenu einstellungen;

    /**
     * Anzeigekomponente
     */
    protected HackenbushGraphics hackenbushGraphics = new HackenbushGraphics(this);

    /**
     * @return the hackenbushGraphics
     */
    public HackenbushGraphics getHackenbushGraphics() {
	return hackenbushGraphics;
    }


    public HackenbushFenster() {
	setFocusable(true);
	anzeige_panel.setLayout(new BorderLayout());
	zoom = new ZoomToolbar(this);
	requestFocus();
	getLogger().info("Starting ....");
	erzeugeFenster();
	getLogger().info("gestarted.");
    }

    public Spiel getSpiel() {
	return hackenbushGraphics.getSpiel();
    }
    

    /**
     * Wird aufgerufen, wenn das Programm beendet wird.
     */
    public void beenden() {
	try {
	    if (hackenbushGraphics.getSpiel() != null) {
		int save = JOptionPane.showConfirmDialog(HackenbushFenster.this, HackenbushConfig.getString("Hackenbush.spiel.speichern.abfrage.1"),
			HackenbushConfig.getString("Hackenbush.spiel.speichern.abfrage.2"), JOptionPane.OK_CANCEL_OPTION);
		if (save == JOptionPane.OK_OPTION) {
		    SpielSpeichernClick();
		}
	    }

	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    /**
     * Initialisiert das MenuItem "Neues Spiel"
     * 
     * @return Menu_itemSpeichern
     */
    protected JMenuItem erzeugeMenuItemBeenden() {
	/**
	 * Neues Spiel Menüeintrag
	 */
	Menu_itemBeenden = new JMenuItem(HackenbushConfig.getString("Hackenbush.menu.datei.beenden"),
		new ImageIcon(this.getClass().getResource(HackenbushConfig.getString("Hackenbush.datei.beenden"))));

	Menu_itemBeenden.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent event) {
		beenden();
	    }
	});
	return Menu_itemBeenden;
    }

    /**
     * Initialisiert das MenuItem "Öffnen"
     * 
     * @return Menu_itemOeffnen
     */
    protected JMenuItem erzeugeMenuItemOeffnen() {
	/**
	 * Öffnen Menüeintrag
	 */

	Menu_itemOeffnen = new JMenuItem(HackenbushConfig.getString("Hackenbush.menu.oeffnen"), new ImageIcon(this.getClass().getResource(HackenbushConfig.getString("Hackenbush.datei.spieloeffnen"))));

	// Registrieren einer Tastenkombination
	Menu_itemOeffnen.setAccelerator(KeyStroke.getKeyStroke('O', Event.CTRL_MASK));

	Menu_itemOeffnen.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent event) {
		SpielOeffnenClick();
	    }
	});

	return Menu_itemOeffnen;
    }

    /**
     * Initialisiert das MenuItem "Speichern"
     * 
     * @return Menu_itemSpeichern
     */
    protected JMenuItem erzeugeMenuItemSpeichern() {
	/**
	 * Öffnen Menüeintrag
	 */
	Menu_itemSpeichern = new JMenuItem(HackenbushConfig.getString("Hackenbush.menu.speichern"), new ImageIcon(this.getClass().getResource(
		HackenbushConfig.getString("Hackenbush.datei.spielspeichern"))));

	// Registrieren einer Tastenkombination
	Menu_itemSpeichern.setAccelerator(KeyStroke.getKeyStroke('S', Event.CTRL_MASK));

	Menu_itemSpeichern.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent event) {
		SpielSpeichernClick();
	    }
	});

	Menu_itemSpeichern.setEnabled(false);
	return Menu_itemSpeichern;
    }

    /**
     * Erzeugt das Hauptfenster
     */
    protected void erzeugeFenster() {
	showSplash();
	anzeige_panel.add(hackenbushGraphics, BorderLayout.CENTER);
	setContentPane(anzeige_panel);
	setVisible(true);
    }

    /**
     * @return Logger von Programm
     */
    public static Logger getLogger() {
	return logger;
    }

    /**
     * Macht die Schaltflaechen anklickbar oder graut diese aus, die mit einem
     * geladen Spiel zu tun haben.
     * 
     * @param loaded
     *            true wenn ein Spiel geladen ist, false wenn nicht
     */
    protected void setSpielGeladen(boolean loaded) {
	Menu_itemSpeichern.setEnabled(loaded);
	Menu_itemSpeichern.setEnabled(loaded);
	Menu_itemNeuesSpiel.setEnabled(!loaded);
	if (loaded) {
	    zoom.aktiviereButtons(hackenbushGraphics.getSpiel().getFigur());
	    hackenbushGraphics.setZoom(zoom);
	}
	repaintFigur();
	repaint();
    }

    /**
     * Zeichnet die Figur neu
     */
    public void repaintFigur() {
	hackenbushGraphics.repaint();
    }

    /**
     * Ändert den SplashScreen und fügt eine Fortschrittsanzeige hinzu. Führt
     * Initialisierungen durch.
     */
    protected void showSplash() {
	//Figur auf dem Splash-Screen stammt hierher: http://en.wikipedia.org/wiki/File:Hackenbush_girl.svg
	//Bild ist bearbeitet
	
	SplashScreen splashScreen = SplashScreen.getSplashScreen();
	if (splashScreen != null) {
	    getLogger().info("SplashScreen vorhanden");
	    Graphics2D graphics2d = splashScreen.createGraphics();
	    graphics2d.setComposite(AlphaComposite.Clear);
	    graphics2d.setPaintMode();

	    // Fortschrittsanzeige
	    try {
		for (int i = 0; i <= 10; i++) {
		    graphics2d.setColor(Color.BLACK);
		    graphics2d.fillRect(10, 100, 200, 20);
		    graphics2d.setColor(Color.ORANGE);
		    graphics2d.drawString("Initialisiere ...", 10, 130);
		    // Fortschrittsbalken zeichnen
		    graphics2d.fillRect(10, 100, 20 * i, 20);
		    // Aktualisieren des SplashScreens
		    splashScreen.update();
		    init(i);
		    Thread.sleep(100);
		}
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	} else {
	    getLogger().info("Kein SplashScreen vorhanden");
	    for (int i = 0; i <= 10; i++) {
		init(i);
	    }
	}
    }

    /**
     * Fuehrt die Initialisierungen durch
     * 
     * @param i
     *            Parameter zwischen 0 und 4, sonst geschieht nichts
     */
    protected void init(int i) {
	switch (i) {
	case 0:
	    getLogger().info("Titel gesetzt");
	    break;
	case 1:
	    setMinimumSize(new Dimension(500, 400));
	    getLogger().info("Groeße gesetzt");
	    break;
	case 2:
	    setLocationRelativeTo(null);
	    getLogger().info("Position gesetzt");
	    break;
	case 3:
	    // Setz neues Aussehen
	    try {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	    } catch (Exception e) {
		getLogger().log(Level.WARNING, "Look and Feel nicht gefunden: " + e.getMessage());
		e.printStackTrace();
	    }
	    getLogger().info("Aussehen gesetzt");
	    break;
	case 4:
	    break;
	default:
	    break;
	}
    }

    /**
     * Wird aufgerufen, wenn der entsprechende Knopf gedrueckt wird.
     */
    protected void SpielOeffnenClick() {
	if (hackenbushGraphics.getSpiel() != null) {
	    int returnstatus = JOptionPane.showConfirmDialog(HackenbushFenster.this, HackenbushConfig.getString("Hackenbush.spiel.oeffnen.nachfrage"),
		    HackenbushConfig.getString("Hackenbush.spiel.oeffnen.nachfrage.titel"), JOptionPane.OK_CANCEL_OPTION);
	    if (returnstatus != JOptionPane.OK_OPTION) {
		return;
	    }
	}

	JFileChooser dateiAuswahl = new JFileChooser(HackenbushConfig.getString("Hackenbush.spiel.oeffnen.defaultspeicherpfad"));
	dateiAuswahl.setFileFilter(new HackenbushFilter());
	dateiAuswahl.setAcceptAllFileFilterUsed(false);
	int status = dateiAuswahl.showOpenDialog(HackenbushFenster.this);
	if (status == JFileChooser.APPROVE_OPTION) {
	    File datei = dateiAuswahl.getSelectedFile();
	    try {
		setSpielGeladen(false);
		hackenbushGraphics.setSpiel((Spiel) SpielManagementTools.load(datei.getAbsolutePath()));
		if (hackenbushGraphics.getSpiel() != null && hackenbushGraphics.getSpiel().getFigur() != null) {
		    JOptionPane.showMessageDialog(HackenbushFenster.this, HackenbushConfig.getString("Hackenbush.spiel.oeffnen.erfolgreich"),
			    HackenbushConfig.getString("Hackenbush.spiel.oeffnen.erfolgreich"), JOptionPane.INFORMATION_MESSAGE);
		    setTitle("Hackenbush - " + hackenbushGraphics.getSpiel().getName());
		    setSpielGeladen(true);
		    int[] kanten = getSpiel().getFigur().getKantenAnzahl();
		    HauptFenster.getLogger().log(Level.INFO, "Kanten Spieler1: " + kanten[0] + " Kanten Spieler1: " + kanten[1]);
		} else {
		    JOptionPane.showMessageDialog(HackenbushFenster.this, HackenbushConfig.getString("Hackenbush.spiel.oeffnen.fehlgeschlagen"),
			    HackenbushConfig.getString("Hackenbush.spiel.oeffnen.fehlgeschlagen.titel"), JOptionPane.ERROR_MESSAGE);
		}
	    } catch (Exception ex) {
		JOptionPane.showMessageDialog(HackenbushFenster.this, HackenbushConfig.getString("Hackenbush.spiel.oeffnen.fehlgeschlagen") + ex.getMessage(),
			HackenbushConfig.getString("Hackenbush.spiel.oeffnen.fehlgeschlagen.titel"), JOptionPane.ERROR_MESSAGE);
		ex.printStackTrace();
	    }
	}
    }

    /**
     * Wird aufgerufen, wenn der entsprechende Knopf gedrueckt wird.
     * 
     * @return Runnable, die ausgeführt werden soll, wenn das Berechnen der
     *         Werte vor dem Speichern abgeschlossen ist
     */
    protected Runnable SpielSpeichernClick() {
	if (hackenbushGraphics.getSpiel() == null) {
	    JOptionPane.showMessageDialog(HackenbushFenster.this, HackenbushConfig.getString("Hackenbush.spiel.speichern.1"), HackenbushConfig.getString("Hackenbush.spiel.speichern.1"),
		    JOptionPane.ERROR_MESSAGE);
	    return null;
	}
	final Runnable r = new Runnable() {

	    @Override
	    public void run() {
		JFileChooser dateiAuswahl = new JFileChooser(".");
		dateiAuswahl.setFileFilter(new HackenbushFilter());
		dateiAuswahl.setAcceptAllFileFilterUsed(false);
		try {
		    dateiAuswahl.setSelectedFile(new File(new File(hackenbushGraphics.getSpiel().getName() + ".hackenbush").getCanonicalPath()));
		} catch (IOException e1) {
		    e1.printStackTrace();
		}
		int status = dateiAuswahl.showSaveDialog(HackenbushFenster.this);
		if (status == JFileChooser.APPROVE_OPTION) {
		    File datei = dateiAuswahl.getSelectedFile();
		    handleSpeichernClick(datei);
		}
	    }
	};
	return r;

    }

    /**
     * Wird aufgerufen, wenn der entsprechende Knopf gedrueckt wird.
     */
    protected void handleSpeichernClick(File datei) {
	try {
	    if (datei.exists()) {
		int returnstatus = JOptionPane.showConfirmDialog(HackenbushFenster.this, HackenbushConfig.getString("Hackenbush.spiel.speichern.abfrage.ueberspeichern"),
			HackenbushConfig.getString("Hackenbush.spiel.speichern.abfrage.ueberspeicherntitel"), JOptionPane.OK_CANCEL_OPTION);
		if (returnstatus == JOptionPane.OK_OPTION) {
		    datei.delete();
		    try {
			SpielManagementTools.speichern(hackenbushGraphics.getSpiel(), datei.getAbsolutePath());
			JOptionPane.showMessageDialog(HackenbushFenster.this, HackenbushConfig.getString("Hackenbush.spiel.speichern.erfolgreich"),
				HackenbushConfig.getString("Hackenbush.spiel.speichern.erfolgreich.titel"), JOptionPane.INFORMATION_MESSAGE);
		    } catch (Exception e) {
			JOptionPane.showMessageDialog(HackenbushFenster.this, HackenbushConfig.getString("Hackenbush.spiel.speichern.fehlgeschlagen") + e.getMessage(),
				HackenbushConfig.getString("Hackenbush.spiel.speichern.fehlgeschlagen.titel"), JOptionPane.ERROR_MESSAGE);
		    }
		}
	    } else {
		try {
		    SpielManagementTools.speichern(hackenbushGraphics.getSpiel(), datei.getAbsolutePath());
		    JOptionPane.showMessageDialog(HackenbushFenster.this, HackenbushConfig.getString("Hackenbush.spiel.speichern.erfolgreich"),
			    HackenbushConfig.getString("Hackenbush.spiel.speichern.erfolgreich.titel"), JOptionPane.INFORMATION_MESSAGE);
		} catch (Exception e) {
		    JOptionPane.showMessageDialog(HackenbushFenster.this, HackenbushConfig.getString("Hackenbush.spiel.speichern.fehlgeschlagen") + e.getMessage(),
			    HackenbushConfig.getString("Hackenbush.spiel.speichern.fehlgeschlagen.titel"), JOptionPane.ERROR_MESSAGE);
		}
	    }
	} catch (Exception ex) {
	    JOptionPane.showMessageDialog(HackenbushFenster.this, HackenbushConfig.getString("Hackenbush.spiel.speichern.fehlgeschlagen") + ex.getMessage(),
		    HackenbushConfig.getString("Hackenbush.spiel.speichern.fehlgeschlagen.titel"), JOptionPane.ERROR_MESSAGE);
	    ex.printStackTrace();
	}
    }
}
