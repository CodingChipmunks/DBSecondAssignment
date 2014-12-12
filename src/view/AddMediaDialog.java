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
import java.util.Hashtable;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import controller.Controller;
import model.Model;

/**
 * WindowBuilder class, compatible with git? Better than Start?
 * 
 */
@SuppressWarnings("serial")
public class AddMediaDialog extends JFrame {	
	private JPanel contentPane, buttonPane, inputPane, comboPane;
	private JComboBox<String> comboMedia; // on change update forms.
	private JButton add;
	private JLabel hint;
	private JButton cancel;
	private int mediaIndex;
	private String mediaType = "None";
	private Field[] field;

	private int WIDTH = 200;
	private int HEIGHT = 135;
	private final String[] HIDDEN = { "id", "review", "rating", "user" };

	private ArrayList<JLabel> text = new ArrayList<JLabel>();
	private ArrayList<JTextField> textField = new ArrayList<JTextField>();

	public AddMediaDialog(Model m, final WBView view,
			final Controller controller) {
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBounds(100, 100, WIDTH, HEIGHT);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		setResizable(false);
		setLocationRelativeTo(null);

		buttonPane = new JPanel();
		inputPane = new JPanel();
		comboPane = new JPanel();
		hint = new JLabel("use ', ' as separator.");
		hint.setFont(new Font("Arial", Font.PLAIN, 12));

		contentPane.add(inputPane, BorderLayout.CENTER);
		contentPane.add(buttonPane, BorderLayout.SOUTH);
		contentPane.add(comboPane, BorderLayout.NORTH);

		// TODO get media from view, do not include hidden fields.
		// the fields must match a class.
		comboMedia = new JComboBox<String>(new String[]{"Album", "Movie", "Book"});
		comboMedia.setPrototypeDisplayValue("as long as this");
		comboMedia.setSelectedIndex(0);
		comboPane.add(comboMedia);
		mediaType = view.getSearchOptions()[0];
		mediaIndex = 0;
		setMediaCombo(comboMedia);

		add = new JButton("Add");
		buttonPane.add(add);
		controller.setSubmit(add, this);

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

	private boolean hiddenField(String name) {
		boolean hidden = false;

		for (int i = 0; i < HIDDEN.length; i++) {
			if (HIDDEN[i].equals(name))
				hidden = true;
		}

		return hidden;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
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
		return table;
	}

	// id, user, review, rating should not show -
	// /*(!field[i].getClass().isAssignableFrom(ArrayList.class))*/
	private void rebuildUI(Class<?> name) {
		inputPane.removeAll();
		text.clear();
		textField.clear();
		field = name.getDeclaredFields();

		for (int i = 0; i < field.length; i++) {
			if (!hiddenField(field[i].getName().toString())) {
				text.add(new JLabel(field[i].getName().toString()));
				text.get(text.size() - 1).setPreferredSize(
						new Dimension(64, 16));
				textField.add(new JTextField(""));
				textField.get(textField.size() - 1).setPreferredSize(
						new Dimension(WIDTH - 64 - 32, 16));
				inputPane.add(text.get(text.size() - 1));
				inputPane.add(textField.get(textField.size() - 1));
			}
		}
		inputPane.add(hint);
		this.setBounds(new Rectangle(100, 100, WIDTH, HEIGHT
				+ (text.size() * 18)));
		this.setLocationRelativeTo(null);
		updateView();
	}

	// reset ui on media-change.
	private void setMediaCombo(final JComboBox<String> combo) {
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

	public void setComboIndex(int index) {
			comboMedia.setSelectedIndex(index);
	}

}
