package com.epam.elevatortask.ui;

import java.awt.EventQueue;

import javax.swing.JTextArea;
import org.apache.log4j.Layout;
import org.apache.log4j.WriterAppender;
import org.apache.log4j.spi.LoggingEvent;

public class TextAreaAppender extends WriterAppender {

    private JTextArea jTextArea;
    public TextAreaAppender(JTextArea jTextArea, Layout layout){
    	this.jTextArea = jTextArea;
    	this.layout = layout;
    }

    /**
     * Format and then append the loggingEvent to the stored JTextArea.
     */
    public void append(LoggingEvent loggingEvent) {
        if (layout != null) {
            final String message = layout.format(loggingEvent);
            StringBuilder sb = new StringBuilder();
            if (loggingEvent.getThrowableInformation() != null) {
                String[] stackTrace = loggingEvent.getThrowableStrRep();
                for (String s : stackTrace) {
                    sb.append(s);
                    sb.append(Layout.LINE_SEP);
                }
            }
            final String stackTrace = sb.toString();
            EventQueue.invokeLater(new Runnable() {
                public void run() {
                    if (jTextArea != null) {
                        jTextArea.append(message);
                        if (!stackTrace.isEmpty()) {
                            jTextArea.append(stackTrace);
                        }
                    }
                }
            });
        }
    }
}