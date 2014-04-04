package com.epam.elevatortask.ui;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import com.epam.elevatortask.logic.Worker;

public class StartButtonListener implements ActionListener {
	private final Worker worker;
//	private final ElevatorFrame elevatorFrame;
	/**
	 * @param worker
	 */
	public StartButtonListener(Worker worker) {
		super();
		this.worker = worker;
//		this.elevatorFrame = elevatorFrame;
	}

	public void actionPerformed(ActionEvent e) {
		JButton button = (JButton) e.getSource();
		final ElevatorFrame elevatorFrame = (ElevatorFrame)  button.getParent().getParent().getParent().getParent();
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
