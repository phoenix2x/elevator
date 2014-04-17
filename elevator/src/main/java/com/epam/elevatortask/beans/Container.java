package com.epam.elevatortask.beans;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Class is a container for storing passengers.
 *
 * @param <T>
 */
public class Container<T extends Passenger> implements Iterable<T> {
	private final List<T> passengersList = new ArrayList<T>();

	/**
	 * @param t
	 *            add passenger in a container
	 */
	public void add(T t) {
		passengersList.add(t);
	}

	/**
	 * @param t
	 *            remove passenger from container
	 */
	public void remove(T t) {
		passengersList.remove(t);
	}

	/**
	 * @return number of passengers in container
	 */
	public int getPassengersNumber() {
		return passengersList.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<T> iterator() {
		return passengersList.iterator();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		if (passengersList != null) {
			builder.append(passengersList);
		}
		return builder.toString();
	}
}
