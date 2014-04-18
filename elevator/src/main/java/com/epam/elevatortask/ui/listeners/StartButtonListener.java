package com.epam.elevatortask.ui.listeners;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.SwingUtilities;

import com.epam.elevatortask.interfaces.IElevatorWorker;
import com.epam.elevatortask.ui.forms.ElevatorFrame;

/**
 * Listener receives start action event from main frame.
 *
 */
public class StartButtonListener implements ActionListener {
	private final IElevatorWorker worker;
	/**
	 * @param worker
	 */
	public StartButtonListener(IElevatorWorker worker) {
		super();
		this.worker = worker;
	}
	/*
	 * Tries to start transportation process through worker.startTransportationGUI
	 * method. Also invokes elevatorFrame.setButtonFinish method after work finished.
	 */
	public void actionPerformed(ActionEvent evt) {
		JButton button = (JButton) evt.getSource();
		final ElevatorFrame elevatorFrame = (ElevatorFrame) SwingUtilities.getRoot(button);
		elevatorFrame.setButtonAbort();
		new Thread(new Runnable() {
			public void run() {
				worker.startTransportationGUI();
				EventQueue.invokeLater(new Runnable() {
					
					public void run() {
						elevatorFrame.setButtonFinish();
					}
				});
			}
		}).start();
	}
}
