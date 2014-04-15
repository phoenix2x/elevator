package com.epam.elevatortask.logic;

import com.epam.elevatortask.beans.Building;
import com.epam.elevatortask.beans.Passenger;
import com.epam.elevatortask.interfaces.IElevatorPainter;

/**
 * Class provide methods to update elevatorForm in GUI mode. Also calls superclass methods to log process.
 *
 */
public class GUIPresenter extends Presenter {
	private final IElevatorPainter elevatorPainter;
	private final Building<Passenger> building;

	public GUIPresenter(IElevatorPainter elevatorPainter, Building<Passenger> building) {
		this.elevatorPainter = elevatorPainter;
		this.building = building;
	}

	@Override
	public void movingElevator(int oldStory, int currentStory) throws InterruptedException {
		super.movingElevator(oldStory, currentStory);
		elevatorPainter.paintElevatorDispatch();
		elevatorPainter.paintElevatorMove(currentStory);
		elevatorPainter.paintElevatorArrival();
	}

	@Override
	public void deboardingPassenger(int currentStory, Passenger currentPassenger) throws InterruptedException {
		super.deboardingPassenger(currentStory, currentPassenger);
		elevatorPainter.paintDeboarding(currentStory, building);
	}

	@Override
	public void boardingPassenger(int currentStory, Passenger currentPassenger) throws InterruptedException {
		super.boardingPassenger(currentStory, currentPassenger);
		elevatorPainter.paintBoarding(currentStory, building);
	}
}
