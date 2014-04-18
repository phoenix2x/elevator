package com.epam.elevatortask.logic;

import org.apache.log4j.Logger;

import com.epam.elevatortask.beans.Passenger;
import com.epam.elevatortask.enums.ControllerActions;

/**
 * Class provides methods to log transportation process.
 * 
 */
public class Presenter {
	private static final Logger LOG = Logger.getLogger(Presenter.class);
	private static final String ON_STORY = " on story-";
	private static final String PASSENGER = "( passenger";
	private static final String CLOSING_PARENTHESIS = ")";
	private static final String TO_STORY = " to story-";
	private static final String FROM_STORY = " (from story-";

	public void movingElevator(int oldStory, int currentStory) throws InterruptedException {
		LOG.info(ControllerActions.MOVING_ELEVATOR.getDescription() + FROM_STORY + oldStory + TO_STORY
				+ currentStory + CLOSING_PARENTHESIS);
	}

	public void deboardingPassenger(int currentStory, Passenger currentPassenger) throws InterruptedException {
		LOG.info(ControllerActions.DEBOADING_OF_PASSENGER.getDescription() + PASSENGER
				+ currentPassenger.getPassengerID() + ON_STORY + currentStory + CLOSING_PARENTHESIS);
	}

	public void boardingPassenger(int currentStory, Passenger currentPassenger) throws InterruptedException {
		LOG.info(ControllerActions.BOADING_OF_PASSENGER.getDescription() + PASSENGER
				+ currentPassenger.getPassengerID() + ON_STORY + currentStory + CLOSING_PARENTHESIS);
	}

}
