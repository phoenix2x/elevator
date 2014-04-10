package com.epam.elevatortask.interfaces;

import com.epam.elevatortask.beans.Building;
import com.epam.elevatortask.beans.Passenger;

public interface IElevatorPainter {
//	void paintForm(int currentStory, DoorState doorState, Building<Passenger> building);
	void paintElevatorArrival(final int currentStory);
	void paintElevatorDispatch();
	void paintDeboarding(int currentStory, Building<Passenger> building);
	void paintBoarding(int currentStory, Building<Passenger> building);
}
