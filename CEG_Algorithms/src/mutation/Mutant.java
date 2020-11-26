package mutation;

import java.util.ArrayList;
import java.util.HashMap;

import graph.Effect;
import jboolexpr.BooleanExpression;
import jboolexpr.MalformedBooleanException;
import org.openide.util.Exceptions;
import test.TestInput;
import test.Value;

public abstract class Mutant {

    private int id;
    private Effect effect;
    private String expression;
    private HashMap<String, ArrayList<Integer>> nodes = new HashMap<>();
    private ArrayList<String> mutants;

    public Mutant(int id, Effect effect) {
        super();
        this.id = id;
        this.effect = effect;
        mutants = new ArrayList<>();
        expression = effect.getRelation().getExpression();
        fillNodes();
    }

    public abstract void createMutant();

    public ArrayList<String> getMutants() {
        return mutants;
    }

    public int getId() {
        return id;
    }

    public Effect getEffect() {
        return effect;
    }

    public String getExpression() {
        return expression;
    }

    public HashMap<String, ArrayList<Integer>> getNodes() {
        return nodes;
    }

    private void fillNodes() {

        String modifiedExp = expression;

        while (modifiedExp.contains("C")) {
            int st = modifiedExp.indexOf("C");
            int e = modifiedExp.indexOf(" ", st);
            String key = modifiedExp.substring(st, e);
            if (nodes.containsKey(key)) {
                ArrayList<Integer> list = nodes.get(key);
                list.add(modifiedExp.indexOf(key));
                nodes.put(key, list);
            } else {
                ArrayList<Integer> list = new ArrayList<>();
                list.add(modifiedExp.indexOf(key));
                nodes.put(key, list);
            }

            String rep = "";
            for (int i = 0; i < key.length(); i++) {
                rep = rep + "1";
            }
            modifiedExp = modifiedExp.replaceFirst(key, rep);
        }
    }
    
    public int evaluateMutant(String mut, TestInput ti){
        String modifiedMut = mut;
        for(String s: ti.getTestInput().keySet()){
            if(mut.contains(s+" ")){
                modifiedMut = modifiedMut.replace(s+" ", ti.valueToString(ti.getTestInput().get(s)));
            }
        }
        try {
            BooleanExpression be = BooleanExpression.readLeftToRight(modifiedMut);
            
            if(be.booleanValue()==true)
                return 1;
            else
                return 0;      
            
        } catch (MalformedBooleanException ex) {
            Exceptions.printStackTrace(ex);
        }
        return 0;
    }
    
    public int m(String mut, TestInput ti){

        String modifiedMutant = mut;

            for (String s : ti.getTestInput().keySet()) {
                while (modifiedMutant.contains(s + " ")) {
                    String check = s + " ";
                    modifiedMutant = modifiedMutant.replace(check, ti.getTestInput().get(s).toString().toLowerCase() + " ");
                }
            }
            
            while (modifiedMutant.contains("C")) {
                int beg = modifiedMutant.indexOf("C");
                int end = modifiedMutant.indexOf(" ", beg);
                String check = modifiedMutant.substring(beg, end);

                modifiedMutant = modifiedMutant.replace(check, "false");
            }
            
            if (!modifiedMutant.contains("C")) {
                try {
                    BooleanExpression be2 = BooleanExpression.readLeftToRight(modifiedMutant);

                    if (be2.booleanValue()) {
                        return 1;
                    }else{
                        return 0;
                    }

                } catch (MalformedBooleanException ex) {
                    Exceptions.printStackTrace(ex);
                }

            }
        

        return 0;
    }
}
