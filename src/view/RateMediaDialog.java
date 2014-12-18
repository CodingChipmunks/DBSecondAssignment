package view;

import java.awt.BorderLayout;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import controller.Controller;
import model.Model;

@SuppressWarnings("serial")
public class RateMediaDialog extends JFrame {

	private int selectedRow;
	
	private JComboBox<String> comboMedia;
	private JPanel contentPane, buttonPane, inputPane;
	private JButton rate;
	private JButton cancel;
	private final String[] ratings = { "1", "2", "3", "4", "5" };

	private JLabel titleLbl;
	
	public RateMediaDialog(Model m, final WBView view, final Controller controller) {
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setSize(200, 130);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		setResizable(false);
		setLocationRelativeTo(null);

		buttonPane = new JPanel();
		inputPane = new JPanel();
		
		contentPane.add(buttonPane, BorderLayout.SOUTH);
		contentPane.add(inputPane, BorderLayout.NORTH);

		comboMedia = new JComboBox<String>(ratings);
		comboMedia.setSelectedIndex(0);
		
		titleLbl = new JLabel("Choose rating");
		
		inputPane.add(titleLbl, BorderLayout.WEST);
		inputPane.add(comboMedia, BorderLayout.EAST);
		
		rate = new JButton("Rate");
		buttonPane.add(rate);
		controller.setSubmitRate(rate, this);

		cancel = new JButton("Cancel");
		buttonPane.add(cancel);
		setCancel(cancel);

		Style.setUITheme(this.getRootPane());
		//this.setModal(true);
		
		JRootPane rootPane = SwingUtilities.getRootPane(rate); 
		rootPane.setDefaultButton(rate);
		
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
	
	// fetch rating 
	public int getValues() {
		return comboMedia.getSelectedIndex() + 1;
	}
	
	private void resetUI() {
		comboMedia.setSelectedIndex(0);
	}
	
	public void updateView() {
		contentPane.updateUI();
	}
	
	public int getSelectedId()
	{
		return this.selectedRow;
	}

	public void setSelectedRow(int selectedRow) {
		this.selectedRow = selectedRow;
	}
}
