package com.simon.huber.hackenbush.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.logging.Level;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.simon.huber.hackenbush.HackenbushFenster;
import com.simon.huber.hackenbush.HauptFenster;
import com.simon.huber.hackenbush.Spiel;
import com.simon.huber.hackenbush.actionlisteners.HackenbushMouseAdapter;
import com.simon.huber.hackenbush.create.HackenbushEditor;
import com.simon.huber.hackenbush.dataobjects.Punkt;
import com.simon.huber.hackenbush.dataobjects.hilfsobjekte.HilfsKante;
import com.simon.huber.hackenbush.exceptions.EinfachheitsRegelException;
import com.simon.huber.hackenbush.exceptions.KanteNichtGefundenException;
import com.simon.huber.hackenbush.exceptions.PunktNichtGefundenException;
import com.simon.huber.hackenbush.exceptions.UnvollstandigeBerechnungException;
import com.simon.huber.hackenbush.resources.Settings;
import com.simon.huber.hackenbush.tools.HackenbushConfig;

/**
 * Führt den Einfügevorgang für eine Kante mit Verbindungspunkt
 * 
 * @author Simon Huber
 * 
 */
public class HackenbushGraphics extends JPanel implements KeyListener {

    private static final long serialVersionUID = 1L;

    /**
     * Genauigkeit zur Kantenerkennung
     */
    private double genauigkeit = 0.5;

    /**
     * Wird zur Erkennung einer Veränderung und Zurücksetzen des Zoom benötigt
     */
    private int lastFigurHoehe = 0;

    /**
     * Makierte Kanten werden in Hellblau oder Orange dargestellt
     */
    private HilfsKante lastMarkedEdge = null;;

    /**
     * Angeklickte Kanten werden Grün dargestellt
     */
    private HilfsKante lastClickedEdge = null;

    /**
     * Aussehen der Linien
     */
    private BasicStroke bstroke = new BasicStroke(1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);

    /**
     * Speicherort der letzten Mausposition
     */
    private double[] lastMousePoint = null;

    /**
     * Ermöglicht das Einfügen von Kanten
     */
    private boolean editMode = false;

    /**
     * MausListener
     */
    private HackenbushMouseAdapter mouseAdapter = new HackenbushMouseAdapter(this);

    /**
     * Geladenes Spiel samt Figur
     */
    private Spiel spiel = null;

    /**
     * Toolbar
     */
    private ZoomToolbar zoom = null;

    /**
     * Werte-Anzeige
     */
    private WerteAnzeigePanel werte_panel = new WerteAnzeigePanel();

    /**
     * @param werte_panel
     *            the werte_panel to set
     */
    public void setWerte_panel(WerteAnzeigePanel werte_panel) {
	this.werte_panel = werte_panel;
    }

    /**
     * @return the werte_panel
     */
    public WerteAnzeigePanel getWerte_panel() {
	return werte_panel;
    }

    private HackenbushFenster frame;

    /**
     * @return the frame
     */
    public HackenbushFenster getFrame() {
	return frame;
    }

    public HackenbushGraphics(HackenbushFenster frame) {
	this.frame = frame;
	// Hinzufügen von Listenern
	addKeyListener(this);
	addMouseListener(mouseAdapter);
	addMouseMotionListener(mouseAdapter);
    }

    public HackenbushMouseAdapter getMouseAdapter() {
	return mouseAdapter;
    }

    /**
     * @return the lastMousePoint
     */
    public double[] getLastMousePoint() {
	return lastMousePoint;
    }

    /**
     * @param lastMousePoint
     *            the lastMousePoint to set
     */
    public void setLastMousePoint(double[] lastMousePoint) {
	this.lastMousePoint = lastMousePoint;
    }

    /**
     * @return the lastMarkedEdge
     */
    public HilfsKante getLastMarkedEdge() {
	return lastMarkedEdge;
    }

    /**
     * @param lastMarkedEdge
     *            the lastMarkedEdge to set
     */
    public void setLastMarkedEdge(HilfsKante lastMarkedEdge) {
	this.lastMarkedEdge = lastMarkedEdge;
    }

    /**
     * @return the lastClickedEdge
     */
    public HilfsKante getLastClickedEdge() {
	return lastClickedEdge;
    }

    /**
     * @param lastClickedEdge
     *            the lastClickedEdge to set
     */
    public void setLastClickedEdge(HilfsKante lastClickedEdge) {
	this.lastClickedEdge = lastClickedEdge;
	if (lastClickedEdge != null) {
	    getWerte_panel().setKantenwert(getSpiel().getFigur().getKantenwert(lastClickedEdge));
	} else {
	    getWerte_panel().setKantenwert(null);
	}
	try {
	    getWerte_panel().setSpielwert(getSpiel().getFigur().getSpielWert());
	} catch (EinfachheitsRegelException | UnvollstandigeBerechnungException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    public void setSpiel(Spiel spiel) {
	this.spiel = spiel;
    }

    public Spiel getSpiel() {
	return spiel;
    }

    public void setZoom(ZoomToolbar zoom) {
	this.zoom = zoom;
    }

    /**
     * Zeichnet die komplette Hackenbush-Position
     */
    @Override
    protected void paintComponent(Graphics g) {
	super.paintComponent(g);
	// Wird bei jedem repaint() aufgerufen
	markEdges(g);
    }

    private void markEdges(Graphics g) {
	if (spiel == null)
	    return;

	// Überprüfen des Zooms
	if (lastFigurHoehe != getSpiel().getFigur().getBreitehoehe().height) {
	    getSpiel().getFigur().setEditHigh(1);
	    if (zoom != null) {
		zoom.setZoomText(getSpiel().getFigur().getEditHigh());
	    }
	    lastFigurHoehe = getSpiel().getFigur().getBreitehoehe().height;
	}

	// Vorbereiten des Graphics2D Opjekts
	Graphics2D g2 = (Graphics2D) g;
	// Scaliert Zeichenfläche entsprechend der Fenstergröße und der Figur
	double scale = getScale();
	g2.scale(scale, scale);
	g2.translate(getVerschiebungMitte(), 0);
	// Zeichnen der Grundlinie
	g2.setColor(Color.GRAY);
	g2.setStroke(bstroke);
	// Zeichne Grundlinie
	g2.drawLine(0, getZeichenKoordinatenY(0), spiel.getFigur().getBreitehoehe().width + 2, getZeichenKoordinatenY(0));

	// Holt sich alle zu zeichnenden Kanten
	ArrayList<HilfsKante> kanten = spiel.getFigur().getZuZeichnendeKanten();

	// Wird aus Zeitgründen außerhalb der Schleife initialisiert und in der
	// Schleife bloß resettet
	boolean drawed = false;
	for (HilfsKante kante : kanten) {
	    if (kante == null) {
		continue;
	    }
	    if (!drawed) {
		drawed = checkDrawingClicked(g2, kante);
	    }
	    if (!drawed) {
		drawed = checkDrawingMarked(g2, kante);
	    }
	    if (!drawed) {
		drawNormalEdges(g2, kante);
	    } else {
		// Reset
		drawed = false;
	    }
	    // Schwarz für die Punkte
	    g2.setColor(Color.BLACK);

	    // Zeichnen der Punkte
	    for (Punkt p : kante.getPunkt()) {
		drawPunkt(g2, p);
	    }
	}

	// Zeichnen der Punkte, die beim Einfügen von neuen Kanten angzeigt
	// werden
	drawHilfsPunkte(g2);
    }

    /**
     * Zeichnet Punkte, die bereits beim Zeichenprozess makiert wurden
     * 
     * @param g2
     *            Graphics2D element (wird zum zeichnen benötigt)
     */
    private void drawHilfsPunkte(Graphics2D g2) {
	g2.setColor(Color.GREEN);
	KanteEinfuegen einf = mouseAdapter.getEinfuegen();
	if (einf != null) {
	    for (Punkt p : einf.getPunkte()) {
		if (p != null) {
		    drawPunkt(g2, p);
		}
	    }
	}
    }

    /**
     * setzt die richtige Farbe für die Kante
     * 
     * @param g2
     *            Graphics2D element (wird zum zeichnen benötigt)
     * @param kante
     *            Kante die überprüft werden soll, welche Farbe sie haben soll
     */
    private void drawNormalEdges(Graphics2D g2, HilfsKante kante) {
	// Wählen der Farbe
	if (kante.getKante().getSpieler() == 1) {
	    g2.setColor(Color.BLUE);
	} else if (kante.getKante().getSpieler() == 2) {
	    g2.setColor(Color.RED);
	} else {
	    return;
	}
	drawKante(g2, kante);
    }

    /**
     * Zeichnet eine Kante, wenn sie angeklickt ist
     * 
     * @param g2
     *            Graphics2D element (wird zum zeichnen benötigt)
     * @param kante
     *            Kante die überprüft werden soll, ob sie angeklickt ist
     * @return true, wenn sie gezeichnet wurde, false wenn nicht
     */
    private boolean checkDrawingClicked(Graphics2D g2, HilfsKante kante) {
	if (lastClickedEdge != null) {
	    for (int i = 0; i < 2; i++) {
		if (lastClickedEdge.equals(kante.punkteUmdrehen())) {
		    // Wählen der Farbe
		    if (kante.getKante().getSpieler() == 0) {
			continue;
		    }
		    g2.setColor(Color.GREEN);
		    drawKante(g2, kante);
		    return true;
		}
	    }
	}
	return false;
    }

    /**
     * Zeichnet eine Kante, wenn sie makiert ist
     * 
     * @param g2
     *            Graphics2D element (wird zum zeichnen benötigt)
     * @param kante
     *            Kante die überprüft werden soll, ob sie makiert ist
     * @return true, wenn sie gezeichnet wurde, false wenn nicht
     */
    private boolean checkDrawingMarked(Graphics2D g2, HilfsKante kante) {
	if (lastMarkedEdge != null) {
	    for (int i = 0; i < 2; i++) {
		if (lastMarkedEdge.equals(kante.punkteUmdrehen())) {
		    // Wählen der Farbe
		    if (kante.getKante().getSpieler() == 1) {
			g2.setColor(new Color(065, 105, 225));
		    } else if (kante.getKante().getSpieler() == 2) {
			g2.setColor(Color.ORANGE);
		    } else {
			continue;
		    }
		    drawKante(g2, kante);
		    return true;
		}
	    }
	}
	return false;
    }

    /**
     * Zeichnet eine Kante
     * 
     * @param g2
     *            Graphics2D element (wird zum zeichnen benötigt)
     * @param kante
     *            Kante die gezeichnet werden soll
     */
    private void drawKante(Graphics2D g2, HilfsKante kante) {
	if (kante.getKante().getVerbindungspunkt() == null) {
	    // Zeichnen
	    g2.drawLine(kante.getPunkt()[0].x, getZeichenKoordinatenY(kante.getPunkt()[0].y), kante.getPunkt()[1].x, getZeichenKoordinatenY(kante.getPunkt()[1].y));
	} else {
	    g2.drawLine(kante.getPunkt()[0].x, getZeichenKoordinatenY(kante.getPunkt()[0].y), kante.getKante().getVerbindungspunkt().x,
		    getZeichenKoordinatenY(kante.getKante().getVerbindungspunkt().y));
	    g2.drawLine(kante.getPunkt()[1].x, getZeichenKoordinatenY(kante.getPunkt()[1].y), kante.getKante().getVerbindungspunkt().x,
		    getZeichenKoordinatenY(kante.getKante().getVerbindungspunkt().y));
	}
    }

    /**
     * Zeichnet einen Punkt
     * 
     * @param g2
     *            Graphics2D element (wird zum zeichnen benötigt)
     * @param p
     *            Punkt der gezeichnet werden soll
     */
    private void drawPunkt(Graphics2D g2, Punkt p) {
	g2.drawLine(p.x, getZeichenKoordinatenY(p.y), p.x, getZeichenKoordinatenY(p.y));
    }

    /**
     * Liefert die benötigte Skalierung zurück, die benötigt wird, um die
     * komplette Figur anzeigen zu können
     * 
     * @return Skalierung
     */
    private double getScale() {
	if (spiel.getFigur().getBreitehoehe().width >= spiel.getFigur().getBreitehoehe().height + spiel.getFigur().getEditHigh()) {
	    return this.getSize().getWidth() / (spiel.getFigur().getBreitehoehe().width + 1);
	} else {
	    return this.getSize().getHeight() / (spiel.getFigur().getBreitehoehe().height + spiel.getFigur().getEditHigh() + 1);
	}
    }

    /**
     * Berechnet die Verschiebung in X-Richtung, damit die Figur in der Mitte
     * gezeichnet wird
     * 
     * @return Verschiebung in X-Richtung
     */
    private int getVerschiebungMitte() {
	int returnvalue = (int) (((this.getSize().width / getScale()) / 2) - (spiel.getFigur().getBreitehoehe().width / 2) - 1);
	// HauptFenster.getLogger().info("Verschiebung X: " + returnvalue);
	return returnvalue;
    }

    /**
     * Rechnet Y-Korrdinaten des Spiels in Koordinaten um, die gezeichnet werden
     * können
     * 
     * @param i
     *            Y-Wert
     * @return neuer Y-Wert
     */
    private int getZeichenKoordinatenY(int i) {
	return (getSpiel().getFigur().getBreitehoehe().height + getSpiel().getFigur().getEditHigh()) - i;
    }

    /**
     * Rechnet Y-Korrdinaten des Fensters in Koordinaten um, die das Spiel
     * benötigt
     * 
     * @param i
     *            Y-Wert
     * @return neuer Y-Wert
     */
    private int getSpielKoordinatenY(int i) {
	return (getSpiel().getFigur().getBreitehoehe().height + getSpiel().getFigur().getEditHigh()) - i;
    }

    /**
     * Rechnet Y-Korrdinaten des Fensters in Koordinaten um, die das Spiel
     * benötigt <br>
     * return: neuer Y-Wert (geänderter Wert des übergeben Objektes)
     * 
     * @param pos
     *            X- und Y-Koordinate des Punktes
     */
    private void getSpielKoordinatenY(double[] pos) {
	pos[1] = getSpielKoordinatenY((int) pos[1]);
    }

    /**
     * Korrigiert die Mausposition. Dies wird durch die Verschiebung der Figur
     * in X-Richtung und wegen der Skalierung benötigt
     * 
     * @param orig
     *            Mausposition als double-Array
     * @return neuer Mausposition als double-Array
     */
    public double[] correctMousePosition(double[] orig) {
	double scale = getScale();
	double[] returnvalue = new double[2];
	returnvalue[0] = (orig[0] / scale) - getVerschiebungMitte();
	returnvalue[1] = (orig[1] / scale);
	getSpielKoordinatenY(returnvalue);
	return returnvalue;
    }

    public boolean isEditMode() {
	return editMode;
    }

    public void setEditMode(boolean editMode) {
	this.editMode = editMode;
    }

    public double getGenauigkeit() {
	return genauigkeit;
    }

    public void setGenauigkeit(double genauigkeit) {
	this.genauigkeit = genauigkeit;
    }

    @Override
    public void keyPressed(KeyEvent e) {
	if (getSpiel() == null) {
	    return;
	}
	HauptFenster.getLogger().info("KeyPressed: " + e.getKeyChar());
	if (e.getKeyCode() == KeyEvent.VK_DELETE && editMode) {
	    handleDelete();
	} else if (e.getKeyCode() == KeyEvent.VK_DELETE) {
	    handleEntfernen();
	} else if (e.getKeyCode() == KeyEvent.VK_N) {
	    Runnable r = new Runnable() {

		@Override
		public void run() {
		    try {
			getWerte_panel().setSpielwert(getSpiel().getFigur().getSpielWert());
		    } catch (EinfachheitsRegelException | UnvollstandigeBerechnungException e) {
			e.printStackTrace();
		    }
		    HackenbushFenster.getLogger().log(Level.INFO, "ArraySize after Calculation: " + getSpiel().getFigur().getOptions().size());
		}
	    };
	    Settings temp = null;
	    if (frame instanceof HauptFenster) {
		HauptFenster hauptFenster = (HauptFenster) frame;
		temp = hauptFenster.getSettings();
	    } else if (frame instanceof HackenbushEditor) {
		HackenbushEditor editor = (HackenbushEditor) frame;
		temp = editor.getHauptFenster().getSettings();
	    }
	    getSpiel().getFigur().starteWerteBerechnung(temp, r, true);

	}
    }

    /**
     * Wird vom Editor beim Drücken der ENTF-Taste aufgerufen
     */
    private void handleEntfernen() {
	if (getLastClickedEdge() != null && getSpiel().getAmZug() != -1) {
	    int returnstatus = JOptionPane.showConfirmDialog(HackenbushGraphics.this, HackenbushConfig.getString("Hackenbush.spiel.entfernen.abfrage"),
		    HackenbushConfig.getString("Hackenbush.spiel.entfernen.abfrage.titel"), JOptionPane.OK_CANCEL_OPTION);
	    if (returnstatus == JOptionPane.OK_OPTION) {
		getSpiel().entferneKante(this, getLastClickedEdge());
		setLastClickedEdge(null);
		repaint();
	    }
	}
    }

    /**
     * Wird vom Hauptfenster beim Drücken der ENTF-Taste aufgerufen
     */
    private void handleDelete() {
	if (getLastClickedEdge() != null) {
	    int returnstatus = JOptionPane.showConfirmDialog(HackenbushGraphics.this, HackenbushConfig.getString("Hackenbush.editor.entfernen.abfrage"),
		    HackenbushConfig.getString("Hackenbush.editor.entfernen.abfrage.titel"), JOptionPane.OK_CANCEL_OPTION);
	    if (returnstatus == JOptionPane.OK_OPTION) {
		try {
		    getSpiel().getFigur().loescheKante(getLastClickedEdge());
		} catch (PunktNichtGefundenException | KanteNichtGefundenException e1) {
		    JOptionPane.showMessageDialog(HackenbushGraphics.this, "Entfernen der Kante fehlgeschlagen: " + e1.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
		    e1.printStackTrace();
		}
		setLastClickedEdge(null);
		repaint();
	    }
	}
    }

    @Override
    public void keyReleased(KeyEvent arg0) {
	// TODO Auto-generated method stub

    }

    @Override
    public void keyTyped(KeyEvent arg0) {
	// TODO Auto-generated method stub

    }

}
