package mutation;

import java.util.ArrayList;

import graph.Effect;

public class VariableNegation extends OperatorFault{

	public VariableNegation(int id, Effect effect) {
		super(id, effect);
	}

public void createMutant(){
		
		ArrayList<String> mutantsVarNeg = new ArrayList<>();
		
		String exp = super.getExpression();

		for(String s : super.getNodes().keySet()){
			for(Integer in : super.getNodes().get(s)){
				String mutant = "";
				if(exp.charAt(in-1)!='!'){
					mutant = exp.substring(0,in) + "!" + exp.substring(in);
				}else{
					mutant = exp.substring(0,in-1) + exp.substring(in);
				}
				mutantsVarNeg.add(mutant);
			}
		}
		
		super.getMutants().addAll(mutantsVarNeg);
	}
	
}
