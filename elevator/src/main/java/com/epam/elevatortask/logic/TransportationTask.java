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
//		System.out.println("thread " + Thread.currentThread().getName() + " start. initFloor "
//				+ dispatchStoryContainer.getStoryNumber() + " dest " + Passenger.this.getDestinationStory());
		boolean operationSuccess = false;
		synchronized (dispatchStoryContainer) {
			controller.decBarrier();
			while (!operationSuccess&&!Thread.currentThread().isInterrupted()) {
				try {
					dispatchStoryContainer.wait();
					operationSuccess = controller.requestBoard(passenger);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
		}
		operationSuccess = false;
		synchronized (elevatorContainer) {
			controller.decBarrier();
			while (!operationSuccess&&!Thread.currentThread().isInterrupted()) {
				try {
					elevatorContainer.wait();
					operationSuccess = controller.requestDeboard(passenger);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
		}
		if (Thread.currentThread().isInterrupted()){
			passenger.setTransportationState(TransportationState.ABORTED);
		}else{
			passenger.setTransportationState(TransportationState.COMPLETED);
		}
	}
}
