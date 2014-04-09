package com.epam.elevatortask.beans;

import java.util.Random;

import com.epam.elevatortask.enums.TransportationState;

public class Passenger {
	
	private static int currentPassengerID = 1;
	private final int passengerID;
	private final int destinationStory;
	private TransportationState transportationState = TransportationState.NOT_STARTED;

	/**
	 * @param initStory
	 * @param storiesNumber
	 */
	public Passenger(int initStory, int storiesNumber) {
		super();
		this.passengerID = currentPassengerID++;
		this.destinationStory = calculateDestinationStory(initStory, storiesNumber);
	}
	private int calculateDestinationStory(int initStory, int storiesNumber){
		Random random = new Random();
		int destinationStory = random.nextInt(storiesNumber);
		while (destinationStory==initStory) {
			destinationStory = random.nextInt(storiesNumber);
		}
		return destinationStory;	
	}

	/**
	 * @return the transportationState
	 */
	public TransportationState getTransportationState() {
		return transportationState;
	}

	/**
	 * @return the passengerID
	 */
	public int getPassengerID() {
		return passengerID;
	}

	/**
	 * @return the destinationStory
	 */
	public int getDestinationStory() {
		return destinationStory;
	}
	public void setTransportationState(TransportationState transportationState){
		this.transportationState = transportationState;
	}
	

	
}
