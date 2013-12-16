package com.simon.huber.hackenbush.gui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import com.simon.huber.hackenbush.HauptFenster;
import com.simon.huber.hackenbush.exceptions.KanteNichtGefundenException;
import com.simon.huber.hackenbush.exceptions.PunktNichtGefundenException;
import com.simon.huber.hackenbush.exceptions.ZuWenigeTeilnehmerException;
import com.simon.huber.hackenbush.spieler.Computer;
import com.simon.huber.hackenbush.spieler.Spieler;
import com.simon.huber.hackenbush.throwable.SpielerGewonnenMessage;

/**
 * Erstellt Popup zum Starten eines neuen Spiels
 * @author Simon Huber
 *
 */
public class StartGameFrame extends JDialog {

    private static final long serialVersionUID = 1L;
    
    private HauptFenster frame;
    private byte[] type = new byte[] { 0, 1 };

    /**
     * Ein neues Spiel kann nur von einem Hauptfenster gestarted werden
     * 
     * @param frame
     *            Hauptfenster
     */
    public StartGameFrame(HauptFenster frame) {
	super(frame, "Neues Spiel", true);
	this.frame = frame;
	setSize(400, 150);
	setLocationRelativeTo(frame);
	setLayout(new GridBagLayout());
	addComponents();
	setVisible(true);
    }

    /**
     * Fügt alle Komponenten zum Fenster hinzu.
     */
    private void addComponents() {
	GridBagConstraints c = new GridBagConstraints();
	c.fill = GridBagConstraints.BOTH;

	c.gridx = 0;
	c.gridy = 1;
	c.gridwidth = 1;
	c.weightx = 1.0F;
	c.weighty = 1.0F;
	c.insets = new Insets(2, 5, 2, 2);
	add(buildPlayerAnzeige(0), c);

	c.gridx = 1;
	c.gridy = 1;
	c.insets = new Insets(2, 2, 2, 5);
	add(buildPlayerAnzeige(1), c);

	//Buttons füllen das Gitter bloß horizontal
	c.fill = GridBagConstraints.HORIZONTAL;
	
	JButton buttonAbbruch = new JButton("Abbrechen");
	buttonAbbruch.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent event) {
		dispose();
	    }
	});
	c.gridx = 0;
	c.gridy = 3;
	add(buttonAbbruch, c);
	
	JButton buttonStart = new JButton("Starten");
	buttonStart.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent event) {
		start();
	    }
	});
	c.gridx = 1;
	c.gridy = 3;
	c.weighty = 0.0F;
	add(buttonStart, c);
    }
    
    /**
     * Erzeugt ein neues Spiel
     */
    private void start() {
	// Spieler erstellen
	for (int i = 0; i < 2; i++) {
	    if (type[i] == 0) {
		frame.getHackenbushGraphics().getSpiel().getTeilnehmer()[i] = new Spieler(frame.getSpiel(), i + 1);
	    } else {
		frame.getHackenbushGraphics().getSpiel().getTeilnehmer()[i] = new Computer(frame.getSpiel(), i + 1);
	    }
	}

	// Spiel starten
	try {
	    frame.getHackenbushGraphics().getSpiel().startGame(frame.getHackenbushGraphics());
	    frame.getSpielMenu().setStartGameEnabled(false);
	} catch (ZuWenigeTeilnehmerException e) {
	    JOptionPane.showMessageDialog(this, e.getMessage(), "Fehler beim Spielstart!", JOptionPane.ERROR_MESSAGE);
	    e.printStackTrace();
	} catch (PunktNichtGefundenException | KanteNichtGefundenException e1) {
	    JOptionPane.showMessageDialog(this, "Entfernen der Kante fehlgeschlagen: " + e1.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
	    e1.printStackTrace();
	} catch (SpielerGewonnenMessage e) {
	    JOptionPane.showMessageDialog(this, e.getMessage(), "Spielende!", JOptionPane.INFORMATION_MESSAGE);
	}
	dispose();
    }

    /**
     * Erzeugt ein JPanel, das zur Initialisierung der Spieler dient
     * 
     * @return Spielerpanel.
     */
    private JPanel buildPlayerAnzeige(final int id) {
	GridBagConstraints c = new GridBagConstraints();
	c.insets = new Insets(2, 2, 2, 2);

	JPanel panel = new JPanel();
	JLabel labelType = new JLabel("Spielertyp:");
	JComboBox<String> comboBoxType = new JComboBox<String>(new String[] { "Spieler", "Computer" });

	panel.setLayout(new GridBagLayout());

	c.gridx = 0;
	c.gridy = 3;
	c.fill = GridBagConstraints.NONE;
	c.weightx = 1.0F;
	c.weighty = 1.0F;
	c.anchor = GridBagConstraints.LINE_START;
	panel.add(labelType, c); //Hinzufügen Typenlabel

	comboBoxType.setSelectedIndex(type[id]);
	comboBoxType.addItemListener(new ItemListener() {
	    @Override
	    public void itemStateChanged(ItemEvent event) {
		if(((String) event.getItem()).equals("Spieler") ) {
		    type[id] = 0;
		} else {
		    type[id] = 1;
		}
	    }
	});
	
	c.gridx = 1;
	c.gridy = 3;
	c.fill = GridBagConstraints.HORIZONTAL;
	c.anchor = GridBagConstraints.CENTER;
	panel.add(comboBoxType, c); //Hinzufügen Auswahlfeld
	
	c.gridx = 0;
	c.gridy = 0;
	c.gridwidth = 2;
	c.gridheight = 2;
	c.fill = GridBagConstraints.BOTH; //Die kompletten zwei Gitter werden mit Farbe gefüllt
	c.anchor = GridBagConstraints.CENTER;
	JPanel farbe = new JPanel();
	farbe.setBackground(id == 0 ? Color.BLUE : Color.RED);
	panel.add(farbe, c); //Hinzufügen Farbanzeige

	return panel;
    }

}
