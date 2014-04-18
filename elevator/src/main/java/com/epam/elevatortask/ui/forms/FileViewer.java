package com.epam.elevatortask.ui.forms;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * Frame displays log file content.
 *
 */
public class FileViewer extends JFrame {

	private static final long serialVersionUID = 1L;
	private static final String VIEW_LOG_FILE = "View log file";
	private JPanel contentPane;
	private JTextArea textArea;

	/**
	 * Create the frame.
	 */
	public FileViewer() {
		setTitle(VIEW_LOG_FILE);
		setBounds(100, 100, 1024, 768);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane, BorderLayout.CENTER);

		textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
	}

	/**
	 * @return textArea
	 */
	public JTextArea getTextArea() {
		return textArea;
	}
}
