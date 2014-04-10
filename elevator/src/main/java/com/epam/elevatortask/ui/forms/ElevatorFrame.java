package com.epam.elevatortask.ui.forms;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JButton;

import com.epam.elevatortask.beans.Building;
import com.epam.elevatortask.beans.Passenger;
import com.epam.elevatortask.interfaces.IElevatorWorker;
import com.epam.elevatortask.ui.components.ElevatorGrapthComponent;
import com.epam.elevatortask.ui.listeners.AbortButtonListener;
import com.epam.elevatortask.ui.listeners.FinishButtonListener;
import com.epam.elevatortask.ui.listeners.StartButtonListener;

import java.awt.Color;

import javax.swing.border.LineBorder;

import java.awt.event.ActionListener;
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
	private final IElevatorWorker worker;
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
	public ElevatorFrame(Building<Passenger> building,int storiesNumber, IElevatorWorker worker, int passengersNumber) {
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
		
		lblElevatorcontinersize = new JLabel(String.valueOf(building.getElevatorContainer().getPassengersNumber()));
		lblElevatorcontinersize.setBounds(216, 11, 284, 14);
		panel.add(lblElevatorcontinersize);
		
		lblTotal = new JLabel("Total");
		lblTotal.setBounds(44, 371, 102, 14);
		panel.add(lblTotal);
		
		lblTotalSize = new JLabel(String.valueOf(passengersNumber));
		lblTotalSize.setBounds(156, 371, 344, 14);
		panel.add(lblTotalSize);
		
		int[] dispatchPassengers = new int[storiesNumber];
		int[] arrivalPassengers = new int[storiesNumber];
		for (int i=0; i < building.getStoriesNumber(); i++){
			dispatchPassengers[i] = building.getDispatchContainer(i).getPassengersNumber();
			arrivalPassengers[i] = building.getArrivalContainer(i).getPassengersNumber();
			
			dispatchPanel.add(new JLabel("Story " + i));
			arrivalPanel.add(new JLabel("Story " + i));
			
			JLabel newDispatchLabel = new JLabel(String.valueOf(dispatchPassengers[i]));
			dispatchLabelsList.add(newDispatchLabel);
			dispatchPanel.add(newDispatchLabel);
			
			JLabel newArrivalLabel = new JLabel(String.valueOf(arrivalPassengers[i]));
			arrivalLabelsList.add(newArrivalLabel);
			arrivalPanel.add(newArrivalLabel);
		}		
		elevatorGrapthComponent = new ElevatorGrapthComponent(storiesNumber, dispatchPassengers, arrivalPassengers);
		elevatorGrapthComponent.setBorder(new LineBorder(Color.BLACK));
		elevatorGrapthComponent.setBackground(Color.WHITE);
		elevatorGrapthComponent.setBounds(10, 11, 476, 659);
		elevatorGrapthComponent.calculateStoriesSize();
		contentPane.add(elevatorGrapthComponent);
	}
//	public void updateData(int elevatorPassengerNumber,int[] dispatchPassengerNumber, int[] arrivalPassengerNumber){
//		lblElevatorcontinersize.setText(String.valueOf(worker.getElevatorContainer().getPassengersList().size()));
//		for (int i = 0; i<dispatchLabelsList.size(); i++){
//			dispatchLabelsList.get(i).setText(String.valueOf(worker.getDispatchStoryContainersList().get(i).getPassengersList().size()));
//			arrivalLabelsList.get(i).setText(String.valueOf(worker.getArrivalStoryContainersList().get(i).getPassengersList().size()));
//		}
//	}
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
	/**
	 * @return the dispatchLabelsList
	 */
	public List<JLabel> getDispatchLabelsList() {
		return dispatchLabelsList;
	}
	/**
	 * @return the arrivalLabelsList
	 */
	public List<JLabel> getArrivalLabelsList() {
		return arrivalLabelsList;
	}
	/**
	 * @return the lblElevatorcontinersize
	 */
	public JLabel getLblElevatorcontinersize() {
		return lblElevatorcontinersize;
	}
}
