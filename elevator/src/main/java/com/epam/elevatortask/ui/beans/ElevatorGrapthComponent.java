package com.epam.elevatortask.ui.beans;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JComponent;

public class ElevatorGrapthComponent extends JComponent {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public enum DoorState{
		OPENED, HALF_OPENED, CLOSED
	}
	
	private final int storiesNumber;
	private int size = 50;
	private int currentStory = 0;
	private DoorState doorState = DoorState.CLOSED;
	/**
	 * 
	 */
	public ElevatorGrapthComponent(int storiesNumber) {
		super();
		this.storiesNumber = storiesNumber;
	}


	/**
	 * @return the currentStory
	 */
	public int getCurrentStory() {
		return currentStory;
	}


	/**
	 * @param currentStory the currentStory to set
	 */
	public void setCurrentStory(int currentStory) {
		this.currentStory = currentStory;
	}


	/**
	 * @param doorState the doorState to set
	 */
	public void setDoorState(DoorState doorState) {
		this.doorState = doorState;
	}


	/**
	 * calculate stories size depending on stories number
	 */
	public void calculateStoriesSize() {
		size = getHeight()/storiesNumber;
	}


	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		for(int i = 0; i<storiesNumber ;i++){
			int currentHeight = getHeight()-(i+1)*size;
			drawElevator(currentHeight, g);
			if(i==currentStory){
				switch (doorState) {
				case OPENED:
					drawOpenedElevator(currentHeight, g);
					break;
				case HALF_OPENED:
					drawHalfeOpenedElevator(currentHeight, g);
					break;
				case CLOSED:
					drawClosedElevator(currentHeight, g);
					break;
				}
			}
		}
	}
	private void drawElevator(int currentHeight, Graphics g){
		g.drawRect(getWidth()/2-size/2, currentHeight, size, size);
		
	}
	private void drawClosedElevator(int currentHeight, Graphics g){
		int doorSize = size/10;
		g.drawRect(getWidth()/2-doorSize/2, currentHeight, doorSize, size);
	}
	private void drawHalfeOpenedElevator(int currentHeight, Graphics g){
		int doorSize = size/3;
		g.setColor(Color.GRAY);
		g.fillRect(getWidth()/2-doorSize/2, currentHeight+size/2, doorSize, size/2+1);
		g.setColor(Color.BLACK);
		g.drawRect(getWidth()/2-doorSize/2, currentHeight, doorSize, size);
	}
	private void drawOpenedElevator(int currentHeight, Graphics g){
		int doorSize = (int) (size/1.5);
		g.setColor(Color.GRAY);
		g.fillRect(getWidth()/2-doorSize/2, currentHeight+size/2, doorSize, size/2+1);
		g.setColor(Color.BLACK);
		g.drawRect(getWidth()/2-doorSize/2, currentHeight, doorSize, size);
	}
	

}
