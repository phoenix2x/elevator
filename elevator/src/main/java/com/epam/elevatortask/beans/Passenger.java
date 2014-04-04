package com.epam.elevatortask.beans;

import com.epam.elevatortask.logic.Controller;

public class Passenger {

	private final int passengerID;
	private final int destinationStory;
	private TransportationState transportationState = TransportationState.NOT_STARTED;

	/**
	 * @param passengerID
	 * @param destinationStory
	 */
	public Passenger(int passengerID, int destinationStory) {
		super();
		this.passengerID = passengerID;
		this.destinationStory = destinationStory;
	}

	/**
	 * @return the transportationState
	 */
	public TransportationState getTransportationState() {
		return transportationState;
	}

	/**
	 * @return the passengerID
	 */
	public int getPassengerID() {
		return passengerID;
	}

	/**
	 * @return the destinationStory
	 */
	public int getDestinationStory() {
		return destinationStory;
	}

	public enum TransportationState {
		NOT_STARTED, IN_PROGRESS, COMPLETED, ABORTED
	}

	public class TransportationTask implements Runnable {
		private final Controller controller;
		private final NumberedStoryContainer<Passenger> dispatchStoryContainer;
		private final StoryContainer<Passenger> elevatorContainer;

		public TransportationTask(Controller controller, NumberedStoryContainer<Passenger> dispatchStoryContainer,
				StoryContainer<Passenger> elevatorContainer) {
			this.controller = controller;
			this.dispatchStoryContainer = dispatchStoryContainer;
			this.elevatorContainer = elevatorContainer;
			transportationState = TransportationState.IN_PROGRESS;
		}

		public void run() {
//			System.out.println("thread " + Thread.currentThread().getName() + " start. initFloor "
//					+ dispatchStoryContainer.getStoryNumber() + " dest " + Passenger.this.getDestinationStory());
			boolean operationSuccess = false;
			synchronized (dispatchStoryContainer) {
				controller.decBarrier();
				while (!operationSuccess&&!Thread.currentThread().isInterrupted()) {
					try {
						dispatchStoryContainer.wait();
						operationSuccess = controller.requestBoard(Passenger.this);
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
						operationSuccess = controller.requestDeboard(Passenger.this);
					} catch (InterruptedException e) {
						Thread.currentThread().interrupt();
					}
				}
			}
			if (Thread.currentThread().isInterrupted()){
				transportationState = TransportationState.ABORTED;
			}else{
				transportationState = TransportationState.COMPLETED;
			}
		}
	}
}
