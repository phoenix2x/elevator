package com.epam.elevatortask.config;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

public class ApplicationConfig {
	private static final Logger LOG = Logger.getLogger(ApplicationConfig.class);
	private static final String FILE_NAME = "config.property";
	private static final String STORIES_NUMBER = "storiesNumber";
	private static final String PASSENGERS_NUMBER = "passengersNumber";
	private static final String ELEVATOR_CAPACITY = "elevatorCapacity";
	private static final String ANIMATION_BOOST = "animationBoost";
	private static final int DEF_STORIES_NUMBER = 10;
	private static final int DEF_PASSENGERS_NUMBER = 50;
	private static final int DEF_ELEVATOR_CAPACITY = 10;
	private static final int DEF_ANIMATION_BOOST = 1000;
	private static final int MIN_ANIMATION_BOOST = 0;
	private static final int MAX_ANIMATION_BOOST = 10000;
	private int storiesNumber;
	private int passengersNumber;
	private int elevatorCapacity;
	private int animationBoost;
	public ApplicationConfig(){
		try (BufferedReader bufferedReader = new BufferedReader(new FileReader(FILE_NAME))){
			Properties properties = new Properties();
			properties.load(bufferedReader);
			storiesNumber = Integer.valueOf(properties.getProperty(STORIES_NUMBER,String.valueOf(DEF_STORIES_NUMBER)));
			if (storiesNumber < 1){
				LOG.error("Stories number must be a positive non-zero. Using defaults");
				storiesNumber = DEF_STORIES_NUMBER;
			}
			passengersNumber = Integer.valueOf(properties.getProperty(PASSENGERS_NUMBER,String.valueOf(DEF_PASSENGERS_NUMBER)));
			if (passengersNumber < 1){
				LOG.error("Pussengers number must be a positive non-zero. Using defaults");
				passengersNumber = DEF_PASSENGERS_NUMBER;
			}
			elevatorCapacity = Integer.valueOf(properties.getProperty(ELEVATOR_CAPACITY,String.valueOf(DEF_ELEVATOR_CAPACITY)));
			if (elevatorCapacity < 1){
				LOG.error("Elevator capacity must be a positive non-zero. Using defaults");
				elevatorCapacity = DEF_ELEVATOR_CAPACITY;
			}
			animationBoost = Integer.valueOf(properties.getProperty(ANIMATION_BOOST,String.valueOf(DEF_ANIMATION_BOOST)));
			if (animationBoost > MAX_ANIMATION_BOOST || animationBoost < MIN_ANIMATION_BOOST){
				LOG.error("Animation boost must be within range " + MIN_ANIMATION_BOOST + " < animationBoost < " + MAX_ANIMATION_BOOST + ". Using defaults");
				animationBoost = DEF_ANIMATION_BOOST;
			}
		} catch (IOException | NumberFormatException e) {
			LOG.error("Error in \"" + FILE_NAME + "\". Using defaults.");
			storiesNumber = DEF_STORIES_NUMBER;
			passengersNumber = DEF_PASSENGERS_NUMBER;
			elevatorCapacity = DEF_ELEVATOR_CAPACITY;
			animationBoost = DEF_ANIMATION_BOOST;
		}
	}
	/**
	 * @return the storiesNumber
	 */
	public int getStoriesNumber() {
		return storiesNumber;
	}
	/**
	 * @return the passengersNumber
	 */
	public int getPassengersNumber() {
		return passengersNumber;
	}
	/**
	 * @return the elevatorCapacity
	 */
	public int getElevatorCapacity() {
		return elevatorCapacity;
	}
	/**
	 * @return the animationBoost
	 */
	public int getAnimationBoost() {
		return animationBoost;
	}
}
