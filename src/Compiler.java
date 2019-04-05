import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class Compiler {
	public String[] lines;
	public List<LogicGate> gates = new ArrayList<LogicGate>();
	public Compiler() {
		initialise();
	}
	public void initialise() {
		LogicGate nand = new LogicGate("nand", 2,1);
		gates.add(nand);
		
	}
	public void compile(String[] text) {
		lines = text;
		String action = null;
		
		 
		int end = 0;
		
		boolean isDefinition;
		for(int i = 0 ; i < lines.length;i ++) {
		//	System.out.println(lines[i] + "///");
			if(lines[i].contains("(") && lines[i].contains(")") && lines[i].contains("->")) {				
				createDefinition(lines,i);
			}
			
			if(lines[i].contains("(") && lines[i].contains(")") && lines[i].contains("=") && lines[i].contains("where")) {
				action = lines[i];
				
				for(int x = i; x < lines.length;x++) {
					
					if(lines[x].contains(";")) {

				
						end=x;
						break;
					}
				}
				for(int y = i+1; y < end; y++) {
					if(!(end==0)) {
						action += "|"+ lines[y];
					}
				}
			}
			if(lines[i].contains(";")) {
				gates.get(gates.size()-1).setDefinition(action);
			//	Terminal.write(gates.get(gates.size()-1).getDefinition());
				action = "";
			}
			if(lines[i].contains("experiment")) {
				int[] temp = experiment(lines,i);
				//Terminal.clear();
				String table = "";
				for(int result : temp) {
					//Terminal.clear();
					table += result + "|";
				}
				Terminal.write("Output: "+table);
				
			}
			if(lines[i].contains("truthtable")) {
				 truthtable(lines,i);
			}
		}

	}
	public int[] experiment(String[] lines, int i) {
		String name = lines[i].substring(lines[i].indexOf(" ")+1, lines[i].indexOf("("));
		String args = lines[i].substring(lines[i].indexOf("("), lines[i].indexOf(")")+1);
		args=args.replace("(", "");
		args = args.replace(")","");
		String[] argArray = args.split(",");
		int[] inputs = new int[argArray.length];
		for(int s = 0; s < inputs.length;s++) {
			inputs[s]=Integer.parseInt(argArray[s]);
		}
		//System.out.println(name);
		int gateId = searchGates(name);
		if(gateId != -1) {
			return execute(gates.get(gateId),inputs);
		}else {
			int[] failure = new int[1];
			failure[0]=-1;
			return failure;
		}
		
	}
	String truthTableInput="";
	public void truthtable(String[] lines, int i) {
		truthTableInput="";
		String name = lines[i].substring(lines[i].indexOf(" ")+1, lines[i].length());
		
		int gateId = searchGates(name);
		int n = gates.get(gateId).getInputs();
	//	int rows = (int) Math.pow(2,n);
		int col = 0;
		String t="";
		int[] temp=new int[n*n];
		
        generateTable(0,n,temp);
        
        String[] splitTable = truthTableInput.split(",");
        for(int string = 0; string < splitTable.length;string++) {
        	String[] inputStrings = splitTable[string].split(" ");
        	String outputLine = splitTable[string] + " | ";
        	int[] parse = new int[inputStrings.length];
        	for(int in = 0; in < inputStrings.length;in++) {
        		
        		parse[in] = Integer.parseInt(inputStrings[in]);
        		
        		
        		
        		
        	}
        	int[] result = execute(gates.get(gateId), parse);
    		for(int re=0;re<result.length;re++) {
    			outputLine+=result[re]+ " ";
    		}
        	Terminal.write(outputLine);
        }
        
	}
	private void generateTable(int index, int size, int[] current) {
        if (index == size) {
        //	int count = 0;
            for (int i = 0; i < size; i++) {
            	
            	truthTableInput += current[i] + " ";
                Terminal.write(truthTableInput,true);
            }
            truthTableInput+=",";
        } else {
            for (int i = 0; i < 2; i++) {
                current[index] = i;
                generateTable(index + 1, size, current);
            }
        }
    }
	//See to be honest I have no idea how this works but it does so dont touch it
	public int[] execute(LogicGate gate, int[] inputs) {
		if(gate.getName().equals("nand")) {
			return gate.nand(inputs[0], inputs[1]);
		}else {
		String[] def = gate.getDefinition();
		String allVariables = "";
		for(int x = 0 ; x < inputs.length;x++) {
			//Terminal.write(""+inputs[x]);
		}
		String equaliser = def[0].substring(0, def[0].indexOf("where"));
		//Create variables for inputs.
		List<Variable> vInput = new ArrayList<>();
		String inputVariables = equaliser.substring(equaliser.indexOf("(")+1, equaliser.indexOf(")"));
		String[] inputVariableArray = inputVariables.split(",");
		//Create variables
		for(int iv = 0; iv < inputVariableArray.length;iv++) {
			vInput.add(new Variable(inputVariableArray[iv].replaceAll(" ", ""), inputs[iv], true,false));
			allVariables += vInput.get(iv).getName() + " ";
			Terminal.write(equaliser  + " " + vInput.get(iv).getName() + ":" + vInput.get(iv).getInteger(), true);
			
		}
		
		//Create variables for outputs;
		List<Variable> vOutput = new ArrayList<>();
		String outputVariables = equaliser.substring(equaliser.indexOf("=")+1);
		String[] outputVariableArray = outputVariables.split(",");
		for(int ov = 0; ov < outputVariableArray.length;ov++) {
			vOutput.add(new Variable(outputVariableArray[ov].replace(" ", ""),false,true));
			allVariables += vOutput.get(ov).getName() + " ";
		}
		
		//Create variables declared in the code(if any)
		List<Variable> otherVariables = new ArrayList<>();
		
		//Process code block
		String[] code = new String[def.length-1];
		for(int i = 0; i < code.length;i++) {
			List<Variable> currentV = new ArrayList<>();
			code[i]=def[i+1];
			
			if(code[i].contains("=")) {
				String left = code[i].substring(0, code[i].indexOf("="));
				String[] leftArray = left.split(",");
				int current;
				for(String s:leftArray) {
					if(!allVariables.contains(s)) {
						Variable temp = new Variable(s,false,false);
						otherVariables.add(temp);
						allVariables += s + " ";
						currentV.add(temp);
					}else {
						current = searchVariables(s,vOutput);
						//Terminal.write(""+current);
						if((current != -1)) {
							currentV.add(vOutput.get(current));
						//	Terminal.write("Current: "+current);
						}else {
							
						}
				
					}
				
				}
				
				
				String right=code[i].substring(code[i].indexOf("=")+1);
				
				if(right.contains("(") && right.contains(")")) {
					String command = right.substring(0, right.indexOf("("));
					String nestedInputString = right.substring(right.indexOf("(")+1, right.indexOf(")"));
					String[] nestedInputArray = nestedInputString.split(",");
					int[] nestedInputs = new int[nestedInputArray.length];
					Terminal.write("NestedInputString " + nestedInputString,true);
					boolean exists=false;
					boolean[] isInput = new boolean[nestedInputs.length];
					boolean[] isOutput=new boolean[nestedInputs.length];
					for(int es = 0; es < nestedInputArray.length;es++) {
						if(allVariables.contains(nestedInputArray[es])) {
							exists=true;
							if(exists) {
								for(Variable v:vInput) {
									if(v.getName().equals(nestedInputArray[es])) {
										
										nestedInputs[es]=v.getInteger();
										Terminal.write("NestedInputValue " +v.getInteger(),true);
										isInput[es]=true;
									}
								}
								if(isInput[es]==false) {
									for(Variable v:vOutput) {
										if(v.getName().equals(nestedInputArray[es])) {
											
											nestedInputs[es]=v.getInteger();
											Terminal.write("NestedOutputValue " +v.getInteger(),true);
											isOutput[es]=true;
										}
									}
								}
								if(isInput[es]==false && isOutput[es] == false) {
									
									for(Variable o:otherVariables) {
										if(o.getName().equals(nestedInputArray[es])) {
											nestedInputs[es]=o.getInteger();
										}
									}
											
								}
								
							}
						}
					}
			//		Terminal.write(command);
					int gateId = searchGates(command);
					int[] result;
					if(gateId != -1) {
						
						//Terminal.write(gates.get(gateId).getName());
						result = execute(gates.get(gateId),nestedInputs);
						//Terminal.write(command + " nested inputs: " + nestedInputs[0] + "," + nestedInputs[1]);
						for(int res = 0; res < currentV.size();res++) {
							Terminal.write(command + " result =" + result[res],true);
							
							currentV.get(res).setInteger(result[res]);
						}
						
					}else {
						//Terminal.write("Gate "+command+" does not exist" );
					}
					for(Variable o: currentV) {
						if(o.getOutput()) {
							int outputID = searchVariables(o.getName(),vOutput);
							vOutput.get(outputID).setInteger(o.getInteger());
							//Terminal.write("Output has been set!");
						}else {
							int variableID = searchVariables(o.getName(),otherVariables);
							otherVariables.get(variableID).setInteger(o.getInteger());
						}
					}
					
				}
				
				
			
			}
		}
		
		
		
		
		
		int[] outputs = new int[gate.getOutputs()];
		for(int fo = 0; fo < outputs.length;fo++) {
			outputs[fo] = vOutput.get(fo).getInteger();
			Terminal.write("CoutPut: "+outputs[fo],true) ;
		}
		
		return outputs;
		
		}
		
	}
	public int searchGates(String q) {
		boolean found = false;
		int pos=-1;
		for(int i = 0; i < gates.size();i++) {
			if(gates.get(i).getName().equals(q)) {
				found = true;
				pos= i;
			}
		}
		if(found==false) {
			return -1;
		}else {
			return pos;
		}
	}
	public int searchVariables(String query, List<Variable> list) {
		int pos = -1;
		for(int i = 0; i < list.size(); i++) {
			//Terminal.write("Query: " + query + " Current: " + list.get(i).getName());
			if(list.get(i).getName().equals(query)) {
				pos=i;
			}
		}
		return pos;
		
	}
	public void createDefinition(String[] lines, int i) {
		String halfs[] = new String[2];
		halfs[0] = lines[i].substring(0, lines[i].indexOf("-"));
		halfs[1] = lines[i].substring(lines[i].indexOf("-"), lines[i].length());
		
		String name = halfs[0].substring(0, halfs[0].indexOf("("));
		String inputs = halfs[0].substring(halfs[0].indexOf("(") + 1, halfs[0].indexOf(")"));
		String input[] = inputs.split(",");
		int noOfInputs = input.length;
		
		String outputString = halfs[1].replaceAll("->", "");
		String outputs[] = outputString.split(",");
		int noOfOutputs=outputs.length;
		LogicGate tempGate = new LogicGate(name,noOfInputs,noOfOutputs);
		gates.add(tempGate);
	//	System.out.println(tempGate.getName());
	}

}
