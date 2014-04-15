package com.epam.elevatortask.enums;

public enum ControllerActions {
	STARTING_TRANSPORTATION("Starting transportation"),
	COMPLETION_TRANSPORTATION("Completion transportation"),
	ABORTING_TRANSPORTATION("Aborting transportation"),
	MOVING_ELEVATOR("Moving elevator"),
	BOADING_OF_PASSENGER("Boarding passenger"),
	DEBOADING_OF_PASSENGER("Deboarding passenger");
	private final String description;
	
	private ControllerActions(String description) {
		this.description = description;
	}
	
	/**
	 * @return description
	 */
	public String getDescription() {
		return description;
	}
}
