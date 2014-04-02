package com.epam.elevatortask.beans;


public class NumberedStoryContainer<T extends Passenger> extends StoryContainer<T>{
	private final int storyNumber;

	/**
	 * @param storyNumber
	 */
	public NumberedStoryContainer(int storyNumber) {
		super();
		this.storyNumber = storyNumber;
	}

	/**
	 * @return the storyNumber
	 */
	public int getStoryNumber() {
		return storyNumber;
	}
}
