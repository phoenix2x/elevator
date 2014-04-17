package com.epam.elevatortask.logic;

import com.epam.elevatortask.beans.Container;
import com.epam.elevatortask.beans.NumberedStoryContainer;
import com.epam.elevatortask.beans.Passenger;
import com.epam.elevatortask.enums.TransportationState;

/**
 * Class represents passengers transportation task. Calls controller
 * synchronized methods to perform transportation process.
 * 
 */
public class TransportationTask implements Runnable {
	private final Controller controller;
	private final NumberedStoryContainer<Passenger> dispatchStoryContainer;
	private final Container<Passenger> elevatorContainer;
	private final Passenger passenger;

	/**
	 * @param controller
	 * @param dispatchStoryContainer
	 * @param elevatorContainer
	 * @param passenger
	 */
	public TransportationTask(Controller controller, NumberedStoryContainer<Passenger> dispatchStoryContainer,
			Container<Passenger> elevatorContainer, Passenger passenger) {
		this.controller = controller;
		this.dispatchStoryContainer = dispatchStoryContainer;
		this.elevatorContainer = elevatorContainer;
		this.passenger = passenger;
		passenger.setTransportationState(TransportationState.IN_PROGRESS);
	}

	/*
	 * Performs transportation task.
	 */
	public void run() {
		boolean operationSuccess = false;
		try {
			// Synchronized block for boarding purposes.
			synchronized (dispatchStoryContainer) {
				// Decrements barrier that allow controller to proceed after all
				// transportation task threads arrive to that position.
				controller.decBarrier();
				while (!operationSuccess && !Thread.currentThread().isInterrupted()) {
					dispatchStoryContainer.wait();
					operationSuccess = controller.requestBoard(passenger);
				}
			}
			operationSuccess = false;
			// Synchronized block for deboarding purposes.
			synchronized (elevatorContainer) {
				// Decrements barrier that allow controller to proceed after all
				// affected transportation task threads arrive to that
				// position.
				controller.decBarrier();
				while (!operationSuccess && !Thread.currentThread().isInterrupted()) {
					elevatorContainer.wait();
					operationSuccess = controller.requestDeboard(passenger);
				}
			}
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		if (Thread.currentThread().isInterrupted()) {
			passenger.setTransportationState(TransportationState.ABORTED);
		} else {
			passenger.setTransportationState(TransportationState.COMPLETED);
		}
	}
}
