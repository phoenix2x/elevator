package com.epam.elevatortask.logic;

import org.apache.log4j.Logger;

import com.epam.elevatortask.beans.Passenger;
import com.epam.elevatortask.enums.ControllerActions;

/**
 * Class provide methods to log transportation process.
 * 
 */
public class Presenter {
	private static final Logger LOG = Logger.getLogger(Presenter.class);

	public void movingElevator(int oldStory, int currentStory) throws InterruptedException {
		LOG.info(ControllerActions.MOVING_ELEVATOR.getDescription() + " (from story-" + oldStory + " to story-"
				+ currentStory + ")");
	}

	public void deboardingPassenger(int currentStory, Passenger currentPassenger) throws InterruptedException {
		LOG.info(ControllerActions.DEBOADING_OF_PASSENGER.getDescription() + "( passenger"
				+ currentPassenger.getPassengerID() + " on story-" + currentStory + ")");
	}

	public void boardingPassenger(int currentStory, Passenger currentPassenger) throws InterruptedException {
		LOG.info(ControllerActions.BOADING_OF_PASSENGER.getDescription() + "( passenger"
				+ currentPassenger.getPassengerID() + " on story-" + currentStory + ")");
	}

}
