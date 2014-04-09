package com.epam.elevatortask.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayDeque;
import java.util.Queue;

import javax.swing.SwingUtilities;
import javax.swing.Timer;

import org.apache.log4j.Logger;

import com.epam.elevatortask.ui.beans.ElevatorGrapthComponent;
import com.epam.elevatortask.ui.beans.ElevatorPaintEvent;

public class ElevatorPainter implements ActionListener{
	private Queue<ElevatorPaintEvent> paintEvents = new ArrayDeque<>();
	private Timer timer;
	private final ElevatorGrapthComponent elevatorGrapthComponent; 
	public ElevatorPainter(ElevatorGrapthComponent elevatorGrapthComponent){
		this.elevatorGrapthComponent = elevatorGrapthComponent;
	}
	public void startTimer(){
		timer = new Timer(30, this);
		timer.start();
	}
	
	public synchronized void addEvent(ElevatorPaintEvent paintEvent){
		paintEvents.add(paintEvent);
	}
	public synchronized ElevatorPaintEvent pollEvent(){
		return paintEvents.poll();
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		ElevatorPaintEvent event = pollEvent();
		if (event!=null){
			elevatorGrapthComponent.setCurrentStory(event.getCurrentStory());
			elevatorGrapthComponent.setDoorState(event.getElevatorAction());
			elevatorGrapthComponent.repaint();
		}
		ElevatorFrame elevatorFrame =  (ElevatorFrame) SwingUtilities.getRoot(elevatorGrapthComponent);
		elevatorFrame.updateData();
	}
}
