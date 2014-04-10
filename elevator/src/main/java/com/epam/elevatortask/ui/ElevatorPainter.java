package com.epam.elevatortask.ui;

import java.awt.EventQueue;

import javax.swing.SwingUtilities;

import com.epam.elevatortask.beans.Building;
import com.epam.elevatortask.beans.Passenger;
import com.epam.elevatortask.interfaces.IElevatorPainter;
import com.epam.elevatortask.ui.components.ElevatorGrapthComponent;
import com.epam.elevatortask.ui.components.ElevatorGrapthComponent.DoorState;
import com.epam.elevatortask.ui.forms.ElevatorFrame;

public class ElevatorPainter implements IElevatorPainter{
	private final ElevatorGrapthComponent elevatorGrapthComponent; 
	private final int delay;
	public ElevatorPainter(ElevatorGrapthComponent elevatorGrapthComponent, int animationBoost){
		this.elevatorGrapthComponent = elevatorGrapthComponent;
		this.delay = 10000/animationBoost;
	}
	private void sleep(){
		try {
			Thread.sleep(delay);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void paintElevatorArrival(final int currentStory){
		sleep();
		EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				elevatorGrapthComponent.setCurrentStory(currentStory);
				elevatorGrapthComponent.setDoorState(DoorState.CLOSED);
				elevatorGrapthComponent.repaint();
			}
			
		});
		sleep();
		EventQueue.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				elevatorGrapthComponent.setDoorState(DoorState.HALF_OPENED);
				elevatorGrapthComponent.repaint();
			}
		});
		sleep();
		EventQueue.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				elevatorGrapthComponent.setDoorState(DoorState.OPENED);
				elevatorGrapthComponent.repaint();
			}
		});
	}
	@Override
	public void paintElevatorDispatch(){
		sleep();
		EventQueue.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				elevatorGrapthComponent.setDoorState(DoorState.HALF_OPENED);
				elevatorGrapthComponent.repaint();
			}
		});
		sleep();
		EventQueue.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				elevatorGrapthComponent.setDoorState(DoorState.CLOSED);
				elevatorGrapthComponent.repaint();
			}
		});
	}
	@Override
	public void paintDeboarding(final int currentStory, final Building<Passenger> building) {
		sleep();
		EventQueue.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				ElevatorFrame elevatorFrame =  (ElevatorFrame) SwingUtilities.getRoot(elevatorGrapthComponent);
				int elevatorPassengers = building.getElevatorContainer().getPassengersNumber();
				int arrivalPassengers = building.getArrivalContainer(currentStory).getPassengersNumber();
				elevatorFrame.getLblElevatorcontinersize().setText(String.valueOf(elevatorPassengers));
				elevatorFrame.getArrivalLabelsList().get(currentStory).setText(String.valueOf(arrivalPassengers));
				elevatorGrapthComponent.setArrivalPassengers(arrivalPassengers);
				elevatorGrapthComponent.setElevatorPassengers(elevatorPassengers);
				elevatorGrapthComponent.repaint();
				
			}
		});
		
		
	}
	@Override
	public void paintBoarding(final int currentStory, final Building<Passenger> building) {
		sleep();
		EventQueue.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				ElevatorFrame elevatorFrame =  (ElevatorFrame) SwingUtilities.getRoot(elevatorGrapthComponent);
				int elevatorPassengers = building.getElevatorContainer().getPassengersNumber();
				int dispatchPassengers = building.getDispatchContainer(currentStory).getPassengersNumber();
				elevatorFrame.getLblElevatorcontinersize().setText(String.valueOf(elevatorPassengers));
				elevatorFrame.getDispatchLabelsList().get(currentStory).setText(String.valueOf(dispatchPassengers));
				elevatorGrapthComponent.setElevatorPassengers(elevatorPassengers);
				elevatorGrapthComponent.setDispatchPassengers(dispatchPassengers);
				elevatorGrapthComponent.repaint();	
			}
		});
	}	
}
