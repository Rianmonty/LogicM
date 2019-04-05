

public class LogicGate {
	public String name;
	
	public int inputs;
	public int outputs;
	
	public String[] definition;
	
	public LogicGate(String name, int inputs, int outputs) {
		this.name=name;
		this.inputs=inputs;
		this.outputs=outputs;
		
	}
	
	public String getName() {
		return this.name;
	}
	
	public int getInputs() {
		return this.inputs;
	}
	
	public int getOutputs() {
		return this.outputs;
	}
	
	public String[] getDefinition() {
		return this.definition;
	}
	public void setDefinition(String definition) {
		this.definition=definition.split("\\|");
	}
	
	
	public int[] nand(int x, int y) {
		int[] array = new int[1];
		if(x==y && x==1) {
			array[0]= 0;
		}else {
			array[0]= 1;
		}
		//Terminal.write("TEST");
		return array;
	}
	
	
	
}
