package com.epam.elevatortask;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.epam.elevatortask.beans.NumberedStoryContainer;
import com.epam.elevatortask.beans.Passenger;
import com.epam.elevatortask.beans.StoryContainer;
import com.epam.elevatortask.logic.Controller;
import com.epam.elevatortask.ui.MainWindow;

public class Runner {

	private static final int storiesNumber = 10;
	private static final int passengersNumber = 50;
	private static final int elevatorCapacity = 10;
	private static final int animationBoost = 0;

	public static void main(String[] args) {
		MainWindow mainWindow = new MainWindow();
		new Thread(mainWindow).start();
		Random random = new Random();
		List<NumberedStoryContainer<Passenger>>  dispatchStoryContainersList = new ArrayList<NumberedStoryContainer<Passenger>>();
		List<NumberedStoryContainer<Passenger>> arrivalStoryContainersList = new ArrayList<NumberedStoryContainer<Passenger>>();
		StoryContainer<Passenger> elevatorContainer = new StoryContainer<Passenger>();
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
		Controller controller = new Controller(dispatchStoryContainersList, arrivalStoryContainersList, elevatorContainer, storiesNumber, elevatorCapacity, passengersNumber);
		ExecutorService threadPool = Executors.newFixedThreadPool(passengersNumber);
		for (int i = 0; i < storiesNumber; i++) {
			NumberedStoryContainer<Passenger> dispatchStoryContainer = dispatchStoryContainersList.get(i);
			for (Passenger passenger: dispatchStoryContainer.getPassengersList()){
//				threadPool.submit(passenger.new TransportationTask(controller, dispatchStoryContainer, elevatorContainer));
				new Thread(passenger.new TransportationTask(controller, dispatchStoryContainer, elevatorContainer),"pass " + passenger.getPassengerID()).start();
			}
		}
		controller.doWork();
	}

}
