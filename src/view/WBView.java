package view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.TreeSelectionModel;

import controller.Controller;
import model.Model;

/**
 * WindowBuilder class, compatible with git? Better than Start?
 * 
 */
@SuppressWarnings("serial")
public class WBView extends JFrame {

	private JPanel contentPane;
	private JTable table;
	private JTextField textField;
	private JComboBox comboBox;
	private JPanel searchPanel;
	private JScrollPane scrollPane;
	private String mediaType;
	private int mediaIndex;
	private String mediaQuery;
	private ArrayList<String> columnFilter = new ArrayList<String>();

	// dialogs
	private AddMediaDialog addMediaDialog;
	private RateMediaDialog rateMediaDialog;
	private ReviewMediaDialog reviewMediaDialog;

	private final String[] searchOptions = { "Album", "Movie", "Book" }; // Used
																			// with
																			// Class.ForName.

	// declare all GUI components here

	/**
	 * Create the frame.
	 */
	public WBView(Model m) {
		Controller controller = new Controller(m, this);

		addMediaDialog = new AddMediaDialog(m, this);
		rateMediaDialog = new RateMediaDialog(m, this);
		reviewMediaDialog = new ReviewMediaDialog(m, this);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		View.setUITheme(this);

		table = new JTable();
		table.setPreferredScrollableViewportSize(new Dimension(500, 70));
		table.setFillsViewportHeight(true);
		scrollPane = new JScrollPane(table);
		contentPane.add(scrollPane, BorderLayout.CENTER);

		JPanel btnPanel = new JPanel();
		contentPane.add(btnPanel, BorderLayout.SOUTH);

		JButton rateBtn = new JButton("Rate Selected");
		// rateBtn.setActionCommand("rateBtn");
		controller.setButtonRate(rateBtn); // rateBtn.addActionListener(controller);
		btnPanel.add(rateBtn);

		JButton reviewBtn = new JButton("Review Selected");
		controller.setButtonReview(reviewBtn); // reviewBtn.setActionCommand("reviewBtn");
		reviewBtn.addActionListener(controller);
		btnPanel.add(reviewBtn);

		JButton addBtn = new JButton("Add Media");
		// addBtn.setActionCommand("addBtn");
		controller.setButtonAdd(addBtn); // addBtn.addActionListener(controller);
		btnPanel.add(addBtn);

		searchPanel = new JPanel();
		contentPane.add(searchPanel, BorderLayout.NORTH);
		searchPanel.setLayout(new BorderLayout(0, 0));

		textField = new JTextField();
		searchPanel.add(textField, BorderLayout.CENTER); // TODO changed from
															// NORTH, approve?
		textField.setColumns(10);
		controller.setQuerySource(textField);

		JButton searchBtn = new JButton("Search");
		// searchBtn.setActionCommand("searchBtn");
		controller.setButtonSearch(searchBtn); // searchBtn.addActionListener(controller);
		searchPanel.add(searchBtn, BorderLayout.EAST);
		JRootPane rootPane = SwingUtilities.getRootPane(searchBtn);
		rootPane.setDefaultButton(searchBtn);

		JComboBox comboBox = new JComboBox(searchOptions);
		// comboBox.setActionCommand("comboBox");
		comboBox.setPrototypeDisplayValue("as long as this");
		comboBox.setSelectedIndex(0);
		setMediaCombo(comboBox);
		mediaType = searchOptions[0];
		mediaIndex = 0;
		// comboBox.addActionListener(controller); -- not required.
		searchPanel.add(comboBox, BorderLayout.WEST);

		// create components in methods
		setVisible(true);
		setLocationRelativeTo(null);//wbview.getSelectedId();
	}

	public String[] getSearchOptions() {
		return searchOptions;
	}

	// updated, no longer requires the index of component, stores values on
	// change.
	public void setMediaCombo(final JComboBox combo) {
		combo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mediaType = combo.getSelectedItem().toString();
				mediaIndex = combo.getSelectedIndex();
				System.out.println("Selected media type: " + mediaType + ": "
						+ mediaIndex);
			}
		});
	}
	
	public int getSelectedId()
	{
		return new Integer(table.getValueAt(table.getSelectedRow(), 0).toString());
	}
	
	public int getSelectedRowCount()
	{
		return table.getSelectedRowCount();
	}

	public String getMediaType() {
		return mediaType;
	}

	public int getMediaIndex() {
		return mediaIndex;
	}

	public String getMediaQuery()

	{
		return textField.getText();
	}

	public void setColumnFilter(String[] text) {
		columnFilter.clear();
		for (int i = 0; i < text.length; i++)
			columnFilter.add(text[i]);
	}

	private String[] filterColumns(Field[] fieldNames) {
		ArrayList<String> columns = new ArrayList<String>();

		for (int i = 0; i < fieldNames.length; i++)
			if (!columnFilter.contains(fieldNames[i].getName()))
				columns.add(fieldNames[i].getName());

		return columns.toArray(new String[columns.size()]);
	}

	// TODO list some column names that should not be displayed, id & rating etc
	public void feedTable(Object[] list) {
		Object[][] data = { { "No Results Found!" } };
		String[] columnNames = { "Error!" };

		if (null != list && list.length > 0) {
			// get fields from the first object in array, one object required to
			// get columns,
			// unless class is added as a parameter? might look better when
			// displaying empty results.
			Field[] fieldNames = list[0].getClass().getDeclaredFields();
			columnNames = new String[fieldNames.length];

			columnNames = filterColumns(fieldNames);

			// specify output size, object count x column count
			data = new Object[list.length][fieldNames.length];

			// iterate over field names getting their values.
			for (int i = 0; i < list.length; i++) {
				// the object
				Object c = list[i];
				for (int j = 0; j < columnNames.length; j++) {
					try {
						// the field, "column"
						Field field = c.getClass().getDeclaredField(
								columnNames[j]);
						field.setAccessible(true);
						Object value = field.get(c);
						if (null != value) {
							System.out.println(value.toString());
							data[i][j] = value.toString().replace("[", "")
									.replace("]", "");
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

		// resize columns, setting minWidth to length of every column
		// loops through every row.
		int[] minWidthColumn = new int[table.getColumnCount()];
		for (int i = 0; i < table.getColumnCount(); i++) {
			for (int j = 0; j < table.getRowCount(); j++) {
				String text = table.getValueAt(j, i).toString();

				int columnWidth = table.getFontMetrics(table.getFont())
						.stringWidth(text);
				if (columnWidth > minWidthColumn[i]) {
					minWidthColumn[i] = columnWidth;
					table.getColumnModel().getColumn(i)
							.setMinWidth(columnWidth + 6);
				}
			}
		}

		updateView();
	}

	public void invokeReviewMediaDialog() {
		reviewMediaDialog.setVisible(true);
		this.setVisible(false);
	}

	public void revokeReviewMediaDialog() {
		reviewMediaDialog.setVisible(false);
		this.setVisible(true);
	}

	public void invokeRateMediaDialog(int selectedRow) {
		// TODO i'll fix this
		rateMediaDialog.setSelectedRow(selectedRow);
		rateMediaDialog.setVisible(true);
		this.setVisible(false);
	}
	
	public void revokeRateMediaDialog() {
		rateMediaDialog.setVisible(false);
		this.setVisible(true);
	}

	public void invokeAddMediaDialog() {
		addMediaDialog.setVisible(true);
		this.setVisible(false);
	}

	public void revokeMediaDialog() {
		addMediaDialog.setVisible(false);
		this.setVisible(true);
	}

	public void updateView() {
		contentPane.updateUI();
	}

	public void showError(String errormsg) {
		JOptionPane.showMessageDialog(this, errormsg);
	}
}
