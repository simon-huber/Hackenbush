package com.simon.huber.hackenbush.tools;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Globale Logger Klasse, die teilweise zum Debugging verwendet wird.
 * Legt eine Datei namens "Logging.txt" an
 * @author Simon Huber
 */
public class HackenbushLogger {

	private FileHandler fileTxt;
	private SimpleFormatter formatterTxt;

	public HackenbushLogger() {
		// Globaler Logger
		Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
		logger.setLevel(Level.ALL);
		try {
			fileTxt = new FileHandler("Logging.txt");
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// txt Formatter
		formatterTxt = new SimpleFormatter();
		fileTxt.setFormatter(formatterTxt);
		logger.addHandler(fileTxt);
	}

}
