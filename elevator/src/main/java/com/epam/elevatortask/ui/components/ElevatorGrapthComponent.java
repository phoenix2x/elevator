package com.epam.elevatortask.ui.components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Ellipse2D;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

/**
 * Component for drawing transportation process.
 *
 */
public class ElevatorGrapthComponent extends JComponent {
	private static final long serialVersionUID = 1L;
	private static final int IMAGES_NUMBER = 16;

	private final int storiesNumber;
	private final int[] dispatchPassengers;
	private final int[] arrivalPassengers;
	private ArrayList<Image> imageList = new ArrayList<>();
	private Iterator<Image> imageIterator;
	private boolean movingDispatchFlag = false;
	private boolean movungArrivalFlag = false;
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
	private int constantArrivalPassengerOffset;
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
		try {
			for (int i = 1; i <= IMAGES_NUMBER; i++){
				this.imageList.add(ImageIO.read(getClass().getResourceAsStream("/img/bender_walk" + i + ".png")));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void newImageIterator(){
		imageIterator = imageList.iterator();
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
	 * Calculates stories size depending on stories number. Also sets defaults.
	 */
	public void calculateStoriesSize() {
		size = getHeight() / storiesNumber;
		doorSizeMax = size;
		doorSizeMin = size / 10;
		doorSize = doorSizeMax;
		currentElevatorHeight = getHeight() - (currentStory + 1) * size;
		defaultDispatchPassengerOffset = size / 2;
		defaultArrivalPassengerOffset = size / 2;
		constantArrivalPassengerOffset = size / 4;
		setArrivalPassengerOffsetToDefault();
		setDispatchPassengerOffsetToDefault();
		for (int i =0; i < imageList.size(); i++){
			imageList.set(i, imageList.get(i).getScaledInstance(-1, size, Image.SCALE_SMOOTH));
		}
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
		if (size/(2*IMAGES_NUMBER) > 1){
			passengerDispatchOffset -= size/(2*IMAGES_NUMBER);
		}else{
			passengerDispatchOffset--;
		}
	}

	public void clearBoardingOffset() {
		dispatchPassengerNumberOffset = 0;
		elevatorPassengerNumberOffset = 0;
		setDispatchPassengerOffsetToDefault();
	}

	public void incArrivalPassengerOffset() {
		if (size/(2*IMAGES_NUMBER) > 1){
			passengerArrivalOffset += size/(2*IMAGES_NUMBER);
		}else{
			passengerArrivalOffset++;
		}
	}

	public boolean isDispatchPassengerOffsetZero() {
		return passengerDispatchOffset <= 0;
	}

	public boolean isArrivalPassengerOffsetDef() {
		return passengerArrivalOffset >= defaultArrivalPassengerOffset + size /2;
	}

	public void setDispatchPassengerOffsetToDefault() {
		passengerDispatchOffset = defaultDispatchPassengerOffset;
	}

	public void setArrivalPassengerOffsetToDefault() {
		passengerArrivalOffset = defaultArrivalPassengerOffset;
	}

	public void changeMovingDispatchFlag(){
		movingDispatchFlag = !movingDispatchFlag;
	}
	public void changeMovingArrivalFlag(){
		movungArrivalFlag = !movungArrivalFlag;
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
			if (i == currentStory && movungArrivalFlag){
				drawMovingArrivalPassengers(currentHeight, (Graphics2D) g, arrivalPassengers[i]
						+ arrivalPassengerNumberOffset, constantArrivalPassengerOffset + passengerArrivalOffset);
			} else {
				drawArrivalPassengers(currentHeight, (Graphics2D) g, arrivalPassengers[i],
						constantArrivalPassengerOffset + defaultArrivalPassengerOffset);
			}
			if (i == currentStory && movingDispatchFlag) {
				drawMovingDispatchPassengers(currentHeight, (Graphics2D) g, dispatchPassengers[i]
						+ dispatchPassengerNumberOffset, passengerDispatchOffset);
			} else {
				drawDispatchPassengers(currentHeight, (Graphics2D) g, dispatchPassengers[i],
						defaultDispatchPassengerOffset);
			}
		}
		drawElevatorDoors(currentElevatorHeight, g, doorSize);
	}
	private void drawMovingDispatchPassengers(int currentHeight, Graphics2D g2, int numberPassengers,int currentPassengerOffset) {
		Image currentImage = null;
		if (!imageIterator.hasNext()){
			newImageIterator();
		}
		currentImage = imageIterator.next();
		for (int i = 0; i < numberPassengers; i++) {
			drawMovingImagePassenger(getWidth() / 2 + currentPassengerOffset + i * size / 2, currentHeight,
					size , g2, currentImage);
		}
	}
	private void drawMovingArrivalPassengers(int currentHeight, Graphics2D g2, int numberPassengers,int currentPassengerOffset) {
		Image currentImage = null;
		if (!imageIterator.hasNext()){
			newImageIterator();
		}
		currentImage = imageIterator.next();
		for (int i = 0; i < numberPassengers; i++) {
			drawMovingImagePassenger(getWidth() / 2 - currentPassengerOffset - i * size / 2, currentHeight,
					size , g2, currentImage);
		}
	}
	private void drawDispatchPassengers(int currentHeight, Graphics2D g2, int numberPassengers,
			int currentPassengerOffset) {
		for (int i = 0; i < numberPassengers; i++) {
			drawImagePassenger(getWidth() / 2 + currentPassengerOffset + i * size / 2, currentHeight,
					size , g2);
		}
	}

	private void drawArrivalPassengers(int currentHeight, Graphics2D g2, int numberPassengers,
			int currentPassengerOffset) {
		for (int i = 1; i <= numberPassengers; i++) {
			drawImagePassenger(getWidth() / 2 - currentPassengerOffset - i * size / 2, currentHeight, size, g2);
		}
	}

	private void drawElevatorPassengers(int currentHeight, Graphics2D g2, int numberPassengers) {
		int maxElevatorPassengers = (numberPassengers < 6 ? numberPassengers : 6);
		for (int i = 1; i <= maxElevatorPassengers; i++) {
			drawImagePassenger(getWidth() / 2 - size * 9 / 10 + i * size / 10, currentHeight, size, g2);
		}
	}
	private void drawMovingImagePassenger(int x, int y, int height, Graphics2D g2, Image currentImage) {
		g2.drawImage(currentImage, x, y, null);
	}
	private void drawImagePassenger(int x, int y, int height, Graphics2D g2) {
		g2.drawImage(imageList.get(imageList.size()-1), x, y, null);
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
