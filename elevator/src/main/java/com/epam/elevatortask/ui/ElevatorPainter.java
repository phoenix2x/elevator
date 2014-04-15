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
 * Class update data on elevatorForm in GUI mode.
 *
 */
public class ElevatorPainter implements IElevatorPainter {
	private static final int MAX_ANIMATION_BOOST = 10000;
	private final ElevatorGrapthComponent elevatorGrapthComponent;
	private final ElevatorFrame elevatorFrame;
	private final int delay;
	private Timer timer;

	/**
	 * @param elevatorFrame
	 * @param animationBoost
	 */
	public ElevatorPainter(ElevatorFrame elevatorFrame, int animationBoost) {
		this.elevatorFrame = elevatorFrame;
		this.elevatorGrapthComponent = elevatorFrame.getElevatorGrapthComponent();
		this.delay = MAX_ANIMATION_BOOST / animationBoost;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.epam.elevatortask.interfaces.IElevatorPainter#paintElevatorArrival()
	 */
	@Override
	public void paintElevatorArrival() throws InterruptedException {
		ActionListener actionListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				elevatorGrapthComponent.incDoorSize();
				elevatorGrapthComponent.repaint();
				if (elevatorGrapthComponent.isDoorSizeMax()) {
					timer.stop();
					synchronized (this) {
						this.notify();
					}
				}
			}
		};
		timer = new Timer(delay, actionListener);
		timer.start();
		synchronized (actionListener) {
			actionListener.wait();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.epam.elevatortask.interfaces.IElevatorPainter#paintElevatorMove(int)
	 */
	@Override
	public void paintElevatorMove(int currentStory) throws InterruptedException {
		elevatorGrapthComponent.setCurrentStory(currentStory);
		ActionListener actionListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				elevatorGrapthComponent.changeCurrentElevatorHeight();
				elevatorGrapthComponent.repaint();
				if (elevatorGrapthComponent.isElevatorOnStory()) {
					timer.stop();
					synchronized (this) {
						this.notify();
					}
				}
			}
		};
		timer = new Timer(delay, actionListener);
		timer.start();
		synchronized (actionListener) {
			actionListener.wait();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.epam.elevatortask.interfaces.IElevatorPainter#paintElevatorDispatch()
	 */
	@Override
	public void paintElevatorDispatch() throws InterruptedException {
		ActionListener actionListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				elevatorGrapthComponent.decDoorSize();
				elevatorGrapthComponent.repaint();
				if (elevatorGrapthComponent.isDoorSizeMin()) {
					timer.stop();
					synchronized (this) {
						this.notify();
					}
				}
			}
		};
		timer = new Timer(delay, actionListener);
		timer.start();
		synchronized (actionListener) {
			actionListener.wait();
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
	public void paintDeboarding(final int currentStory, final Building<Passenger> building) throws InterruptedException {
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
					synchronized (this) {
						this.notify();
					}
				}
			}
		};
		timer = new Timer(delay, actionListener);
		elevatorGrapthComponent.setArrivalPassengerOffsetToMin();
		timer.start();
		synchronized (actionListener) {
			actionListener.wait();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.epam.elevatortask.interfaces.IElevatorPainter#paintBoarding(int,
	 * com.epam.elevatortask.beans.Building)
	 */
	@Override
	public void paintBoarding(final int currentStory, final Building<Passenger> building) throws InterruptedException {
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
					synchronized (this) {
						this.notify();
					}
				}
			}
		};
		timer = new Timer(delay, actionListener);
		timer.start();
		synchronized (actionListener) {
			actionListener.wait();
		}
	}
}
