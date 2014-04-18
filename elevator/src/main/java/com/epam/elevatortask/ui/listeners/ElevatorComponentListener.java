package com.epam.elevatortask.ui.listeners;

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import com.epam.elevatortask.ui.components.ElevatorGrapthComponent;

public class ElevatorComponentListener implements ComponentListener {
	private final ElevatorGrapthComponent elevatorGrapthComponent;

	public ElevatorComponentListener(ElevatorGrapthComponent elevatorGrapthComponent) {
		this.elevatorGrapthComponent = elevatorGrapthComponent;
	}

	@Override
	public void componentHidden(ComponentEvent arg0) {
	}

	@Override
	public void componentMoved(ComponentEvent arg0) {
	}

	@Override
	public void componentResized(ComponentEvent arg0) {
		elevatorGrapthComponent.resize();
	}

	@Override
	public void componentShown(ComponentEvent arg0) {
	}
}
