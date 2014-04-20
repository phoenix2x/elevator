package com.epam.elevatortask.config;

import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * Class parses, validates and stores the input parameters from the given
 * property file.
 * 
 */
public class ApplicationConfig {
	private static final Logger LOG = Logger.getLogger(ApplicationConfig.class);
	private static final String HYPHEN = " - ";
	private static final String ERROR_IN = "Error in ";
	private static final String USING_DEFAULTS = ". Using defaults";
	private static final String ANIMATION_BOOST_ERROR = "Animation boost must be within range ";
	private static final String ELEVATOR_CAPACITY_ERROR = "Elevator capacity must be positive nonzero. Using defaults";
	private static final String PASSENGERS_NUMBER_ERROR = "Passengers number must be positive nonzero. Using defaults";
	private static final String STORIES_NUMBER_ERROR = "Stories number must be positive nonzero. Using defaults";
	private static final String STORIES_NUMBER_IN_GUI_MODE_ERROR = "Stories number in GUI mode must be leser than ";
	private static final String FILE_PATH = "/";
	private static final String STORIES_NUMBER = "storiesNumber";
	private static final String PASSENGERS_NUMBER = "passengersNumber";
	private static final String ELEVATOR_CAPACITY = "elevatorCapacity";
	private static final String ANIMATION_BOOST = "animationBoost";
	private static final int MIN_VALUE = 1;
	private static final int DEF_STORIES_NUMBER = 10;
	private static final int DEF_PASSENGERS_NUMBER = 50;
	private static final int DEF_ELEVATOR_CAPACITY = 10;
	private static final int DEF_ANIMATION_BOOST = 300;
	private static final int MIN_ANIMATION_BOOST = 0;
	private static final int MAX_ANIMATION_BOOST = 10000;
	private static final int MAX_STRORIES_NUMBER_GUI_MOD = 300;
	private int storiesNumber;
	private int passengersNumber;
	private int elevatorCapacity;
	private int animationBoost;

	/**
	 * @param fileName
	 */
	public ApplicationConfig(String fileName) {
		try (InputStream inputStream = getClass().getResourceAsStream(FILE_PATH + fileName)) {
			Properties properties = new Properties();
			properties.load(inputStream);
			animationBoost = Integer.valueOf(properties.getProperty(ANIMATION_BOOST,
					String.valueOf(DEF_ANIMATION_BOOST)));
			if (animationBoost > MAX_ANIMATION_BOOST || animationBoost < MIN_ANIMATION_BOOST) {
				LOG.error(ANIMATION_BOOST_ERROR + MIN_ANIMATION_BOOST + HYPHEN + MAX_ANIMATION_BOOST + USING_DEFAULTS);
				animationBoost = DEF_ANIMATION_BOOST;
			}
			storiesNumber = Integer.valueOf(properties.getProperty(STORIES_NUMBER, String.valueOf(DEF_STORIES_NUMBER)));
			if (storiesNumber < MIN_VALUE) {
				LOG.error(STORIES_NUMBER_ERROR);
				storiesNumber = DEF_STORIES_NUMBER;
			} else {
				if (animationBoost != MIN_ANIMATION_BOOST && storiesNumber > MAX_STRORIES_NUMBER_GUI_MOD) {
					LOG.error(STORIES_NUMBER_IN_GUI_MODE_ERROR + MAX_STRORIES_NUMBER_GUI_MOD + USING_DEFAULTS);
				}
			}
			passengersNumber = Integer.valueOf(properties.getProperty(PASSENGERS_NUMBER,
					String.valueOf(DEF_PASSENGERS_NUMBER)));
			if (passengersNumber < MIN_VALUE) {
				LOG.error(PASSENGERS_NUMBER_ERROR);
				passengersNumber = DEF_PASSENGERS_NUMBER;
			}
			elevatorCapacity = Integer.valueOf(properties.getProperty(ELEVATOR_CAPACITY,
					String.valueOf(DEF_ELEVATOR_CAPACITY)));
			if (elevatorCapacity < MIN_VALUE) {
				LOG.error(ELEVATOR_CAPACITY_ERROR);
				elevatorCapacity = DEF_ELEVATOR_CAPACITY;
			}
		} catch (Exception e) {
			LOG.error(ERROR_IN + fileName + USING_DEFAULTS);
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
