import java.util.Scanner;

public class Main {
	Scanner scanner;
	Compiler compile;
	Editor editor;	
	Terminal term;
	
	public Main() {
	
		editor = new Editor();
		term = new Terminal();
		
	}
	
	public static void main(String[] args) {
		
		new Main();
	}
}	
