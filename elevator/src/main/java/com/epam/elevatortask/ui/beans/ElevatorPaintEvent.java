package com.epam.elevatortask.ui.beans;

import com.epam.elevatortask.ui.beans.ElevatorGrapthComponent.DoorState;

public class ElevatorPaintEvent {
	private final int currentStory;
	private final DoorState doorState;
	/**
	 * @param currentStory
	 * @param doorState
	 */
	public ElevatorPaintEvent(int currentStory, DoorState doorState) {
		super();
		this.currentStory = currentStory;
		this.doorState = doorState;
	}
	/**
	 * @return the currentStory
	 */
	public int getCurrentStory() {
		return currentStory;
	}
	/**
	 * @return the doorState
	 */
	public DoorState getDoorState() {
		return doorState;
	}

}
