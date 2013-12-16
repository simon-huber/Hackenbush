package com.simon.huber.hackenbush;

import java.awt.BorderLayout;
import java.awt.Event;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;

import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import com.simon.huber.hackenbush.create.HackenbushEditor;
import com.simon.huber.hackenbush.gui.info.EditorInfo;
import com.simon.huber.hackenbush.gui.info.HauptfensterInfo;
import com.simon.huber.hackenbush.gui.info.InfoMenu;
import com.simon.huber.hackenbush.menu.EinstellungenMenu;
import com.simon.huber.hackenbush.menu.SpieleMenu;
import com.simon.huber.hackenbush.resources.Settings;
import com.simon.huber.hackenbush.tools.HackenbushConfig;
import com.simon.huber.hackenbush.tools.ObjektManager;

public class HauptFenster extends HackenbushFenster {

    /**
     * Dieses Programm dient zur Lösung von Hackenbush durch eine KI. Wie dieser
     * Code zustande kommt, beschreibt die dazugehörige Arbeit genauer.
     */

    /**
     * wird von JFrame gefordert
     */
    private static final long serialVersionUID = 1160917263822092616L;

    /**
     * Start Methode (wird beim Programmstart aufgerufen)
     * 
     * @param args
     */
    public static void main(String[] args) {
	try {
	    // Erzeugt neues Objekt
	    final HauptFenster dialog = new HauptFenster();
	    // Registriert neuen Listener für das schließen des Programms
	    dialog.addWindowListener(new java.awt.event.WindowAdapter() {
		@Override
		public void windowClosing(java.awt.event.WindowEvent e) {
		    dialog.beenden();
		}
	    });
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    /**
     * Der Editor
     */
    private HackenbushEditor editor = null;

    /**
     * Settings
     */
    private Settings settings;

    /**
     * Menü zum Starten des Spiels
     */
    private SpieleMenu spielMenu;

    /**
     * @return the spielMenu
     */
    public SpieleMenu getSpielMenu() {
	return spielMenu;
    }

    /**
     * Konstruktor des Hauptfensters
     */
    public HauptFenster() {
	super();
	if (settings.isShowHauptfensterInfo()) {
	    new HauptfensterInfo(settings, this);
	}
    }

    /**
     * Wird aufgerufen, wenn das Programm beendet wird.
     */
    @Override
    public void beenden() {
	super.beenden();
	try {
	    if (editor != null) {
		if (editor.getSpiel() != null) {
		    editor.beenden();
		}
	    }
	    File file = new File("./data/");
	    file.mkdirs();
	    ObjektManager.save(settings, "./data/hackenbush.settings");
	    HauptFenster.this.dispose();
	    System.exit(0);
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    /**
     * Erzeugt und fuegt Menue zum Programm hinzu.
     * 
     * @return zusammengesetztes Menü
     */
    protected JMenuBar erzeugeMenu() {
	// Initialisiern des Menüs
	JMenuBar MenuBar = new JMenuBar();
	JMenu Menu = new JMenu(HackenbushConfig.getString("Hackenbush.menu.datei.name"));
	try {

	    /**
	     * Zusammenstellen des "Datei" Menüs
	     */
	    Menu.add(erzeugeMenuItemNeuesSpiel());
	    Menu.addSeparator();
	    Menu.add(erzeugeMenuItemOeffnen());
	    Menu.add(erzeugeMenuItemSpeichern());
	    Menu.addSeparator();
	    Menu.add(erzeugeMenuItemBeenden());
	    MenuBar.add(Menu);

	    // Einfügen des Spielemenüs
	    MenuBar.add(spielMenu = new SpieleMenu(this));

	    // Einfügen des Einstellungsmenüs
	    MenuBar.add(einstellungen = new EinstellungenMenu(settings));

	    // Einfügen des Infomenüs
	    MenuBar.add(new InfoMenu());

	    // Hinzufügen der ToolBar zur ContentPane
	    JPanel toolbar = new JPanel();
	    toolbar.setLayout(new BorderLayout());
	    toolbar.add(zoom, BorderLayout.CENTER);
	    toolbar.add(hackenbushGraphics.getWerte_panel(), BorderLayout.SOUTH);
	    anzeige_panel.add(toolbar, BorderLayout.BEFORE_FIRST_LINE);

	} catch (Exception e) {
	    e.printStackTrace();
	}
	return MenuBar;
    }

    /**
     * Initialisiert das MenuItem "Neues Spiel"
     * 
     * @return Menu_itemSpeichern
     */
    private JMenuItem erzeugeMenuItemNeuesSpiel() {
	/**
	 * Neues Spiel Menüeintrag
	 */
	Menu_itemNeuesSpiel = new JMenuItem(HackenbushConfig.getString("Hackenbush.menu.datei.neuesspiel"), new ImageIcon(this.getClass().getResource(
		HackenbushConfig.getString("Hackenbush.datei.neuesspiel"))));

	// Registrieren einer Tastenkombination
	Menu_itemNeuesSpiel.setAccelerator(KeyStroke.getKeyStroke('N', Event.CTRL_MASK));

	Menu_itemNeuesSpiel.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent event) {
		editor = new HackenbushEditor(HauptFenster.this);
		setEditorActivated(false);
	    }
	});
	return Menu_itemNeuesSpiel;
    }

    /**
     * Muss beim Erzeugen und schliesen eines Editors aufgerufen werden.
     * Auswirkungen: Deaktiviert und Aktiviert die Moeglichkeit einen neuen
     * Editor zu oeffnen
     * 
     * @param isActivated
     */
    public void setEditorActivated(boolean isActivated) {
	Menu_itemNeuesSpiel.setEnabled(isActivated);
    }

    @Override
    protected void setSpielGeladen(boolean loaded) {
	super.setSpielGeladen(loaded);
	spielMenu.setEnabled(loaded);
	if (loaded) {
	    hackenbushGraphics.getSpiel().setSettings(settings);
	}
	// Spiel_oeffnen.setEnabled(loaded);
	// Spiel_speichern.setEnabled(loaded);
    }

    /**
     * @return the editor
     */
    public HackenbushEditor getEditor() {
	return editor;
    }

    /**
     * Fuehrt die Initialisierungen durch
     * 
     * @param i
     *            Parameter zwischen 0 und 4, sonst geschieht nichts
     */
    @Override
    protected void init(int i) {
	super.init(i);
	if (i == 0) {
	    getSettings();
	}
	if (i == 4) {
	    setTitle(HackenbushConfig.getString("Hackenbush.titel.default"));
	    setJMenuBar(erzeugeMenu());
	    getLogger().info("Menu gesetzt");
	}
    }
    
    public void rebuildEinstellungsMenu() {
	einstellungen.getShowHauptfensterInfoItem().setSelected(settings.isShowHauptfensterInfo());
	einstellungen.getShowEditorInfoItem().setSelected(settings.isShowEditorInfo());
    }


    /**
     * Lädt und gibt Settings zurück
     * 
     * @return Settings
     */
    public Settings getSettings() {
	if (settings == null) {
	    try {
		settings = (Settings) ObjektManager.load("./data/hackenbush.settings");
		getLogger().info("Einstellungen geladen");
	    } catch (ClassNotFoundException | IOException e2) {
		e2.printStackTrace();
	    }
	}
	if (settings == null) {
	    settings = new Settings();
	    getLogger().info("Default Einstellungen gesetzt");
	}
	return settings;
    }

    @Override
    protected Runnable SpielSpeichernClick() {
	final Runnable r = super.SpielSpeichernClick();
	Thread t = new Thread(new Runnable() {

	    @Override
	    public void run() {
		hackenbushGraphics.getSpiel().getFigur().starteWerteBerechnung(settings, r);
	    }
	});
	t.start();
	return r;
    }

}
