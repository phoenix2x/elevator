package com.epam.elevatortask.ui;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.SwingUtilities;

import com.epam.elevatortask.logic.Worker;

public class AbortButtonListener implements ActionListener {
	private final Worker worker;
	
	public AbortButtonListener(Worker worker){
		this.worker = worker;
	}

	@Override
	public void actionPerformed(ActionEvent evt) {
		JButton button = (JButton) evt.getSource();
		final ElevatorFrame elevatorFrame = (ElevatorFrame) SwingUtilities.getRoot(button);
		button.setEnabled(false);
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				worker.abortTransportation();
//				worker.printOnAbort();
				
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						elevatorFrame.setButtonFinish();
					}
				});
				
			}
		}).start();
		
	}

}
