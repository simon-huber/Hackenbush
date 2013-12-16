package com.simon.huber.hackenbush.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JRadioButtonMenuItem;

import com.simon.huber.hackenbush.resources.Settings;

/**
 * Erstellt ein Menü, das alle Programmeinstellungen editierbar macht
 * 
 * @author Simon Huber
 * 
 */
public class EinstellungenMenu extends JMenu {

    private static final long serialVersionUID = 1L;
    private Settings settings;
    
    private JCheckBoxMenuItem showEditorInfoItem;
    private JCheckBoxMenuItem showHauptfensterInfoItem;

    // Anzeigename der Einstellungen
    private String[] names = new String[] { "Zeige Editoreinführung", "Zeige Hauptfenstereinführung", "Schließe das Berechnungsfenster automatisch", "Nutze Multithreading", "Nutze Singethreading" };

    public EinstellungenMenu(Settings settings) {
	super("Einstellungen");
	this.settings = settings;
	create();
    }
    
    public JCheckBoxMenuItem getShowEditorInfoItem() {
	return showEditorInfoItem;
    }
    
    public JCheckBoxMenuItem getShowHauptfensterInfoItem() {
	return showHauptfensterInfoItem;
    }
    

    public void create() {
	showEditorInfoItem = new JCheckBoxMenuItem(names[0]);
	showEditorInfoItem.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent arg0) {
		JCheckBoxMenuItem item = (JCheckBoxMenuItem) arg0.getSource();
		settings.setShowEditorInfo(item.isSelected());
	    }
	});
	showEditorInfoItem.setSelected(settings.isShowEditorInfo());
	add(showEditorInfoItem);
	
	showHauptfensterInfoItem = new JCheckBoxMenuItem(names[1]);
	showHauptfensterInfoItem.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent arg0) {
		JCheckBoxMenuItem item = (JCheckBoxMenuItem) arg0.getSource();
		settings.setShowHauptfensterInfo(item.isSelected());
	    }
	});
	showHauptfensterInfoItem.setSelected(settings.isShowHauptfensterInfo());
	add(showHauptfensterInfoItem);

	JCheckBoxMenuItem fortschritt_beendenItem = new JCheckBoxMenuItem(names[2]);
	fortschritt_beendenItem.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent arg0) {
		JCheckBoxMenuItem item = (JCheckBoxMenuItem) arg0.getSource();
		settings.setFortschritt_automatisch_beenden(item.isSelected());
	    }
	});
	fortschritt_beendenItem.setSelected(settings.isFortschritt_automatisch_beenden());
	add(fortschritt_beendenItem);

	addSeparator();

	ButtonGroup gruppe_threading = new ButtonGroup();
	JRadioButtonMenuItem multithreading = new JRadioButtonMenuItem(names[3]);
	gruppe_threading.add(multithreading);
	multithreading.setSelected(settings.isMultithreading());
	multithreading.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent arg0) {
		settings.setMultithreading(true);
	    }
	});
	add(multithreading);

	JRadioButtonMenuItem singlethreading = new JRadioButtonMenuItem(names[4]);
	gruppe_threading.add(singlethreading);
	singlethreading.setSelected(!settings.isMultithreading());
	singlethreading.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent arg0) {
		settings.setMultithreading(false);
	    }
	});
	add(singlethreading);
    }

}
