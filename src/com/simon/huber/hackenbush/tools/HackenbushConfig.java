package com.simon.huber.hackenbush.tools;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Zentrale Verwaltungsklasse für im Programm verwendete Strings
 * @author Simon Huber
 */
public class HackenbushConfig {

    /**
     * Pfad zum Speicherort des Language-Resourcepacks
     */
    private static final String BUNDLE_NAME = "com.simon.huber.hackenbush.resources.messages";

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

    private HackenbushConfig() {
    }

    /**
     * Gibt einen String für einen Key zurück, der in dem Resourcen Bundle gespeichert ist
     * @param key Key des Strings
     * @return String
     */
    public static String getString(String key) {
	try {
	    return RESOURCE_BUNDLE.getString(key);
	} catch (MissingResourceException e) {
	    return '!' + key + '!';
	}
    }
}
