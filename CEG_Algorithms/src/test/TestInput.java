package test;

import java.util.HashMap;

import graph.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import jboolexpr.BooleanExpression;
import jboolexpr.MalformedBooleanException;
import mutation.MutantDetail;
import org.openide.util.Exceptions;

public class TestInput {

    private int id;
    private HashMap<String, Value> testInput;
    private String effect;
    private Value effectValue;
    private HashMap<String, Value> effectMap;
    private HashMap<String, Value> interMap;
    private CEG ceg;
    private ArrayList<MutantDetail> killedMutants;
    public boolean killer = false;
    private String binaryTest;

    public TestInput(int id, CEG ceg) {
        this.id = id;
        testInput = new HashMap<>();
        effectValue = Value.NA;
        effect = null;
        effectMap = new HashMap<>();
        interMap = new HashMap<>();
        this.ceg = ceg;
        killedMutants = new ArrayList<>();
        //createMap(ceg);
    }

    public TestInput(TestInput testInput) {
        this.id = testInput.getId();
        this.effect = testInput.getEffect();
        this.effectValue = testInput.getEffectValue();
        this.testInput = testInput.getTestInput();
        this.effectMap = testInput.getEffectMap();
        this.interMap = testInput.getInterMap();
    }

    public TestInput(int id) {
        this.id = id;
        testInput = new HashMap<>();
        effectValue = Value.NA;
        effect = null;
        effectMap = new HashMap<>();
        interMap = new HashMap<>();
        //createMap(ceg);
    }
    
    public String getBinaryTest(){        
        String binary = "";
        int numOfCause = ceg.getCauseNodes().size();
        
        for(int i=0;i<numOfCause;i++){
            String c = "C"+(i+1);
            Value v = testInput.get(c);
            if(v==Value.True){
                binary = binary + "1";
            }else{
                binary = binary + "0";
            }
        }        
        binaryTest = binary;
        return binary;
    }

    public int getId() {
        return id;
    }

    public HashMap<String, Value> getEffectMap() {
        return effectMap;
    }

    public HashMap<String, Value> getInterMap() {
        return interMap;
    }

    public void addEffectToMap(String e, Value v) {
        effectMap.put(e, v);
    }

    public void addInterToMap(String e, Value v) {
        interMap.put(e, v);
    }

    public Value getEffectValue() {
        return effectValue;
    }

    public void addKilledMutant(MutantDetail mutant) {
        if (killedMutants == null) {
            killedMutants = new ArrayList<>();
        }
        killedMutants.add(mutant);
    }

    public ArrayList<MutantDetail> getKilledMutants() {
        return killedMutants;
    }

    public void setEffectValue(String effectValue) {

        if (effectValue.equals("true")) {
            this.effectValue = Value.True;
        } else if (effectValue.equals("false")) {
            this.effectValue = Value.False;
        } else {
            this.effectValue = Value.NA;
        }
    }

    public void setResultValue(Value value) {
        this.effectValue = value;
    }

    public HashMap<String, Value> getTestInput() {
        return testInput;
    }

    public void addTestInput(String node, Value value) {
        testInput.put(node, value);
    }

    public void changeValue(String node, Value value) {
        testInput.put(node, value);
    }

    public void createMap(CEG ceg) {
        for (CEG_Node node : ceg.getCauseNodes()) {
            testInput.put(node.getLabel(), Value.NA);
        }

    }

    public boolean containSame(TestInput ti) {

        if (testInput.keySet().containsAll(ti.getTestInput().keySet())) {
            for (String s : testInput.keySet()) {
                if (ti.getTestInput().get(s) != null) {
                    if (testInput.get(s) != ti.getTestInput().get(s)) {
                        return false;
                    }
                }
            }
            return true;
        }

        return false;
    }

    //Considers TestInputs contain all possible cause keys.
    public boolean isSameExceptKey(TestInput ti, String str) {

        if (testInput.get(str).equals(ti.getTestInput().get(str))) {
            return false;
        }

        for (String s : testInput.keySet()) {
            if (!s.equals(str)) {
                if (!testInput.get(s).equals(ti.getTestInput().get(s))) {
                    return false;
                }
            }
        }

        return true;

    }

    public boolean isSame(TestInput ti) {

        if (testInput.keySet().size() == ti.getTestInput().size() && testInput.keySet().containsAll(ti.getTestInput().keySet())) {
            for (String s : testInput.keySet()) {
                if (testInput.get(s) != ti.getTestInput().get(s)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public String toString() {

        String st = "";

        for (String s : testInput.keySet()) {
            st += " (" + s + ",";
            st += testInput.get(s) + ")";
        }
        if (effectValue != Value.NA) {
            st += " (" + effect + "," + effectValue + ")";
        }

        if (!effectMap.isEmpty()) {
            for (String s : effectMap.keySet()) {
                if (effect != null && s != null) {
                    if (!s.equals(effect)) {
                        st += " (" + s + "," + effectMap.get(s) + ")";
                    }
                }
            }
        }

        return st;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + id;
        result = prime * result + ((testInput == null) ? 0 : testInput.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        TestInput other = (TestInput) obj;

        if (testInput == null) {
            if (other.testInput != null) {
                return false;
            }
        } else {
            if (testInput.keySet().size() != other.getTestInput().keySet().size()) {
                return false;
            }
            for (String s : testInput.keySet()) {
                if (testInput.get(s) != other.getTestInput().get(s)) {
                    return false;
                }
            }
        }
        return true;
    }

    public String getEffect() {
        return effect;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }

    public void setResult(String effect, Value value) {
        setEffect(effect);
        setResultValue(value);
    }

    public String valueToString(Value value) {
        if (value == Value.True) {
            return "true";
        } else if (value == Value.False) {
            return "false";
        } else {
            return "N/A";
        }
    }

    public Value stringToValue(String s) {
        if (s.equalsIgnoreCase("true")) {
            return Value.True;
        } else if (s.equalsIgnoreCase("false")) {
            return Value.False;
        } else {
            return Value.NA;
        }
    }

    public Value binaryToValue(char s) {
        if (s == '1') {
            return Value.True;
        } else if (s == '0') {
            return Value.False;
        } else {
            return Value.NA;
        }
    }

    public void fillWithBits(int input, CEG ceg, Effect effect) {
        String s = Integer.toBinaryString(input);

        String copyBinaryTest = s;

        if (s.toCharArray().length != effect.getExpressionNodes().size()) {

            int f = effect.getExpressionNodes().size() - s.toCharArray().length;

            for (int e = 0; e < f; e++) {
                copyBinaryTest = "0" + copyBinaryTest;
            }
        }

        char[] sToAr = copyBinaryTest.toCharArray();

        for (int i = sToAr.length - 1; i >= 0; i--) {
            Value val = Value.False;
            if (sToAr[i] == '1') {
                val = Value.True;
            }
            this.addTestInput(effect.getExpressionNodes().get(sToAr.length - i - 1).getLabel(), val);

        }

        //fillEffectValues(ceg);
        //fillTestInputs(ceg);
    }

    /**
     * Fills the effect truth value in the generated test inputs
     */
    /*
    private void fillEffectValues(CEG ceg) {

        for (Effect e : ceg.getEffectNodes()) {
            String exp = e.getRelation().getExpression();

            String modifiedExp = exp;

            for (String s : this.getTestInput().keySet()) {
                if (s.startsWith("C")) {
                    String key = s + " ";
                    modifiedExp = modifiedExp.replace(key, this.getTestInput().get(s).toString().toLowerCase());
                }
            }

            if (!modifiedExp.contains("C")) {
                try {
                    BooleanExpression be = BooleanExpression.readLeftToRight(modifiedExp);

                    if (e.getLabel().equals(effect)) {
                        this.setEffectValue(String.valueOf(be.booleanValue()));
                        this.setEffect(e.getLabel());
                    } else {
                        this.addEffectToMap(e.getLabel(), stringToValue(String.valueOf(be.booleanValue())));
                    }

                } catch (MalformedBooleanException ex) {
                    // TODO Auto-generated catch block
                    ex.printStackTrace();
                }
            }

        }

    }*/
    /**
     * Generates all possible test inputs by using selected test inputs in CEG
     */
    /*
    private void fillTestInputs(CEG ceg) {

        Set<TestInput> tis = new HashSet<>();

        for (Intermediate i : ceg.getInterNodes()) {
            if (ceg.getEffectNode(effect).getRelation().getRelatedNodes().contains(i)) {
                Relation r = ceg.getInterMap().get(i.getLabel()).get(0);
                String exp = r.getExpression();

                for (String s : this.getTestInput().keySet()) {
                    String check = s + " ";
                    if (exp.contains(check)) {
                        exp = exp.replace(check, String.valueOf(this.getTestInput().get(s)).toLowerCase());
                    }
                }

                if (!exp.contains("C")) {
                    exp = exp.replaceAll("AND", "&&");

                    exp = exp.replaceAll("OR", "||");
                    exp = exp.replaceAll("~", "!");

                    try {
                        BooleanExpression be = BooleanExpression.readLeftToRight(exp);
                        Value val;

                        if (be.booleanValue()) {
                            val = Value.True;
                        } else if (be.booleanValue() == false) {
                            val = Value.False;
                        } else {
                            val = Value.NA;
                        }

                        this.addInterToMap(i.getLabel(), val);                       

                    } catch (MalformedBooleanException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                }
            }

        }

    }*/
    /**
     *
     * @param effect
     * @return
     */
    public Value getCorrespondingEffectValue(Effect e) {
        Value v = Value.False;

        String exp = e.getRelation().getExpression();

        String modifiedExp = exp;

        for (CEG_Node n : e.getExpressionNodes()) {

            String key = n.getLabel() + " ";
            modifiedExp = modifiedExp.replace(key, this.getTestInput().get(n.getLabel()).toString().toLowerCase());

        }

        while (modifiedExp.contains("C")) {
            int beg = modifiedExp.indexOf("C");
            int end = modifiedExp.indexOf(" ", beg);
            String check = modifiedExp.substring(beg, end);

            modifiedExp = modifiedExp.replace(check, "false");
        }

        if (!modifiedExp.contains("C")) {

            try {
                BooleanExpression be = BooleanExpression.readLeftToRight(modifiedExp);

                boolean res = be.booleanValue();

                if (res) {
                    v = Value.True;

                }
            } catch (MalformedBooleanException ex) {

                ex.printStackTrace();
            }
        }

        return v;
    }

}
