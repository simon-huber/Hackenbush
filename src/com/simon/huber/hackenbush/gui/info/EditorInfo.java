package com.simon.huber.hackenbush.gui.info;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.simon.huber.hackenbush.create.HackenbushEditor;
import com.simon.huber.hackenbush.resources.Settings;

/**
 * Hilfe Popup für den Editor
 * @author Simon Huber
 *
 */
public class EditorInfo extends JDialog {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * ContentPane
     */
    private JPanel anzeige = new JPanel();
    
    /**
     * Settings damit checkBox "nicht mehr anzeigen" funktioniert
     */
    private Settings settings;
    private HackenbushEditor fenster;

    public EditorInfo(Settings settings, HackenbushEditor f) {
	this.settings = settings;
	fenster = f;
	setAlwaysOnTop(true);
	setLayout(new BorderLayout());
	setModalityType(ModalityType.APPLICATION_MODAL);
	setTitle("Editorinfo");
	setSize(500, 500);
	setLocationByPlatform(true);
	addContent();
	setVisible(true);
    }
    
    /**
     * Fügt Text hinzu
     */
    private void addContent() {
	setContentPane(anzeige);
	
	JLabel content = new JLabel();
	content.setText("<html>\n" +
			"<h2>Zoom</h2>\n" +
			"<p>Durch das Feld zusätliche Höhe \n" +
			"kann man aus der Figur herauszoomen und somit <br>\n" +
			"weitere Kanten einfügen.</p>\n" +
			"<h2>Einfügen von Kanten</h2>\n" +
			"<ol>\n" +
			"<li>Wählen Sie den Spieler, dem die Kante <br>" +
			"gehören soll.</li> \n" +
			"<li>Wählen Sie die Kantenart <br>" +
			"(Normalen Kante hat zwei Punkte, eine Kante <br>" +
			"mit Verbindungspunkt, drei Punkte) </li> \n" +
			"<li>Den ersten Punkt der neuen Kante anklicken " +
			"<br>(beachten Sie, dass die Kante später eine Verbindung <br>" +
			"zum Boden haben muss)</li> \n" +
			"<li>Gegebenenfalls nun den Verbindugspunkt setzen. </li> \n" +
			"<li>Den zweiten Punkt der neuen Kante anklicken " +
			"<br>(beachten Sie, dass die Kante  jetzt eine Verbindung <br>" +
			"zum Boden haben muss)</li> \n" +
			"</ol>" +
			"<h2>Tastenzuordnungen</h2> \n" +
			"Beachten Sie, dass die Figur den Fokus hat!" +
			"<ul> \n" +
			"<li>ENTF: Löschen einer makierten Kante</li> \n" +
			"<li>N: Neuberechnen der Kantenwerte</li> \n" +
			"<li>Die normalen Tastenkürzel, die im Menü angegeben sind.</li> \n" +
			"</ul> \n" +
			"</html>");
	anzeige.add(content, BorderLayout.NORTH);
	
	
	JCheckBox checkbox = new JCheckBox("Nicht mehr anzeigen");
	checkbox.addActionListener(new ActionListener() {
	    
	    @Override
	    public void actionPerformed(ActionEvent arg0) {
		JCheckBox item = (JCheckBox) arg0.getSource();
		settings.setShowEditorInfo(!item.isSelected());
		fenster.rebuildEinstellungsMenu();
	    }
	});
	anzeige.add(checkbox, BorderLayout.CENTER);
	
	
	JButton button = new JButton("Schließen");
	button.addActionListener(new ActionListener() {
	    
	    @Override
	    public void actionPerformed(ActionEvent arg0) {
		dispose();
	    }
	});
	anzeige.add(button, BorderLayout.AFTER_LAST_LINE);
    }
    
}
