package com.epam.elevatortask.ui;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.SwingUtilities;

import com.epam.elevatortask.logic.Worker;

public class StartButtonListener implements ActionListener {
	private final Worker worker;
	/**
	 * @param worker
	 */
	public StartButtonListener(Worker worker) {
		super();
		this.worker = worker;
	}

	public void actionPerformed(ActionEvent evt) {
		JButton button = (JButton) evt.getSource();
		final ElevatorFrame elevatorFrame = (ElevatorFrame) SwingUtilities.getRoot(button);
		elevatorFrame.setButtonAbort();
		new Thread(new Runnable() {
			public void run() {
				worker.startTransportation();
				EventQueue.invokeLater(new Runnable() {
					
					public void run() {
						elevatorFrame.setButtonFinish();
					}
				});
			}
		}).start();
	}
}
