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
	public void setFrame(ElevatorFrame elevatorFrame){
		this.elevatorFrame = elevatorFrame;
	}
	public void abortTransportation(){
		threadPool.shutdownNow();
//		try {
//			threadPool.awaitTermination(1000, null);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	/**
	 * @param mainFrame the mainFrame to set
	 */
	
	public void initialization(){
		Random random = new Random();
		dispatchStoryContainersList = new ArrayList<NumberedStoryContainer<Passenger>>();
		arrivalStoryContainersList = new ArrayList<NumberedStoryContainer<Passenger>>();
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
		StoryContainer<Passenger> elevatorContainer = new StoryContainer<Passenger>();
		controller = new Controller(dispatchStoryContainersList, arrivalStoryContainersList, elevatorContainer, storiesNumber, elevatorCapacity, passengersNumber);
		if (elevatorFrame!=null){
			ElevatorPainter elevatorPainter = new ElevatorPainter(elevatorFrame.getElevatorGrapthComponent()); 
			elevatorPainter.startTimer();
			controller.setElevatorPainter(elevatorPainter);
		}
		threadPool = Executors.newFixedThreadPool(passengersNumber);
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
