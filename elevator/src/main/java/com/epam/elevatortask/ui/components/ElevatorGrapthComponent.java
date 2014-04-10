package com.epam.elevatortask.ui.components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

import javax.swing.JComponent;

public class ElevatorGrapthComponent extends JComponent {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public enum DoorState {
		OPENED, HALF_OPENED, CLOSED
	}

	private final int storiesNumber;
	private final int[] dispatchPassengers;
	private final int[] arrivalPassengers;
	private int elevatorPassengers;
	private int size;
	private int currentStory;
	private DoorState elevatorAction = DoorState.CLOSED;

	/**
	 * 
	 */
	public ElevatorGrapthComponent(int storiesNumber, int[] dispatchPassengers, int[] arrivalPassengers) {
		super();
		this.storiesNumber = storiesNumber;
		this.dispatchPassengers = dispatchPassengers;
		this.arrivalPassengers = arrivalPassengers;
	}

	/**
	 * @return the currentStory
	 */
	public int getCurrentStory() {
		return currentStory;
	}

	/**
	 * @param currentStory
	 *            the currentStory to set
	 */
	public void setCurrentStory(int currentStory) {
		this.currentStory = currentStory;
	}

	/**
	 * @param doorState
	 *            the doorState to set
	 */
	public void setDoorState(DoorState doorState) {
		this.elevatorAction = doorState;
	}

	/**
	 * calculate stories size depending on stories number
	 */
	public void calculateStoriesSize() {
		size = getHeight() / storiesNumber;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (elevatorAction != DoorState.CLOSED) {
			drawElevatorPassengers(getHeight() - (currentStory + 1) * size, (Graphics2D) g, elevatorPassengers);
		}
		for (int i = 0; i < storiesNumber; i++) {
			int currentHeight = getHeight() - (i + 1) * size;
			drawElevator(currentHeight, g);
			drawStory(currentHeight, g);
			drawDispatchPassengers(currentHeight, (Graphics2D) g, dispatchPassengers[i]);
			drawArrivalPassengers(currentHeight, (Graphics2D) g, arrivalPassengers[i]);
			if (i == currentStory) {
				switch (elevatorAction) {
				case OPENED:
					drawElevatorDoors(currentHeight, g, (int) (size / 1.5));
					// drawOpenedElevator(currentHeight, g);
					break;
				case HALF_OPENED:
					drawElevatorDoors(currentHeight, g, size / 3);
					// drawHalfeOpenedElevator(currentHeight, g);
					break;
				case CLOSED:
					drawElevatorDoors(currentHeight, g, size / 10);
					// drawClosedElevator(currentHeight, g);
					break;
				}
			}
		}
	}

	private void drawDispatchPassengers(int currentHeight, Graphics2D g2, int numberPassengers) {
		for (int i = 1; i <= numberPassengers; i++) {
			drawPassenger(getWidth() / 2 + size / 3 + i * size / 3, currentHeight + size / 3, size * 2 / 3, g2);
		}
	}

	private void drawArrivalPassengers(int currentHeight, Graphics2D g2, int numberPassengers) {
		for (int i = 1; i <= numberPassengers; i++) {
			drawPassenger(getWidth() / 2 - size / 2 - i * size / 3, currentHeight + size / 3, size * 2 / 3, g2);
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

	// private void drawClosedElevator(int currentHeight, Graphics g){
	// int doorSize = size/10;
	// g.setColor(Color.GRAY);
	// g.fillRect(getWidth()/2-size/2+1, currentHeight, size/2-doorSize/2,
	// size);
	// g.fillRect(getWidth()/2+doorSize/2+1, currentHeight, size/2-doorSize/2,
	// size);
	// g.setColor(Color.BLACK);
	// // g.drawRect(getWidth()/2-doorSize/2, currentHeight, doorSize, size);
	// }
	// private void drawHalfeOpenedElevator(int currentHeight, Graphics g){
	// int doorSize = size/3;
	// g.setColor(Color.GRAY);
	// g.fillRect(getWidth()/2-size/2+1, currentHeight, size/2-doorSize/2,
	// size);
	// g.fillRect(getWidth()/2+doorSize/2+1, currentHeight, size/2-doorSize/2,
	// size);
	// g.setColor(Color.BLACK);
	// // g.setColor(Color.GRAY);
	// // g.fillRect(getWidth()/2-doorSize/2, currentHeight+size/2, doorSize,
	// size/2+1);
	// // g.setColor(Color.BLACK);
	// // g.drawRect(getWidth()/2-doorSize/2, currentHeight, doorSize, size);
	// }
	// private void drawOpenedElevator(int currentHeight, Graphics g){
	// int doorSize = (int) (size/1.5);
	// g.setColor(Color.GRAY);
	// g.fillRect(getWidth()/2-size/2+1, currentHeight, size/2-doorSize/2,
	// size);
	// g.fillRect(getWidth()/2+doorSize/2+1, currentHeight, size/2-doorSize/2,
	// size);
	// g.setColor(Color.BLACK);
	// // g.setColor(Color.GRAY);
	// // g.fillRect(getWidth()/2-doorSize/2, currentHeight+size/2, doorSize,
	// size/2+1);
	// // g.setColor(Color.BLACK);
	// // g.drawRect(getWidth()/2-doorSize/2, currentHeight, doorSize, size);
	// }

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

}
