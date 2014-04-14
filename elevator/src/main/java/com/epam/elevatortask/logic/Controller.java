package com.epam.elevatortask.logic;

import org.apache.log4j.Logger;

import com.epam.elevatortask.beans.Building;
import com.epam.elevatortask.beans.Passenger;
import com.epam.elevatortask.beans.Container;
import com.epam.elevatortask.enums.ControllerActions;




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
	
	public Controller(Building<Passenger> building, int passengersNumber) {
		this.building = building;
		this.initialPassengerNumber = passengersNumber;
		this.remainPassengersNumber = passengersNumber;
		this.presenter = new Presenter();
		elevator = new Elevator(building.getStoriesNumber(), presenter);
	}

	
	public int getInitialPassengersNumber(){
		return initialPassengerNumber;
	}

	public void doWork(){
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
	
	private synchronized void deboard() throws InterruptedException{
		while (barrier!=0 && !Thread.currentThread().isInterrupted()) {
			this.wait();
		}
		notifyPassengersSetLoop(building.getElevatorContainer());
		while ((loop!=0 || bufferPassenger!=null) && !Thread.currentThread().isInterrupted()) {
			while (bufferPassenger==null && loop!=0 && !Thread.currentThread().isInterrupted()) {
				this.wait();
			}
			if (bufferPassenger != null){
				building.removeElevatorPassenger(bufferPassenger);
				building.addArrivalPassenger(currentStory, bufferPassenger);
				presenter.deboardingPassenger(currentStory, bufferPassenger);
				bufferPassenger = null;
				remainPassengersNumber--;
				this.notifyAll();
			}
		}
	}
	private synchronized void board() throws InterruptedException{
		notifyPassengersSetLoop(building.getDispatchContainer(currentStory));
		while ((loop!=0 || bufferPassenger!=null) && !Thread.currentThread().isInterrupted()) {
			while (bufferPassenger==null && loop!=0 && !Thread.currentThread().isInterrupted()) {
				this.wait();
			}
			if (bufferPassenger != null){
				building.removeDispatchPassenger(currentStory, bufferPassenger);
				building.addElevatorPassenger(bufferPassenger);
				presenter.boardingPassenger(currentStory, bufferPassenger);
				bufferPassenger = null;
				this.notifyAll();
			}
		}
	}
	public synchronized boolean requestDeboard(Passenger passenger) throws InterruptedException{
		if (passenger.getDestinationStory() == currentStory){
			while (bufferPassenger!=null && !Thread.currentThread().isInterrupted()) {
				this.wait();
			}
			bufferPassenger = passenger;
			decLoopNotify();
			return true;
		}else{
			decLoopNotify();
			return false;
		}	
	}
	
	public synchronized boolean requestBoard(Passenger passenger) throws InterruptedException{
		if (passenger.getDirection().equals(elevator.getCurrentDirection())) {
			while (bufferPassenger!=null && !Thread.currentThread().isInterrupted()){
				this.wait();
			}
			if (!building.isElevatorFull()){
				bufferPassenger = passenger;
				decLoopNotify();
				incBarrier();
				return true;
			}else{
				decLoopNotify();
				return false;
			}
		}else{
			decLoopNotify();
			return false;
		}
		
	}
	private void notifyPassengersSetLoop(Container<Passenger> storyContainer){
		synchronized (storyContainer) {
			storyContainer.notifyAll();
			loop = storyContainer.getPassengersNumber();
		}
	}
	private void decLoopNotify(){
		this.notifyAll();
		loop--;
	}
	private void incBarrier(){
		barrier++;
	}
	public synchronized void decBarrier(){
		this.notifyAll();
		barrier--;
	}


	/**
	 * @param presenter the presenter to set
	 */
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
		elevator.setPresenter(presenter);
	}
}
