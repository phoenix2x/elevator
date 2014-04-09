package com.epam.elevatortask.ui.beans;

import com.epam.elevatortask.ui.beans.ElevatorGrapthComponent.ElevatorAction;

public class ElevatorPaintEvent {
	private final int currentStory;
	private final ElevatorAction elevatorAction;
	/**
	 * @param currentStory
	 * @param elevatorAction
	 */
	public ElevatorPaintEvent(int currentStory, ElevatorAction elevatorAction) {
		super();
		this.currentStory = currentStory;
		this.elevatorAction = elevatorAction;
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
	public ElevatorAction getElevatorAction() {
		return elevatorAction;
	}

}
