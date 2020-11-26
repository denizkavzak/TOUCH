package mutation;

import java.util.ArrayList;

import graph.Effect;

public class OperatorReference extends OperatorFault {

    public OperatorReference(int id, Effect effect) {
        super(id, effect);

    }

    @Override
    public void createMutant() {
        ArrayList<String> mutants = new ArrayList<>();
        if (!super.getAndCount().isEmpty()) {
            mutants.addAll(createMutantAnd());
        }
        if (!super.getOrCount().isEmpty()) {
            mutants.addAll(createMutantOr());
        }
        super.getMutants().addAll(mutants);
    }

    public ArrayList<String> createMutantAnd() {

        ArrayList<String> mutantsAnd = new ArrayList<>();

        if (!super.getAndCount().isEmpty()) {
            for (int i = 0; i < super.getAndCount().size(); i++) {
                String exp = super.getExpression();
                String firstPart = exp.substring(0, super.getAndCount().get(i));
                String secondPart = exp.substring(super.getAndCount().get(i));
                secondPart = secondPart.replaceFirst("\\&\\&", "||");
                mutantsAnd.add(firstPart + secondPart);
            }

            return mutantsAnd;
        } else {
            return null;
        }
    }

    public ArrayList<String> createMutantOr() {
        ArrayList<String> mutantsOr = new ArrayList<>();
        if (!super.getOrCount().isEmpty()) {
            for (int i = 0; i < super.getOrCount().size(); i++) {
                String exp = super.getExpression();
                String firstPart = exp.substring(0, super.getOrCount().get(i));
                String secondPart = exp.substring(super.getOrCount().get(i));
                secondPart = secondPart.replaceFirst("\\|\\|", "&&");
                mutantsOr.add(firstPart + secondPart);
            }
            return mutantsOr;
        } else {
            return null;
        }
    }

    /*
	public ArrayList<String> createMutantNot(){
		
		ArrayList<String> mutantsNot = new ArrayList<>();
		
		if(!super.getNotCount().isEmpty()){
			for(int i=0; i<super.getNotCount().size(); i++){
				String exp = super.getExpression();
				String firstPart = exp.substring(0, super.getNotCount().get(i));
				String secondPart = exp.substring(super.getNotCount().get(i));
				secondPart = secondPart.replaceFirst("\\!", "");
				mutantsNot.add(firstPart+secondPart);
			}
			return mutantsNot;
		}else{
			return null;
		}
	}
     */
}
