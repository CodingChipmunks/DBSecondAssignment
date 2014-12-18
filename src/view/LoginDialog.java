	package view;

	import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

	import controller.Controller;
import model.Model;

/***
 * 
 * @author chilimannen
 *	The login form is used to get input user/pass from the user. 
 *	These values are then checked against a stored procedure in the
 *  database. No session is established, it is convenient for the
 *  user to know when the account does not exist. 
 */

@SuppressWarnings("serial")
public class LoginDialog extends JDialog{
		private JPanel inputPane, buttonPane, contentPane;
		private JButton login;
		private JButton cancel;
		private JTextField user;
		private JPasswordField pass;

		private int WIDTH = 200;
		private int HEIGHT = 135;

		public LoginDialog(Model model, final WBView view, final Controller controller) {
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

			contentPane.add(inputPane, BorderLayout.CENTER);
			contentPane.add(buttonPane, BorderLayout.SOUTH);
			
			user = new JTextField(16);
			pass = new JPasswordField(16);
			user.setText(model.getUser());
			pass.setText(model.getPass());
			inputPane.add(user);
			inputPane.add(pass);

			login = new JButton("Login");
			buttonPane.add(login);
			controller.setLogin(login, this);

			cancel = new JButton("Cancel");
			buttonPane.add(cancel);
			setCancel(cancel);
			
			JRootPane rootPane = SwingUtilities.getRootPane(login); 
			rootPane.setDefaultButton(login);
			
			Style.setUITheme(this.getRootPane());
			
			setVisible(true);
			setLocationRelativeTo(null);
			this.setTitle("Login!");
			
			// listener for show/hide events.
			this.addComponentListener(new ComponentAdapter() {
				public void componentHidden(ComponentEvent e) {
					//view.setVisible(true);
				}

				public void componentShown(ComponentEvent e) {
				}
			});
		}


		private void setCancel(JButton button) {
			button.addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent evt) {
					setVisible(false);
				}
			});
		}


		public void updateView() {
			contentPane.updateUI();
		}

		public void showError(String errormsg) {
			JOptionPane.showMessageDialog(this, errormsg);
		}


		public String getPass() {
			return new String(pass.getPassword());
		}


		public String getUser() {
			return user.getText();
		}


		public void loginFailed() {
			pass.setText("");
		}
}
