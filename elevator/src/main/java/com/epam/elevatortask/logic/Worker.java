package com.epam.elevatortask.logic;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.epam.elevatortask.beans.Building;
import com.epam.elevatortask.beans.NumberedStoryContainer;
import com.epam.elevatortask.beans.Passenger;
import com.epam.elevatortask.config.ApplicationConfig;
import com.epam.elevatortask.enums.ControllerActions;
import com.epam.elevatortask.enums.TransportationState;
import com.epam.elevatortask.interfaces.IElevatorPainter;
import com.epam.elevatortask.interfaces.IElevatorWorker;
import com.epam.elevatortask.ui.ElevatorPainter;
import com.epam.elevatortask.ui.forms.ElevatorFrame;

/**
 * Class creates instances of required classes, start transportation tasks. Also
 * provides methods to start and abort transportation process.
 * 
 */
public class Worker implements IElevatorWorker {
	private static final Logger LOG = Logger.getLogger(Worker.class);
	private final Building<Passenger> building;
	private Thread guiWorkerThread;
	private ElevatorFrame elevatorFrame;
	private ExecutorService threadPool;
	private Controller controller;
	private final int animationBoost;

	public Worker(ApplicationConfig applicationConfig) {
		this.building = new Building<>(applicationConfig.getStoriesNumber(), applicationConfig.getElevatorCapacity());
		this.animationBoost = applicationConfig.getAnimationBoost();
		Random random = new Random();
		for (int i = 0; i < applicationConfig.getPassengersNumber(); i++) {
			int initStory = random.nextInt(applicationConfig.getStoriesNumber());
			building.addDispatchPassenger(initStory, new Passenger(initStory, applicationConfig.getStoriesNumber()));
		}
		controller = new Controller(building, applicationConfig.getPassengersNumber());
	}

	/**
	 * @return building
	 */
	public Building<Passenger> getBuilding() {
		return building;
	}

	/**
	 * @param elevatorFrame
	 *            to set
	 */
	public void setFrame(ElevatorFrame elevatorFrame) {
		this.elevatorFrame = elevatorFrame;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.epam.elevatortask.interfaces.IElevatorWorker#abortTransportation()
	 */
	public void abortTransportation() {
		threadPool.shutdownNow();
		guiWorkerThread.interrupt();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.epam.elevatortask.interfaces.IElevatorWorker#startTransportationGUI()
	 */
	@Override
	public void startTransportationGUI() {
		guiWorkerThread = Thread.currentThread();
		if (elevatorFrame != null) {
			IElevatorPainter elevatorPainter = new ElevatorPainter(elevatorFrame, animationBoost);
			GUIPresenter presenter = new GUIPresenter(elevatorPainter, building);
			controller.setPresenter(presenter);
		}
		startTransportation();
	}

	/**
	 * Creating and launching transportation task threads. Passes control to
	 * controller. Validate results after work finished.
	 */
	public void startTransportation() {
		threadPool = Executors.newCachedThreadPool();
		for (int i = 0; i < building.getStoriesNumber(); i++) {
			NumberedStoryContainer<Passenger> dispatchStoryContainer = building.getDispatchContainer(i);
			for (Passenger passenger : dispatchStoryContainer) {
				threadPool.submit(new TransportationTask(controller, dispatchStoryContainer, building
						.getElevatorContainer(), passenger));
			}
		}
		controller.doWork();
		threadPool.shutdown();
		try {
			threadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		if (!Thread.currentThread().isInterrupted()) {
			validateResult();
		} else {
			LOG.info(ControllerActions.ABORTING_TRANSPORTATION.getDescription());
		}
	}

	private void validateResult() {
		boolean result = true;
		int currentDispatchPassengersNumber;
		for (int i = 0; i < building.getStoriesNumber(); i++) {
			currentDispatchPassengersNumber = building.getDispatchContainer(i).getPassengersNumber();
			LOG.info("DispatchContainer " + i + " size: " + currentDispatchPassengersNumber);
			if (currentDispatchPassengersNumber != 0) {
				result = false;
			}
		}
		currentDispatchPassengersNumber = building.getElevatorContainer().getPassengersNumber();
		LOG.info("ElevatorContainer size: " + currentDispatchPassengersNumber);
		if (currentDispatchPassengersNumber != 0) {
			result = false;
		}
		int passengerNumber = 0;
		for (int i = 0; i < building.getStoriesNumber(); i++) {
			LOG.info("Story " + i);
			for (Passenger passenger : building.getArrivalContainer(i)) {
				passengerNumber++;
				LOG.info("Passenger " + passenger.getPassengerID() + " destinationStory "
						+ passenger.getDestinationStory() + " transportationState "
						+ passenger.getTransportationState());
				if (passenger.getDestinationStory() != i
						|| passenger.getTransportationState() != TransportationState.COMPLETED) {
					result = false;
				}
			}
		}
		int initialPassengersNumber = controller.getInitialPassengersNumber();
		LOG.info("Inital number passenger " + initialPassengersNumber + ", final number " + passengerNumber);
		if (initialPassengersNumber != passengerNumber) {
			result = false;
		}
		LOG.info("Test succed: " + result);
		LOG.info(ControllerActions.COMPLETION_TRANSPORTATION.getDescription());
	}
}
