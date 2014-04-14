package com.epam.elevatortask.logic;

import com.epam.elevatortask.beans.Container;
import com.epam.elevatortask.beans.NumberedStoryContainer;
import com.epam.elevatortask.beans.Passenger;
import com.epam.elevatortask.enums.TransportationState;

public class TransportationTask implements Runnable {
	private final Controller controller;
	private final NumberedStoryContainer<Passenger> dispatchStoryContainer;
	private final Container<Passenger> elevatorContainer;
	private final Passenger passenger;

	public TransportationTask(Controller controller, NumberedStoryContainer<Passenger> dispatchStoryContainer,
			Container<Passenger> elevatorContainer, Passenger passenger) {
		this.controller = controller;
		this.dispatchStoryContainer = dispatchStoryContainer;
		this.elevatorContainer = elevatorContainer;
		this.passenger = passenger;
		passenger.setTransportationState(TransportationState.IN_PROGRESS);
	}

	public void run() {
		boolean operationSuccess = false;
		try {
			synchronized (dispatchStoryContainer) {
				controller.decBarrier();
				while (!operationSuccess && !Thread.currentThread().isInterrupted()) {
					dispatchStoryContainer.wait();
					operationSuccess = controller.requestBoard(passenger);
				}
			}
			operationSuccess = false;
			synchronized (elevatorContainer) {
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
