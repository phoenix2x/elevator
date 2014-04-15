package com.epam.elevatortask.ui.components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

import javax.swing.JComponent;

/**
 * Component to draw transportation process.
 *
 */
public class ElevatorGrapthComponent extends JComponent {
	private static final long serialVersionUID = 1L;

	private final int storiesNumber;
	private final int[] dispatchPassengers;
	private final int[] arrivalPassengers;
	private int doorSizeMax;
	private int doorSizeMin;
	private int doorSize;
	private int currentElevatorHeight;
	private int elevatorPassengers;
	private int passengerDispatchOffset;
	private int passengerArrivalOffset;
	private int dispatchPassengerNumberOffset;
	private int arrivalPassengerNumberOffset;
	private int elevatorPassengerNumberOffset;
	private int defaultDispatchPassengerOffset;
	private int defaultArrivalPassengerOffset;
	private int size;
	private int currentStory;
	
	/**
	 * @param storiesNumber
	 * @param dispatchPassengers
	 * @param arrivalPassengers
	 */
	public ElevatorGrapthComponent(int storiesNumber, int[] dispatchPassengers, int[] arrivalPassengers) {
		super();
		this.storiesNumber = storiesNumber;
		this.dispatchPassengers = dispatchPassengers;
		this.arrivalPassengers = arrivalPassengers;
	}

	/**
	 * @param currentStory
	 *            the currentStory to set
	 */
	public void setCurrentStory(int currentStory) {
		this.currentStory = currentStory;
	}

	/**
	 * @param dispatchPassengers
	 *            the dispatchPassengers to set
	 */
	public void setDispatchPassengers(int dispatchPassengers) {
		this.dispatchPassengers[currentStory] = dispatchPassengers;
	}

	/**
	 * @param arrivalPassengers
	 *            the arrivalPassengers to set
	 */
	public void setArrivalPassengers(int arrivalPassengers) {
		this.arrivalPassengers[currentStory] = arrivalPassengers;
	}

	/**
	 * @param elevatorPassengers
	 *            the elevatorPassengers to set
	 */
	public void setElevatorPassengers(int elevatorPassengers) {
		this.elevatorPassengers = elevatorPassengers;
	}

	/**
	 * Calculate stories size depending on stories number. Also set defaults.
	 */
	public void calculateStoriesSize() {
		size = getHeight() / storiesNumber;
		doorSizeMax = size;
		doorSizeMin = size / 10;
		doorSize = doorSizeMax;
		currentElevatorHeight = getHeight() - (currentStory + 1) * size;
		defaultDispatchPassengerOffset = size / 3;
		defaultArrivalPassengerOffset = size / 2;
		setArrivalPassengerOffsetToDefault();
		setDispatchPassengerOffsetToDefault();
	}

	public void incDoorSize() {
		doorSize++;
	}

	public void decDoorSize() {
		doorSize--;
	}

	public boolean isDoorSizeMax() {
		return doorSize >= doorSizeMax;
	}

	public boolean isDoorSizeMin() {
		return doorSize <= doorSizeMin;
	}

	public boolean isElevatorOnStory() {
		return currentElevatorHeight == getHeight() - (currentStory + 1) * size;
	}

	public void changeCurrentElevatorHeight() {
		if (currentElevatorHeight < getHeight() - (currentStory + 1) * size) {
			currentElevatorHeight++;
		} else {
			currentElevatorHeight--;
		}
	}

	public void setBoardingOffset() {
		dispatchPassengerNumberOffset = 1;
		elevatorPassengerNumberOffset = -1;
		passengerDispatchOffset--;
	}

	public void clearBoardingOffset() {
		dispatchPassengerNumberOffset = 0;
		elevatorPassengerNumberOffset = 0;
		setDispatchPassengerOffsetToDefault();
	}

	public void incArrivalPassengerOffset() {
		passengerArrivalOffset++;
	}

	public boolean isDispatchPassengerOffsetZero() {
		return passengerDispatchOffset <= 0;
	}

	public boolean isArrivalPassengerOffsetDef() {
		return passengerArrivalOffset >= defaultArrivalPassengerOffset;
	}

	public void setDispatchPassengerOffsetToDefault() {
		passengerDispatchOffset = defaultDispatchPassengerOffset;
	}

	public void setArrivalPassengerOffsetToDefault() {
		passengerArrivalOffset = defaultArrivalPassengerOffset;
	}

	public void setArrivalPassengerOffsetToMin() {
		passengerArrivalOffset = defaultArrivalPassengerOffset - size / 3;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (!isDoorSizeMin()) {
			drawElevatorPassengers(getHeight() - (currentStory + 1) * size, (Graphics2D) g, elevatorPassengers
					+ elevatorPassengerNumberOffset);
		}
		for (int i = 0; i < storiesNumber; i++) {
			int currentHeight = getHeight() - (i + 1) * size;
			drawElevator(currentHeight, g);
			drawStory(currentHeight, g);
			if (i == currentStory) {
				drawDispatchPassengers(currentHeight, (Graphics2D) g, dispatchPassengers[i]
						+ dispatchPassengerNumberOffset, passengerDispatchOffset);
				drawArrivalPassengers(currentHeight, (Graphics2D) g, arrivalPassengers[i]
						+ arrivalPassengerNumberOffset, passengerArrivalOffset);
			} else {
				drawDispatchPassengers(currentHeight, (Graphics2D) g, dispatchPassengers[i],
						defaultDispatchPassengerOffset);
				drawArrivalPassengers(currentHeight, (Graphics2D) g, arrivalPassengers[i],
						defaultArrivalPassengerOffset);
			}
		}
		drawElevatorDoors(currentElevatorHeight, g, doorSize);
	}

	private void drawDispatchPassengers(int currentHeight, Graphics2D g2, int numberPassengers,
			int currentPassengerOffset) {
		for (int i = 1; i <= numberPassengers; i++) {
			drawPassenger(getWidth() / 2 + currentPassengerOffset + i * size / 3, currentHeight + size / 3,
					size * 2 / 3, g2);
		}
	}

	private void drawArrivalPassengers(int currentHeight, Graphics2D g2, int numberPassengers,
			int currentPassengerOffset) {
		for (int i = 1; i <= numberPassengers; i++) {
			drawPassenger(getWidth() / 2 - currentPassengerOffset - i * size / 3, currentHeight + size / 3,
					size * 2 / 3, g2);
		}
	}

	private void drawElevatorPassengers(int currentHeight, Graphics2D g2, int numberPassengers) {
		int maxElevatorPassengers = (numberPassengers < 7 ? numberPassengers : 7);
		for (int i = 1; i <= maxElevatorPassengers; i++) {
			drawPassenger(getWidth() / 2 - size / 2 + i * size / 10, currentHeight + size / 3, size * 2 / 3, g2);
		}
	}

	private void drawPassenger(int x, int y, int height, Graphics2D g2) {
		int width = height / 3;
		g2.draw(new Ellipse2D.Double(x + width / 4, y, width / 2, height / 6));
		g2.draw(new Ellipse2D.Double(x + width / 4, y + height / 6, width / 2, height / 2));
		g2.drawLine(x, y + height / 2, x + width / 3, y + height / 5);
		g2.drawLine(x + width, y + height / 2, x + width * 2 / 3, y + height / 5);
		g2.drawLine(x, y + height, x + width / 2 - width / 7, y + height * 2 / 3);
		g2.drawLine(x + width, y + height, x + width / 2 + width / 7, y + height * 2 / 3);
	}

	private void drawStory(int currentHeight, Graphics g) {
		g.drawLine(0, currentHeight, getWidth(), currentHeight);
	}

	private void drawElevator(int currentHeight, Graphics g) {
		g.drawRect(getWidth() / 2 - size / 2, currentHeight, size, size);

	}

	private void drawElevatorDoors(int currentHeight, Graphics g, int doorSize) {
		g.setColor(Color.GRAY);
		g.fillRect(getWidth() / 2 - size / 2 + 1, currentHeight, size / 2 - doorSize / 2, size);
		g.fillRect(getWidth() / 2 + doorSize / 2 + 1, currentHeight, size / 2 - doorSize / 2, size);
		g.setColor(Color.BLACK);
	}
}
