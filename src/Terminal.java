import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;

public class Terminal implements ActionListener{
	JFrame frame;
	JScrollPane scroll;
	static JTextArea text;
	public Terminal() {
		frame = new JFrame("LogicM Console");
		frame.setMinimumSize(new Dimension(400,300));
		JToolBar toolbar = new JToolBar();
		text = new JTextArea();
		scroll = new JScrollPane(text);
		text.setEditable(false);
		JButton clear = new JButton("Clear");
		clear.addActionListener(this);
		clear.setActionCommand("clear");
		frame.setLayout(new BorderLayout());
		//text.setAutoscrolls(true);
		//scroll.add(text);
		toolbar.add(clear);
		frame.add(toolbar, BorderLayout.NORTH);
		frame.add(scroll, BorderLayout.CENTER);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	public static void write(String s) {
		
		text.setText(text.getText()+ s  + System.lineSeparator() );
	}
	public static void writeLine(String s) {
		
		text.setText(text.getText()+ s  );
	}
	public static void write(String s, boolean debug) {
		if(debug==true) {
			
		}
			
	}
	public static void clear() {
		text.setText("");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		switch(command){
		case "clear":
			clear();
			break;
		}
		
	}
}
