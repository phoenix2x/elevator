package com.epam.elevatortask.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.TextArea;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class MainWindow extends JFrame implements Runnable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int tmp = 0;
	private JLabel label;
	private JButton button;
	private JTextArea textArea;
	

	public void run() {
		this.setBackground(Color.BLACK);
		this.setSize(new Dimension(500, 300));
		this.setTitle("Elevator");
		label = new JLabel("test: "+ tmp);
		button = new JButton("Action");
		textArea = new JTextArea(5,30);
		JScrollPane scrollPane = new JScrollPane(textArea);
		JPanel buttonsPanel = new JPanel(new FlowLayout());
		buttonsPanel.add(button);
		add(buttonsPanel,BorderLayout.SOUTH);
		this.getContentPane().add(scrollPane, BorderLayout.NORTH);
		this.setVisible(true);
//		this.repaint();
		
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int i=0; i<30; i++){
			textArea.append("\r\nrow2");
		}
		
	}


	/* (non-Javadoc)
	 * @see java.awt.Window#paint(java.awt.Graphics)
	 */
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.drawString("test", 100, 100);
		
	}
	

}
