package com.epam.elevatortask;

import java.awt.EventQueue;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import com.epam.elevatortask.config.ApplicationConfig;
import com.epam.elevatortask.logic.Worker;
import com.epam.elevatortask.ui.TextAreaAppender;
import com.epam.elevatortask.ui.forms.ElevatorFrame;

public class Runner {

	private static final String CONSOLE_APPENDER_LAYOUT = "%d{ISO8601} - %m%n";
	private static final String TEXT_AREA_APPENDER_LAYOUT = "%m%n";

	public static void main(String[] args) {
		ApplicationConfig applicationConfig = new ApplicationConfig();
		Worker worker = new Worker(applicationConfig);
		if (applicationConfig.getAnimationBoost() == 0){
			ConsoleAppender consoleAppender = new ConsoleAppender(new PatternLayout(CONSOLE_APPENDER_LAYOUT));
			Logger.getRootLogger().addAppender(consoleAppender);
			worker.startTransportation();;
		}else{
			final ElevatorFrame frame = new ElevatorFrame(worker.getBuilding(),applicationConfig.getStoriesNumber(), worker, applicationConfig.getPassengersNumber());
			worker.setFrame(frame);
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						frame.setVisible(true);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			Logger.getRootLogger().addAppender(new TextAreaAppender(frame.getJTextArea(),new PatternLayout(TEXT_AREA_APPENDER_LAYOUT)));
		}
	}	
}
