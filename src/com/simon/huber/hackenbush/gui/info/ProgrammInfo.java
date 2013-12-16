package com.simon.huber.hackenbush.gui.info;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JDialog;
import javax.swing.JLabel;

/**
 * Erstellt und zeigt ein kurzen Info-Popup zum Programm an
 * @author Simon Huber
 *
 */
public class ProgrammInfo extends JDialog{

    private static final long serialVersionUID = 1L;

    public ProgrammInfo() {
	super();
	init();
    }
    
    /**
     * Befüllt das Popup
     */
    private void init() {
	setLayout(new GridBagLayout());
	setAlwaysOnTop(true);
	setModalityType(ModalityType.APPLICATION_MODAL);
	setTitle("Programm Infos");
	setSize(400, 150);
	setLocationByPlatform(true);
	
	GridBagConstraints c = new GridBagConstraints();
	c.insets = new Insets(2, 2, 2, 2);
	
	c.gridx = 0;
	c.gridy = 0;
	c.fill = GridBagConstraints.HORIZONTAL;
	c.weightx = 1.0F;
	c.weighty = 1.0F;
	c.anchor = GridBagConstraints.CENTER;
	
	add(new JLabel("Hackenbush-Programm von Simon Huber"), c);
	
	c.gridx = 0;
	c.gridy = 1;
	c.fill = GridBagConstraints.HORIZONTAL;
	c.weightx = 1.0F;
	c.weighty = 1.0F;
	c.anchor = GridBagConstraints.CENTER;
	
	add(new JLabel("Version: 1.0_release"), c);
	
	c.gridx = 0;
	c.gridy = 2;
	c.fill = GridBagConstraints.HORIZONTAL;
	c.weightx = 1.0F;
	c.weighty = 1.0F;
	c.anchor = GridBagConstraints.CENTER;
	
	add(new JLabel("Build: 2013-11-05/1"), c);
	
	c.gridx = 0;
	c.gridy = 3;
	c.fill = GridBagConstraints.HORIZONTAL;
	c.weightx = 1.0F;
	c.weighty = 1.0F;
	c.anchor = GridBagConstraints.CENTER;
	
	add(new JLabel("Entstanden im Zuge der Seminararbeit für das Seminar \"Spieltheorie\""), c);
	
	setVisible(true);
    }
}
