package com.simon.huber.hackenbush.tools;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Verwaltet das Serialisieren und laden von Objekten
 * 
 * @author Simon Huber
 * 
 */
public class ObjektManager {
    
    /**
     * Lädt ein Objekt
     * @param path Speicherort + Dateiname des Objekts
     * @return Objekt
     * @throws FileNotFoundException
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static <T extends Object> T load(String path) throws FileNotFoundException, IOException, ClassNotFoundException {
	ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path));
	@SuppressWarnings("unchecked")
	T result = (T) ois.readObject();
	ois.close();
	return result;
    }

    /**
     * Speichert ein Objekt
     * @param obj Zu speicherndes Objekt
     * @param path Speicherort + Dateiname
     * @throws FileNotFoundException
     * @throws IOException
     */
    public static <T extends Object> void save(T obj, String path) throws FileNotFoundException, IOException {
	ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path));
	oos.writeObject(obj);
	oos.flush();
	oos.close();
    }
}
