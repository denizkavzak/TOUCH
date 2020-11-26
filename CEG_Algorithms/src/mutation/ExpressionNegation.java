package mutation;

import java.util.ArrayList;

import graph.Effect;

public class ExpressionNegation extends OperatorFault{

	public ExpressionNegation(int id, Effect effect) {
		super(id, effect);
	}
	
	
	public void createMutant(){
		
		ArrayList<String> mutantsExpNeg = new ArrayList<>();
		
		String exp = super.getExpression();
		
		int i=1; //don't take the first, since it surrounds all expression in all effects.
		
		while(i<exp.length()){
			
			if(exp.substring(i).contains("(")){
				String mutant ="";
				
				if(exp.charAt(i + exp.substring(i).indexOf('(')-1) != '!'){
					mutant = mutant + exp.substring(0, i + exp.substring(i).indexOf('(')) + "!" + exp.substring(i + exp.substring(i).indexOf('('));
				}else{
					mutant = mutant + exp.substring(0, i + exp.substring(i).indexOf('(')-1) + exp.substring(i + exp.substring(i).indexOf('('));
				}
				mutantsExpNeg.add(mutant);
			}else{
				break;
			}
			i = i + exp.substring(i).indexOf('(') + 1;
		}
		
		super.getMutants().addAll(mutantsExpNeg);
	}

}
