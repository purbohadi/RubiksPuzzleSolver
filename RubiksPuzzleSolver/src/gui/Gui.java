package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.*;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.border.EmptyBorder;

import pipes.RunCommand;
import robotics.CreateArduinoSketch;

public class Gui extends JFrame {
	private static final long serialVersionUID = 1L;
	private JButton[][] facelet = new JButton[6][9];
	private final JButton[] colorSel = new JButton[6];
	private JEditorPane textArea = new JEditorPane();
	private final Color[] COLORS = { Color.white, Color.red, Color.green, Color.yellow, Color.orange, Color.blue };
	private Color curCol = COLORS[0]; 

	//initiate JFrame
	public static void main(String[] args) {
		Gui frame = new Gui();
		frame.setVisible(true);
	}

	public Gui() {
		
		//setup  dimensions and settings
		super("kociemba's sub-optimal cube algorithm");
		
		JPanel container = new JPanel();
		container.setLayout(new BorderLayout());
		setContentPane(container);
	    setDefaultCloseOperation(EXIT_ON_CLOSE);
	    setSize(1200, 800);
	    setLocationRelativeTo(null);
	    setMinimumSize(new Dimension(1300, 840));

	    
	    JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, CubePanel(), OutputPanel());
        splitPane.setResizeWeight(0.83);
        splitPane.setOpaque(false);
		container.add(TitlePanel(), BorderLayout.NORTH	);
		container.add(splitPane, BorderLayout.CENTER);
		container.add(new VisionPanel(), BorderLayout.EAST	);
	}
	
	//output panel that will display output to the user 
	private JPanel TitlePanel(){
		
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
	    panel.setBackground(Color.GRAY);
		panel.setPreferredSize(new Dimension(Integer.MAX_VALUE, 55 ));
		panel.setBorder(BorderFactory.createLineBorder(Color.black));

		JPanel leftpanel = new JPanel();
		leftpanel.setLayout(new BorderLayout());
		leftpanel.setBackground(Color.GRAY);
		
		JLabel title = new JLabel("   Rubiks Puzzle Solver");
		title.setFont(new Font("Serif", Font.BOLD, 22));
		title.setForeground(Color.WHITE);
		
		JLabel name = new JLabel("    By Jordan Waddell");
		name.setFont(new Font("Serif", Font.BOLD, 14));
		name.setForeground(Color.WHITE);
		name.setBorder(new EmptyBorder(10, 0, 0, 0));
		
		leftpanel.add(title,  BorderLayout.WEST);
		leftpanel.add(name,  BorderLayout.EAST);
		
		panel.add(leftpanel,  BorderLayout.WEST);
		
		return panel;
	} 
		
	
	//output panel that will display output to the user 
	private JPanel OutputPanel(){
		JPanel outputpanel = new JPanel();
		outputpanel.setLayout(new GridLayout(1, 1));
		outputpanel.setBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, Color.BLACK));
		
		textArea.setFont(new Font("Serif", Font.PLAIN, 16));
		textArea.setEditable(false);
		textArea.setText("");
	
		outputpanel.add(textArea);

		
		JPanel titlepanel = new JPanel();
		titlepanel.setLayout(new BorderLayout());
		titlepanel.setBackground(Color.GRAY);
		titlepanel.setBorder(new EmptyBorder(5, 2, 8, 0));
		
		JLabel title = new JLabel("   Program Output");
		title.setFont(new Font("Serif", Font.BOLD, 17));
		title.setForeground(Color.WHITE);
		titlepanel.add(title);
		
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(titlepanel,  BorderLayout.NORTH);
		panel.add(outputpanel,  BorderLayout.CENTER);
	
		return panel;
	} 
		
	
	//this panel allows the user to input the location of ecahc colotr on the rubiks cube
	private JPanel CubePanel() {
		int FSIZE = 60;
		int[] XOFF = { 4, 7, 4, 4, 1, 10 };
		int[] YOFF = { 1, 4, 4, 7, 4, 4 };
		
		JPanel panel = new JPanel();
		
		for (int i = 0; i < 6; i++)
			for (int j = 0; j < 9; j++) {
				facelet[i][j] = new JButton();
				getContentPane().add(facelet[i][j]);
				facelet[i][j].setBackground(Color.gray);
				facelet[i][j].setRolloverEnabled(false);
				facelet[i][j].setOpaque(true);
				facelet[i][j].setBounds(FSIZE * XOFF[i] + FSIZE * (j % 3), FSIZE * YOFF[i] + FSIZE * (j / 3), FSIZE, FSIZE);
				facelet[i][j].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent evt) {
						((JButton) evt.getSource()).setBackground(curCol);
					}
				});
			}
		
		String[] txt = { "U", "R", "F", "D", "L", "B" };
		
		for (int i = 0; i < 6; i++)
			facelet[i][4].setText(txt[i]);
		
		for (int i = 0; i < 6; i++) {
			colorSel[i] = new JButton();
			getContentPane().add(colorSel[i]);
			colorSel[i].setBackground(COLORS[i]);
			colorSel[i].setOpaque(true);
			colorSel[i].setBounds(FSIZE * (XOFF[1] + 1) + FSIZE / 4 * 3 * i, FSIZE * (YOFF[3] + 1), FSIZE / 4 * 3, FSIZE / 4 * 3);
			colorSel[i].setName("" + i);
			colorSel[i].addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					curCol = COLORS[Integer.parseInt(((JButton) evt.getSource()).getName())];
				}
			});
		}

		JButton resetb = new JButton("Reset Cube");
		getContentPane().add(resetb);
		resetb.setBounds(460, 190 ,120, 30);
		resetb.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
				ResetCubeState();
		    }
		});
		
		JButton solveb = new JButton("Solve Cube");
		getContentPane().add(solveb);
		solveb.setBounds(600, 190 ,120, 30);
		solveb.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		    	textArea.setText(" Solving cube ... This may take a few moments");
		        Thread t1 = new Thread(new Runnable() {
		            public void run(){
				    	solveCube();
		            }});  
		            t1.start();
		    }
		});
		
		return panel;
	}

	//take the users input and parse it into a string where it can be passed to the cube solving algorithm 
	private void solveCube() {
		
		StringBuffer s = new StringBuffer(54);

		for (int i = 0; i < 54; i++)
			s.insert(i, 'B');// default initialization
		for (int i = 0; i < 6; i++)
			// read the 54 facelets
			for (int j = 0; j < 9; j++) {
				if (facelet[i][j].getBackground() == facelet[0][4].getBackground())
					s.setCharAt(9 * i + j, 'U');
				if (facelet[i][j].getBackground() == facelet[1][4].getBackground())
					s.setCharAt(9 * i + j, 'R');
				if (facelet[i][j].getBackground() == facelet[2][4].getBackground())
					s.setCharAt(9 * i + j, 'F');
				if (facelet[i][j].getBackground() == facelet[3][4].getBackground())
					s.setCharAt(9 * i + j, 'D');
				if (facelet[i][j].getBackground() == facelet[4][4].getBackground())
					s.setCharAt(9 * i + j, 'L');
				if (facelet[i][j].getBackground() == facelet[5][4].getBackground())
					s.setCharAt(9 * i + j, 'B');
			}
		
		String formatedstate = FormatStringForCubeState(s.toString());
		RunCommand c = new RunCommand("miker.exe", formatedstate );
		textArea.setText(c.getSolution());
		if(c.getSuccess()){
			new CreateArduinoSketch(c.getSolution());
		}

	}
	
	//retrieve cube state and execute cube program and motors
	private void ResetCubeState() {
		for (int i = 0; i < 6; i++){
			for (int j = 0; j < 9; j++) {
				facelet[i][j].setBackground(Color.gray);
				facelet[i][j].setRolloverEnabled(false);
			}
		}
		textArea.setText("");
	}
	
	//sorts string array into a format taht can be passed to solver program
	private String FormatStringForCubeState(String cubestr){
		int[][] indexorder ={{7, 19},{5, 10},{1, 46},{3, 37},{28, 25},{32, 16},{34, 52},{30, 43},{23, 12},{21, 41},{48, 14},{50, 39},	
				{8, 20, 9},{2, 11, 45},{0, 47, 36},{6, 38, 18},{29, 15, 26},{27, 24, 44}, {33, 42, 53},{35, 51, 17}};
		
		StringBuilder sb = new StringBuilder(100);
		
		for(int x=0; x < indexorder.length; x++){
			for(int i=0; i < indexorder[x].length; i++){
				sb.append(cubestr.charAt(indexorder[x][i]));
			}
			sb.append(",");
		}
		return sb.toString();
		
	}
	
}
