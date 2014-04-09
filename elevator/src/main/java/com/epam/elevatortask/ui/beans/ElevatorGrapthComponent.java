package com.epam.elevatortask.ui.beans;

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
	
	public enum ElevatorAction{
		OPENED, HALF_OPENED, CLOSED
	}
	
	private final int storiesNumber;
	private int size = 50;
	private int currentStory = 0;
	private ElevatorAction elevatorAction = ElevatorAction.CLOSED;
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
	 * @param elevatorAction the doorState to set
	 */
	public void setDoorState(ElevatorAction elevatorAction) {
		this.elevatorAction = elevatorAction;
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
			drawStory(currentHeight, g);
			drawDispatchPassengers(currentHeight, (Graphics2D) g, 10);
			if(i==currentStory){
				switch (elevatorAction) {
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
	private void drawDispatchPassengers(int currentHeight,Graphics2D g2, int numberPassengers){
		for(int i=1; i <= numberPassengers; i++){
			drawPassenger(getWidth()/2+size/3 + i*size/3, currentHeight+size/3, size*2/3, g2);
		}
	}
	private void drawPassenger(int x, int y, int height, Graphics2D g2){
		int width = height/3;
		g2.draw(new Ellipse2D.Double(x+width/4, y, width/2, height/6));
		g2.draw(new Ellipse2D.Double(x+width/4, y+height/6, width/2, height/2));
		g2.drawLine(x, y+height/2, x+width/3, y+height/5);
		g2.drawLine(x+width, y+height/2, x+width*2/3, y+height/5);
		g2.drawLine(x, y+height, x+width/2-width/7, y+height*2/3);
		g2.drawLine(x+width, y+height, x+width/2+width/7, y+height*2/3);
	}
	private void drawStory(int currentHeight,Graphics g){
		g.drawLine(0, currentHeight, getWidth(), currentHeight);
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
