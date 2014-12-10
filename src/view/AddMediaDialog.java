package view;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import model.Album;
import model.Book;
import model.Model;
import model.Movie;

/**
 * WindowBuilder class, compatible with git? Better than Start?
 * 
 */
@SuppressWarnings("serial")
public class AddMediaDialog extends JFrame {
	private JPanel contentPane, buttonPane, inputPane, comboPane;
	private JComboBox comboMedia; // on change update forms.
	private JButton add;
	private JLabel hint;
	private JButton cancel;
	private WBView view;
	private int mediaIndex;
	private String mediaType = "None";
	private Field[] field;

	private int WIDTH = 200;
	private int HEIGHT = 135;
	private final String[] HIDDEN = { "id", "review", "rating", "user" };

	private ArrayList<JLabel> text = new ArrayList<JLabel>();
	private ArrayList<JTextField> textField = new ArrayList<JTextField>();

	public AddMediaDialog(Model m, final WBView view) {
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(100, 100, WIDTH, HEIGHT);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		setResizable(false);
		setLocationRelativeTo(null);
		this.view = view;

		buttonPane = new JPanel();
		inputPane = new JPanel();
		comboPane = new JPanel();
		hint = new JLabel("use ', ' as separator.");
		hint.setFont(new Font("Arial", Font.PLAIN, 12));

		contentPane.add(inputPane, BorderLayout.CENTER);
		contentPane.add(buttonPane, BorderLayout.SOUTH);
		contentPane.add(comboPane, BorderLayout.NORTH);

		comboMedia = new JComboBox(view.getSearchOptions());
		comboMedia.setPrototypeDisplayValue("as long as this");
		comboMedia.setSelectedIndex(0);
		comboPane.add(comboMedia);
		mediaType = view.getSearchOptions()[0];
		mediaIndex = 0;
		setMediaCombo(comboMedia);

		add = new JButton("Add");
		buttonPane.add(add);
		setSubmit(add);

		cancel = new JButton("Cancel");
		buttonPane.add(cancel);
		setCancel(cancel);

		View.setUITheme(this);
		
		JRootPane rootPane = SwingUtilities.getRootPane(add); 
		rootPane.setDefaultButton(add);
		
		// listener for show/hide events.
		this.addComponentListener(new ComponentAdapter() {
			public void componentHidden(ComponentEvent e) {
				view.setVisible(true);
			}

			public void componentShown(ComponentEvent e) {
				// reset input values, form size etc.
				try {
					rebuildUI(Class.forName("model." + mediaType));
				} catch (ClassNotFoundException ex) {
					ex.printStackTrace();
				}
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

	private boolean hiddenField(String name) {
		boolean hidden = false;

		for (int i = 0; i < HIDDEN.length; i++) {
			if (HIDDEN[i].equals(name))
				hidden = true;
		}

		return hidden;
	}

	@SuppressWarnings("unchecked")
	public Hashtable getValues() {
		Hashtable table = new Hashtable();

		for (int i = 0; i < textField.size(); i++) {
			
			if (!field[i].getClass().isAssignableFrom(ArrayList.class)) {
				table.put(text.get(i).getText(), textField.get(i).getText());
			} else {
				String[] split = textField.get(i).getText().split(", ");
				for (int j = 0; j < split.length; j++)
					table.put(text.get(i).getText(), split[j]);
			}

		}

		Enumeration e = table.keys();
		while (e.hasMoreElements()) {
			String key = (String) e.nextElement();
			System.out.println(key + " : " + table.get(key));
		}

		return table;
	}

	// id, user, review, rating should not show - /*(!field[i].getClass().isAssignableFrom(ArrayList.class))*/
	private void rebuildUI(Class<?> name) {
		inputPane.removeAll();
		text.clear();
		textField.clear();
		field = name.getDeclaredFields();
		int hidden = 0;

		for (int i = 0; i < field.length; i++) {
			System.out.println("Field: " + field[i].getName().toString());
			if (!hiddenField(field[i].getName().toString())) {
				//if (true) {
					System.out.println("Visible Field: " + field[i].getName().toString());
					// do normal
					
					text.add(new JLabel(field[i].getName().toString()));
					text.get(text.size() - 1)
							.setPreferredSize(new Dimension(64, 16));
					textField.add(new JTextField(""));
					textField.get(textField.size() - 1).setPreferredSize(
							new Dimension(WIDTH - 64 - 32, 16));
					inputPane.add(text.get(text.size() - 1));
					inputPane.add(textField.get(textField.size() - 1));
				//}
			} else
				hidden++;
		}
		inputPane.add(hint);
		this.setBounds(new Rectangle(100, 100, WIDTH, HEIGHT + (text.size() * 18)));
		this.setLocationRelativeTo(null);
		updateView();
	}

	// reset ui on media-change.
	private void setMediaCombo(final JComboBox combo) {
		combo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				mediaIndex = combo.getSelectedIndex();
				mediaType = combo.getSelectedItem().toString();
				try {
					rebuildUI(Class.forName("model." + mediaType));
				} catch (ClassNotFoundException e1) {
					e1.printStackTrace();
				}
			}
		});
	}

	public int getMediaIndex() {
		return mediaIndex;
	}

	public String getMediaType() {
		return mediaType;
	}

	public void updateView() {
		contentPane.updateUI();
	}

	public void showError(String errormsg) {
		JOptionPane.showMessageDialog(this, errormsg);
	}
}
