package com.epam.elevatortask.ui.components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

import com.epam.elevatortask.ui.exceptions.ResourceNotFoundException;

/**
 * Component for drawing transportation process.
 * 
 */
public class ElevatorGrapthComponent extends JComponent {
	private static final long serialVersionUID = 1L;
	private static final int IMAGES_NUMBER = 16;
	private static final int MAX_PASSENGERS_ELEVATOR = 7;
	private static final String FILE_PATH = "/img/";
	private static final String FILE_NAME = "bender_walk";
	private static final String FILE_EXTENSION = ".png";

	private final int storiesNumber;
	private final int[] dispatchPassengers;
	private final int[] arrivalPassengers;
	private final ArrayList<Image> scaledImageList;
	private final ArrayList<Image> nonscaledImageList;
	private Iterator<Image> imageIterator;
	private boolean movingDispatchFlag = false;
	private boolean movungArrivalFlag = false;
	private boolean firstRun = true;
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
	private int center;
	private int size;
	private int passengerWidth;
	private int currentStory;

	/**
	 * @param storiesNumber
	 * @param dispatchPassengers
	 * @param arrivalPassengers
	 * @throws ResourceNotFoundException
	 * @throws IOException
	 * @throws IllegalArgumentException
	 */
	public ElevatorGrapthComponent(int storiesNumber, int[] dispatchPassengers, int[] arrivalPassengers)
			throws ResourceNotFoundException {
		super();
		this.scaledImageList = new ArrayList<>();
		this.nonscaledImageList = new ArrayList<>();
		this.storiesNumber = storiesNumber;
		this.dispatchPassengers = dispatchPassengers;
		this.arrivalPassengers = arrivalPassengers;
		for (int i = 1; i <= IMAGES_NUMBER; i++) {
			try {
				this.nonscaledImageList.add(ImageIO.read(getClass().getResource(
						FILE_PATH + FILE_NAME + i + FILE_EXTENSION)));
				this.scaledImageList.add(ImageIO.read(getClass()
						.getResource(FILE_PATH + FILE_NAME + i + FILE_EXTENSION)));
			} catch (Exception e) {
				throw new ResourceNotFoundException();
			}
		}
	}

	/**
	 * @param currentStory
	 *            the currentStory to set
	 */
	public void setCurrentStory(int currentStory) {
		this.currentStory = currentStory;
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

	public void beforeDeboarding(int arrivalPassengers, int elevatorPassengers) {
		setArrivalPassengers(arrivalPassengers);
		setElevatorPassengers(elevatorPassengers);
		changeMovingArrivalFlag();
		newImageIterator();
	}

	public void afterDeboarding() {
		setArrivalPassengerOffsetToDefault();
		changeMovingArrivalFlag();
	}

	public void beforeBoarding(int elevatorPassengers, int dispatchPassengers) {
		setElevatorPassengers(elevatorPassengers);
		setDispatchPassengers(dispatchPassengers);
		changeMovingDispatchFlag();
		newImageIterator();
		setBoardingOffset();
	}

	public void afterBoarding() {
		clearBoardingOffset();
		changeMovingDispatchFlag();
	}

	public void decDispatchPassengerOffset() {
		if (passengerWidth / IMAGES_NUMBER > 1) {
			passengerDispatchOffset -= passengerWidth / IMAGES_NUMBER;
		} else {
			passengerDispatchOffset--;
		}
	}

	public void incArrivalPassengerOffset() {
		if (passengerWidth / IMAGES_NUMBER > 1) {
			passengerArrivalOffset += passengerWidth / IMAGES_NUMBER;
		} else {
			passengerArrivalOffset++;
		}
	}

	public boolean isBoardingComplete() {
		return passengerDispatchOffset <= 0;
	}

	public boolean isDeboardingComplete() {
		return passengerArrivalOffset >= defaultArrivalPassengerOffset + passengerWidth;
	}

	public void resize() {
		center = getWidth() / 2;
		size = getHeight() / storiesNumber;
		passengerWidth = size / 2;
		doorSizeMax = size;
		doorSizeMin = size / 10;
		// TODO calc doorsize
		doorSize = size;
		defaultDispatchPassengerOffset = size / 2;
		defaultArrivalPassengerOffset = size / 2;
		constantArrivalPassengerOffset = size / 4;
		// TODO calc curElHeight depend on previous
		currentElevatorHeight = getHeight() - (currentStory + 1) * size;
		scaleImages();
		if (firstRun) {
			initialize();
			firstRun = false;
		}
		repaint();
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
			if (i == currentStory && movungArrivalFlag) {
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

	private void drawMovingDispatchPassengers(int currentHeight, Graphics2D g2, int numberPassengers,
			int currentPassengerOffset) {
		Image currentImage = null;
		if (!imageIterator.hasNext()) {
			newImageIterator();
		}
		currentImage = imageIterator.next();
		for (int i = 0; i < numberPassengers; i++) {
			drawMovingImagePassenger(center + currentPassengerOffset + i * passengerWidth, currentHeight, size, g2,
					currentImage);
		}
	}

	private void drawMovingArrivalPassengers(int currentHeight, Graphics2D g2, int numberPassengers,
			int currentPassengerOffset) {
		Image currentImage = null;
		if (!imageIterator.hasNext()) {
			newImageIterator();
		}
		currentImage = imageIterator.next();
		for (int i = 0; i < numberPassengers; i++) {
			drawMovingImagePassenger(center - currentPassengerOffset - i * passengerWidth, currentHeight, size, g2,
					currentImage);
		}
	}

	private void drawDispatchPassengers(int currentHeight, Graphics2D g2, int numberPassengers,
			int currentPassengerOffset) {
		for (int i = 0; i < numberPassengers; i++) {
			drawImagePassenger(center + currentPassengerOffset + i * passengerWidth, currentHeight, size, g2);
		}
	}

	private void drawArrivalPassengers(int currentHeight, Graphics2D g2, int numberPassengers,
			int currentPassengerOffset) {
		for (int i = 1; i <= numberPassengers; i++) {
			drawImagePassenger(center - currentPassengerOffset - i * passengerWidth, currentHeight, size, g2);
		}
	}

	private void drawElevatorPassengers(int currentHeight, Graphics2D g2, int numberPassengers) {
		int maxElevatorPassengers = (numberPassengers < MAX_PASSENGERS_ELEVATOR ? numberPassengers : MAX_PASSENGERS_ELEVATOR);
		for (int i = 1; i <= maxElevatorPassengers; i++) {
			drawImagePassenger(center - size * 9 / 10 + i * size / 10, currentHeight, size, g2);
		}
	}

	private void drawMovingImagePassenger(int x, int y, int height, Graphics2D g2, Image currentImage) {
		g2.drawImage(currentImage, x, y, null);
	}

	private void drawImagePassenger(int x, int y, int height, Graphics2D g2) {
		g2.drawImage(scaledImageList.get(scaledImageList.size() - 1), x, y, null);
	}

	private void drawStory(int currentHeight, Graphics g) {
		g.drawLine(0, currentHeight, getWidth(), currentHeight);
	}

	private void drawElevator(int currentHeight, Graphics g) {
		g.drawRect(center - size / 2, currentHeight, size, size);

	}

	private void drawElevatorDoors(int currentHeight, Graphics g, int doorSize) {
		g.setColor(Color.GRAY);
		g.fillRect(center - size / 2 + 1, currentHeight, size / 2 - doorSize / 2, size);
		g.fillRect(center + doorSize / 2 + 1, currentHeight, size / 2 - doorSize / 2, size);
		g.setColor(Color.BLACK);
	}

	private void scaleImages() {
		for (int i = 0; i < nonscaledImageList.size(); i++) {
			scaledImageList.set(i, nonscaledImageList.get(i).getScaledInstance(-1, size, Image.SCALE_SMOOTH));
		}
	}

	private void setDispatchPassengerOffsetToDefault() {
		passengerDispatchOffset = defaultDispatchPassengerOffset;
	}

	private void setArrivalPassengerOffsetToDefault() {
		passengerArrivalOffset = defaultArrivalPassengerOffset;
	}

	private void changeMovingDispatchFlag() {
		movingDispatchFlag = !movingDispatchFlag;
	}

	private void changeMovingArrivalFlag() {
		movungArrivalFlag = !movungArrivalFlag;
	}

	private void setBoardingOffset() {
		dispatchPassengerNumberOffset = 1;
		elevatorPassengerNumberOffset = -1;
	}

	private void clearBoardingOffset() {
		dispatchPassengerNumberOffset = 0;
		elevatorPassengerNumberOffset = 0;
		setDispatchPassengerOffsetToDefault();
	}

	private void setDispatchPassengers(int dispatchPassengers) {
		this.dispatchPassengers[currentStory] = dispatchPassengers;
	}

	private void setArrivalPassengers(int arrivalPassengers) {
		this.arrivalPassengers[currentStory] = arrivalPassengers;
	}

	private void setElevatorPassengers(int elevatorPassengers) {
		this.elevatorPassengers = elevatorPassengers;
	}

	private void newImageIterator() {
		imageIterator = scaledImageList.iterator();
	}

	private void initialize() {
		setArrivalPassengerOffsetToDefault();
		setDispatchPassengerOffsetToDefault();
	}
}
