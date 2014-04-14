package com.epam.elevatortask.interfaces;

import com.epam.elevatortask.beans.Building;
import com.epam.elevatortask.beans.Passenger;

public interface IElevatorPainter {
	void paintElevatorMove(final int currentStory) throws InterruptedException;
	void paintElevatorArrival() throws InterruptedException;
	void paintElevatorDispatch() throws InterruptedException;
	void paintDeboarding(int currentStory, Building<Passenger> building) throws InterruptedException;
	void paintBoarding(int currentStory, Building<Passenger> building) throws InterruptedException;
}
