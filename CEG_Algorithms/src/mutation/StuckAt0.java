package mutation;

import java.util.ArrayList;

import graph.Effect;

public class StuckAt0 extends VariableFault{

	public StuckAt0(int id, Effect effect) {
		super(id, effect);
	}

	@Override
	public void createMutant() {

	ArrayList<String> mutantsS0 = new ArrayList<>();
		
		String exp = super.getExpression();
		
		//System.out.println(super.getNodes().values());
		//System.out.println(super.getNodes().keySet());
		
		for(String s : super.getNodes().keySet()){
			for(Integer in : super.getNodes().get(s)){
				String mutant = "";

				String firstPart = exp.substring(0,in);
				String secondPart = exp.substring(in).replaceFirst(s,"false");
				mutant = firstPart + secondPart;
				mutantsS0.add(mutant);
			}
		}	
		super.getMutants().addAll(mutantsS0);

	}

}
