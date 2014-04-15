package com.epam.elevatortask.beans;

import java.util.ArrayList;
import java.util.List;

/**
 * Class stores passengers containers. And provides methods for managing them.
 *
 * @param <T>
 */
public class Building<T extends Passenger> {
	private final List<NumberedStoryContainer<T>> dispatchStoryContainersList;
	private final List<NumberedStoryContainer<T>> arrivalStoryContainersList;
	private final Container<T> elevatorContainer;
	private final int storiesNumber;
	private final int elevatorCapacity;

	/**
	 * @param storiesNumber
	 * @param elevatorCapacity
	 */
	public Building(int storiesNumber, int elevatorCapacity) {
		super();
		this.dispatchStoryContainersList = new ArrayList<>(storiesNumber);
		this.arrivalStoryContainersList = new ArrayList<>(storiesNumber);
		for (int i = 0; i < storiesNumber; i++) {
			dispatchStoryContainersList.add(new NumberedStoryContainer<T>(i));
			arrivalStoryContainersList.add(new NumberedStoryContainer<T>(i));
		}
		this.elevatorContainer = new Container<>();
		this.storiesNumber = storiesNumber;
		this.elevatorCapacity = elevatorCapacity;
	}

	/**
	 * @return the storiesNumber
	 */
	public int getStoriesNumber() {
		return storiesNumber;
	}

	/**
	 * @param storyNumber
	 * @return dispatch container from that story
	 */
	public NumberedStoryContainer<T> getDispatchContainer(int storyNumber) {
		return dispatchStoryContainersList.get(storyNumber);
	}

	/**
	 * @param storyNumber
	 * @return arrival container from that story
	 */
	public NumberedStoryContainer<T> getArrivalContainer(int storyNumber) {
		return arrivalStoryContainersList.get(storyNumber);
	}

	/**
	 * @return elevatorContainer
	 */
	public Container<T> getElevatorContainer() {
		return elevatorContainer;
	}

	/**
	 * @return true if number of passengers in elevatorContainer =
	 *         elevatorCapacity.
	 */
	public boolean isElevatorFull() {
		return elevatorContainer.getPassengersNumber() == elevatorCapacity;
	}

	/**
	 * @param storyNumber
	 * @param t
	 *            Add passenger to dispatch container on specified story.
	 */
	public void addDispatchPassenger(int storyNumber, T t) {
		dispatchStoryContainersList.get(storyNumber).add(t);
	}

	/**
	 * @param storyNumber
	 * @param t
	 *            Add passenger to arrival container on specified story.
	 */
	public void addArrivalPassenger(int storyNumber, T t) {
		arrivalStoryContainersList.get(storyNumber).add(t);
	}

	/**
	 * @param t
	 *            Add passenger to elevator container.
	 */
	public void addElevatorPassenger(T t) {
		elevatorContainer.add(t);
	}

	/**
	 * @param storyNumber
	 * @param t
	 *            Remove passenger from dispatch container on specified story.
	 */
	public void removeDispatchPassenger(int storyNumber, T t) {
		dispatchStoryContainersList.get(storyNumber).remove(t);
	}

	/**
	 * @param t
	 *            Remove passenger from elevator container.
	 */
	public void removeElevatorPassenger(T t) {
		elevatorContainer.remove(t);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		if (dispatchStoryContainersList != null) {
			builder.append(dispatchStoryContainersList).append(";");
		}
		if (arrivalStoryContainersList != null) {
			builder.append(arrivalStoryContainersList).append(";");
		}
		if (elevatorContainer != null) {
			builder.append(elevatorContainer).append(";");
		}
		builder.append(storiesNumber).append(";").append(elevatorCapacity);
		return builder.toString();
	}
}
