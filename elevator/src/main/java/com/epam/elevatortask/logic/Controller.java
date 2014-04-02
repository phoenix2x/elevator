package com.epam.elevatortask.logic;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;

import com.epam.elevatortask.beans.NumberedStoryContainer;
import com.epam.elevatortask.beans.Passenger;
import com.epam.elevatortask.beans.StoryContainer;




public class Controller {
	private static final Logger LOG = Logger.getLogger(Controller.class);
	private final StoryContainer<Passenger> elevatorContainer;
	private final List<NumberedStoryContainer<Passenger>> dispatchStoryContainersList;
	private final List<NumberedStoryContainer<Passenger>> arrivalStoryContainersList;
	private final int storiesNumber;
	private final int elevatorCapacity;
	private final int initalPassengerNumber;
	private AtomicInteger loop=new AtomicInteger();
	private AtomicInteger barrier = new AtomicInteger();
	private int remainPassengersNumber;
	private int currentStory = -1;
	private Direction lastDirection = Direction.UP;
	private Passenger bufferPassenger;
	private enum Direction {
		UP, DOWN
	}
	public Controller(List<NumberedStoryContainer<Passenger>> dispatchStoryContainersList, List<NumberedStoryContainer<Passenger>> arrivalStoryContainersList, StoryContainer<Passenger> elevatorContainer, int storiesNumber, int elevatorCapacity, int passengersNumber) {
		this.dispatchStoryContainersList = dispatchStoryContainersList;
		this.arrivalStoryContainersList = arrivalStoryContainersList;
		this.storiesNumber = storiesNumber;
		this.elevatorCapacity = elevatorCapacity;
		this.initalPassengerNumber = passengersNumber;
		this.remainPassengersNumber = passengersNumber;
		this.elevatorContainer = elevatorContainer;
	}

	
	public void doWork(){
		LOG.info("STARTING_TRANSPORTATION");
		barrier.addAndGet(initalPassengerNumber);
		while (remainPassengersNumber!=0) {
//			System.out.println("remain pass " + remainPassengersNumber);
//			try {
//				Thread.sleep(10);
//			} catch (InterruptedException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//			System.out.println();
			move();
//			System.out.println("curFlor" + currentFloor + "remain pass" + remainPassengersNumber);
//			barrier.addAndGet(elevatorContainer.getPassengersList().size());
//			System.out.println("barrier after add " + barrier.get());
			deboard();
//			barrier.addAndGet(dispatchStoryContainersList.get(currentFloor).getPassengersList().size());
			board();
			
			
			
			
			
		}
		validateResult();
		
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
		notifyPassengersSetLoop(elevatorContainer);
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
				elevatorContainer.getPassengersList().remove(bufferPassenger);
				arrivalStoryContainersList.get(currentStory).getPassengersList().add(bufferPassenger);
//				System.out.println(bufferPassenger.getPassengerID() + "deboarded");
				LOG.info("DEBOADING_OF_PASSENGER (passenger " + bufferPassenger.getPassengerID() + " on story-" + currentStory + ")");
				bufferPassenger = null;
				remainPassengersNumber--;
				this.notifyAll();
			}
		}
		
	}
	private void movePassenger(StoryContainer<Passenger> sourceStory, StoryContainer<Passenger> destinationStory){
		notifyPassengersSetLoop(sourceStory);
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
				sourceStory.getPassengersList().remove(bufferPassenger);
				destinationStory.getPassengersList().add(bufferPassenger);
//				System.out.println(bufferPassenger.getPassengerID() + "deboarded");
				bufferPassenger = null;
				remainPassengersNumber--;
				this.notifyAll();
			}
		}
	}
	private synchronized void board(){
		notifyPassengersSetLoop(dispatchStoryContainersList.get(currentStory));
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
				dispatchStoryContainersList.get(currentStory).getPassengersList().remove(bufferPassenger);
				elevatorContainer.getPassengersList().add(bufferPassenger);
//				System.out.println(bufferPassenger.getPassengerID() + " boarded");
				LOG.info("BOADING_OF_PASSENGER (passenger " + bufferPassenger.getPassengerID() + " on story-" + currentStory + ")");
				bufferPassenger = null;
				this.notifyAll();
			}
		}
	}
	public synchronized boolean requestDeboard(Passenger passenger){
		if (passenger.getDestinationStory() == currentStory){
			while (bufferPassenger!=null) {
				try {
					this.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
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
	
	public synchronized boolean requestBoard(Passenger passenger){
		if (passengerDirection(passenger).equals(calculateDirection())) {
			while (bufferPassenger!=null){
				try {
					this.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (!isFull()){
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
	private void notifyPassengersSetLoop(StoryContainer<Passenger> storyContainer){
		synchronized (storyContainer) {
//			System.out.println("signal to " + storyContainer);
			storyContainer.notifyAll();
			loop.set(storyContainer.getPassengersList().size());
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
			if (currentStory==storiesNumber-1){
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
	private boolean isFull() {
		return elevatorContainer.getPassengersList().size() == elevatorCapacity;
	}
	public void validateResult(){
		boolean result = true;
		for(NumberedStoryContainer<Passenger> dispatchStoryContainer: dispatchStoryContainersList){
			LOG.info("DispatchContainer " + dispatchStoryContainer.getStoryNumber() + " size: " + dispatchStoryContainer.getPassengersList().size());
			if (dispatchStoryContainer.getPassengersList().size()!=0){
				result = false;
			}
		}
		LOG.info("ElevatorContainer size: " + elevatorContainer.getPassengersList().size());
		if (elevatorContainer.getPassengersList().size()!=0){
			result = false;
		}
		int passengerNumber = 0;
		for(NumberedStoryContainer<Passenger> arrivalStoryContainer: arrivalStoryContainersList){
			LOG.info("Story " + arrivalStoryContainer.getStoryNumber());
			for (Passenger passenger: arrivalStoryContainer.getPassengersList()){
				passengerNumber++;
				LOG.info("Passenger " + passenger.getPassengerID() + " destinationStory " + passenger.getDestinationStory() + " transportationState " + passenger.getTransportationState());
				if (passenger.getDestinationStory()!=arrivalStoryContainer.getStoryNumber() || passenger.getTransportationState() != Passenger.TransportationState.COMPLETED){
					result = false;
				}
			}
		}
		LOG.info("Inital number passenger " + initalPassengerNumber + ", final number " + passengerNumber);
		if ( initalPassengerNumber!=passengerNumber){
			result = false;
		}
		LOG.info("Test succed: " + result);
		LOG.info("COMPLETION_TRANSPORTATION");
	}
}
