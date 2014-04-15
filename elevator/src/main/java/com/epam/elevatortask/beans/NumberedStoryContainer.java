package com.epam.elevatortask.beans;

/**
 * Class is a container for storing passengers. Also store number of floor.
 *
 * @param <T>
 */
public class NumberedStoryContainer<T extends Passenger> extends Container<T> {
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(storyNumber).append(";");
		if (super.toString() != null) {
			builder.append(super.toString());
		}
		return builder.toString();
	}
}
