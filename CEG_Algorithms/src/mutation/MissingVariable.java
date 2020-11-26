package mutation;

import java.util.ArrayList;
import graph.Effect;

public class MissingVariable extends VariableFault {

    public MissingVariable(int id, Effect effect) {
        super(id, effect);
    }

    @Override
    public void createMutant() {

        ArrayList<String> mutantsMisVar = new ArrayList<>();

        String exp = super.getExpression();

        String[] list = exp.split(" ");
        //System.out.println(super.getNodes().values());
        //System.out.println(super.getNodes().keySet());

        //System.out.println("exp: " + exp);

        for (String s : super.getNodes().keySet()) {

            ArrayList<Integer> index = new ArrayList<>();
            for (int i = 0; i < list.length; i++) {
                if (list[i].contains("!")) {
                    if (list[i].substring(1).equals(s)) {
                        index.add(i);
                        if (list[i + 1].equals(")")) {
                            index.add(i - 1);
                        } else {
                            index.add(i + 1);
                        }
                    }
                } else if (list[i].equals(s)) {
                    index.add(i);
                    if (list[i + 1].equals(")")) {
                        index.add(i - 1);
                    } else {
                        index.add(i + 1);
                    }
                }
            }

            String mutant = "";

            for (int i = 0; i < list.length; i++) {
                if (!index.contains(i)) {
                    mutant += list[i] + " ";
                }
            }

            mutant = mutant.substring(0, mutant.lastIndexOf(" "));

            //System.out.println("mutant: " + mutant);

            mutantsMisVar.add(mutant);

            /*for (Integer in : super.getNodes().get(s)) {
                String mutant = "";

                String firstPart = exp.substring(0, in);
                String secondPart = exp.substring(in).replaceFirst(s, "");
                mutant = firstPart + secondPart;
                mutantsMisVar.add(mutant);
            }*/
        }
        super.getMutants().addAll(mutantsMisVar);
    }
}
