package com.epam.elevatortask.logic;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;

import com.epam.elevatortask.beans.Building;
import com.epam.elevatortask.beans.Passenger;
import com.epam.elevatortask.beans.Container;
import com.epam.elevatortask.enums.Direction;
import com.epam.elevatortask.interfaces.IElevatorPainter;




public class Controller {
	private static final Logger LOG = Logger.getLogger(Controller.class);
	private final Building<Passenger> building;
	private final int initialPassengerNumber;
	private IElevatorPainter elevatorPainter;
	private volatile AtomicInteger loop=new AtomicInteger();
	private volatile AtomicInteger barrier = new AtomicInteger();
	private int remainPassengersNumber;
	private int currentStory = -1;
	private Direction lastDirection = Direction.UP;
	private Passenger bufferPassenger;
	
	public Controller(Building<Passenger> building, int passengersNumber) {
		this.building = building;
		this.initialPassengerNumber = passengersNumber;
		this.remainPassengersNumber = passengersNumber;
	}

	
	/**
	 * @param elevatorPainter the elevatorPainter to set
	 */
	public void setElevatorPainter(IElevatorPainter elevatorPainter) {
		this.elevatorPainter = elevatorPainter;
	}
	public int getInitialPassengersNumber(){
		return initialPassengerNumber;
	}

	public void doWork(){
		LOG.info("STARTING_TRANSPORTATION");
		barrier.addAndGet(initialPassengerNumber);
		while (remainPassengersNumber!=0) {
			move();
			elevatorPainter.paintElevatorArrival(currentStory);
			deboard();
			board();
			elevatorPainter.paintElevatorDispatch();
		}
		
	}
	private void paint(){
		
	}
	private synchronized void deboard(){
		while (barrier.get()!=0) {
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
				LOG.info("DEBOADING_OF_PASSENGER (passenger " + bufferPassenger.getPassengerID() + " on story-" + currentStory + ")");
				bufferPassenger = null;
				remainPassengersNumber--;
				elevatorPainter.paintDeboarding(currentStory, building);
				this.notifyAll();
			}
		}
	}
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
				LOG.info("BOADING_OF_PASSENGER (passenger " + bufferPassenger.getPassengerID() + " on story-" + currentStory + ")");
				bufferPassenger = null;
				elevatorPainter.paintBoarding(currentStory, building);
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
			decLoopNotify();
			return true;
		}else{
			decLoopNotify();
			return false;
		}
		
	}
	
	public synchronized boolean requestBoard(Passenger passenger) {
		if (passenger.getDirection().equals(calculateDirection())) {
			while (bufferPassenger!=null&&!Thread.currentThread().isInterrupted()){
				try {
					this.wait();
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
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
			loop.set(storyContainer.getPassengersNumber());
		}
	}
	private void decLoopNotify(){
		this.notifyAll();
		loop.decrementAndGet();
	}
	private void incBarrier(){
		barrier.incrementAndGet();
	}
	public synchronized void decBarrier(){
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
			goUp();
			break;
		case DOWN:
			goDown();
			break;
		}
		LOG.info("MOVING_ELEVATOR (from story-" + tmpStory + " to story-" + currentStory + ")");
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
}
