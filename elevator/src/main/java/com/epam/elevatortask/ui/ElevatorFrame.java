package com.epam.elevatortask.ui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JButton;

import com.epam.elevatortask.beans.NumberedStoryContainer;
import com.epam.elevatortask.beans.Passenger;
import com.epam.elevatortask.logic.Worker;
import com.epam.elevatortask.ui.beans.ElevatorGrapthComponent;

import java.awt.Color;

import javax.swing.border.LineBorder;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;

import java.awt.GridLayout;
import javax.swing.SwingConstants;

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
	private JPanel panel;
	private JScrollPane scrollPane_1;
	private JScrollPane scrollPane_2;
	private JLabel lblElevatorcontainer;
	private JLabel lblElevatorcontinersize;
	private List<JLabel> dispatchLabelsList = new ArrayList<>();
	private List<JLabel> arrivalLabelsList = new ArrayList<>();
	private JPanel arrivalPanel;
	private JPanel dispatchPanel;
	private JLabel lblArrival;
	private JLabel lblDispatch;
	private JLabel lblTotal;
	private JLabel lblTotalSize;
	


	/**
	 * Create the frame.
	 */
	public ElevatorFrame(Worker worker,int storiesNumber) {
		setTitle("Elevator");
		this.worker = worker;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1024, 768);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(496, 415, 510, 315);
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
		
		panel = new JPanel();
		panel.setBounds(496, 11, 510, 393);
		contentPane.add(panel);
		panel.setLayout(null);
		
		scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(10, 52, 240, 308);
		panel.add(scrollPane_1);
		
		arrivalPanel = new JPanel();
		scrollPane_1.setViewportView(arrivalPanel);
		arrivalPanel.setLayout(new GridLayout(0, 2, 0, 0));
		
		lblArrival = new JLabel("Arrival");
		lblArrival.setHorizontalAlignment(SwingConstants.CENTER);
		scrollPane_1.setColumnHeaderView(lblArrival);
		
		
		
		scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(260, 52, 240, 308);
		panel.add(scrollPane_2);
		
		dispatchPanel = new JPanel();
		scrollPane_2.setViewportView(dispatchPanel);
		dispatchPanel.setLayout(new GridLayout(0, 2, 0, 0));
		
		lblDispatch = new JLabel("Dispatch");
		lblDispatch.setHorizontalAlignment(SwingConstants.CENTER);
		scrollPane_2.setColumnHeaderView(lblDispatch);
		
		lblElevatorcontainer = new JLabel("ElevatorContainer");
		lblElevatorcontainer.setBounds(44, 11, 162, 14);
		panel.add(lblElevatorcontainer);
		
		lblElevatorcontinersize = new JLabel(String.valueOf(worker.getElevatorContainer().getPassengersList().size()));
		lblElevatorcontinersize.setBounds(216, 11, 284, 14);
		panel.add(lblElevatorcontinersize);
		
		lblTotal = new JLabel("Total");
		lblTotal.setBounds(44, 371, 102, 14);
		panel.add(lblTotal);
		
		lblTotalSize = new JLabel(String.valueOf(worker.getPassengersNumber()));
		lblTotalSize.setBounds(156, 371, 344, 14);
		panel.add(lblTotalSize);
		
		
		for (NumberedStoryContainer<Passenger> storyContainer: worker.getDispatchStoryContainersList()){
			dispatchPanel.add(new JLabel("Story " + storyContainer.getStoryNumber()));
			
			JLabel lblNewLabel_1 = new JLabel(String.valueOf(storyContainer.getPassengersList().size()));
			dispatchLabelsList.add(lblNewLabel_1);
			dispatchPanel.add(lblNewLabel_1);
		}
		for (NumberedStoryContainer<Passenger> storyContainer: worker.getArrivalStoryContainersList()){
			arrivalPanel.add(new JLabel("Story " + storyContainer.getStoryNumber()));
			
			JLabel lblNewLabel_1 = new JLabel(String.valueOf(storyContainer.getPassengersList().size()));
			arrivalLabelsList.add(lblNewLabel_1);
			arrivalPanel.add(lblNewLabel_1);
		}
		
	}
	public void updateData(){
		lblElevatorcontinersize.setText(String.valueOf(worker.getElevatorContainer().getPassengersList().size()));
		for (int i = 0; i<dispatchLabelsList.size(); i++){
			dispatchLabelsList.get(i).setText(String.valueOf(worker.getDispatchStoryContainersList().get(i).getPassengersList().size()));
			arrivalLabelsList.get(i).setText(String.valueOf(worker.getArrivalStoryContainersList().get(i).getPassengersList().size()));
		}
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
		btnStart.addActionListener(new AbortButtonListener(worker));	
	}
	public void setButtonFinish(){
		btnStart.setText(FINISH);
		ActionListener[] actionListeners = btnStart.getActionListeners();
		if (actionListeners.length!=0){
			btnStart.removeActionListener(actionListeners[0]);
		}
		btnStart.addActionListener(new FinishButtonListener());
		btnStart.setEnabled(true);
	}
}
