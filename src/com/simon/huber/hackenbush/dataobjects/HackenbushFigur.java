package com.simon.huber.hackenbush.dataobjects;

import java.awt.Dimension;
import java.awt.geom.Line2D;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;

import com.simon.huber.hackenbush.HackenbushFenster;
import com.simon.huber.hackenbush.calculation.HackenbushArbeitsThread;
import com.simon.huber.hackenbush.calculation.HackenbushSingleArbeitsThread;
import com.simon.huber.hackenbush.dataobjects.hilfsobjekte.HilfsKante;
import com.simon.huber.hackenbush.dataobjects.hilfsobjekte.HilfsVerbindung;
import com.simon.huber.hackenbush.exceptions.EinfachheitsRegelException;
import com.simon.huber.hackenbush.exceptions.KanteNichtGefundenException;
import com.simon.huber.hackenbush.exceptions.PunktDoppeltException;
import com.simon.huber.hackenbush.exceptions.PunktNichtGefundenException;
import com.simon.huber.hackenbush.exceptions.SpielNichtVorhandenException;
import com.simon.huber.hackenbush.exceptions.UnvollstandigeBerechnungException;
import com.simon.huber.hackenbush.gui.FortschrittsPanel;
import com.simon.huber.hackenbush.resources.Settings;
import com.simon.huber.hackenbush.tools.EinfachheitsRegel;

/**
 * Klasse die die Hackenbush-Figur darstellt und das Einfügen, Entfernen,
 * Berechnen von Kanten übernimmt
 * 
 * @author Simon Huber
 * 
 */
public class HackenbushFigur implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * Adjazenzmatrix als Array Feld
     */
    private Punkt[] punkte;
    private Verbindung[][] matrix;
    // Mindesthöhe, damit Zeichnen möglich ist
    private int editHigh = 1;
    private Dimension breitehoehe;

    /**
     * Hierdrin werden alle fertig berechneten "unterfiguren" gespeichert, um
     * doppelte Berechnungsvorgänge zu vermeiden.
     */
    private ArrayList<HackenbushFigur> options = new ArrayList<>();

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + Arrays.hashCode(matrix);
	result = prime * result + Arrays.hashCode(punkte);
	return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
	if (this == obj) {
	    return true;
	}
	if (obj == null) {
	    return false;
	}
	if (!(obj instanceof HackenbushFigur)) {
	    return false;
	}
	HackenbushFigur other = (HackenbushFigur) obj;
	if (!Arrays.deepEquals(matrix, other.matrix)) {
	    return false;
	}
	if (!Arrays.equals(punkte, other.punkte)) {
	    return false;
	}
	return true;
    }

    /**
     * Konstruktor
     */
    public HackenbushFigur() {
	punkte = new Punkt[0];
	matrix = new Verbindung[0][0];
	breitehoehe = berechneSpielBreiteundHoehe();
    }

    /**
     * Kunstruktor
     * 
     * @param hackenbushFigur
     *            Eine Hackenbush-Figur
     */
    public HackenbushFigur(HackenbushFigur hackenbushFigur) {
	breitehoehe = new Dimension();
	breitehoehe.height = hackenbushFigur.breitehoehe.height;
	breitehoehe.width = hackenbushFigur.breitehoehe.width;
	editHigh = hackenbushFigur.editHigh;
	punkte = new Punkt[hackenbushFigur.punkte.length];
	for (int i = 0; i < hackenbushFigur.punkte.length; i++) {
	    punkte[i] = new Punkt(hackenbushFigur.punkte[i]);
	}
	matrix = new Verbindung[hackenbushFigur.matrix.length][hackenbushFigur.matrix[0].length];
	for (int i = 0; i < hackenbushFigur.matrix.length; i++) {
	    for (int i2 = 0; i2 < hackenbushFigur.matrix.length; i2++) {
		if (hackenbushFigur.matrix[i][i2] == null || hackenbushFigur.matrix[i][i2].isEmpty()) {
		    continue;
		}
		matrix[i][i2] = new Verbindung(hackenbushFigur.matrix[i][i2]);
	    }
	}
    }

    /**
     * Berechnet die Maximale Größe, damit Spielfeld erstellt werden kann
     * 
     * @return Dimension, die die Figur hat
     * @throws SpielNichtVorhandenException
     */
    private Dimension berechneSpielBreiteundHoehe() {
	Dimension dim = new Dimension();
	for (Punkt p : getPunkte()) {
	    for (Punkt punkte_y : getPunkte()) {
		HilfsVerbindung ver = null;
		if ((ver = getVerbindung(p, punkte_y)) != null && ver.getVerbindung().aktiveKanten()) {
		    Dimension ver_dim = berechnemaxBreiteHoeheVerbindung(ver);
		    if (ver_dim.height > dim.height)
			dim.height = ver_dim.height;
		    if (ver_dim.width > dim.width)
			dim.width = ver_dim.width;
		}
	    }
	}
	if (dim.height < editHigh) {
	    dim.height = editHigh;
	}
	return dim;
    }

    /**
     * Berechnet Dimension der Verbindung
     * 
     * @param ver
     *            Verbindung zwischen Punkt und noch einem weiteren Punkt
     *            (geregelt durch berechneSpielBreiteundHoehe)
     * @return Dimension der Verbindung
     */
    private Dimension berechnemaxBreiteHoeheVerbindung(HilfsVerbindung ver) {
	Dimension dim = new Dimension();
	dim.height = dim.width = 0;
	for (Punkt p : ver.getPunkt()) {
	    // setze Maxima, wenn eine nicht entfernte Kante
	    // vorhanden ist
	    if (p.x > dim.width) {
		dim.width = p.x;
	    }
	    if (p.y > dim.height) {
		dim.height = p.y;
	    }
	}
	for (Kante k : ver.getVerbindung().getAktiveKanten()) {
	    if (k.getVerbindungspunkt() != null) {
		// Falls ein Verbindungspunkt größer ist, so
		// wird dieser zum Maximum
		if (k.getVerbindungspunkt().x > dim.width) {
		    dim.width = k.getVerbindungspunkt().x;
		}
		if (k.getVerbindungspunkt().y > dim.height) {
		    dim.height = k.getVerbindungspunkt().y;
		}
	    }
	}
	return dim;
    }

    /**
     * Gibt alle gespeichert Punkte zurück, auch null
     * 
     * @return Gibt alle gespeichert Punkte zurück, auch null
     */
    public Punkt[] getPunkte() {
	return punkte;
    }

    /**
     * Fügt einen neuen Punkt in die Adjazenzmatrix ein.
     * 
     * @param p
     *            Einzufügender Punkt
     * @throws PunktDoppeltException
     *             Falls ein Punkt schon existiert
     * @return gibt eine Referenz auf den Punkt in der Adjazenzmatrix zurück
     */
    public Punkt einfuegenPunkt(Punkt p) {
	try {
	    return punkte[getIndexOfPunkt(p)];
	} catch (PunktNichtGefundenException e) {
	}
	/**
	 * Erweitern des Punkte arrays
	 */
	Punkt[] alt = punkte;
	Punkt[] neu = new Punkt[alt.length + 1];
	for (int i = 0; i < alt.length; i++) {
	    neu[i] = alt[i];
	}
	neu[neu.length - 1] = p;

	punkte = neu;

	/**
	 * Erweitern der Adjazenzmatrix
	 */
	Verbindung[][] matrixalt = matrix;
	Verbindung[][] matrixneu = new Verbindung[matrixalt.length + 1][matrix.length + 1];
	for (int i = 0; i < matrixalt.length; i++) {
	    for (int i2 = 0; i2 < matrixalt.length; i2++) {
		matrixneu[i][i2] = matrixalt[i][i2];
	    }
	}

	matrix = matrixneu;
	/**
	 * Einfügen von Verbindungen der neuen Punkte entfällt, da für die neuen
	 * Punkte keine Verbindungen vorhanden sind.
	 */
	try {
	    return punkte[getIndexOfPunkt(p)];
	} catch (PunktNichtGefundenException e) {
	    return null;
	}

    }

    /**
     * Fügt eine Linie in die Matrix ein.
     * 
     * @param a
     *            Punkt 1
     * @param b
     *            Punkt 2
     * @param spieler
     *            Spieler dem die Kante zugeordnet wird
     * @throws PunktNichtGefundenException
     */
    public void einfügenKante(Punkt a, Punkt b, int spieler, VerbindungsPunkt graph) throws PunktNichtGefundenException {
	HilfsVerbindung ver = null;
	if ((ver = getVerbindung(a, b)) == null) {
	    Verbindung temp = matrix[getIndexOfPunkt(a)][getIndexOfPunkt(b)] = new Verbindung();
	    ver = new HilfsVerbindung(a, b, temp);
	}
	ver.getVerbindung().add(new Kante(spieler, graph));
	breitehoehe = berechneSpielBreiteundHoehe();
    }

    public void einfügenKante(Punkt a, Punkt b, int spieler) throws PunktNichtGefundenException {
	einfügenKante(a, b, spieler, null);
    }

    /**
     * Gibt Verbindung aus Adjazenzmatrix zurück
     * 
     * @param a
     *            Erster Punkt
     * @param b
     *            Zweiter Punkt
     * @return Linie, die beide Punkte verbindet.
     */
    public HilfsVerbindung getVerbindung(Punkt a, Punkt b) {
	return getVerbindung(a, b, matrix);
    }

    /**
     * Gibt Verbindung aus einer Adjazenzmatrix zurück
     * 
     * @param a
     *            Erster Punkt
     * @param b
     *            Zweiter Punkt
     * @param matrix
     *            Adjazenzmatrix
     * @return Linie, die beide Punkte verbindet.
     */
    public HilfsVerbindung getVerbindung(Punkt a, Punkt b, Verbindung[][] matrix) {
	try {
	    int pa = getIndexOfPunkt(a), pb = getIndexOfPunkt(b);
	    if (matrix[pa][pb] != null) {
		return new HilfsVerbindung(a, b, matrix[pa][pb]);
	    }
	    if (matrix[pb][pa] != null) {
		return new HilfsVerbindung(b, a, matrix[pb][pa]);
	    }
	} catch (PunktNichtGefundenException e) {
	    e.printStackTrace();
	}
	return null;
    }

    /**
     * wird benötigt um alle Kanten zeichnen zu könnnen
     * 
     * @return ArrayList mit den Kanten, die gezeichnet werden müssen
     */
    public ArrayList<HilfsKante> getZuZeichnendeKanten() {
	ArrayList<HilfsKante> kanten = new ArrayList<>();
	for (Punkt punkte : getPunkte()) {
	    for (Punkt punkte_y : getPunkte()) {
		HilfsVerbindung ver = null;
		if ((ver = getVerbindung(punkte, punkte_y)) != null && !ver.getVerbindung().isEmpty()) {
		    for (Kante k : ver.getVerbindung()) {
			HilfsKante hk = new HilfsKante(punkte, punkte_y, k);
			fuegezuZeichnendeKantenhinzu(kanten, hk);
		    }
		}
	    }
	}
	return kanten;
    }

    /**
     * Fügt Kanten zu einer ArrayList hinzu, wenn diese gezeichnet werden
     * müssen.
     * 
     * @param kanten
     *            Der Array zu dem die Kanten hinzugefügt werden sollen
     * @param k
     *            Kante die geprüft werden soll
     */
    private void fuegezuZeichnendeKantenhinzu(ArrayList<HilfsKante> kanten, HilfsKante k) {
	if (k.getKante().isEntfernt()) {
	    return;
	}
	// if (k.getKante().getVerbindungspunkt() == null) {
	if (!kanten.contains(k) && !kanten.contains(k.punkteUmdrehen())) {
	    kanten.add(new HilfsKante(k.getPunkt()[0], k.getPunkt()[1], k.getKante()));
	}
	// } else {
	// // Es kann nur einen Verbindungspunkt geben -> zwei
	// // Strecken
	// HilfsKante temp1 = new HilfsKante(k.getPunkt()[0],
	// k.getKante().getVerbindungspunkt(), k.getKante());
	// HilfsKante temp2 = new HilfsKante(k.getKante().getVerbindungspunkt(),
	// k.getPunkt()[0], k.getKante());
	// if (!kanten.contains(temp1) &&
	// !kanten.contains(temp1.punkteUmdrehen()))
	// kanten.add(temp1);
	// if (!kanten.contains(temp2) &&
	// !kanten.contains(temp2.punkteUmdrehen()))
	// kanten.add(temp2);
	// }
    }

    /**
     * wird benötigt um zu überprüfen, ob Punkt in der Nähe ist
     * 
     * @param suche
     *            Punkt, der geprüft werden soll (X, Y)
     * @return null, wenn keine passender Punkt gefunden wurde, ansonsten ein
     *         Objekt des Typs Punkt
     */
    public Punkt getPunktdurchKoordinaten(double[] suche, double genauigkeit) {
	return getPunktdurchKoordinaten(suche[0], suche[1], genauigkeit);
    }

    /**
     * wird benötigt um zu überprüfen, ob Punkt in der Nähe ist
     * 
     * @param suchex
     *            Punkt, der geprüft werden soll (X-Wert)
     * @param suchey
     *            Punkt, der geprüft werden soll (Y-Wert)
     * @param genauigkeit
     *            Genauigkeit der Suche
     * @return null, wenn keine passender Punkt gefunden wurde, ansonsten ein
     *         Objekt des Typs Punkt
     */
    public Punkt getPunktdurchKoordinaten(double suchex, double suchey, double genauigkeit) {
	for (Punkt p : punkte) {
	    if (Line2D.ptSegDist(p.x, p.y, p.x, p.y, suchex, suchey) < genauigkeit) {
		return p;
	    }
	}
	return null;
    }

    public Punkt getNaechstenPunkt(double[] suche) {
	return getNaechstenPunkt(suche[0], suche[1]);
    }

    /**
     * wird benötigt um den nächsten Punkt zu finden
     * 
     * @param suchex
     *            Punkt, der geprüft werden soll (X-Wert)
     * @param suchey
     *            Punkt, der geprüft werden soll (Y-Wert)
     * @return null, wenn keine passender Punkt gefunden wurde, ansonsten ein
     *         Objekt des Typs Punkt
     */
    public Punkt getNaechstenPunkt(double suchex, double suchey) {
	double min_entf = Line2D.ptSegDist(punkte[0].x, punkte[0].y, punkte[0].x, punkte[0].y, suchex, suchey);
	Punkt min_punkt = null;
	for (Punkt p : punkte) {
	    if (Line2D.ptSegDist(p.x, p.y, p.x, p.y, suchex, suchey) < min_entf) {
		min_punkt = p;
	    }
	}
	return min_punkt;
    }

    /**
     * wird benötigt um einen Klick einer Kante zuzuordnen
     * 
     * @param suche
     *            Punkt, der geprüft werden soll (X, Y)
     * @param genauigkeit
     * @return null, wenn keine passende Kante gefunden wurde, ansonsten ein
     *         Objekt des Typs HilfsKante
     */
    public HilfsKante getKantedurchKoordinaten(double[] suche, double genauigkeit) {
	for (Punkt punkte : getPunkte()) {
	    for (Punkt punkte_y : getPunkte()) {
		HilfsVerbindung ver = null;
		if ((ver = getVerbindung(punkte, punkte_y)) != null && !ver.getVerbindung().isEmpty()) {
		    for (Kante k : ver.getVerbindung()) {
			HilfsKante hk = new HilfsKante(punkte, punkte_y, k);
			if (isPunktAufKante(hk, suche, genauigkeit)) {
			    return hk;
			}
		    }
		}
	    }
	}
	// Keine passende Kante gefunden
	return null;
    }

    /**
     * Gibt den Kantenwert für eine Kante zurück
     * 
     * @param kante
     * @return Wert der Kante
     */
    public Bruch getKantenwert(HilfsKante kante) {
	for (Punkt punkte : getPunkte()) {
	    for (Punkt punkte_y : getPunkte()) {
		HilfsVerbindung ver = null;
		if ((ver = getVerbindung(punkte, punkte_y)) != null && !ver.getVerbindung().isEmpty()) {
		    for (Kante k : ver.getVerbindung()) {
			HilfsKante hk = new HilfsKante(punkte, punkte_y, k);
			if (hk.equals(kante)) {
			    return k.getWert();
			}
		    }
		}
	    }
	}
	return null;
    }

    /**
     * Prüft, ob ein angegebener Punkt auf einer Kante hk liegt
     * 
     * @param hk
     *            Hilfskante, die geprüft werden soll
     * @param suche
     *            Koordinaten, die überprüft werden sollen
     * @param genauigkeit
     *            Toleranz bei der Überprüfung
     * @return boolean, ob die Koordianaten auf der Kante liegen
     */
    private boolean isPunktAufKante(HilfsKante hk, double[] suche, double genauigkeit) {
	if (hk.getKante().getVerbindungspunkt() == null) {
	    // Überprüfung der Entfernung zur Linie
	    if (Line2D.ptSegDist(hk.getPunkt()[0].x, hk.getPunkt()[0].y, hk.getPunkt()[1].x, hk.getPunkt()[1].y, suche[0], suche[1]) < genauigkeit) {
		return true;
	    } else {
		return false;
	    }
	} else if (Line2D.ptSegDist(hk.getPunkt()[0].x, hk.getPunkt()[0].y, hk.getKante().getVerbindungspunkt().x, hk.getKante().getVerbindungspunkt().y, suche[0], suche[1]) < genauigkeit) {
	    // True wenn der Punkt auf der Ersten der beiden Linien
	    // liegt, die die Punkte eins und zwei mit dem
	    // Verbindungspunkt verbinden
	    return true;
	} else if (Line2D.ptSegDist(hk.getPunkt()[1].x, hk.getPunkt()[1].y, hk.getKante().getVerbindungspunkt().x, hk.getKante().getVerbindungspunkt().y, suche[0], suche[1]) < genauigkeit) {
	    // True wenn der Punkt auf der Zweiten der beiden Linien
	    // liegt, die die Punkte eins und zwei mit dem
	    // Verbindungspunkt verbinden
	    return true;
	}
	return false;
    }

    /**
     * Sucht den Index eines Punktes
     * 
     * @param p
     *            Zu suchender Punkt
     * @return Index des Punktes in der Adjazenzmatrix
     * @throws PunktNichtGefundenException
     */
    private int getIndexOfPunkt(Punkt p) throws PunktNichtGefundenException {
	for (int i = 0; i < punkte.length; i++) {
	    if (p.getX() == punkte[i].getX() && p.getY() == punkte[i].getY()) {
		return i;
	    }
	}
	throw new PunktNichtGefundenException(p);
    }

    /**
     * Überprüft ob diese Kante schon existiert
     * 
     * @param kante
     *            Zu überprüfende Kante
     * @return true, wenn sie existiert, false, wenn sie noch nicht existiert
     */
    public boolean checkKanteExist(HilfsKante kante) {
	HilfsVerbindung ver = null;
	// Exception kann geworfen werden, wenn ein Punkt nicht gefunden wurde
	if ((ver = getVerbindung(kante.getPunkt()[0], kante.getPunkt()[1])) != null && ver.getVerbindung().getKante(kante.getKante()) != null) {
	    return true;
	}
	return false;
    }

    /**
     * Löscht eine Kante und ändert Matrix
     * 
     * @param kante
     *            Kante, die entfernt werden soll
     * @throws PunktNichtGefundenException
     *             Exception wird geworfen, wenn ein Punkt nicht gefunden wurde
     * @throws KanteNichtGefundenException
     *             Exception wird geworfen, wenn keine Verbindung existiert
     */
    public void loescheKante(HilfsKante kante) throws PunktNichtGefundenException, KanteNichtGefundenException {
	HilfsVerbindung ver = null;
	if ((ver = getVerbindung(kante.getPunkt()[0], kante.getPunkt()[1])) != null && ver.getVerbindung().aktiveKanten()) {
	    ver.getVerbindung().loesche(kante.getKante());
	}
	if ((ver = getVerbindung(kante.getPunkt()[1], kante.getPunkt()[0])) != null && ver.getVerbindung().aktiveKanten()) {
	    ver.getVerbindung().loesche(kante.getKante());
	}
	entfernenVonFliegendenKantenStart(true);
	breitehoehe = berechneSpielBreiteundHoehe();
    }

    /**
     * Entfernt eine Kante und ändert Matrix
     * 
     * @param kante
     *            Kante, die entfernt werden soll
     * @throws PunktNichtGefundenException
     *             Exception wird geworfen, wenn ein Punkt nicht gefunden wurde
     *             // * @throws KanteNichtGefundenException // * Exception wird
     *             geworfen, wenn keine Verbindung existiert
     */
    public void entferneKante(HilfsKante kante) throws PunktNichtGefundenException {
	HilfsVerbindung ver = null;
	if ((ver = getVerbindung(kante.getPunkt()[0], kante.getPunkt()[1])) != null && ver.getVerbindung().aktiveKanten()) {
	    ver.getVerbindung().setEntfernt(kante.getKante());
	}
	if ((ver = getVerbindung(kante.getPunkt()[1], kante.getPunkt()[0])) != null && ver.getVerbindung().aktiveKanten()) {
	    ver.getVerbindung().setEntfernt(kante.getKante());
	}
	entfernenVonFliegendenKantenStart();
	breitehoehe = berechneSpielBreiteundHoehe();
    }

    /**
     * Geht alle Kanten ab, bis der entsprechende Punkt gefunden wurde
     * 
     * @param p
     *            Punkt, der überprüft werden soll
     * @return true, wenn diese mit dem Boden verbunden ist, false, falls nicht
     *         gefunden oder nicht verbunden
     * 
     *         FEHLER!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
     */
    public boolean istMitBodenVerbundenPunkt(Punkt p) {
	ArrayList<HilfsVerbindung> checkedkante = new ArrayList<>();
	for (Punkt punkt : getPunkte()) {
	    if (punkt.istAmBoden()) {
		if (p.equals(punkt)) {
		    return true;
		}
		for (Punkt punkte_y : getPunkte()) {
		    if (p.equals(punkte_y)) {
			return true;
		    }
		    HilfsVerbindung ver = null;
		    if ((ver = getVerbindung(punkt, punkte_y)) != null && ver.getVerbindung().aktiveKanten()) {
			if (checkedkante.contains(ver)) {
			    continue;
			} else {
			    checkedkante.add(ver);
			}
			if (istMitBodenVerbundenPunkt(checkedkante, punkte_y)) {
			    return true;
			}
		    }
		}
	    }
	}
	return false;
    }

    /**
     * Wird nur von der Methode istMitBodenVerbunden aufgerufen
     * 
     * @param punkt
     *            Punkt ab dem weiter gesucht werden soll
     * @param checkedkante
     *            Liste mit bereits abgesuchten
     * @return true, wenn gefunden, false, falls nicht
     */
    private boolean istMitBodenVerbundenPunkt(ArrayList<HilfsVerbindung> checkedkante, Punkt punkt) {
	for (Punkt punkte_y : getPunkte()) {
	    if (punkt.equals(punkte_y)) {
		return true;
	    }
	    HilfsVerbindung ver = null;
	    if ((ver = getVerbindung(punkt, punkte_y)) != null && ver.getVerbindung().aktiveKanten()) {
		if (checkedkante.contains(ver)) {
		    continue;
		} else {
		    checkedkante.add(ver);
		}
		if (istMitBodenVerbundenPunkt(checkedkante, punkte_y)) {
		    return true;
		}
	    }
	}
	return false;
    }

    /**
     * Übertragt alle mit dem Boden verbunden Kanten in eine Übergangsmatrix und
     * gleicht diese dann mit der echten ab und setzt ggf. Kanten auf
     * "entfernt = true".
     */
    public void entfernenVonFliegendenKantenStart() {
	entfernenVonFliegendenKantenStart(false);
    }

    /**
     * Übertragt alle mit dem Boden verbunden Kanten in eine Übergangsmatrix und
     * gleicht diese dann mit der echten ab und setzt ggf. Kanten auf
     * "entfernt = true".
     * 
     * @param loeschen
     *            löscht entsprechende Kanten gleich komplett (wird im Editor
     *            verwendet)
     */
    public void entfernenVonFliegendenKantenStart(boolean loeschen) {
	Verbindung[][] matrixneu = new Verbindung[matrix.length][matrix.length];
	for (Punkt punkte : getPunkte()) {
	    if (punkte.istAmBoden()) {
		for (Punkt punkte_y : getPunkte()) {
		    HilfsVerbindung ver = null;
		    if (getVerbindung(punkte, punkte_y, matrixneu) == null && (ver = getVerbindung(punkte, punkte_y)) != null && ver.getVerbindung().aktiveKanten()) {
			try {
			    matrixneu[getIndexOfPunkt(ver.getPunkt()[0])][getIndexOfPunkt(ver.getPunkt()[1])] = ver.getVerbindung();
			} catch (PunktNichtGefundenException e) {
			    e.printStackTrace();
			}
			entfernenVonFliegendenKanten(punkte_y, matrixneu);
		    }
		}
	    }
	}
	uebertrageAenderungeninMatrix(matrixneu, loeschen);
	breitehoehe = berechneSpielBreiteundHoehe();
    }

    /**
     * Ändert die Matrix aufgrund übergebener Daten von der Methode
     * "entferneFliegendeKanten()"
     * 
     * @param matrixneu
     * @param loeschen
     */
    private void uebertrageAenderungeninMatrix(Verbindung[][] matrixneu, boolean loeschen) {
	for (int i = 0; i < matrix.length; i++) {
	    for (int i2 = 0; i2 < matrix.length; i2++) {
		// Wurde nicht übertragen -> keine Verbindung zum Boden
		if (matrixneu[i][i2] == null) {
		    if (matrix[i][i2] != null) {
			if (loeschen) {
			    matrix[i][i2] = null;
			    continue;
			}
			for (Kante kante : matrix[i][i2]) {
			    kante.setEntfernt(true);
			}
		    }
		} else {
		    if (!matrixneu[i][i2].equals(matrix[i][i2])) {
			for (Kante kante : matrix[i][i2]) {
			    if (!matrixneu[i][i2].contains(kante)) {
				kante.setEntfernt(true);
				if (loeschen) {
				    matrix[i][i2].loesche(kante);
				}
			    }
			}
		    }
		}
	    }
	}
    }

    /**
     * Rekursive Methode, die von einem Punkt ausgehend fliegende Kanten
     * entfernt
     * 
     * @param start
     *            Startpunkt
     * @param matrixneu
     *            Änderungsspeicher
     */
    private void entfernenVonFliegendenKanten(Punkt start, Verbindung[][] matrixneu) {
	try {
	    for (Punkt punkte_y : getPunkte()) {
		HilfsVerbindung ver = null;
		if (getVerbindung(start, punkte_y, matrixneu) == null && (ver = getVerbindung(start, punkte_y)) != null && ver.getVerbindung().aktiveKanten()) {
		    matrixneu[getIndexOfPunkt(ver.getPunkt()[0])][getIndexOfPunkt(ver.getPunkt()[1])] = ver.getVerbindung();
		    entfernenVonFliegendenKanten(punkte_y, matrixneu);
		}
	    }
	} catch (PunktNichtGefundenException e) {
	}
    }

    /**
     * @return zusätzliche Anzeigehöhe
     */
    public int getEditHigh() {
	return editHigh;
    }

    /**
     * @param editHigh
     *            zusätzliche Anzeigehöhe
     */
    public void setEditHigh(int editHigh) {
	this.editHigh = editHigh;
    }

    /**
     * @return Breite und Höhe der Figur
     */
    public Dimension getBreitehoehe() {
	return breitehoehe;
    }

    /**
     * Gibt die Kante mit dem besten Wert für den Spieler zurück
     * 
     * @param spieler
     * @return Hilfskante, die die Kante representiert
     */
    public HilfsKante getBesteKante(int spieler) {
	HilfsKante besteKanteSpieler = null;
	ArrayList<HilfsVerbindung> pkombi = new ArrayList<>();
	for (Punkt punkt : getPunkte()) {
	    for (Punkt punkt2 : getPunkte()) {
		HilfsVerbindung ver = null;
		if ((ver = getVerbindung(punkt, punkt2)) != null && ver.getVerbindung().aktiveKanten()) {
		    if (pkombi.contains(ver)) {
			continue;
		    } else {
			pkombi.add(ver);
		    }
		    Kante bestevonverbindung = null;
		    if ((bestevonverbindung = ver.getVerbindung().getBesteKante(spieler)) != null) {
			if (besteKanteSpieler == null) {
			    besteKanteSpieler = new HilfsKante(punkt, punkt2, bestevonverbindung);
			} else if (besteKanteSpieler.getKante().getWert().vergleiche(bestevonverbindung.getWert()) == (spieler == 1 ? 1 : -1)) {
			    besteKanteSpieler = new HilfsKante(punkt, punkt2, bestevonverbindung);
			}
		    }
		}
	    }
	}
	return besteKanteSpieler;
    }

    /**
     * Berechnet für jede Kante, die noch nicht entfernt wurde, einen Bruch, der
     * den Wert der Kante representiert
     * 
     * @throws Exception
     */
    public FortschrittsPanel starteWerteBerechnung(Settings settings, Runnable r) {
	return starteWerteBerechnung(settings, r, false);
    }

    /**
     * Berechnet für jede Kante, die noch nicht entfernt wurde, einen Bruch, der
     * den Wert der Kante representiert
     * 
     * @throws Exception
     */
    public FortschrittsPanel starteWerteBerechnung(Settings settings, Runnable r, boolean neurechnen) {
	int[] kantenanzahlarray = getKantenAnzahl();
	int kantenanzahl = kantenanzahlarray[0] + kantenanzahlarray[1];
	if (neurechnen) {
	    options.clear();
	}
	HackenbushFenster.getLogger().log(Level.INFO, "ArraySize: " + kantenanzahl);

	FortschrittsPanel panel = new FortschrittsPanel(settings, kantenanzahl, r);
	HackenbushFenster.getLogger().log(Level.INFO, "Multithreading: " + settings.isMultithreading());

	if (settings.isMultithreading()) {
	    try {
		ArrayList<HilfsVerbindung> pkombi = new ArrayList<>();
		for (Punkt punkt : getPunkte()) {
		    for (Punkt punkt2 : getPunkte()) {
			HilfsVerbindung ver = null;
			if ((ver = getVerbindung(punkt, punkt2)) != null && ver.getVerbindung().aktiveKanten()) {
			    if (pkombi.contains(ver)) {
				continue;
			    } else {
				pkombi.add(ver);
			    }
			    if (!handleVerbindung(panel, ver)) {
				return panel;
			    }
			}
		    }
		}
	    } catch (Exception e) {
		panel.abbruch();
		e.printStackTrace();
	    }
	} else {
	    HackenbushSingleArbeitsThread thread = new HackenbushSingleArbeitsThread(this, panel, options);
	    thread.start();
	}
	return panel;
    }

    /**
     * Startet eine Berechnung für eine Verbindung (Multithreading)
     * 
     * @param frame
     *            Referenz auf die Fortschrittsanzeige
     * @param ver
     *            Verbindung, für die die Berechnung gestartet werden soll.
     * @return true, wenn Berechnung erfolgreich war
     * @throws Exception
     */
    private boolean handleVerbindung(FortschrittsPanel frame, HilfsVerbindung ver) throws Exception {
	for (Kante k : ver.getVerbindung().getAktiveKanten()) {
	    HilfsKante kante = new HilfsKante(ver.getPunkt()[0], ver.getPunkt()[1], k);
	    HackenbushFigur neueFigur = new HackenbushFigur(this);
	    neueFigur.loescheKante(kante);
	    Bruch besondereWerte = neueFigur.checkBesondereSpielWerte();
	    if (besondereWerte != null) {
		k.setWert(besondereWerte);
		// HackenbushFenster.getLogger().log(Level.INFO,
		// "Besonderer Spielwert Kantenberechnung k: "
		// + punkt.x + "/" + punkt2.y + " - " +
		// +punkt2.x + "/" + punkt.y +
		// ", Kantenwert: " +
		// k.getWert().getAnzeige());
		frame.addFortschritt();
		continue;
	    }
	    boolean gefunden = false;
	    // Suche aus bereits berechneten Figuren Werte
	    for (HackenbushFigur b : options) {
		if (neueFigur.equals(b)) {
		    k.setWert(b.getSpielWert());
		    frame.addFortschritt();
		    gefunden = true;
		    break;
		}
	    }
	    if (!gefunden || (gefunden && k.getWert() == null)) {
		HackenbushArbeitsThread thread = new HackenbushArbeitsThread(k, neueFigur, frame, options);
		thread.start();
	    }
	    // HackenbushFenster.getLogger().log(Level.INFO,
	    // "Kantenberechnung k: " + punkt.x + "/" +
	    // punkt2.y + " - " + +punkt2.x + "/" + punkt.y
	    // + ", Kantenwert: " +
	    // k.getWert().getAnzeige());
	}
	return true;
    }

    /**
     * @return Überprüft die Hackenbush-Figur auf spezielle Spielwerte, wie 0,
     *         oder nur noch Kanten von einem Spieler
     */
    public Bruch checkBesondereSpielWerte() {
	if (!hatAktiveKanten()) {
	    return new Bruch(BigInteger.valueOf(0), BigInteger.valueOf(1));
	}
	int[] anzahlkanten = getKantenAnzahl();
	int nurnochkantenvon = beinhaltetNurNochKantenVon();
	if (nurnochkantenvon == 1) {
	    return new Bruch(BigInteger.valueOf(anzahlkanten[0]), BigInteger.valueOf(1));
	}
	if (nurnochkantenvon == 2) {
	    return new Bruch(BigInteger.valueOf(anzahlkanten[1]).negate(), BigInteger.valueOf(1));
	}
	return null;
    }

    /**
     * Berechnet den Spielwert für diese Hackenbush-Figur
     * 
     * @return Bruch, der den Spielwert dieser Hackenbush-Figur darstellt
     * @throws EinfachheitsRegelException
     * @throws UnvollstandigeBerechnungException
     */
    public Bruch getSpielWert() throws EinfachheitsRegelException, UnvollstandigeBerechnungException {
	Bruch besondereWerte = checkBesondereSpielWerte();
	if (besondereWerte != null) {
	    // HackenbushFenster.getLogger().log(Level.INFO, "Ergebnis: " +
	    // besondereWerte.getAnzeige());
	    return besondereWerte;
	}
	Bruch min = null, max = null;
	ArrayList<HilfsVerbindung> pkombi = new ArrayList<>();
	for (Punkt punkt : getPunkte()) {
	    for (Punkt punkt2 : getPunkte()) {
		HilfsVerbindung ver = null;
		if ((ver = getVerbindung(punkt, punkt2)) != null && ver.getVerbindung().aktiveKanten()) {
		    if (pkombi.contains(ver)) {
			continue;
		    } else {
			pkombi.add(ver);
		    }
		    for (Kante k : ver.getVerbindung().getAktiveKanten()) {
			if (k.getWert() == null) {
			    throw new UnvollstandigeBerechnungException(new HilfsKante(ver.getPunkt()[0], ver.getPunkt()[1], k));
			}
			if (min == null && k.getSpieler() == 2) {
			    min = k.getWert();
			}
			if (max == null && k.getSpieler() == 1) {
			    max = k.getWert();
			}
			if (k.getSpieler() == 2 && min.vergleiche(k.getWert()) == -1) {
			    min = k.getWert();
			    continue;
			}
			if (k.getSpieler() == 1 && max.vergleiche(k.getWert()) == 1) {
			    max = k.getWert();
			    continue;
			}
		    }
		}
	    }
	}
	try {
	    // HackenbushFenster.getLogger().log(Level.INFO,
	    // "Anwendung der Einfachheitsregel!");
	    Bruch b = EinfachheitsRegel.einfachsterBruchZwischen(min, max);
	    // HackenbushFenster.getLogger().log(Level.INFO, "Ergebnis: " +
	    // b.getAnzeige());
	    return b;
	} catch (EinfachheitsRegelException e) {
	    throw e;
	}
    }

    /**
     * Überprüft die Figur, ob nur noch Kanten eines Spielers vorhanden sind
     * 
     * @return 0 wenn beide Spieler Kanten haben, 1 wenn nur noch Spieler 1
     *         Kanten hat, 2 wenn nur noch Spieler 2 Kanten hat
     */
    public int beinhaltetNurNochKantenVon() {
	boolean spieler1 = false, spieler2 = false;
	ArrayList<HilfsVerbindung> pkombi = new ArrayList<>();
	for (Punkt punkt : getPunkte()) {
	    for (Punkt punkt2 : getPunkte()) {
		HilfsVerbindung ver = null;
		if ((ver = getVerbindung(punkt, punkt2)) != null && ver.getVerbindung().aktiveKanten()) {
		    if (pkombi.contains(ver)) {
			continue;
		    } else {
			pkombi.add(ver);
		    }
		    int ret = ver.getVerbindung().beinhaltenNurNochKantenVon();
		    if (ret == 0) {
			return 0;
		    } else if (ret == 1) {
			spieler1 = true;
		    } else if (ret == 2) {
			spieler2 = true;
		    }
		}
	    }
	}
	return (spieler1 == spieler2) ? 0 : (spieler1 ? 1 : 2);
    }

    /**
     * Überprüft die Figur, ob aktive Kanten vorhanden sind
     * 
     * @return falls ja, true, falls nein, false
     */
    private boolean hatAktiveKanten() {
	for (Punkt punkt : getPunkte()) {
	    for (Punkt punkt2 : getPunkte()) {
		HilfsVerbindung ver = null;
		if ((ver = getVerbindung(punkt, punkt2)) != null && ver.getVerbindung().aktiveKanten()) {
		    return true;
		}
	    }
	}
	return false;
    }

    /**
     * Zählt Anzahl der noch nicht entfernten Kanten eines Spielers
     * 
     * @return Array mit Anzahl Spieler 1 / Spieler 2
     */
    public int[] getKantenAnzahl() {
	int[] ret = new int[2];
	ArrayList<HilfsVerbindung> pkombi = new ArrayList<>();
	for (Punkt punkt : getPunkte()) {
	    for (Punkt punkt2 : getPunkte()) {
		HilfsVerbindung ver = null;
		if ((ver = getVerbindung(punkt, punkt2)) != null && ver.getVerbindung().aktiveKanten()) {
		    if (pkombi.contains(ver)) {
			continue;
		    } else {
			pkombi.add(ver);
		    }
		    int[] ver_anzahl = ver.getVerbindung().getKantenAnzahl();
		    ret[0] = ret[0] + ver_anzahl[0];
		    ret[1] = ret[1] + ver_anzahl[1];
		}
	    }
	}
	return ret;
    }

    /**
     * @return Alle bereits berechneten Konstellationen
     */
    public ArrayList<HackenbushFigur> getOptions() {
	return options;
    }

    /**
     * @param options
     *            bereits berechnete Konstellationen
     */
    public void setOptions(ArrayList<HackenbushFigur> options) {
	this.options = options;
    }

}
