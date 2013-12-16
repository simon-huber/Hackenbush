package com.simon.huber.hackenbush.actionlisteners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;

import com.simon.huber.hackenbush.dataobjects.HackenbushFigur;
import com.simon.huber.hackenbush.gui.ZoomToolbar;

/**
 * ActionListener für den Minus-Button der ZoomToolbar
 * @author Simon Huber
 *
 */
public class ZoomMinusActionListener implements ActionListener {

    private HackenbushFigur figur;
    private JTextField zoom;
    private ZoomToolbar zoomtoolbar;
    
    public ZoomMinusActionListener(ZoomToolbar zoomToolbar, HackenbushFigur figur, JTextField zoom) {
	this.figur = figur;
	this.zoom = zoom;
	this.zoomtoolbar = zoomToolbar;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
	//Zusätzliche Höhe mindestens 1
	if(figur.getEditHigh() <= 1) {
	    return;
	}
	figur.setEditHigh(figur.getEditHigh() -1);
	zoom.setText(figur.getEditHigh() + "");
	zoomtoolbar.getMitToolbar().repaint();
    }

}
