package com.simon.huber.hackenbush.tools;

import java.io.File;

import javax.swing.filechooser.FileFilter;

/**
 * Dateifilter für Dateiauswahl Dialog
 * 
 * @author Simon Huber
 */
public class HackenbushFilter extends FileFilter {

    @Override
    public boolean accept(File f) {
	/**
	 * Akzeptiert nur File-Objekte, die die Dateiendung ".hackenbush" haben
	 */
	if (f == null) {
	    return false;
	}
	if (f.isDirectory()) {
	    return true;
	}
	return f.getName().toLowerCase().endsWith(".hackenbush");
    }

    @Override
    public String getDescription() {
	/**
	 * Inhalt der Beschreibung des Dateifilters
	 */
	return "Hackenbush (.hackenbush)";
    }

}
