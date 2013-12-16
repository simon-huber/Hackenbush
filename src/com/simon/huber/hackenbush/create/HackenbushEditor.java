package com.simon.huber.hackenbush.create;

import java.awt.BorderLayout;
import java.awt.Event;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import com.simon.huber.hackenbush.HackenbushFenster;
import com.simon.huber.hackenbush.HauptFenster;
import com.simon.huber.hackenbush.Spiel;
import com.simon.huber.hackenbush.create.HackenbushEditor;
import com.simon.huber.hackenbush.exceptions.NameNotDefinedException;
import com.simon.huber.hackenbush.exceptions.PunktNichtGefundenException;
import com.simon.huber.hackenbush.gui.KantenToolbar;
import com.simon.huber.hackenbush.gui.info.EditorInfo;
import com.simon.huber.hackenbush.gui.info.InfoMenu;
import com.simon.huber.hackenbush.tools.HackenbushConfig;

/**
 * Editorfenster, das das Bearbeiten einer Figur erlaubt
 * @author Simon Huber
 */
public class HackenbushEditor extends HackenbushFenster {

    private static final long serialVersionUID = 1L;

    /**
     * Referenz auf das Hauptfenster (wird im Konstruktor initialisiert)
     */
    private HauptFenster hauptFenster;

    /**
     * @return the hauptFenster
     */
    public HauptFenster getHauptFenster() {
	return hauptFenster;
    }

    /**
     * Toolbar zum Wählen des Kantentyps
     */
    protected KantenToolbar kantentoolbar = null;

    /**
     * Konstruktor des Hauptfensters
     */
    public HackenbushEditor(HauptFenster hauptFenster) {
	super();
	kantentoolbar = new KantenToolbar(hackenbushGraphics);
	setJMenuBar(erzeugeMenu());
	getLogger().info("Menu gesetzt");
	this.hauptFenster = hauptFenster;
	hackenbushGraphics.setEditMode(true);
	addWindowListener(new java.awt.event.WindowAdapter() {
	    @Override
	    public void windowClosing(java.awt.event.WindowEvent e) {
		beenden();
	    }
	});
	if (hauptFenster.getSettings().isShowEditorInfo()) {
	    new EditorInfo(hauptFenster.getSettings(), this);
	}
    }
    
    public void rebuildEinstellungsMenu() {
	setJMenuBar(erzeugeMenu());
    }

    /**
     * Erzeugt und fuegt Menue zum Programm hinzu.
     * 
     * @return zusammengesetztes Menue
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

	    // Einfügen des Infomenüs
	    MenuBar.add(new InfoMenu());

	    JPanel toolbar = new JPanel();
	    toolbar.setLayout(new BorderLayout());
	    toolbar.add(kantentoolbar, BorderLayout.NORTH);
	    toolbar.add(zoom, BorderLayout.CENTER);
	    toolbar.add(hackenbushGraphics.getWerte_panel(), BorderLayout.SOUTH);
	    anzeige_panel.add(toolbar, BorderLayout.BEFORE_FIRST_LINE);

	} catch (Exception e) {
	    e.printStackTrace();
	}
	return MenuBar;
    }

    @Override
    public void beenden() {
	super.beenden();
	hauptFenster.setEditorActivated(false);
	hauptFenster.requestFocus();
	HackenbushEditor.this.dispose();
    }

    @Override
    protected void init(int i) {
	if (i == 0) {
	    setTitle(HackenbushConfig.getString("Hackenbush.titel.editor"));
	}
	super.init(i);
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
	Menu_itemNeuesSpiel = new JMenuItem("Neues Spiel", new ImageIcon(this.getClass().getResource(
		HackenbushConfig.getString("Hackenbush.datei.neuesspiel"))));

	// Registrieren einer Tastenkombination
	Menu_itemNeuesSpiel.setAccelerator(KeyStroke.getKeyStroke('N', Event.CTRL_MASK));

	Menu_itemNeuesSpiel.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent event) {
		try {
		    hackenbushGraphics.setSpiel(new Spiel(getSpielNameperPopup(), 25));
		} catch (PunktNichtGefundenException e) {
		    JOptionPane.showMessageDialog(HackenbushEditor.this, e.getMessage(), HackenbushConfig.getString("Hackenbush.spiel.neu.fehlgeschlagen"), JOptionPane.ERROR_MESSAGE);
		    return;
		} catch (NameNotDefinedException e) {
		    JOptionPane.showMessageDialog(HackenbushEditor.this, e.getMessage(), HackenbushConfig.getString("Hackenbush.spiel.neu.fehlgeschlagen"), JOptionPane.ERROR_MESSAGE);
		    return;
		}
		setSpielGeladen(true);
		setTitle("Hackenbush - " + hackenbushGraphics.getSpiel().getName());
		JOptionPane.showMessageDialog(HackenbushEditor.this, HackenbushConfig.getString("Hackenbush.spiel.neu.erfolgreich"), HackenbushConfig.getString("Hackenbush.spiel.neu.erfolgreich"),
			JOptionPane.INFORMATION_MESSAGE);
	    }
	});
	return Menu_itemNeuesSpiel;
    }

    private String getSpielNameperPopup() throws NameNotDefinedException {
	try {
	    String result = JOptionPane.showInputDialog(HackenbushEditor.this, HackenbushConfig.getString("Hackenbush.spiel.neu.spielname")).toString();
	    HauptFenster.getLogger().log(Level.INFO, "Spielname: " + result);
	    if (!result.isEmpty()) {
		return result;
	    }
	} catch (Exception e) {
	}
	throw new NameNotDefinedException();
    }

    /**
     * Macht die Schaltflaechen anklickbar oder graut diese aus, die mit einem
     * geladen Spiel zu tun haben.
     * 
     * @param loaded
     *            true wenn ein Spiel geladen ist, false wenn nicht
     */
    @Override
    protected void setSpielGeladen(boolean loaded) {
	super.setSpielGeladen(loaded);
	if (loaded) {
	    KantenToolbar.resetStartButton();
	    kantentoolbar.repaint();
	}
    }

    /**
     * Word überschrieben, damit das richtige Settings-Objekt übergeben wird
     */
    @Override
    protected Runnable SpielSpeichernClick() {
	final Runnable r = super.SpielSpeichernClick();
	Thread t = new Thread(new Runnable() {

	    @Override
	    public void run() {
		hackenbushGraphics.getSpiel().getFigur().starteWerteBerechnung(hauptFenster.getSettings(), r);
	    }
	});
	t.start();
	return r;
    }
}
