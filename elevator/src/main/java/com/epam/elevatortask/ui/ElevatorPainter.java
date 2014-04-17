package com.epam.elevatortask.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import com.epam.elevatortask.beans.Building;
import com.epam.elevatortask.beans.Passenger;
import com.epam.elevatortask.interfaces.IElevatorPainter;
import com.epam.elevatortask.ui.components.ElevatorGrapthComponent;
import com.epam.elevatortask.ui.forms.ElevatorFrame;

/**
 * Class updates data on elevatorForm in GUI mode.
 *
 */
public class ElevatorPainter implements IElevatorPainter {
	private static final int MAX_ANIMATION_BOOST = 10000;
	private final ElevatorGrapthComponent elevatorGrapthComponent;
	private final ElevatorFrame elevatorFrame;
	private final int delay;
	private final int elevatorDelay;
	private Timer timer;

	/**
	 * @param elevatorFrame
	 * @param animationBoost
	 */
	public ElevatorPainter(ElevatorFrame elevatorFrame, int animationBoost) {
		this.elevatorFrame = elevatorFrame;
		this.elevatorGrapthComponent = elevatorFrame.getElevatorGrapthComponent();
		this.delay = MAX_ANIMATION_BOOST / animationBoost;
		this.elevatorDelay = delay/10;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.epam.elevatortask.interfaces.IElevatorPainter#paintElevatorArrival()
	 */
	@Override
	public void drawElevatorArrival() throws InterruptedException {
		ActionListener actionListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				elevatorGrapthComponent.incDoorSize();
				elevatorGrapthComponent.repaint();
				if (elevatorGrapthComponent.isDoorSizeMax()) {
					timer.stop();
					synchronized (this) {
						this.notifyAll();
					}
				}
			}
		};
		timer = new Timer(elevatorDelay, actionListener);
		synchronized (actionListener) {
			timer.start();
			try{
				actionListener.wait();
			}catch(InterruptedException e){
				timer.stop();
				throw e;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.epam.elevatortask.interfaces.IElevatorPainter#paintElevatorMove(int)
	 */
	@Override
	public void drawElevatorMove(int currentStory) throws InterruptedException {
		elevatorGrapthComponent.setCurrentStory(currentStory);
		ActionListener actionListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				elevatorGrapthComponent.changeCurrentElevatorHeight();
				elevatorGrapthComponent.repaint();
				if (elevatorGrapthComponent.isElevatorOnStory()) {
					timer.stop();
					synchronized (this) {
						this.notifyAll();
					}
				}
			}
		};
		timer = new Timer(elevatorDelay, actionListener);
		synchronized (actionListener) {
			timer.start();
			try{
				actionListener.wait();
			}catch(InterruptedException e){
				timer.stop();
				throw e;
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.epam.elevatortask.interfaces.IElevatorPainter#paintElevatorDispatch()
	 */
	@Override
	public void drawElevatorDispatch() throws InterruptedException {
		ActionListener actionListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				elevatorGrapthComponent.decDoorSize();
				elevatorGrapthComponent.repaint();
				if (elevatorGrapthComponent.isDoorSizeMin()) {
					timer.stop();
					synchronized (this) {
						this.notifyAll();
					}
				}
			}
		};
		timer = new Timer(elevatorDelay, actionListener);
		synchronized (actionListener) {
			timer.start();
			try{
				actionListener.wait();
			}catch(InterruptedException e){
				timer.stop();
				throw e;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.epam.elevatortask.interfaces.IElevatorPainter#paintDeboarding(int,
	 * com.epam.elevatortask.beans.Building)
	 */
	@Override
	public void drawDeboarding(final int currentStory, final Building<Passenger> building) throws InterruptedException {
		ActionListener actionListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int elevatorPassengers = building.getElevatorContainer().getPassengersNumber();
				int arrivalPassengers = building.getArrivalContainer(currentStory).getPassengersNumber();
				elevatorFrame.getLblElevatorcontinersize().setText(String.valueOf(elevatorPassengers));
				elevatorFrame.getArrivalLabelsList().get(currentStory).setText(String.valueOf(arrivalPassengers));
				elevatorGrapthComponent.setArrivalPassengers(arrivalPassengers);
				elevatorGrapthComponent.setElevatorPassengers(elevatorPassengers);
				elevatorGrapthComponent.incArrivalPassengerOffset();
				elevatorGrapthComponent.repaint();
				if (elevatorGrapthComponent.isArrivalPassengerOffsetDef()) {
					timer.stop();
					elevatorGrapthComponent.setArrivalPassengerOffsetToDefault();
					elevatorGrapthComponent.changeMovingArrivalFlag();
					synchronized (this) {
						this.notifyAll();
					}
				}
			}
		};
		timer = new Timer(delay, actionListener);
		elevatorGrapthComponent.changeMovingArrivalFlag();
		elevatorGrapthComponent.newImageIterator();
//		elevatorGrapthComponent.setArrivalPassengerOffsetToMin();
		synchronized (actionListener) {
			timer.start();
			try{
				actionListener.wait();
			}catch(InterruptedException e){
				timer.stop();
				throw e;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.epam.elevatortask.interfaces.IElevatorPainter#paintBoarding(int,
	 * com.epam.elevatortask.beans.Building)
	 */
	@Override
	public void drawBoarding(final int currentStory, final Building<Passenger> building) throws InterruptedException {
		ActionListener actionListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int elevatorPassengers = building.getElevatorContainer().getPassengersNumber();
				int dispatchPassengers = building.getDispatchContainer(currentStory).getPassengersNumber();
				elevatorFrame.getLblElevatorcontinersize().setText(String.valueOf(elevatorPassengers));
				elevatorFrame.getDispatchLabelsList().get(currentStory).setText(String.valueOf(dispatchPassengers));
				elevatorGrapthComponent.setElevatorPassengers(elevatorPassengers);
				elevatorGrapthComponent.setDispatchPassengers(dispatchPassengers);
				elevatorGrapthComponent.setBoardingOffset();
				elevatorGrapthComponent.repaint();
				if (elevatorGrapthComponent.isDispatchPassengerOffsetZero()) {
					timer.stop();
					elevatorGrapthComponent.clearBoardingOffset();
					elevatorGrapthComponent.changeMovingDispatchFlag();
					synchronized (this) {
						this.notifyAll();
					}
				}
			}
		};
		elevatorGrapthComponent.changeMovingDispatchFlag();
		elevatorGrapthComponent.newImageIterator();
		timer = new Timer(delay, actionListener);
		synchronized (actionListener) {
			timer.start();
			try{
				actionListener.wait();
			}catch(InterruptedException e){
				timer.stop();
				throw e;
			}
		}
	}
}
