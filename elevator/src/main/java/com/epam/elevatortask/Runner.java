package com.epam.elevatortask;

import java.awt.EventQueue;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import com.epam.elevatortask.logic.Worker;
import com.epam.elevatortask.ui.ElevatorFrame;
import com.epam.elevatortask.ui.TextAreaAppender;

public class Runner {

	private static final String CONSOLE_APPENDER_LAYOUT = "%d{ISO8601} - %m%n";
	private static final String TEXT_AREA_APPENDER_LAYOUT = "%m%n";
	private static final int storiesNumber = 10;
	private static final int passengersNumber = 200;
	private static final int elevatorCapacity = 10;

	public static void main(String[] args) {
		int animationBoost = 1;
		Worker worker = new Worker(storiesNumber, passengersNumber, elevatorCapacity);
		worker.initialization();
		if (animationBoost == 0){
			ConsoleAppender consoleAppender = new ConsoleAppender(new PatternLayout(CONSOLE_APPENDER_LAYOUT));
			Logger.getRootLogger().addAppender(consoleAppender);
			worker.startTransportation();;
		}else{
			final ElevatorFrame frame = new ElevatorFrame(worker,storiesNumber);
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
