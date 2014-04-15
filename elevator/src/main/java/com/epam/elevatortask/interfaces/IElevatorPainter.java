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
	 *             Paint elevator move through elevator frame.
	 */
	void paintElevatorMove(final int currentStory) throws InterruptedException;

	/**
	 * @throws InterruptedException
	 *             Paint opening of elevator doors through elevator frame.
	 */
	void paintElevatorArrival() throws InterruptedException;

	/**
	 * @throws InterruptedException
	 *             Paint closing of elevator doors through elevator frame.
	 */
	void paintElevatorDispatch() throws InterruptedException;

	/**
	 * @param currentStory
	 * @param building
	 * @throws InterruptedException
	 *             Paint passenger move during deboarding through elevator frame.
	 *             Update another information on frame.
	 */
	void paintDeboarding(int currentStory, Building<Passenger> building) throws InterruptedException;

	/**
	 * @param currentStory
	 * @param building
	 * @throws InterruptedException
	 *             Paint passenger move during boarding through elevator frame.
	 *             Update another information on frame.
	 */
	void paintBoarding(int currentStory, Building<Passenger> building) throws InterruptedException;
}
