package com.epam.elevatortask.beans;

import java.util.ArrayList;
import java.util.List;

public class Building<T extends Passenger> {
	private final List<NumberedStoryContainer<T>> dispatchStoryContainersList;
	private final List<NumberedStoryContainer<T>> arrivalStoryContainersList;
	private final Container<T> elevatorContainer;
	private final int storiesNumber;
	private final int elevatorCapacity;
	/**
	 * @param dispatchStoryContainersList
	 * @param arrivalStoryContainersList
	 * @param elevatorContainer
	 */
	public Building(int storiesNumber,int elevatorCapacity) {
		super();
		this.dispatchStoryContainersList = new ArrayList<>(storiesNumber);
		this.arrivalStoryContainersList = new ArrayList<>(storiesNumber);
		for (int i = 0; i < storiesNumber; i++){
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
	public NumberedStoryContainer<T> getDispatchContainer(int storyNumber){
		return dispatchStoryContainersList.get(storyNumber);
	}
	public NumberedStoryContainer<T> getArrivalContainer(int storyNumber){
		return arrivalStoryContainersList.get(storyNumber);
	}
	public Container<T> getElevatorContainer(){
		return elevatorContainer;
	}
	public boolean isElevatorFull(){
		return elevatorContainer.getPassengersNumber() == elevatorCapacity;
	}
	public void addDispatchPassenger(int storyNumber, T t){
		dispatchStoryContainersList.get(storyNumber).add(t);
	}
	public void addArrivalPassenger(int storyNumber, T t){
		arrivalStoryContainersList.get(storyNumber).add(t);
	}
	public void addElevatorPassenger(T t){
		elevatorContainer.add(t);
	}
	public void removeDispatchPassenger(int storyNumber, T t){
		dispatchStoryContainersList.get(storyNumber).remove(t);
	}
	public void removeElevatorPassenger(T t){
		elevatorContainer.remove(t);
	}
}
