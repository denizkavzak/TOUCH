package mutation;

import java.util.ArrayList;

import graph.Effect;

public class StuckAt1 extends VariableFault{

	public StuckAt1(int id, Effect effect) {
		super(id, effect);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void createMutant() {

	ArrayList<String> mutantsS1 = new ArrayList<>();
		
		String exp = super.getExpression();
		
		//System.out.println(super.getNodes().values());
		//System.out.println(super.getNodes().keySet());
		
		for(String s : super.getNodes().keySet()){
			for(Integer in : super.getNodes().get(s)){
				String mutant = "";

				String firstPart = exp.substring(0,in);
				String secondPart = exp.substring(in).replaceFirst(s,"true");
				mutant = firstPart + secondPart;
				mutantsS1.add(mutant);
			}
		}	
		super.getMutants().addAll(mutantsS1);

	}

}
