package view;

import java.awt.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import controller.Controller;
import model.Album;
import model.Artist;
import model.Model;
import model.QueryExecuter;
import model.QueryInterpreter;

/**
 * WindowBuilder class, compatible with git? Better than Start?
 * 
 */
public class WBView extends JFrame {

	private JPanel contentPane;
	private JTable table;
	private JTextField textField;
	private JComboBox comboBox;
	private JPanel searchPanel;
	private JScrollPane scrollPane;

	// private AddMediaDialog add;
	// private RateMediaDialog rate;
	// private ReviewMediaDialog review;

	private String[] searchOptions = { "Album", "Movie", "E-Book" };

	// declare all GUI components here

	/**
	 * Create the frame.
	 */
	public WBView(Model m) {
		Controller controller = new Controller(m, this);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		table = new JTable();
		table.setPreferredScrollableViewportSize(new Dimension(500, 70));
		table.setFillsViewportHeight(true);
		scrollPane = new JScrollPane(table);
		contentPane.add(scrollPane, BorderLayout.CENTER);

		JPanel btnPanel = new JPanel();
		contentPane.add(btnPanel, BorderLayout.SOUTH);

		JButton rateBtn = new JButton("Rate Selected");
		rateBtn.setActionCommand("rateBtn");
		rateBtn.addActionListener(controller);
		btnPanel.add(rateBtn);

		JButton reviewBtn = new JButton("Review Selected");
		reviewBtn.setActionCommand("reviewBtn");
		reviewBtn.addActionListener(controller);
		btnPanel.add(reviewBtn);

		JButton addBtn = new JButton("Add Media");
		addBtn.setActionCommand("addBtn");
		addBtn.addActionListener(controller);
		btnPanel.add(addBtn);

		searchPanel = new JPanel();
		contentPane.add(searchPanel, BorderLayout.NORTH);
		searchPanel.setLayout(new BorderLayout(0, 0));

		textField = new JTextField();
		searchPanel.add(textField, BorderLayout.CENTER); // TODO changed from
															// NORTH, approve?
		textField.setColumns(10);

		JButton searchBtn = new JButton("Search");
		searchBtn.setActionCommand("searchBtn");
		searchBtn.addActionListener(controller);
		searchPanel.add(searchBtn, BorderLayout.EAST);

		JComboBox comboBox = new JComboBox(searchOptions);
		comboBox.setActionCommand("comboBox");
		comboBox.setPrototypeDisplayValue("as long as this");
		comboBox.setSelectedIndex(0);
		comboBox.addActionListener(controller);
		searchPanel.add(comboBox, BorderLayout.WEST);

		// create components in methods
		createDialogs();
	}

	// PArt of the dangerous solution
	public JComboBox getSearchPanelComponent() {
		return (JComboBox) searchPanel.getComponent(2);
	}

	// unusable ref.
	// public JComboBox getComboBox() {
	// return comboBox;
	// }

	// unusable ref.
	// public int getSelectedItem() {
	// return comboBox.getSelectedIndex();
	// }

	// fed with an arraylist?, on click: get index id, map to ArrayList in Model
	// and grab Pk-Id.
	// TODO align columns by size, not equal size.
	// TODO list some column names that should not be displayed, id & rating etc
	// TODO specify ArrayList argument.
	public void feedTable() {
		// -------------------------- BEGIN TEST BLOCK --------------------------------
		Album album1 = new Album("Namesy-1", "2013", "usr", 1);
		Album album2 = new Album("Namesy-2", "2013", "usr", 1);
		Album album3 = new Album("Namesy-3", "2013", "usr", 1);
		album2.AddArtist("Celine-Dion");

		ArrayList<Album> albumish = new ArrayList<Album>();

		if (new Random().nextBoolean()) {
			albumish.add(album1);
			albumish.add(album2);
			albumish.add(album3);
		}

		// ------------------------- END TEST BLOCK -------------------------------
		Object[][] data = { {"No Results Found!"} };
		String[] columnNames = {"Error!"};

		if (!albumish.isEmpty()) {
			// get fields from the first object in array, one object required to get columns,
			// unless class is added as a parameter? might look better when displaying empty results.
			Field[] fieldNames = albumish.get(0).getClass().getDeclaredFields();
			columnNames = new String[fieldNames.length];

			// copy field names to column names. TODO check if the column should be used
			// or not, "if fieldname.getname in DontUseThisColumn" ...
			for (int i = 0; i < fieldNames.length; i++)
				columnNames[i] = fieldNames[i].getName();

			// specify output size, object count x column count
			data = new Object[albumish.size()][fieldNames.length];

			// iterate over field names getting their values.
			for (int i = 0; i < albumish.size(); i++) {
				// the object
				Album c = albumish.get(i);
				for (int j = 0; j < columnNames.length; j++) {
					try {
						// the field, "column"
						Field field = c.getClass().getDeclaredField(
								columnNames[j]);
						field.setAccessible(true);
						Object value = field.get(c);
						if (null != value) {
							System.out.println(value.toString());
							data[i][j] = value.toString();
						} else
							data[i][j] = "";
						field.setAccessible(false);
					} catch (NoSuchFieldException | SecurityException
							| IllegalArgumentException | IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
		}

		// remove old
		scrollPane.remove(table);
		contentPane.remove(scrollPane);

		// add new
		table = new JTable(data, columnNames);
		scrollPane = new JScrollPane(table);
		contentPane.add(scrollPane);

		// update view
		updateView();
	}

	public void createDialogs() {
		// add = new AddMediaDialog();
		// add.setVisible(false);
		// rate = new RateMediaDialog();
		// rate.setVisible(false);
		// review = new ReviewMediaDialog();
		// review.setVisible(false);
	}

	// show?
	public void invokeReviewMediaDialog() {
		// review.setVisible(true);
	}

	public void invokeRateMediaDialog() {
		// rate.setVisible(true);
	}

	public void invokeAddMediaDialog() {
		// add.setVisible(true);
	}

	public String getSearchFieldText() {
		return textField.getText();
	}

	public void updateView() {
		contentPane.updateUI();
	}
}
