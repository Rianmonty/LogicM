
public class Variable {
	private String name;
	private String value;
	private int integerValue;
	private boolean isInput;
	private boolean isOutput;
	public Variable(String name, String v,boolean isInput, boolean isOutput) {
		this.name=name;
		this.value=value;
		this.isInput=isInput;
		this.isOutput=isOutput;
	}
	public Variable(String name, int v, boolean isInput, boolean isOutput) {
		this.name=name;
		this.integerValue=v;
		this.isInput=isInput;
		this.isOutput=isOutput;
	}
	public Variable(String name, boolean isInput, boolean isOutput) {
		this.name=name;
		this.isInput=isInput;
		this.isOutput=isOutput;
	}
	
	public void setInteger(int i) {
		this.integerValue=i;
	}
	
	public String getName() {
		return name;
	}
	
	public String getValue() {
		return value;
	}
	public int getInteger() {
		return integerValue;
	}
	public boolean getInput() {
		return isInput;
	}
	public boolean getOutput() {
		return isOutput;
	}
	
	
}
