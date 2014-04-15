package com.epam.elevatortask.ui.listeners;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JButton;

import com.epam.elevatortask.ui.forms.FileViewer;

/**
 * Listener receive open log action event from main frame, and open log file content in fileViewer frame.
 *
 */
public class FinishButtonListener implements ActionListener {

	private static final String LOG_FILENAME = "application.log";

	@Override
	public void actionPerformed(ActionEvent e) {
		final JButton button = (JButton) e.getSource();
		button.setEnabled(false);
		final FileViewer fileViewer = new FileViewer();
		new Thread(new Runnable() {
			@Override
			public void run() {
				try (BufferedReader reader = new BufferedReader(new FileReader(LOG_FILENAME))) {
					fileViewer.getTextArea().read(reader, LOG_FILENAME);
				} catch (IOException e) {
					e.printStackTrace();
					fileViewer.getTextArea().append(e.toString());
				}
				EventQueue.invokeLater(new Runnable() {
					@Override
					public void run() {
						fileViewer.setVisible(true);
						button.setEnabled(true);
					}
				});
			}
		}).start();
	}
}
