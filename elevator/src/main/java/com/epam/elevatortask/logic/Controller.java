package com.epam.elevatortask.logic;

import org.apache.log4j.Logger;

import com.epam.elevatortask.beans.Building;
import com.epam.elevatortask.beans.Passenger;
import com.epam.elevatortask.beans.Container;
import com.epam.elevatortask.enums.ControllerActions;

/**
 * Class represents controller of the elevator. Provides synchronized methods for
 * transportation tasks, and method which start transportation process.
 * Synchronization is performed on instance of that class, and on instances of containers(elevator and dispatch).
 * 
 */
public class Controller {
	private static final Logger LOG = Logger.getLogger(Controller.class);
	private final Building<Passenger> building;
	private final int initialPassengerNumber;
	private final Elevator elevator;
	private Presenter presenter;
	private int loop;
	private int barrier;
	private int remainPassengersNumber;
	private int currentStory;
	private Passenger bufferPassenger;

	/**
	 * @param building
	 * @param passengersNumber
	 */
	public Controller(Building<Passenger> building, int passengersNumber) {
		this.building = building;
		this.initialPassengerNumber = passengersNumber;
		this.remainPassengersNumber = passengersNumber;
		this.presenter = new Presenter();
		elevator = new Elevator(building.getStoriesNumber(), presenter);
	}

	/**
	 * @param presenter
	 *            the presenter to set. Also pass presenter to elevator.
	 */
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
		elevator.setPresenter(presenter);
	}

	/**
	 * @return initial number of passengers for validation purposes.
	 */
	public int getInitialPassengersNumber() {
		return initialPassengerNumber;
	}

	/**
	 * Method performs transportation process.
	 */
	public void doWork() {
		LOG.info(ControllerActions.STARTING_TRANSPORTATION.getDescription());
		synchronized (this) {
			barrier = barrier + initialPassengerNumber;
		}
		while (remainPassengersNumber != 0 && !Thread.currentThread().isInterrupted()) {
			try {
				deboard();
				board();
				currentStory = elevator.move();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	}

	/**
	 * Synchronized method. Transportation task threads reported achieving
	 * particular position.
	 */
	public synchronized void decBarrier() {
		this.notifyAll();
		barrier--;
	}

	/**
	 * Synchronized method. Transportation task threads asks controller for
	 * deboarding.
	 * 
	 * @param passenger
	 * @return true if passenger successfully deboarded.
	 * @throws InterruptedException
	 */
	public synchronized boolean requestDeboard(Passenger passenger) throws InterruptedException {
		if (passenger.getDestinationStory() == currentStory) {
			while (bufferPassenger != null && !Thread.currentThread().isInterrupted()) {
				this.wait();
			}
			// Asks the controller to deboard passenger, putting it in the buffer.
			bufferPassenger = passenger;
			decLoopNotify();
			return true;
		} else {
			decLoopNotify();
			return false;
		}
	}

	/**
	 * Synchronized method. Transportation task threads asks controller for
	 * boarding.
	 * 
	 * @param passenger
	 * @return true if passenger successfully boarded.
	 * @throws InterruptedException
	 */
	public synchronized boolean requestBoard(Passenger passenger) throws InterruptedException {
		if (passenger.getDirection().equals(elevator.getCurrentDirection())) {
			while (bufferPassenger != null && !Thread.currentThread().isInterrupted()) {
				this.wait();
			}
			if (!building.isElevatorFull()) {
				// Asks the controller to board passenger, putting it in the buffer.
				bufferPassenger = passenger;
				decLoopNotify();
				incBarrier();
				return true;
			} else {
				decLoopNotify();
				return false;
			}
		} else {
			decLoopNotify();
			return false;
		}
	}

	/**
	 * Synchronized method. Controller thread performs deboarding of passengers.
	 * 
	 * @throws InterruptedException
	 */
	private synchronized void deboard() throws InterruptedException {
		// Wait until all affected transportation tasks arrived at specific
		// position.
		while (barrier != 0 && !Thread.currentThread().isInterrupted()) {
			this.wait();
		}
		// Notify transportation tasks and calculate their number in given
		// container.
		notifyPassengersSetLoop(building.getElevatorContainer());
		// Performs passengers deboarding.
		while ((loop != 0 || bufferPassenger != null) && !Thread.currentThread().isInterrupted()) {
			while (bufferPassenger == null && loop != 0 && !Thread.currentThread().isInterrupted()) {
				this.wait();
			}
			if (bufferPassenger != null) {
				building.removeElevatorPassenger(bufferPassenger);
				building.addArrivalPassenger(currentStory, bufferPassenger);
				// Log controllers action and informs elevatorForm in GUI mode.
				presenter.deboardingPassenger(currentStory, bufferPassenger);
				bufferPassenger = null;
				remainPassengersNumber--;
				this.notifyAll();
			}
		}
	}

	/**
	 * Synchronized method. Controller thread performs boarding of passengers.
	 * 
	 * @throws InterruptedException
	 */
	private synchronized void board() throws InterruptedException {
		// Notify transportation tasks and calculate their number in given
		// container.
		notifyPassengersSetLoop(building.getDispatchContainer(currentStory));
		// Performs passengers boarding.
		while ((loop != 0 || bufferPassenger != null) && !Thread.currentThread().isInterrupted()) {
			while (bufferPassenger == null && loop != 0 && !Thread.currentThread().isInterrupted()) {
				this.wait();
			}
			if (bufferPassenger != null) {
				building.removeDispatchPassenger(currentStory, bufferPassenger);
				building.addElevatorPassenger(bufferPassenger);
				// Log controllers actions and informs elevatorForm in GUI mode.
				presenter.boardingPassenger(currentStory, bufferPassenger);
				bufferPassenger = null;
				this.notifyAll();
			}
		}
	}

	/**
	 * Controller notifies transportation tasks waiting on specified monitor. Also
	 * calculates their number.
	 * 
	 * @param storyContainer
	 */
	private void notifyPassengersSetLoop(Container<Passenger> storyContainer) {
		synchronized (storyContainer) {
			storyContainer.notifyAll();
			loop = storyContainer.getPassengersNumber();
		}
	}

	/**
	 * Transportation task threads decrements number of remained threads in
	 * current working circle. Also evokes waiting threads.
	 */
	private void decLoopNotify() {
		this.notifyAll();
		loop--;
	}

	/**
	 * Transportation task threads increments barrier. Controller thread will
	 * wait until they did not reduce it again, after reaching a certain
	 * position.
	 */
	private void incBarrier() {
		barrier++;
	}
}
