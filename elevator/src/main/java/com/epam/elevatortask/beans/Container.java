package com.epam.elevatortask.beans;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Container<T extends Passenger> implements Iterable<T>{
	private final List<T> passengersList = new ArrayList<T>();

	public void add(T t){
		passengersList.add(t);
	}
	public void remove(T t){
		passengersList.remove(t);
	}
	public int getPassengersNumber(){
		return passengersList.size();
	}
	@Override
	public Iterator<T> iterator() {
		return passengersList.iterator();
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		if (passengersList != null)
			builder.append(passengersList);
		return builder.toString();
	}
}
