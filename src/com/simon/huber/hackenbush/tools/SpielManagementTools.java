package com.simon.huber.hackenbush.tools;

import java.io.FileNotFoundException;
import java.io.IOException;

import com.simon.huber.hackenbush.Spiel;

/**
 * Ist für das Speichern von Spielen zuständig
 * 
 * @author Simon Huber
 *
 */
public class SpielManagementTools {
    
    /**
     * Laedt eine Spielinstanz und gibt diese zurueck
     * 
     * @param path
     *            den Pfad zum gespeicherten Spiel
     * @return das geladene Spiel
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws FileNotFoundException
     */
    public static Spiel load(String path) throws FileNotFoundException, ClassNotFoundException, IOException {
	return ObjektManager.load(path);
    }

    /**
     * Speichert eine Spieleinstanz
     * 
     * @param spiel
     *            Das zu speichernde Spiel
     * @param path
     *            Speicherpfad, an den das Spiel gespeichert werden soll.
     * @throws IOException
     * @throws FileNotFoundException
     */
    public static void speichern(Spiel spiel, String path) throws FileNotFoundException, IOException {
	ObjektManager.save(spiel, path);
    }

}
