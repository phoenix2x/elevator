package com.epam.elevatortask.beans;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Container<T extends Passenger> implements Iterable<T>{
	private final List<T> passengersList = new ArrayList<T>();

//	/**
//	 * @return the passengersList
//	 */
//	public List<T> getPassengersList() {
//		return passengersList;
//	}
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
}
