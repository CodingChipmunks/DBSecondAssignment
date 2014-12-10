package view;

import java.awt.BorderLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Field;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import model.Model;

@SuppressWarnings("serial")
public class RateMediaDialog extends JFrame {

	private int selectedRow;
	
	private String mediaType = "None";
	private WBView view;
	
	private JComboBox comboMedia;
	private JPanel contentPane, buttonPane, inputPane, comboPane, titlePane;
	private JButton rate;
	private JLabel hint;
	private JButton cancel;
	private int mediaIndex;
	private Field[] field;
	private final String[] ratings = { "1", "2", "3", "4", "5" };

	private JLabel titleLbl;
	private String defaultText;

	private int WIDTH = 200;
	private int HEIGHT = 135;
	
	public RateMediaDialog(Model m, final WBView view) {
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
//		setBounds(300, 300, WIDTH, HEIGHT);
		setSize(200, 130);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		setResizable(false);
		setLocationRelativeTo(null);
		this.view = view;

		buttonPane = new JPanel();
//		inputPane = new JPanel();
		comboPane = new JPanel();
		inputPane = new JPanel();

		//contentPane.add(inputPane, BorderLayout.CENTER);
		contentPane.add(buttonPane, BorderLayout.SOUTH);
		contentPane.add(inputPane, BorderLayout.NORTH);

		comboMedia = new JComboBox(ratings);
//		comboMedia.setPrototypeDisplayValue("");
		comboMedia.setSelectedIndex(0);
//		comboPane.add(comboMedia);
		
		titleLbl = new JLabel("Choose rating");
		
		inputPane.add(titleLbl, BorderLayout.WEST);
		inputPane.add(comboMedia, BorderLayout.EAST);
		
		rate = new JButton("Rate");
		buttonPane.add(rate);
		setSubmit(rate);

		cancel = new JButton("Cancel");
		buttonPane.add(cancel);
		setCancel(cancel);

		View.setUITheme(this);
		
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
		// Debug
		System.out.println("!!! Selected row " + selectedRow);
	}
	
	
	private void hideFrame() {
		this.setVisible(false);
	}
	
	private void setCancel(JButton button) {
		button.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent evt) {
				hideFrame();
				// TODO seems to work afterall
				System.out.println("!!! Selected row " + selectedRow);
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
		// TODO fetch and save rating to db
	}
	
	private void resetUI() {
		comboMedia.setSelectedIndex(0);
	}
	
	
	public void updateView() {
		contentPane.updateUI();
	}


	public void setSelectedRow(int selectedRow) {
		this.selectedRow = selectedRow;
	}
	
	
	
}
