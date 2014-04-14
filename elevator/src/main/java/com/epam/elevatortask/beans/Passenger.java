package com.epam.elevatortask.beans;

import java.util.Random;

import com.epam.elevatortask.enums.Direction;
import com.epam.elevatortask.enums.TransportationState;

public class Passenger {
	
	private static int passengerIDGenerator = 1;
	private final int passengerID;
	private final int destinationStory;
	private final Direction direction;
	private TransportationState transportationState = TransportationState.NOT_STARTED;

	/**
	 * @param initStory
	 * @param storiesNumber
	 */
	public Passenger(int initStory, int storiesNumber) {
		super();
		this.passengerID = passengerIDGenerator++;
		this.destinationStory = calculateDestinationStory(initStory, storiesNumber);
		if (destinationStory < initStory){
			direction = Direction.DOWN;
		}else{
			direction = Direction.UP;
		}
	}
	public Direction getDirection(){
		return direction;
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
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(passengerID).append(";").append(destinationStory).append(";");
		if (direction != null)
			builder.append(direction).append(";");
		if (transportationState != null)
			builder.append(transportationState);
		return builder.toString();
	}
	

	
}
