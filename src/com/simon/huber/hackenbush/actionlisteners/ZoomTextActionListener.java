package com.simon.huber.hackenbush.actionlisteners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JTextField;

import com.simon.huber.hackenbush.dataobjects.HackenbushFigur;
import com.simon.huber.hackenbush.gui.ZoomToolbar;

/**
 * ActionListener für den das Eingabefeld der ZoomToolbar
 * @author Simon Huber
 *
 */
public class ZoomTextActionListener implements MouseListener, ActionListener {

    private HackenbushFigur figur;
    private JTextField textfield;
    private ZoomToolbar toolbar;

    public ZoomTextActionListener(ZoomToolbar toolbar, HackenbushFigur figur, JTextField textfield) {
	this.toolbar = toolbar;
	this.figur = figur;
	this.textfield = textfield;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
	if (textfield.equals(e.getComponent())) {
	    textfield.requestFocus();
	}
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void actionPerformed(ActionEvent e) {
	int temp = figur.getEditHigh();
	try {
	    temp = Integer.parseInt(textfield.getText());
	} catch (Exception e1) {
	    e1.printStackTrace();
	    //Keine Veränderung
	}
	if (temp < 1) {
	    temp = 1;
	}
	//Setzen neuer Höhe
	figur.setEditHigh(temp);
	textfield.setText(figur.getEditHigh() + "");
	toolbar.getMitToolbar().repaint();
	toolbar.getMitToolbar().requestFocus();
    }

}
