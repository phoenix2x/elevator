package com.epam.elevatortask.interfaces;

import com.epam.elevatortask.beans.Building;
import com.epam.elevatortask.beans.Passenger;

/**
 * Interface for update elevatorFrame data.
 */
public interface IElevatorPainter {
	/**
	 * @param currentStory
	 * @throws InterruptedException
	 *             Draws elevator movement through elevator frame
	 */
	void drawElevatorMove(final int currentStory) throws InterruptedException;

	/**
	 * @throws InterruptedException
	 *             Draws opening of elevator doors through elevator frame.
	 */
	void drawElevatorArrival() throws InterruptedException;

	/**
	 * @throws InterruptedException
	 *             Draws closing of elevator doors through elevator frame.
	 */
	void drawElevatorDispatch() throws InterruptedException;

	/**
	 * @param currentStory
	 * @param building
	 * @throws InterruptedException
	 *             Draws passengers movement during deboarding through elevator frame.
	 *             Updates other information on the frame.
	 */
	void drawDeboarding(int currentStory, Building<Passenger> building) throws InterruptedException;

	/**
	 * @param currentStory
	 * @param building
	 * @throws InterruptedException
	 *             Draw passengers movement during boarding through elevator frame.
	 *             Updates other information on the frame.
	 */
	void drawBoarding(int currentStory, Building<Passenger> building) throws InterruptedException;
}
