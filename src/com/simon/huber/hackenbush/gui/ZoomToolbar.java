package com.simon.huber.hackenbush.gui;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.simon.huber.hackenbush.HackenbushFenster;
import com.simon.huber.hackenbush.actionlisteners.ZoomMinusActionListener;
import com.simon.huber.hackenbush.actionlisteners.ZoomPlusActionListener;
import com.simon.huber.hackenbush.actionlisteners.ZoomTextActionListener;
import com.simon.huber.hackenbush.dataobjects.HackenbushFigur;

public class ZoomToolbar extends JPanel {

    private static final long serialVersionUID = 1L;

    private HackenbushFenster editor;
    // Buttons
    private JButton zoomplus = null;
    private JButton zoomminus = null;
    
    private JTextField zoom;

    public ZoomToolbar(HackenbushFenster t) {
	editor = t;
	initToolbar();
	// Damit KeyListener immer im Editor ankommt
	this.setFocusable(false);
    }

    private void initToolbar() {
	JLabel label_zoom = new JLabel(" Zusätzliche Anzeigehöhe: ");
	this.add(label_zoom);
	
	zoomminus = new JButton("-");
	zoomminus.setFocusable(false);
	this.add(zoomminus);
	
	zoom = new JTextField("1", 5);
	this.add(zoom);
	
	zoomplus = new JButton("+");
	zoomplus.setFocusable(false);
	this.add(zoomplus);
	
	zoom.setEnabled(false);
	zoomminus.setEnabled(false);
	zoomplus.setEnabled(false);
	
	repaint();
    }

    public void aktiviereButtons(HackenbushFigur figur) {
	zoomminus.addActionListener(new ZoomMinusActionListener(this, figur, zoom));
	ZoomTextActionListener temp = new ZoomTextActionListener(this, figur, zoom);
	zoom.addActionListener(temp);
	zoomplus.addActionListener(new ZoomPlusActionListener(this, figur, zoom));
	zoom.setEnabled(true);
	zoomminus.setEnabled(true);
	zoomplus.setEnabled(true);
    }
    
    public void deaktiviereButtons() {
	zoom.setEnabled(false);
	zoomminus.setEnabled(false);
	zoomplus.setEnabled(false);
    }
    
    public HackenbushFenster getMitToolbar() {
	return editor;
    }
    
    public void setZoomText(int i) {
	zoom.setText(i + "");
    }
}
