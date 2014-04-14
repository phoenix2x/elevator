package com.epam.elevatortask.logic;

import com.epam.elevatortask.enums.Direction;

public class Elevator {
	private final int upperStory;
	private Presenter presenter;
	private int currentStory;
	private Direction currentDirection;
	public Elevator(int storiesNumber, Presenter presenter){
		this.upperStory = storiesNumber - 1;
		this.presenter = presenter;
		this.currentStory = 0;
		this.currentDirection = Direction.UP;
	}
	public Direction getCurrentDirection(){
		return currentDirection;
	}
	public int move() throws InterruptedException{
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
	private void calculateDirection() {
		if (currentStory==0){
			currentDirection = Direction.UP;	
		}else{
			if (currentStory==upperStory){
				currentDirection=Direction.DOWN;
			}
		}
	}
	private void goUp() {
		currentStory++;
	}

	private void goDown() {
		currentStory--;
	}
	/**
	 * @param presenter the presenter to set
	 */
	public void setPresenter(Presenter presenter) {
		this.presenter = presenter;
	}
}
