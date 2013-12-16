package com.simon.huber.hackenbush.actionlisteners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;

import com.simon.huber.hackenbush.dataobjects.HackenbushFigur;
import com.simon.huber.hackenbush.gui.ZoomToolbar;

/**
 * ActionListener für den Plus-Button der ZoomToolbar
 * @author Simon Huber
 *
 */
public class ZoomPlusActionListener implements ActionListener {

    private HackenbushFigur figur;
    private JTextField zoom;
    private ZoomToolbar toolbar;
    
    public ZoomPlusActionListener(ZoomToolbar toolbar, HackenbushFigur figur, JTextField zoom) {
	this.toolbar = toolbar;
	this.figur = figur;
	this.zoom = zoom;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
	figur.setEditHigh(figur.getEditHigh() + 1);
	zoom.setText(figur.getEditHigh() + "");
	toolbar.getMitToolbar().repaint();
    }

}
