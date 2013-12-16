package com.simon.huber.hackenbush.gui.info;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

/**
 * Erstellt und befüllt das Info-Menü
 * @author Simon Huber
 *
 */
public class InfoMenu extends JMenu {

    private static final long serialVersionUID = 1L;

    public InfoMenu() {
	super("Infos");
	createMenu();
    }
    
    /**
     * Befüllt das neue Menü
     */
    private void createMenu() {
	JMenuItem about = new JMenuItem("Programminfos");
	about.addActionListener(new ActionListener() {
	    
	    @Override
	    public void actionPerformed(ActionEvent arg0) {
		new ProgrammInfo();
	    }
	});
	add(about);
    }

}
