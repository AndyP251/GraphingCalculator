package CalcGuiTwo;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JTextField;

import javax.swing.JMenuBar;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

public class calculatorFrame extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;

	// todo
	// refactor to use scientific notation when # is too large
	// graphing plot for validated functions, validated variable chart
	// functions for the graph plot and labeling

	private calculatorPanel dividerPanel; // border east west for graph
	// east panel
	private calculatorPanel eastPanel;
	
	private JLabel graphFuncLabel;

	// delete for revision

	// west panel
	private calculatorPanel masterPanel;

	private JLabel computationLabel;

	private JButton clearButton;
	private JButton[] mapGrid;

	private JMenuItem basic;
	private JMenuItem scientific;

	private JTextField functionText;

	private String Stringcurr;
	private String Savedstr;
	private int currOpp;

	private ArrayList<String> validOpperands;

	public calculatorFrame() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		validOpperands = new ArrayList<String>();
		validOpperands.add("+");
		validOpperands.add("^");
		validOpperands.add("*");
		validOpperands.add(" ");
		validOpperands.add("=");

		// caclulator menu
		JMenuBar menuBar = new JMenuBar();
		JMenu calcType = new JMenu("Type");

		basic = new JMenuItem("Basic");
		scientific = new JMenuItem("Scientific");

		basic.addActionListener(this);
		scientific.addActionListener(this);

		calcType.add(basic);
		calcType.add(scientific);

		menuBar.add(calcType);

		this.setJMenuBar(menuBar);

		// paneling
		masterPanel = new calculatorPanel();
		masterPanel.setLayout(new BorderLayout());

		dividerPanel = new calculatorPanel();
		dividerPanel.setLayout(new BorderLayout());

		eastPanel = new calculatorPanel();
		eastPanel.setLayout(new BorderLayout());

		graphFuncLabel = new JLabel("Graphed Function: ");

		calculatorPanel topPanel = new calculatorPanel();
		topPanel.setLayout(new BorderLayout());

		calculatorPanel buttonPanel = new calculatorPanel();
		buttonPanel.setLayout(new GridLayout(3, 4));

		int count = 0;
		mapGrid = new JButton[15];
		for (int i = 0; i < mapGrid.length; i++) {
			switch (i) {
			case 3:
				mapGrid[i] = new JButton("+");
				break;
			case 4:
				mapGrid[i] = new JButton("-");
				break;
			case 8:
				mapGrid[i] = new JButton("*");
				break;
			case 9:
				mapGrid[i] = new JButton("/");
				break;
			case 13:
				mapGrid[i] = new JButton("=");
				break;
			case 14:
				mapGrid[i] = new JButton("e");
				break;
			default:
				count++;
				mapGrid[i] = new JButton(count + "");
			}

			buttonPanel.add(mapGrid[i]);
			mapGrid[i].setBackground(new Color(128, 128, 128));// new Color(72, 209, 204));
			mapGrid[i].addActionListener(this);
			mapGrid[i].setActionCommand(String.valueOf(i));
			mapGrid[i].setFont(new Font("Arial", Font.BOLD, 20));
			mapGrid[i].setForeground(Color.BLACK);
//			mapGrid[i].setPreferredSize(new Dimension(100, 50));
		}
		masterPanel.add(buttonPanel, BorderLayout.CENTER);

		computationLabel = new JLabel("888");
		computationLabel.setFont(new Font("Arial", Font.BOLD, 50));

		clearButton = new JButton("CE");
		clearButton.addActionListener(this);

		functionText = new JTextField("Enter Function Here: ");
		functionText.addActionListener(this);
		masterPanel.add(functionText, BorderLayout.SOUTH);

		topPanel.add(clearButton, BorderLayout.WEST);

		topPanel.add(computationLabel, BorderLayout.EAST);

		masterPanel.add(topPanel, BorderLayout.NORTH);

		calculatorPanel southEast = new calculatorPanel();
		southEast.setLayout(new BorderLayout());
		southEast.add(graphFuncLabel, BorderLayout.EAST);
		eastPanel.add(southEast, BorderLayout.SOUTH);
		

		dividerPanel.add(masterPanel, BorderLayout.WEST);
		dividerPanel.add(eastPanel, BorderLayout.EAST);

		this.setContentPane(dividerPanel);
		this.setPreferredSize(new Dimension(300, 500));
		this.setTitle("Basic Calculator");
		this.pack();

		currOpp = -1;
		Stringcurr = "";
		Savedstr = "";

	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() instanceof JTextField) {
			JTextField text = (JTextField) e.getSource();

			// enter text code here:
			System.out.println(text.getText());
			char[] inputChars = text.getText().toCharArray();
			boolean validated = true;
			for (Character _char : inputChars) {
				if (!(Character.isDigit(_char)) && _char != 'x' && _char != 'y'
						&& !(validOpperands.contains(Character.toString(_char)))) {
					text.setText("Invalid Input - numerical input, variables & opperands");
					validated = false;
					break;
				}

			}
			System.out.println(validated);
			if (validated) {

				graphFuncLabel.setText("Graphed Function: " + text.getText().strip());

			}

		}

		if (e.getSource() instanceof JMenuItem) {
			JMenuItem item = (JMenuItem) e.getSource();

			if (item == basic) {
				System.out.println("basic calculator selected");
			}

			if (item == scientific) {
				System.out.println("scientific calculator selected");
			}

		}

		if (e.getSource() instanceof JButton) {
			JButton src = (JButton) e.getSource();

			switch (src.getText()) {
			case "+":
				currOpp = 0;
				Savedstr = Stringcurr;
				Stringcurr = "";
				disableOpperators();

				break;
			case "-":
				currOpp = 1;

				Savedstr = Stringcurr;
				Stringcurr = "";
				disableOpperators();

				break;

			case "*":
				currOpp = 2;

				Savedstr = Stringcurr;
				Stringcurr = "";
				disableOpperators();

				break;

			case "/":
				currOpp = 3;

				Savedstr = Stringcurr;
				Stringcurr = "";
				disableOpperators();

				break;

			case "=":

				float result = computation(Float.parseFloat(Savedstr), Float.parseFloat(Stringcurr), currOpp);
				double rounded = Math.round(result * 100.0) / 100.0;
				computationLabel.setText(Double.toString(rounded));
				Stringcurr = Float.toString(result);
				enableOpperators();
				break;

			case "e":
				currOpp = 5;

				Savedstr = Stringcurr;
				Stringcurr = "";
				disableOpperators();

				break;

			case "CE":

				Stringcurr = "";
				computationLabel.setText(Stringcurr);
				enableOpperators();

				break;
			default:
				Stringcurr += src.getText();
				computationLabel.setText(Stringcurr);
			}
		}
	}

	private float computation(float num1, float num2, int oppSign) {
		switch (currOpp) {
		case 0:
			return num1 += num2;
		case 1:
			return num1 -= num2;
		case 2:
			return num1 *= num2;
		case 3:
			return num1 /= num2;

		case 4:
			return num1 %= num2;
		case 5:
			return (int) Math.pow(num1, num2);
		default:
			System.out.println("No opperation reached");

		}
		return -1;
	}

	private void disableOpperators() {
		mapGrid[3].setEnabled(false);
		mapGrid[4].setEnabled(false);
		mapGrid[8].setEnabled(false);
		mapGrid[9].setEnabled(false);
		mapGrid[14].setEnabled(false);
	}

	private void enableOpperators() {
		mapGrid[3].setEnabled(true);
		mapGrid[4].setEnabled(true);
		mapGrid[8].setEnabled(true);
		mapGrid[9].setEnabled(true);
		mapGrid[14].setEnabled(true);
	}

}
