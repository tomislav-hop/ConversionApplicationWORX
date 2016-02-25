package window;

import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import thop.ConversionApp;

import javax.swing.JComboBox;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JProgressBar;

public class ConversionApplicationWindow {

	private JFrame frmConversionApplication;
	private JTextField txtUrlBaze;
	private JTextField txtNazivBaze;
	private JTextField txtUser;
	private JTextField txtPassword;
	private JTextField txtMongoDB;
	private JButton btnConvert;
	private JButton btnExit;
	private JComboBox<String> comboBoxTipSQL;
	private JLabel lblTipBaze;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ConversionApplicationWindow window = new ConversionApplicationWindow();
					window.frmConversionApplication.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ConversionApplicationWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmConversionApplication = new JFrame();
		frmConversionApplication.setTitle("Conversion Application");
		frmConversionApplication.setBounds(100, 100, 475, 306);
		frmConversionApplication.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JLabel lblUrlBaze = new JLabel("URL baze:");

		txtUrlBaze = new JTextField();
		txtUrlBaze.setColumns(10);

		JLabel lblNazivBaze = new JLabel("Naziv baze:");

		txtNazivBaze = new JTextField();
		txtNazivBaze.setColumns(10);

		JLabel lblUser = new JLabel("User:");

		txtUser = new JTextField();
		txtUser.setColumns(10);

		JLabel lblPassword = new JLabel("Password:");

		txtPassword = new JTextField();
		txtPassword.setText("");
		txtPassword.setColumns(10);

		JLabel lblMongodbNaziv = new JLabel("MongoDB naziv:");

		txtMongoDB = new JTextField();
		txtMongoDB.setText("");
		txtMongoDB.setColumns(10);

		JProgressBar progressBar = new JProgressBar();
		progressBar.setStringPainted(true);
		progressBar.setBackground(Color.red);

		btnConvert = new JButton("Convert");
		btnConvert.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (txtMongoDB.getText().equals("") || txtUrlBaze.getText().equals("")
						|| comboBoxTipSQL.getSelectedItem().toString().equals("")) {
					// U sluèaju da nedostaju url baze, naziv MongoDB baze te
					// ako nije odabran tip SQL baze izbacujem warning
					JOptionPane.showMessageDialog(frmConversionApplication,
							"Uvijek morate:\n - unesti url baze\n - unesti naziv mongoDB baze\n - odabrati tip SQL baze.",
							"Greška", JOptionPane.ERROR_MESSAGE);
				} else {
					ConversionApp conversionApp = new ConversionApp();
					if (conversionApp.startConversionApplication(txtNazivBaze.getText(), txtUrlBaze.getText(),
							txtUser.getText(), txtPassword.getText(), txtMongoDB.getText(),
							comboBoxTipSQL.getSelectedItem().toString())) {
						progressBar.setBackground(Color.green);
						progressBar.setString("100%");
					}
				}
			}
		});

		btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(1);
			}
		});

		comboBoxTipSQL = new JComboBox<String>();
		comboBoxTipSQL.addItem("MySQL");
		comboBoxTipSQL.addItem("PostgreSQL");
		comboBoxTipSQL.addItem("Microsoft SQL");
		comboBoxTipSQL.addItem("MariaDB");
		comboBoxTipSQL.addItem("SQLite");

		lblTipBaze = new JLabel("Tip Baze:");

		GroupLayout groupLayout = new GroupLayout(frmConversionApplication.getContentPane());
		groupLayout.setHorizontalGroup(groupLayout.createParallelGroup(Alignment.LEADING).addGroup(groupLayout
				.createSequentialGroup().addContainerGap(14, Short.MAX_VALUE)
				.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(lblUrlBaze, Alignment.TRAILING).addComponent(lblNazivBaze, Alignment.TRAILING)
						.addComponent(lblUser, Alignment.TRAILING).addComponent(lblPassword, Alignment.TRAILING)
						.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(lblTipBaze, Alignment.TRAILING)
								.addComponent(lblMongodbNaziv, Alignment.TRAILING)))
				.addGap(6)
				.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
						.addComponent(btnExit, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
								Short.MAX_VALUE)
						.addComponent(btnConvert, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE,
								GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(txtUrlBaze, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 345, Short.MAX_VALUE)
						.addComponent(txtNazivBaze, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 345, Short.MAX_VALUE)
						.addComponent(txtUser, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 345, Short.MAX_VALUE)
						.addComponent(txtPassword, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 345, Short.MAX_VALUE)
						.addComponent(txtMongoDB, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 345, Short.MAX_VALUE)
						.addComponent(comboBoxTipSQL, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
				.addContainerGap(17, Short.MAX_VALUE))
				.addComponent(progressBar, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 459, Short.MAX_VALUE));
		groupLayout
				.setVerticalGroup(
						groupLayout.createParallelGroup(Alignment.LEADING)
								.addGroup(groupLayout.createSequentialGroup().addGap(6)
										.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
												.addGroup(groupLayout.createSequentialGroup().addGap(3)
														.addComponent(lblUrlBaze))
												.addComponent(txtUrlBaze, GroupLayout.PREFERRED_SIZE,
														GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
				.addGap(6)
				.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup().addGap(3).addComponent(lblNazivBaze))
						.addComponent(txtNazivBaze, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE))
				.addGap(6)
				.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup().addGap(3).addComponent(lblUser))
						.addComponent(txtUser, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE))
				.addGap(6)
				.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup().addGap(3).addComponent(lblPassword))
						.addComponent(txtPassword, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE))
				.addGap(6)
				.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup().addGap(3).addComponent(lblMongodbNaziv))
						.addComponent(txtMongoDB, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE))
				.addPreferredGap(ComponentPlacement.RELATED)
				.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(comboBoxTipSQL, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
								GroupLayout.PREFERRED_SIZE)
						.addComponent(lblTipBaze)).addPreferredGap(ComponentPlacement.RELATED).addComponent(btnConvert)
				.addPreferredGap(ComponentPlacement.RELATED).addComponent(btnExit)
				.addPreferredGap(ComponentPlacement.UNRELATED)
				.addComponent(progressBar, GroupLayout.DEFAULT_SIZE, 31, Short.MAX_VALUE).addContainerGap()));
		frmConversionApplication.getContentPane().setLayout(groupLayout);
	}

	public JTextField getTxtUrlBaze() {
		return txtUrlBaze;
	}

	public void setTxtUrlBaze(JTextField txtUrlBaze) {
		this.txtUrlBaze = txtUrlBaze;
	}

	public JTextField getTxtNazivBaze() {
		return txtNazivBaze;
	}

	public void setTxtNazivBaze(JTextField txtNazivBaze) {
		this.txtNazivBaze = txtNazivBaze;
	}

	public JTextField getTxtUser() {
		return txtUser;
	}

	public void setTxtUser(JTextField txtUser) {
		this.txtUser = txtUser;
	}

	public JTextField getTxtPassword() {
		return txtPassword;
	}

	public void setTxtPassword(JTextField txtPassword) {
		this.txtPassword = txtPassword;
	}

	public JTextField getTxtMongoDB() {
		return txtMongoDB;
	}

	public void setTxtMongoDB(JTextField txtMongoDB) {
		this.txtMongoDB = txtMongoDB;
	}

	public JComboBox<String> getComboBoxTipSQL() {
		return comboBoxTipSQL;
	}

	public void setComboBoxTipSQL(JComboBox<String> comboBoxTipSQL) {
		this.comboBoxTipSQL = comboBoxTipSQL;
	}
}
