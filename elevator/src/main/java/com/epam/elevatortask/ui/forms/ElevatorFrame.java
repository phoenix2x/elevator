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
import com.epam.elevatortask.ui.listeners.ElevatorComponentListener;
import com.epam.elevatortask.ui.listeners.FinishButtonListener;
import com.epam.elevatortask.ui.listeners.StartButtonListener;

import java.awt.Color;

import javax.swing.border.LineBorder;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;

import java.awt.GridLayout;
import java.io.IOException;

import javax.swing.SwingConstants;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

import java.awt.BorderLayout;

/**
 * Main application frame.
 * 
 */
public class ElevatorFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private static final String STORY = "Story ";
	private static final String DISPATCH = "Dispatch";
	private static final String ARRIVAL = "Arrival";
	private static final String TOTAL = "Total";
	private static final String ELEVATOR_CONTAINER = "ElevatorContainer";
	private static final String ELEVATOR = "Elevator";
	private static final String START = "Start";
	private static final String ABORT = "Abort";
	private static final String FINISH = "View log file";
	private final IElevatorWorker worker;
	private JPanel contentPane;
	private JTextArea textArea;
	private JButton mainButton;
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
	private JPanel panel_1;
	private JPanel panel_2;
	private JPanel panel_3;

	/**
	 * Create the frame.
	 * 
	 * @throws IOException
	 */
	public ElevatorFrame(Building<Passenger> building, int storiesNumber, IElevatorWorker worker, int passengersNumber)
			throws IOException, IllegalArgumentException {
		setTitle(ELEVATOR);
		this.worker = worker;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1024, 768);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		mainButton = new JButton(START);
		mainButton.setAlignmentY(Component.BOTTOM_ALIGNMENT);
		mainButton.addActionListener(new StartButtonListener(worker));
		int[] dispatchPassengers = new int[storiesNumber];
		int[] arrivalPassengers = new int[storiesNumber];
		for (int i = 0; i < building.getStoriesNumber(); i++) {
			dispatchPassengers[i] = building.getDispatchContainer(i).getPassengersNumber();
			arrivalPassengers[i] = building.getArrivalContainer(i).getPassengersNumber();
		}
		elevatorGrapthComponent = new ElevatorGrapthComponent(storiesNumber, dispatchPassengers, arrivalPassengers);
		elevatorGrapthComponent.setMinimumSize(new Dimension(540, 660));
		elevatorGrapthComponent.setPreferredSize(new Dimension(540, 660));
		elevatorGrapthComponent.setBorder(new LineBorder(Color.BLACK));
		elevatorGrapthComponent.setBackground(Color.WHITE);
		elevatorGrapthComponent.addComponentListener(new ElevatorComponentListener(elevatorGrapthComponent));

		panel = new JPanel();
		panel.setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane = new JScrollPane();

		textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(6)
							.addComponent(elevatorGrapthComponent, GroupLayout.DEFAULT_SIZE, 540, Short.MAX_VALUE))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGap(248)
							.addComponent(mainButton)))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addComponent(panel, GroupLayout.DEFAULT_SIZE, 444, Short.MAX_VALUE)
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 444, Short.MAX_VALUE))
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(elevatorGrapthComponent, GroupLayout.DEFAULT_SIZE, 660, Short.MAX_VALUE)
							.addGap(18)
							.addComponent(mainButton))
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(panel, GroupLayout.DEFAULT_SIZE, 358, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 337, GroupLayout.PREFERRED_SIZE)))
					.addGap(19))
		);

		panel_1 = new JPanel();
		panel.add(panel_1, BorderLayout.NORTH);

		lblElevatorcontainer = new JLabel(ELEVATOR_CONTAINER);
		panel_1.add(lblElevatorcontainer);
		lblElevatorcontainer.setAlignmentY(Component.TOP_ALIGNMENT);

		lblElevatorcontinersize = new JLabel(String.valueOf(building.getElevatorContainer().getPassengersNumber()));
		panel_1.add(lblElevatorcontinersize);

		panel_2 = new JPanel();
		panel.add(panel_2, BorderLayout.SOUTH);

		lblTotal = new JLabel(TOTAL);
		panel_2.add(lblTotal);

		lblTotalSize = new JLabel(String.valueOf(passengersNumber));
		panel_2.add(lblTotalSize);

		scrollPane_1 = new JScrollPane();
		scrollPane_1.setPreferredSize(new Dimension(200, 200));
		panel.add(scrollPane_1, BorderLayout.WEST);

		arrivalPanel = new JPanel();
		scrollPane_1.setViewportView(arrivalPanel);
		arrivalPanel.setLayout(new GridLayout(0, 2, 0, 0));

		lblArrival = new JLabel(ARRIVAL);
		lblArrival.setHorizontalAlignment(SwingConstants.CENTER);
		scrollPane_1.setColumnHeaderView(lblArrival);

		scrollPane_2 = new JScrollPane();
		scrollPane_2.setPreferredSize(new Dimension(200, 200));
		panel.add(scrollPane_2, BorderLayout.EAST);

		dispatchPanel = new JPanel();
		scrollPane_2.setViewportView(dispatchPanel);
		dispatchPanel.setLayout(new GridLayout(0, 2, 0, 0));

		lblDispatch = new JLabel(DISPATCH);
		lblDispatch.setHorizontalAlignment(SwingConstants.CENTER);
		scrollPane_2.setColumnHeaderView(lblDispatch);

		panel_3 = new JPanel();
		panel_3.setPreferredSize(new Dimension(10, 200));
		panel.add(panel_3, BorderLayout.CENTER);
		contentPane.setLayout(gl_contentPane);

		for (int i = 0; i < building.getStoriesNumber(); i++) {
			dispatchPanel.add(new JLabel(STORY + i));
			arrivalPanel.add(new JLabel(STORY + i));

			JLabel newDispatchLabel = new JLabel(String.valueOf(dispatchPassengers[i]));
			dispatchLabelsList.add(newDispatchLabel);
			dispatchPanel.add(newDispatchLabel);

			JLabel newArrivalLabel = new JLabel(String.valueOf(arrivalPassengers[i]));
			arrivalLabelsList.add(newArrivalLabel);
			arrivalPanel.add(newArrivalLabel);
		}
	}

	/**
	 * @return textArea
	 */
	public JTextArea getJTextArea() {
		return textArea;
	}

	/**
	 * @return elevatorGrapthComponent
	 */
	public ElevatorGrapthComponent getElevatorGrapthComponent() {
		return elevatorGrapthComponent;
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

	/**
	 * Change main button function to abort
	 */
	public void setButtonAbort() {
		mainButton.setText(ABORT);
		for (ActionListener actionListener : mainButton.getActionListeners()) {
			mainButton.removeActionListener(actionListener);
		}
		mainButton.addActionListener(new AbortButtonListener(worker));
	}

	/**
	 * Change main button function to finish
	 */
	public void setButtonFinish() {
		mainButton.setText(FINISH);
		for (ActionListener actionListener : mainButton.getActionListeners()) {
			mainButton.removeActionListener(actionListener);
		}
		mainButton.addActionListener(new FinishButtonListener());
		mainButton.setEnabled(true);
	}
}
