package mutation;

import java.util.ArrayList;
import java.util.Random;

import graph.Effect;

public class ClauseDisjunction extends VariableFault{

	public ClauseDisjunction(int id, Effect effect) {
		super(id, effect);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createMutant() {
		
		ArrayList<String> mutantsClaDis = new ArrayList<>();
		
		String exp = super.getExpression();

		for(String s : super.getNodes().keySet()){
			for(Integer in : super.getNodes().get(s)){
				String mutant = "";
				
				Object[] ar = super.getNodes().keySet().toArray();
				Random r = new Random();
				String c = ar[r.nextInt(ar.length)].toString();
				while(c.equals(s)){
					c = ar[r.nextInt(ar.length)].toString();
				}
				String firstPart = exp.substring(0,in);
				String secondPart = exp.substring(in).replaceFirst(s,(" ( " + c + " || " + s + " ) "));
				mutant = firstPart + secondPart;
				mutantsClaDis.add(mutant);
			}
		}	
		super.getMutants().addAll(mutantsClaDis);
	}

}
