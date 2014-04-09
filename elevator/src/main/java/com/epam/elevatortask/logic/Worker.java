package com.epam.elevatortask.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.epam.elevatortask.beans.NumberedStoryContainer;
import com.epam.elevatortask.beans.Passenger;
import com.epam.elevatortask.beans.StoryContainer;
import com.epam.elevatortask.ui.ElevatorFrame;
import com.epam.elevatortask.ui.ElevatorPainter;

public class Worker {
	private List<NumberedStoryContainer<Passenger>> dispatchStoryContainersList;
	private List<NumberedStoryContainer<Passenger>> arrivalStoryContainersList;
	private StoryContainer<Passenger> elevatorContainer;
	private int storiesNumber;
	private int passengersNumber;
	private int elevatorCapacity;
	private ElevatorFrame elevatorFrame;
	private ExecutorService threadPool;
	private Controller controller;
	public Worker(int storiesNumber, int passengersNumber, int elevatorCapacity){
		this.storiesNumber = storiesNumber;
		this.passengersNumber = passengersNumber;
		this.elevatorCapacity = elevatorCapacity;
	}
	/**
	 * @return the dispatchStoryContainersList
	 */
	public List<NumberedStoryContainer<Passenger>> getDispatchStoryContainersList() {
		return dispatchStoryContainersList;
	}
	/**
	 * @return the arrivalStoryContainersList
	 */
	public List<NumberedStoryContainer<Passenger>> getArrivalStoryContainersList() {
		return arrivalStoryContainersList;
	}
	/**
	 * @return the elevatorContainer
	 */
	public StoryContainer<Passenger> getElevatorContainer() {
		return elevatorContainer;
	}
	/**
	 * @return the passengersNumber
	 */
	public int getPassengersNumber() {
		return passengersNumber;
	}
	public void setFrame(ElevatorFrame elevatorFrame){
		this.elevatorFrame = elevatorFrame;
	}
	public void abortTransportation(){
		threadPool.shutdownNow();
	}
	/**
	 * @param mainFrame the mainFrame to set
	 */
	
	public void initialization(){
		Random random = new Random();
		dispatchStoryContainersList = new ArrayList<NumberedStoryContainer<Passenger>>();
		arrivalStoryContainersList = new ArrayList<NumberedStoryContainer<Passenger>>();
		elevatorContainer = new StoryContainer<Passenger>();
		for (int i = 0; i < storiesNumber; i++){
			dispatchStoryContainersList.add(new NumberedStoryContainer<Passenger>(i));
			arrivalStoryContainersList.add(new NumberedStoryContainer<Passenger>(i));
		}
		for (int i = 0; i < passengersNumber; i++) {
			int initFloor = random.nextInt(storiesNumber);
			int destFloor = random.nextInt(storiesNumber);
			while (initFloor==destFloor) {
				destFloor = random.nextInt(storiesNumber);
			}
			dispatchStoryContainersList.get(initFloor).getPassengersList().add(new Passenger(i, destFloor));
		}
	}
	public void startTransportation(){
		controller = new Controller(dispatchStoryContainersList, arrivalStoryContainersList, elevatorContainer, storiesNumber, elevatorCapacity, passengersNumber);
		if (elevatorFrame!=null){
			ElevatorPainter elevatorPainter = new ElevatorPainter(elevatorFrame.getElevatorGrapthComponent()); 
			elevatorPainter.startTimer();
			controller.setElevatorPainter(elevatorPainter);
		}
		threadPool = Executors.newCachedThreadPool();
		for (int i = 0; i < storiesNumber; i++) {
			NumberedStoryContainer<Passenger> dispatchStoryContainer = dispatchStoryContainersList.get(i);
			for (Passenger passenger: dispatchStoryContainer.getPassengersList()){
				threadPool.submit(passenger.new TransportationTask(controller, dispatchStoryContainer, elevatorContainer));
			}
		}
		controller.doWork();
		threadPool.shutdownNow();	
	}
	public void printOnAbort(){
		controller.printOnAbort();
	}
}
