package com.epam.elevatortask.beans;

import java.util.ArrayList;
import java.util.List;

public class StoryContainer<T extends Passenger> {
	private final List<T> passengersList = new ArrayList<T>();

	/**
	 * @return the passengersList
	 */
	public List<T> getPassengersList() {
		return passengersList;
	}
}
