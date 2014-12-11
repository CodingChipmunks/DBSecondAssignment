package view;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import model.*;

@SuppressWarnings("serial")
public class ReviewMediaDialog extends JFrame {
	
	private JPanel contentPane, buttonPane, inputPane, titlePane;
	private JButton add;
	private JButton cancel;
	
	private JTextField title;
	private JTextArea text;
	private JLabel titleLbl;
	private String defaultText;

	private int WIDTH = 350;
	private int HEIGHT = 300;


	public ReviewMediaDialog(Model m, final WBView view) {
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setSize(WIDTH, HEIGHT);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		setResizable(false);
		setLocationRelativeTo(null);

		buttonPane = new JPanel();
		inputPane = new JPanel();
		titlePane = new JPanel();

		contentPane.add(inputPane, BorderLayout.CENTER);
		contentPane.add(buttonPane, BorderLayout.SOUTH);
		contentPane.add(titlePane, BorderLayout.NORTH);

		defaultText = "Review of maximum 500 characters";
		titleLbl = new JLabel("Review title");
		title = new JTextField(16);
		text = new JTextArea(15,26);
		text.setText(defaultText);
		
		titlePane.add(titleLbl, BorderLayout.WEST);
		titlePane.add(title, BorderLayout.EAST);
		inputPane.add(text, BorderLayout.CENTER);
		
		add = new JButton("Add");
		buttonPane.add(add);
		setSubmit(add);

		cancel = new JButton("Cancel");
		buttonPane.add(cancel);
		setCancel(cancel);

		Style.setUITheme(this);
		
		JRootPane rootPane = SwingUtilities.getRootPane(add); 
		rootPane.setDefaultButton(add);
		
		// listener for show/hide events.
		this.addComponentListener(new ComponentAdapter() {
			public void componentHidden(ComponentEvent e) {
				view.setVisible(true);
			}

			public void componentShown(ComponentEvent e) {
				resetUI();
			}
		});
		
	}
	
	private void hideFrame() {
		this.setVisible(false);
	}
	
	private void setCancel(JButton button) {
		button.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent evt) {
				hideFrame();
			}
		});
	}
	
	private void setSubmit(JButton button) {
		button.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent evt) {
				getValues();
			}
		});
	}
	
	public void getValues() {
		// fetch review to db
	}
	
	private void resetUI() {
		title.setText("");
		text.setText(defaultText);
	}
	
	
	public void updateView() {
		contentPane.updateUI();
	}
	

}
