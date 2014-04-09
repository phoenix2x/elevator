package com.epam.elevatortask.logic;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import com.epam.elevatortask.beans.Building;
import com.epam.elevatortask.beans.NumberedStoryContainer;
import com.epam.elevatortask.beans.Passenger;
import com.epam.elevatortask.enums.TransportationState;
import com.epam.elevatortask.ui.ElevatorFrame;
import com.epam.elevatortask.ui.ElevatorPainter;

public class Worker {
	private static final Logger LOG = Logger.getLogger(Worker.class);
	private final Building<Passenger> building;
//	private int passengersNumber;
	private ElevatorFrame elevatorFrame;
	private ExecutorService threadPool;
	private Controller controller;
	public Worker(int storiesNumber, int passengersNumber, int elevatorCapacity){
		this.building = new Building<>(storiesNumber, elevatorCapacity);
//		this.passengersNumber = passengersNumber;
		Random random = new Random();
		for (int i = 0; i < passengersNumber; i++) {
			int initStory = random.nextInt(storiesNumber);
			building.addDispatchPassenger(initStory, new Passenger(initStory, storiesNumber));
		}
		controller = new Controller(building, passengersNumber);
	}
	public Building<Passenger> getBuilding(){
		return building;
	}
	/**
	 * @return the passengersNumber
	 */
//	public int getPassengersNumber() {
//		return passengersNumber;
//	}
	public void setFrame(ElevatorFrame elevatorFrame){
		this.elevatorFrame = elevatorFrame;
	}
	public void abortTransportation(){
		threadPool.shutdownNow();
	}
	
	public void startTransportation(){
		if (elevatorFrame!=null){
			ElevatorPainter elevatorPainter = new ElevatorPainter(elevatorFrame.getElevatorGrapthComponent()); 
			elevatorPainter.startTimer();
			controller.setElevatorPainter(elevatorPainter);
		}
		threadPool = Executors.newCachedThreadPool();
		for (int i = 0; i < building.getStoriesNumber(); i++) {
			NumberedStoryContainer<Passenger> dispatchStoryContainer = building.getDispatchContainer(i);
			for (Passenger passenger: dispatchStoryContainer){
				threadPool.submit(new TransportationTask(controller, dispatchStoryContainer, building.getElevatorContainer(), passenger));
			}
		}
		controller.doWork();
		threadPool.shutdown();
		validateResult();
	}
	public void validateResult(){
		boolean result = true;
		int currentDispatchPassengersNumber;
		for(int i = 0; i<building.getStoriesNumber(); i++){
			currentDispatchPassengersNumber = building.getDispatchContainer(i).getPassengersNumber();
			LOG.info("DispatchContainer " + i + " size: " + currentDispatchPassengersNumber);
			if (currentDispatchPassengersNumber!=0){
				result = false;
			}
		}
		currentDispatchPassengersNumber = building.getElevatorContainer().getPassengersNumber();
		LOG.info("ElevatorContainer size: " + currentDispatchPassengersNumber);
		if (currentDispatchPassengersNumber!=0){
			result = false;
		}
		int passengerNumber = 0;
		for(int i = 0; i<building.getStoriesNumber(); i++){
			LOG.info("Story " + i);
			for (Passenger passenger: building.getArrivalContainer(i)){
				passengerNumber++;
				LOG.info("Passenger " + passenger.getPassengerID() + " destinationStory " + passenger.getDestinationStory() + " transportationState " + passenger.getTransportationState());
				if (passenger.getDestinationStory()!=i || passenger.getTransportationState() != TransportationState.COMPLETED){
					result = false;
				}
			}
		}
		int initialPassengersNumber = controller.getInitialPassengersNumber();
		LOG.info("Inital number passenger " + initialPassengersNumber + ", final number " + passengerNumber);
		if (initialPassengersNumber!=passengerNumber){
			result = false;
		}
		LOG.info("Test succed: " + result);
		LOG.info("COMPLETION_TRANSPORTATION");
	}
//	public void printOnAbort(){
//		controller.printOnAbort();
//	}
}
