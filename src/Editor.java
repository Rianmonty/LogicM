import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;

import com.rian.thirdparty.TextLineNumber;
import com.apple.eawt.Application;
public class Editor implements ActionListener{
	
	Compiler compiler;
	JTextArea text;
	JScrollPane scroll;
	TextLineNumber lineNo;
	JFrame frame;
	public Editor() {
		ImageIcon icon = new ImageIcon();
		
		icon.setImage(getImage("/icon.png"));
		
		frame = new JFrame("LogicM");
		
		
		JToolBar toolbar = new JToolBar();
		toolbar.setFloatable(false);
		compiler = new Compiler();
		text = new JTextArea();
		scroll = new JScrollPane(text);
		lineNo = new TextLineNumber(text);
		scroll.setRowHeaderView(lineNo);
		text.setTabSize(1);
		JButton run = new JButton("Run");
		JButton save = new JButton("Save");
		JButton open = new JButton("Open");
		save.addActionListener(this);
		open.addActionListener(this);
		run.addActionListener(this);
		open.setActionCommand("open");
		save.setActionCommand("save");
		run.setActionCommand("run");
		toolbar.add(open);
		toolbar.add(save);
		toolbar.add(run);
		frame.setLayout(new BorderLayout());
		
	
		frame.add(toolbar,BorderLayout.NORTH);
		frame.add(scroll, BorderLayout.CENTER);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(true);
		frame.setMinimumSize(new Dimension(800,600));
		Application.getApplication().setDockIconImage(icon.getImage());
		 System.setProperty(
		            "com.apple.mrj.application.apple.menu.about.name", "LogicM");
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icon.png")));
		frame.setVisible(true);
	}
	
	public void save() {
		JFileChooser chooser = new JFileChooser("");
		int option = chooser.showSaveDialog(frame);
		File file = new File(chooser.getSelectedFile().getPath());
		
		
		try {
			String source = text.getText();
			char buffer[] = new char[source.length()];
			source.getChars(0,source.length(),buffer,0);

			FileWriter f1= new FileWriter(file);
			for(int i=0;i<buffer.length;i++)
			{
			f1.write(buffer[i]);
			}
			f1.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	public void open() {
		JFileChooser open = new JFileChooser("");
		int option = open.showOpenDialog(frame);
		File file = new File(open.getSelectedFile().getPath());
		try {
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);
			String s;
			text.setText("");
			while((s=br.readLine())!=null) {
				text.append(s + "\n");
			}
			fr.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public Image getImage(String path) {
		Image image=null;
		try {
			image = ImageIO.read(this.getClass().getResourceAsStream(path));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return image;
	}
	public void actionPerformed(ActionEvent e) {
		//System.out.println(e.getActionCommand());
		String command=e.getActionCommand();
		switch(command) {
		case "run":
			compiler.compile(text.getText().split("\\n"));
			break;
		case "save":
			save();
			break;
		case "open":
			open();
			break;
			//System.out.println("hello");
		}
		
	}
	
	
}
