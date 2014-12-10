package view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

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
	
	// dialogs
	private AddMediaDialog addMediaDialog;
	// private RateMediaDialog rate;
	private ReviewMediaDialog reviewMediaDialog;
	
	private String[] searchOptions = { "Album", "Movie", "Book" }; // Used with Class.ForName.

	// declare all GUI components here

	/**
	 * Create the frame.
	 */
	public WBView(Model m) {
		Controller controller = new Controller(m, this);
		
		addMediaDialog = new AddMediaDialog(m, this);
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
		//rateBtn.setActionCommand("rateBtn");
		controller.setButtonRate(rateBtn); //rateBtn.addActionListener(controller);
		btnPanel.add(rateBtn);

		JButton reviewBtn = new JButton("Review Selected");
		controller.setButtonReview(reviewBtn); //reviewBtn.setActionCommand("reviewBtn");
		reviewBtn.addActionListener(controller);
		btnPanel.add(reviewBtn);

		JButton addBtn = new JButton("Add Media");
		//addBtn.setActionCommand("addBtn");
		controller.setButtonAdd(addBtn); //addBtn.addActionListener(controller);
		btnPanel.add(addBtn);

		searchPanel = new JPanel();
		contentPane.add(searchPanel, BorderLayout.NORTH);
		searchPanel.setLayout(new BorderLayout(0, 0));

		textField = new JTextField();
		searchPanel.add(textField, BorderLayout.CENTER); // TODO changed from
															// NORTH, approve?
		textField.setColumns(10);

		JButton searchBtn = new JButton("Search");
		//searchBtn.setActionCommand("searchBtn");
		controller.setButtonSearch(searchBtn); //searchBtn.addActionListener(controller);
		searchPanel.add(searchBtn, BorderLayout.EAST);

		JComboBox comboBox = new JComboBox(searchOptions);
		//comboBox.setActionCommand("comboBox");
		comboBox.setPrototypeDisplayValue("as long as this");
		comboBox.setSelectedIndex(0);
		setMediaCombo(comboBox);
		mediaType = searchOptions[0];
		mediaIndex = 0;
		//comboBox.addActionListener(controller); -- not required.
		searchPanel.add(comboBox, BorderLayout.WEST);

		// create components in methods
		createDialogs();
		setVisible(true);
		setLocationRelativeTo(null);
	}

	// PArt of the dangerous solution
	//public JComboBox getSearchPanelComponent() {
	//	return (JComboBox) searchPanel.getComponent(2);
	//}
	
	public String[] getSearchOptions()
	{
		return searchOptions;
	}
	
	// updated, no longer requires the index of component, stores values on change.
	public void setMediaCombo(final JComboBox combo)
	{
		combo.addActionListener (new ActionListener () {
		    public void actionPerformed(ActionEvent e) {
		        mediaType = combo.getSelectedItem().toString();
		        mediaIndex = combo.getSelectedIndex();
		        System.out.println("Selected media type: " + mediaType + ": " + mediaIndex);
		    }
		});
	}
	
	public String getMediaType()
	{
		return mediaType;
	}

	public int getMediaIndex()
	{
		return mediaIndex;
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

			// copy field names to column names. TODO check if the column should
			// be used
			// or not, "if fieldname.getname in DontUseThisColumn" ...
			for (int i = 0; i < fieldNames.length; i++)
				columnNames[i] = fieldNames[i].getName();

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
		reviewMediaDialog.setVisible(true);
		this.setVisible(false);
	}

	public void invokeRateMediaDialog() {
		// rate.setVisible(true);
	}

	public void invokeAddMediaDialog() {
		addMediaDialog.setVisible(true);
		this.setVisible(false);
	}
	
	public void revokeMediaDialog()
	{
		addMediaDialog.setVisible(false);
		this.setVisible(true);
	}

	public String getSearchFieldText() {
		return textField.getText();
	}

	public void updateView() {
		contentPane.updateUI();
	}

	public void showError(String errormsg) {
		JOptionPane.showMessageDialog(this, errormsg);	
	}
}
