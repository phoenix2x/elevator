package com.epam.elevatortask.logic;

import com.epam.elevatortask.enums.Direction;

/**
 * Class calculates and stores the current floor and direction of the elevator.
 * Also draws elevator movement through given presenter.
 * 
 */
public class Elevator {
	private final int upperStory;
	private Presenter presenter;
	private int currentStory;
	private Direction currentDirection;

	/**
	 * @param storiesNumber
	 * @param presenter
	 */
	public Elevator(int storiesNumber, Presenter presenter) {
		this.upperStory = storiesNumber - 1;
		this.presenter = presenter;
		this.currentStory = 0;
		this.currentDirection = Direction.UP;
	}

	/**
	 * @param presenter
	 *            the presenter to set
	 */
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}

	/**
	 * @return current elevator direction
	 */
	public Direction getCurrentDirection() {
		return currentDirection;
	}

	/**
	 * Tells elevator to move to the next floor
	 * 
	 * @return story number after move
	 * @throws InterruptedException
	 */
	public int move() throws InterruptedException {
		int tmpStory = currentStory;
		switch (currentDirection) {
		case UP:
			goUp();
			break;
		case DOWN:
			goDown();
			break;
		}
		calculateDirection();
		presenter.movingElevator(tmpStory, currentStory);
		return currentStory;
	}

	/**
	 * Calculates direction of next move.
	 */
	private void calculateDirection() {
		if (currentStory == 0) {
			currentDirection = Direction.UP;
		} else {
			if (currentStory == upperStory) {
				currentDirection = Direction.DOWN;
			}
		}
	}

	private void goUp() {
		currentStory++;
	}

	private void goDown() {
		currentStory--;
	}
}
