package com.epam.elevatortask.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class FinishButtonListener implements ActionListener {

	public void actionPerformed(ActionEvent e) {
		FileViewer fileViewer = new FileViewer();
		try {
			fileViewer.getTextArea().read(new FileReader("Controller.log"), null);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		fileViewer.setVisible(true);
	}
}
