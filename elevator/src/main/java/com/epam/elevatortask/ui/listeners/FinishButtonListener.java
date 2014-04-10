package com.epam.elevatortask.ui.listeners;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JButton;

import com.epam.elevatortask.ui.forms.FileViewer;

public class FinishButtonListener implements ActionListener {

	private static final String LOG_FILENAME = "Controller.log";

	public void actionPerformed(ActionEvent e) {
		final JButton button = (JButton) e.getSource();
		button.setEnabled(false);
		new Thread(new Runnable() {	
			@Override
			public void run() {
				FileViewer fileViewer = new FileViewer();
				try {
					fileViewer.getTextArea().read(new FileReader(LOG_FILENAME), null);
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				fileViewer.setVisible(true);
				EventQueue.invokeLater(new Runnable() {
					@Override
					public void run() {
						button.setEnabled(true);
					}
				});
			}
		}).start();
	}
}
