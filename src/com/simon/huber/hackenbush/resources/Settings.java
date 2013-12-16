package com.simon.huber.hackenbush.resources;

import java.io.Serializable;

/**
 * Verwaltet Programmeinstellungen
 * @author Simon Huber
 *
 */

public class Settings implements Serializable {

    private static final long serialVersionUID = -8429156308308988322L;

    /**
     * Bestimmt, ob ein Popup beim Starten des Editors aufgerufen werden soll
     */
    private boolean showEditorInfo = true;
    
    /**
     * Bestimmt, ob ein Popup beim Starten des Hauptfensters aufgerufen werden soll
     */
    private boolean showHauptfensterInfo = true;

    /**
     * @return the showHauptfensterInfo
     */
    public boolean isShowHauptfensterInfo() {
        return showHauptfensterInfo;
    }


    /**
     * @param showHauptfensterInfo the showHauptfensterInfo to set
     */
    public void setShowHauptfensterInfo(boolean showHauptfensterInfo) {
        this.showHauptfensterInfo = showHauptfensterInfo;
    }



    /**
     * Bestimmt, ob sich das Fenster, das den Status des Rechenvorgangs anzeigt, automatisch schlieﬂen soll
     */
    private boolean fortschritt_automatisch_beenden = true;
    
    /**
     * Bestimmt die Rechenart
     */
    private boolean multithreading = false;
   
    /**
     * @return the showEditorInfo
     */
    public boolean isShowEditorInfo() {
        return showEditorInfo;
    }


    /**
     * @param showEditorInfo the showEditorInfo to set
     */
    public void setShowEditorInfo(boolean showEditorInfo) {
        this.showEditorInfo = showEditorInfo;
    }
    

    /**
     * @return the fortschritt_automatisch_beenden
     */
    public boolean isFortschritt_automatisch_beenden() {
        return fortschritt_automatisch_beenden;
    }


    /**
     * @param fortschritt_automatisch_beenden the fortschritt_automatisch_beenden to set
     */
    public void setFortschritt_automatisch_beenden(boolean fortschritt_automatisch_beenden) {
        this.fortschritt_automatisch_beenden = fortschritt_automatisch_beenden;
    }


    
    /**
     * @return {@link Boolean} Multithreading
     */
    public boolean isMultithreading() {
	return multithreading;
    }


    
    /**
     * @param multithreading
     */
    public void setMultithreading(boolean multithreading) {
	this.multithreading = multithreading;
    }
    
}
