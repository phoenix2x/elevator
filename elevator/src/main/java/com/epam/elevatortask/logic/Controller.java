package com.epam.elevatortask.logic;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;

import com.epam.elevatortask.beans.Building;
import com.epam.elevatortask.beans.Passenger;
import com.epam.elevatortask.beans.Container;
import com.epam.elevatortask.ui.ElevatorPainter;
import com.epam.elevatortask.ui.beans.ElevatorPaintEvent;
import com.epam.elevatortask.ui.beans.ElevatorGrapthComponent.ElevatorAction;




public class Controller {
	private static final Logger LOG = Logger.getLogger(Controller.class);
//	private final Container<Passenger> elevatorContainer;
//	private final List<NumberedStoryContainer<Passenger>> dispatchStoryContainersList;
//	private final List<NumberedStoryContainer<Passenger>> arrivalStoryContainersList;
	private final Building<Passenger> building;
	private final int initialPassengerNumber;
	private ElevatorPainter elevatorPainter;
	private volatile AtomicInteger loop=new AtomicInteger();
	private volatile AtomicInteger barrier = new AtomicInteger();
	private int remainPassengersNumber;
	private int currentStory = -1;
	private Direction lastDirection = Direction.UP;
	private Passenger bufferPassenger;
	private enum Direction {
		UP, DOWN
	}
	public Controller(Building<Passenger> building, int passengersNumber) {
		this.building = building;
		this.initialPassengerNumber = passengersNumber;
		this.remainPassengersNumber = passengersNumber;
	}

	
	/**
	 * @param elevatorPainter the elevatorPainter to set
	 */
	public void setElevatorPainter(ElevatorPainter elevatorPainter) {
		this.elevatorPainter = elevatorPainter;
	}
	public int getInitialPassengersNumber(){
		return initialPassengerNumber;
	}

	public void doWork(){
//		elevatorPainter = new ElevatorPainter();
		LOG.info("STARTING_TRANSPORTATION");
		barrier.addAndGet(initialPassengerNumber);
		while (remainPassengersNumber!=0) {
//			try {
//				Thread.sleep(100);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			move();
			if (elevatorPainter!=null){
				elevatorPainter.addEvent(new ElevatorPaintEvent(currentStory, ElevatorAction.CLOSED));
				elevatorPainter.addEvent(new ElevatorPaintEvent(currentStory, ElevatorAction.OPENED));
			}
			deboard();
			board();
			if (elevatorPainter!=null){
				elevatorPainter.addEvent(new ElevatorPaintEvent(currentStory, ElevatorAction.CLOSED));
			}
		}
//		validateResult();
		
	}
	public void guiDoWork(){
//		elevatorPainter = new ElevatorPainter();
		LOG.info("STARTING_TRANSPORTATION");
		barrier.addAndGet(initialPassengerNumber);
		while (remainPassengersNumber!=0) {
			move();
			if (elevatorPainter!=null){
				elevatorPainter.addEvent(new ElevatorPaintEvent(currentStory, ElevatorAction.CLOSED));
				elevatorPainter.addEvent(new ElevatorPaintEvent(currentStory, ElevatorAction.OPENED));
			}
			deboard();
			board();
			if (elevatorPainter!=null){
				elevatorPainter.addEvent(new ElevatorPaintEvent(currentStory, ElevatorAction.CLOSED));
			}
		}
//		validateResult();
		
	}
	
	private synchronized void deboard(){
//		System.out.println("deboard barrier before while " + barrier.get());
		while (barrier.get()!=0) {
//			System.out.println("deboard barrier " + barrier.get());
			try {
				this.wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		notifyPassengersSetLoop(building.getElevatorContainer());
		while (loop.get()!=0 || bufferPassenger!=null) {
			
		
			while (bufferPassenger==null && loop.get()!=0) {
				try {
					this.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (bufferPassenger != null){
				building.removeElevatorPassenger(bufferPassenger);
				building.addArrivalPassenger(currentStory, bufferPassenger);
//				System.out.println(bufferPassenger.getPassengerID() + "deboarded");
				LOG.info("DEBOADING_OF_PASSENGER (passenger " + bufferPassenger.getPassengerID() + " on story-" + currentStory + ")");
				bufferPassenger = null;
				remainPassengersNumber--;
				this.notifyAll();
			}
		}
		
	}
//	private void movePassenger(Container<Passenger> sourceStory, Container<Passenger> destinationStory){
//		notifyPassengersSetLoop(sourceStory);
//		while (loop.get()!=0 || bufferPassenger!=null) {
//			
//		
//			while (bufferPassenger==null && loop.get()!=0) {
//				try {
//					this.wait();
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//			if (bufferPassenger != null){
//				sourceStory.getPassengersList().remove(bufferPassenger);
//				destinationStory.getPassengersList().add(bufferPassenger);
////				System.out.println(bufferPassenger.getPassengerID() + "deboarded");
//				bufferPassenger = null;
//				remainPassengersNumber--;
//				this.notifyAll();
//			}
//		}
//	}
	private synchronized void board(){
		notifyPassengersSetLoop(building.getDispatchContainer(currentStory));
		while (loop.get()!=0 || bufferPassenger!=null) {
			
			
			while (bufferPassenger==null && loop.get()!=0) {
				try {
					this.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (bufferPassenger != null){
				building.removeDispatchPassenger(currentStory, bufferPassenger);
				building.addElevatorPassenger(bufferPassenger);
//				System.out.println(bufferPassenger.getPassengerID() + " boarded");
				LOG.info("BOADING_OF_PASSENGER (passenger " + bufferPassenger.getPassengerID() + " on story-" + currentStory + ")");
				bufferPassenger = null;
				this.notifyAll();
			}
		}
	}
	public synchronized boolean requestDeboard(Passenger passenger){
		if (passenger.getDestinationStory() == currentStory){
			while (bufferPassenger!=null&&!Thread.currentThread().isInterrupted()) {
				try {
					this.wait();
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
			bufferPassenger = passenger;
			this.notifyAll();
			decLoop();
			return true;
		}else{
			this.notifyAll();
			decLoop();
			return false;
		}
		
	}
	
	public synchronized boolean requestBoard(Passenger passenger) {
		if (passengerDirection(passenger).equals(calculateDirection())) {
			while (bufferPassenger!=null&&!Thread.currentThread().isInterrupted()){
				try {
					this.wait();
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
			if (!building.isElevatorFull()){
				bufferPassenger = passenger;
				this.notifyAll();
				decLoop();
				incBarrier();
				return true;
			}else{
				this.notifyAll();
				decLoop();
				return false;
			}
			
		}else{
			this.notifyAll();
			decLoop();
			return false;
		}
		
	}
	private Direction passengerDirection(Passenger passenger){
		if (passenger.getDestinationStory()<currentStory){
			return Direction.DOWN;
		}else{
			return Direction.UP;
		}
	}
	private void notifyPassengersSetLoop(Container<Passenger> storyContainer){
		synchronized (storyContainer) {
//			System.out.println("signal to " + storyContainer);
			storyContainer.notifyAll();
			loop.set(storyContainer.getPassengersNumber());
		}
	}
	public synchronized void decLoop(){
//		System.out.println(Thread.currentThread().getName() + "dec loop");
		loop.decrementAndGet();
	}
	public synchronized void incBarrier(){
//		System.out.println(Thread.currentThread().getName() + " inc barrier");
		barrier.incrementAndGet();
	}
	public synchronized void decBarrier(){
//		System.out.println(Thread.currentThread().getName() + " dec barrier");
		this.notifyAll();
		barrier.decrementAndGet();
	}
	/**
	 * @return the currentFloor
	 */
	public int getCurrentStory() {
		return currentStory;
	}
	private void move(){
		Direction direction = calculateDirection();
		int tmpStory = currentStory;
		switch (direction) {
		case UP:
//			System.out.println("going up");
			goUp();
			LOG.info("MOVING_ELEVATOR (from story-" + tmpStory + " to story-" + currentStory + ")");
			break;
		case DOWN:
//			System.out.println("going down");
			goDown();
			LOG.info("MOVING_ELEVATOR (from story-" + tmpStory + " to story-" + currentStory + ")");
			break;
		}
	}
	private Direction calculateDirection() {
		if (currentStory==0){
			lastDirection = Direction.UP;	
		}else{
			if (currentStory==building.getStoriesNumber()-1){
				lastDirection=Direction.DOWN;
			}
		}
		return lastDirection;
	}
	private void goUp() {
		currentStory++;
	}

	private void goDown() {
		currentStory--;
	}
//	public void validateResult(){
//		boolean result = true;
//		for(NumberedStoryContainer<Passenger> dispatchStoryContainer: dispatchStoryContainersList){
//			LOG.info("DispatchContainer " + dispatchStoryContainer.getStoryNumber() + " size: " + dispatchStoryContainer.getPassengersList().size());
//			if (dispatchStoryContainer.getPassengersList().size()!=0){
//				result = false;
//			}
//		}
//		LOG.info("ElevatorContainer size: " + elevatorContainer.getPassengersList().size());
//		if (elevatorContainer.getPassengersList().size()!=0){
//			result = false;
//		}
//		int passengerNumber = 0;
//		for(NumberedStoryContainer<Passenger> arrivalStoryContainer: arrivalStoryContainersList){
//			LOG.info("Story " + arrivalStoryContainer.getStoryNumber());
//			for (Passenger passenger: arrivalStoryContainer.getPassengersList()){
//				passengerNumber++;
//				LOG.info("Passenger " + passenger.getPassengerID() + " destinationStory " + passenger.getDestinationStory() + " transportationState " + passenger.getTransportationState());
//				if (passenger.getDestinationStory()!=arrivalStoryContainer.getStoryNumber() || passenger.getTransportationState() != Passenger.TransportationState.COMPLETED){
//					result = false;
//				}
//			}
//		}
//		LOG.info("Inital number passenger " + initalPassengerNumber + ", final number " + passengerNumber);
//		if ( initalPassengerNumber!=passengerNumber){
//			result = false;
//		}
//		LOG.info("Test succed: " + result);
//		LOG.info("COMPLETION_TRANSPORTATION");
//	}
//	public void printOnAbort(){
//		for(NumberedStoryContainer<Passenger> arrivalStoryContainer: arrivalStoryContainersList){
//			LOG.info("Story " + arrivalStoryContainer.getStoryNumber());
//			for (Passenger passenger: arrivalStoryContainer.getPassengersList()){
//				LOG.info("Passenger " + passenger.getPassengerID() + " transportationState " + passenger.getTransportationState());
//			}
//		}
//
//		for(NumberedStoryContainer<Passenger> dispatchStoryContainer: dispatchStoryContainersList){
//			LOG.info("Dispatch story " + dispatchStoryContainer.getStoryNumber());
//			for (Passenger passenger: dispatchStoryContainer.getPassengersList()){
//				LOG.info("Passenger " + passenger.getPassengerID() + " transportationState " + passenger.getTransportationState());
//			}
//		}
//		LOG.info("Elevator container");
//		for(Passenger passenger: elevatorContainer.getPassengersList()){
//			LOG.info("Passenger " + passenger.getPassengerID() + " transportationState " + passenger.getTransportationState());
//		}
//	}
}
