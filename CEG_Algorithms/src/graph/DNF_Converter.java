package graph;

import com.bpodgursky.jbool_expressions.Expression;
import com.bpodgursky.jbool_expressions.parsers.ExprParser;
import com.bpodgursky.jbool_expressions.rules.RuleSet;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jboolexpr.BooleanExpression;
import jboolexpr.MalformedBooleanException;
import test.TestInput;
import test.Value;

public class DNF_Converter {

    private CEG ceg;
    private ArrayList<Relation> constraintRelations;
    private Set<Set<CEG_Node>> selectedSet;
    private Set<TestInput> selectedTestInputs;
    private Set<TestInput> allPossibleTestInputs;

    public DNF_Converter(CEG ceg) {
        this.ceg = ceg;
        constraintRelations = new ArrayList<>();
        selectedTestInputs = new HashSet<>();
        allPossibleTestInputs = new HashSet<>();
        setConstraintRelations();
        setDNFs();
        setDNFexpressions();
        ceg.reformDNF();
        setMinDNFs();
        //setAllPossibleTestInputs();
    }
    
    public void setAllPossibleTestInputs() {
    	// uncomment selectValid function to consider the constraints
        ArrayList<String> subsetBinaryStrings = subsetAlgorithm(ceg.getCauseNodes().size());//selectValid(subsetAlgorithm(effect.getExpressionNodes().size()),effect);
        int id = 0;
        for (String s : subsetBinaryStrings) {

            TestInput ti = new TestInput(id++);

            for (int i = 0; i < s.toCharArray().length; i++) {
                if (s.charAt(i) == '0') {
                    ti.addTestInput(ceg.getCauseNodes().get(i).getLabel(), Value.False);
                } else {
                    ti.addTestInput(ceg.getCauseNodes().get(i).getLabel(), Value.True);
                }
            }

            allPossibleTestInputs.add(ti);
        }
    }

    public Set<TestInput> getAllPossibleTestInputs() {
        return allPossibleTestInputs;
    }
    
    public Set<TestInput> getAllPossibleValidTestInputs() {
        
        Set<TestInput> allValid = new HashSet<>();
        for(Effect effect: ceg.getEffectNodes()){        
            allValid.addAll(getValidAllPossibleTests(effect));             
        }
        
        return allValid;
    }

    /**
     * Fills all the effect truth value in the all possible generated test
     * inputs
     */
    /*
    private void fillAllEffectValuesInAllPossibleTestInputs() {

        for (Effect effect : ceg.getEffectNodes()) {
            String exp = effect.getRelation().getExpression();

            for (TestInput ti : allPossibleTestInputs) {

                String modifiedExp = exp;
                for (String s : ti.getTestInput().keySet()) {
                    String key = s + " ";
                    modifiedExp = modifiedExp.replace(key, ti.getTestInput().get(s).toString().toLowerCase());
                }

                while (modifiedExp.contains("C")) {
                    String s = modifiedExp.substring(modifiedExp.indexOf("C"), modifiedExp.indexOf(" ", modifiedExp.indexOf("C")));
                    String key = s + " ";
                    modifiedExp = modifiedExp.replace(key, "false");
                }

                if (!modifiedExp.contains("C")) {
                    try {
                        BooleanExpression be = BooleanExpression.readLeftToRight(modifiedExp);
                        
                        ti.addEffectToMap(effect.getLabel(), ti.stringToValue((String.valueOf(be.booleanValue())).toLowerCase()));

                        
                        ti.setEffectValue(String.valueOf(be.booleanValue()));

                    } catch (MalformedBooleanException e) {
                        
                        e.printStackTrace();
                    }
                }
            }
        }
    }*/
    public Set<TestInput> getSelectedTestInputs() {
        return selectedTestInputs;
    }

    public CEG getCEG() {
        return ceg;
    }

    public Set<Set<CEG_Node>> getSelectedSet() {
        return selectedSet;
    }

    /**
     * Creates all possible truth values for nodes (truth table values)
     *
     * @param originalSet
     * @return
     */
    public Set<Set<CEG_Node>> powerSet(Set<CEG_Node> originalSet) {
        Set<Set<CEG_Node>> sets = new HashSet<Set<CEG_Node>>();
        if (originalSet.isEmpty()) {
            sets.add(new HashSet<CEG_Node>());
            return sets;
        }
        List<CEG_Node> list = new ArrayList<CEG_Node>(originalSet);
        CEG_Node head = list.get(0);
        Set<CEG_Node> rest = new HashSet<CEG_Node>(list.subList(1, list.size()));
        for (Set<CEG_Node> set : powerSet(rest)) {
            Set<CEG_Node> newSet = new HashSet<CEG_Node>();
            newSet.add(head);
            newSet.addAll(set);

            sets.add(newSet);
            sets.add(set);

        }
        return sets;
    }

    public ArrayList<String> subsetAlgorithm(int parentSize) {
        int rows = (int) Math.pow(2, parentSize);

        ArrayList<String> bool = new ArrayList<>(rows);

        for (int i = 0; i < rows; i++) {
            String str = "";
            for (int j = parentSize - 1; j >= 0; j--) {
                str = (i / (int) Math.pow(2, j)) % 2 + str;
            }
            bool.add(str);
        }

        return bool;
    }

    /**
     * Checks whether given node exists in given set or not.
     *
     * @param set
     * @param node
     * @return
     */
    private boolean contain(Set<CEG_Node> set, CEG_Node node) {

        for (CEG_Node n : set) {
            if (n.getLabel().equals(node.getLabel())) {
                return true;
            }
        }

        return false;
    }

    /**
     * Gets the relations containing constraint
     */
    private void setConstraintRelations() {

        for (Relation r : ceg.getRelations()) {
            if (r.getConstraint() != null) {
                constraintRelations.add(r);

            }
        }
    }

    /**
     * Checks given input possibility to see if it violates any constraint or
     * not
     */
    private boolean isValid(Set<CEG_Node> candidateSet) {

        boolean flag = true;

        if (!constraintRelations.isEmpty()) {

            for (Relation r : constraintRelations) {
                if (r.getConstraint().getConstraintType().equals("E")) {

                    ArrayList<CEG_Node> c = r.getRelatedNodes();

                    int count = 0;

                    for (int i = 0; i < c.size(); i++) {
                        if (contain(candidateSet, c.get(i))) {
                            count++;
                        }
                    }

                    if (c.size() % 2 == 0) {
                        if (count != 1) {
                            flag = false;
                        }
                    } else if (c.size() % 2 != 0) {
                        if (count > 1 && count < c.size()) {
                            flag = false;
                        }
                        if (count == 0) {
                            flag = false;
                        }
                    }

                } else if (r.getConstraint().getConstraintType().equals("R")) {
                    CEG_Node n = r.getRelatedNodes().get(0);
                    if (contain(candidateSet, n)) {
                        boolean tmp = true;
                        for (CEG_Node cn : candidateSet) {
                            if (cn.getLabel().equals(r.getConstraint().getResultNodeLabel())) {
                                tmp = false;
                            }
                        }

                        if (tmp) {
                            flag = false;
                        }
                    }
                } else if (r.getConstraint().getConstraintType().equals("O")) {

                    ArrayList<CEG_Node> c = r.getRelatedNodes();

                    int count = 0;

                    for (int i = 0; i < c.size(); i++) {
                        if (contain(candidateSet, c.get(i))) {
                            count++;
                        }
                    }

                    if (count != 1) {
                        flag = false;
                    }

                } else if (r.getConstraint().getConstraintType().equals("I")) {

                    ArrayList<CEG_Node> c = r.getRelatedNodes();

                    int count = 0;

                    for (int i = 0; i < c.size(); i++) {
                        if (contain(candidateSet, c.get(i))) {
                            count++;
                        }
                    }

                    if (count == 0) {
                        flag = false;
                    }
                }
            }
        }

        for (Constraint con : ceg.getConsNodes()) {

            if (con.getConstraintType().equals("M")) {

                String[] c = con.getResultNodeLabel().split("\\-");

                Effect e1 = ceg.getEffectNode(c[0]);
                Effect e2 = ceg.getEffectNode(c[1]);

                String exp1 = e1.getRelation().getExpression();
                String exp2 = e2.getRelation().getExpression();

                for (CEG_Node nd : candidateSet) {
                    while (exp1.contains(nd.getLabel() + " ")) {
                        String check = nd.getLabel() + " ";
                        exp1 = exp1.replace(check, "true ");
                    }

                    while (exp2.contains(nd.getLabel() + " ")) {
                        String check = nd.getLabel() + " ";
                        exp2 = exp2.replace(check, "true ");
                    }
                }

                while (exp1.contains("C")) {
                    int st = exp1.indexOf("C");
                    int e = exp1.indexOf(" ", st);
                    exp1 = exp1.replaceFirst(exp1.substring(st, e), "false");
                }

                while (exp2.contains("C")) {
                    int st = exp2.indexOf("C");
                    int e = exp2.indexOf(" ", st);
                    exp2 = exp2.replaceFirst(exp2.substring(st, e), "false");
                }
                try {
                    BooleanExpression be1 = BooleanExpression.readLeftToRight(exp1);
                    BooleanExpression be2 = BooleanExpression.readLeftToRight(exp2);

                    if (be1.booleanValue()) {
                        if (be2.booleanValue()) {
                            flag = false;
                        }
                    }
                } catch (MalformedBooleanException e) {

                    e.printStackTrace();
                }

            }
        }

        return flag;
    }

    /**
     * Checks all possibilities for their validity on constraints and returns
     * the chosen ones
     *
     * @param sets
     * @return
     */
    public Set<Set<CEG_Node>> selectValid(Set<Set<CEG_Node>> sets) {

        Set<Set<CEG_Node>> selectedSet = new HashSet<Set<CEG_Node>>();

        for (Set<CEG_Node> set : sets) {
            if (isValid(set)) {
                selectedSet.add(set);
            }
        }
        return selectedSet;
    }

    /**
     * Checks all possibilities for their validity on constraints and returns
     * the chosen ones
     *
     * @param binarySets
     * @param effect
     * @return
     */
    private ArrayList<String> selectValid(ArrayList<String> binarySets, Effect effect) {

        ArrayList<String> selectedSet = new ArrayList<>();

        for (String s : binarySets) {
            Set<CEG_Node> set = new HashSet<>();
            for (int i = 0; i < s.toCharArray().length; i++) {
                if (s.charAt(i) == '1') {
                    set.add(effect.getExpressionNodes().get(i));
                }
            }
            if (isValid(set)) {
                selectedSet.add(s);
            }
        }

        return selectedSet;
    }

    /**
     * Uses the jboolexpressions tool to convert Boolean expressions of effects
     * into DNF.
     */
    private void setMinDNFs() {

        for (Effect effect : ceg.getEffectNodes()) {

            String s = effect.getRelation().getExpression();
            s = s.replaceAll("\\&\\&", "\\&");
            s = s.replaceAll("\\|\\|", "\\|");

            Expression<String> nonStandard = ExprParser.parse(s);

            Expression<String> sopForm;
            sopForm = RuleSet.toDNF(nonStandard);

            effect.setMinDNF(sopForm.toString());
        }
    }

    /**
     * Sets the DNF of effects in the selected row form
     */
    private void setDNFs() {
        for (Effect effect : ceg.getEffectNodes()) {
            effect.setDNFasInputs(selectDNF(effect));
        }
    }

    /**
     * Sets the DNF of effects in the string expression
     */
    private void setDNFexpressions() {
        for (Effect effect : ceg.getEffectNodes()) {
            effect.setDNFasExp(ceg);
        }
    }

    /**
     * Get all possible test variations from the truth table by checking the
     * constraints
     *
     * @param effect
     * @return
     */
    public Set<TestInput> getValidAllPossibleTests(Effect effect) {

        ArrayList<String> subsetBinaryStrings = selectValid(subsetAlgorithm(effect.getExpressionNodes().size()),effect); //subsetAlgorithm(effect.getExpressionNodes().size());
        Set<TestInput> selectedAllTests = new HashSet<>();
        int id = 0;
        for (String s : subsetBinaryStrings) {

            TestInput ti = new TestInput(id++);

            for (int i = 0; i < s.toCharArray().length; i++) {
                if (s.charAt(i) == '0') {
                    ti.addTestInput(effect.getExpressionNodes().get(i).getLabel(), Value.False);
                } else {
                    ti.addTestInput(effect.getExpressionNodes().get(i).getLabel(), Value.True);
                }
            }

            selectedAllTests.add(ti);
        }

        return selectedAllTests;
    }

    public Set<Set<CEG_Node>> convertToSet(ArrayList<String> subsetBinaryStrings, Effect effect) {
        Set<Set<CEG_Node>> selected = new HashSet<>();

        for (String s : subsetBinaryStrings) {

            Set<CEG_Node> set = new HashSet<>();

            for (int i = 0; i < s.toCharArray().length; i++) {
                if (s.charAt(i) == '1') {
                    set.add(effect.getExpressionNodes().get(i));
                }
            }
            selected.add(set);
        }

        return selected;
    }

    public Set<Set<CEG_Node>> convertToSetFromTestInput(Set<TestInput> testInputs, Effect effect) {
        Set<Set<CEG_Node>> selected = new HashSet<>();

        for (TestInput ti : testInputs) {

            Set<CEG_Node> set = new HashSet<>();

            for (String s : ti.getTestInput().keySet()) {
                if (ti.getTestInput().get(s) == Value.True) {
                    for (CEG_Node n : effect.getExpressionNodes()) {
                        if (n.getLabel().equals(s)) {
                            set.add(n);
                        }
                    }
                }
            }
            selected.add(set);
        }
        return selected;
    }

    /**
     * Converts the Boolean expression of given effect into DNF by using the
     * truth table conversion algorithm. Selected rows are returned
     *
     * @param effect
     * @return
     */
    public Set<TestInput> selectDNF(Effect effect) {

        Set<TestInput> tests = getValidAllPossibleTests(effect);

        String exp = effect.getRelation().getExpression();
        String modifiedExp;
        for (TestInput ti : tests) {

            modifiedExp = exp;

            for (String nd : ti.getTestInput().keySet()) {
                while (modifiedExp.contains(nd + " ")) {
                    String check = nd + " ";
                    modifiedExp = modifiedExp.replace(check, ti.valueToString(ti.getTestInput().get(nd)) + " ");
                }
            }

            try {
                BooleanExpression be = BooleanExpression.readLeftToRight(modifiedExp);

                if (be.booleanValue() == true) {
                    ti.addEffectToMap(effect.getLabel(), Value.True);
                } else {
                    ti.addEffectToMap(effect.getLabel(), Value.False);
                }

                if (be.booleanValue()) {

                    selectedTestInputs.add(ti);
                }
            } catch (MalformedBooleanException e) {

                e.printStackTrace();
            }

        }
        return tests;

    }

    /**
     * Converts a chosen possibility into the Test Input form
     */
    /*
    private void convertSetToTestInput() {

        int id = 0;

        for (Set<CEG_Node> set : selectedSet) {

            TestInput ti = new TestInput(id++);

            for (CEG_Node n : set) {
                ti.addTestInput(n.getLabel(), Value.True);
            }

            selectedTestInputs.add(ti);
        }

    }*/
}
