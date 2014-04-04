package com.epam.elevatortask.ui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JButton;

import com.epam.elevatortask.logic.Worker;
import com.epam.elevatortask.ui.beans.ElevatorGrapthComponent;

import java.awt.Color;

import javax.swing.border.LineBorder;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ElevatorFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String START = "Start";
	private static final String ABORT = "Abort";
	private static final String FINISH = "View log file";
	private final Worker worker;
	private JPanel contentPane;
	private JTextArea textArea;
	private JButton btnStart;
	private ElevatorGrapthComponent elevatorGrapthComponent;


	/**
	 * Create the frame.
	 */
	public ElevatorFrame(Worker worker,int storiesNumber) {
		this.worker = worker;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1024, 768);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(496, 11, 510, 719);
		contentPane.add(scrollPane);
		
		textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		
		btnStart = new JButton(START);
		btnStart.addActionListener(new StartButtonListener(worker));
		btnStart.setBounds(184, 685, 129, 23);
		contentPane.add(btnStart);
		
		elevatorGrapthComponent = new ElevatorGrapthComponent(storiesNumber);
		elevatorGrapthComponent.setBorder(new LineBorder(Color.BLACK));
		elevatorGrapthComponent.setBackground(Color.WHITE);
		elevatorGrapthComponent.setBounds(10, 11, 476, 659);
		elevatorGrapthComponent.calculateStoriesSize();
		contentPane.add(elevatorGrapthComponent);
		
		JButton btnNewButton = new JButton("New button");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("qqq");
			}
		});
		btnNewButton.setBounds(348, 685, 91, 23);
		contentPane.add(btnNewButton);
	}
	public JTextArea getJTextArea(){
		return textArea;
	}
	public ElevatorGrapthComponent getElevatorGrapthComponent(){
		return elevatorGrapthComponent;
	}
	public void setButtonAbort(){
		btnStart.setText(ABORT);
		ActionListener[] actionListeners = btnStart.getActionListeners();
		if (actionListeners.length!=0){
			btnStart.removeActionListener(actionListeners[0]);
		}
		btnStart.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						worker.abortTransportation();
						worker.printOnAbort();
						setButtonFinish();
						
					}
				}).start();
				
			}
		});
	}
	public void setButtonFinish(){
		btnStart.setText(FINISH);
		ActionListener[] actionListeners = btnStart.getActionListeners();
		if (actionListeners.length!=0){
			btnStart.removeActionListener(actionListeners[0]);
		}
		btnStart.addActionListener(new FinishButtonListener());
	}
}
