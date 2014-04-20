package com.epam.elevatortask;

import java.awt.EventQueue;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import com.epam.elevatortask.config.ApplicationConfig;
import com.epam.elevatortask.logic.Worker;
import com.epam.elevatortask.ui.TextAreaAppender;
import com.epam.elevatortask.ui.exceptions.ResourceNotFoundException;
import com.epam.elevatortask.ui.forms.ElevatorFrame;

public class Runner {
	
	private static final Logger LOG = Logger.getLogger(Runner.class);
	private static final String NOT_FOUND_IMAGE = "Probably not found image resources, check /img folder";
	private static final String FILE_NAME = "config.property";
	private static final String CONSOLE_APPENDER_LAYOUT = "%d{ISO8601} - %m%n";
	private static final String TEXT_AREA_APPENDER_LAYOUT = "%m%n";

	public static void main(String[] args) {
		ApplicationConfig applicationConfig = new ApplicationConfig(FILE_NAME);
		Worker worker = new Worker(applicationConfig);
		if (applicationConfig.getAnimationBoost() == 0) {
			ConsoleAppender consoleAppender = new ConsoleAppender(new PatternLayout(CONSOLE_APPENDER_LAYOUT));
			Logger.getRootLogger().addAppender(consoleAppender);
			worker.startTransportation();
		} else {
			try {
				final ElevatorFrame frame = new ElevatorFrame(worker.getBuilding(), applicationConfig.getStoriesNumber(), worker, applicationConfig.getPassengersNumber());
				worker.setFrame(frame);
				Logger.getRootLogger().addAppender(new TextAreaAppender(frame.getJTextArea(), new PatternLayout(TEXT_AREA_APPENDER_LAYOUT)));
				EventQueue.invokeLater(new Runnable() {
					public void run() {
						try {
							frame.setVisible(true);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			} catch (ResourceNotFoundException e) {
				LOG.fatal(NOT_FOUND_IMAGE, e);
				e.printStackTrace();
			}
		}
	}	
}
