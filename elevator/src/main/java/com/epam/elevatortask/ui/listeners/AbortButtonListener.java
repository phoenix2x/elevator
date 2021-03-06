package com.epam.elevatortask.ui.listeners;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.SwingUtilities;

import com.epam.elevatortask.interfaces.IElevatorWorker;
import com.epam.elevatortask.ui.forms.ElevatorFrame;

/**
 * Listener receives abort action event from main frame. 
 *
 */
public class AbortButtonListener implements ActionListener {
	private final IElevatorWorker worker;

	public AbortButtonListener(IElevatorWorker worker) {
		this.worker = worker;
	}

	/*
	 * Tries to abort transportation process through worker.stopTransportation
	 * method. Also invokes elevatorFrame.setButtonFinish method.
	 */
	@Override
	public void actionPerformed(ActionEvent evt) {
		JButton button = (JButton) evt.getSource();
		final ElevatorFrame elevatorFrame = (ElevatorFrame) SwingUtilities.getRoot(button);
		button.setEnabled(false);
		new Thread(new Runnable() {
			@Override
			public void run() {
				worker.abortTransportation();
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						elevatorFrame.setButtonFinish();
					}
				});
			}
		}).start();
	}
}
