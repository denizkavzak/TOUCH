package mcdc;

import java.util.HashSet;
import java.util.Set;

import graph.CEG;
import graph.CEG_Node;
import graph.Effect;
import java.util.ArrayList;
import java.util.Collections;
import test.TestInput;

/**
 * Chooses unique MC/DC Test Inputs for given effect
 *
 * @author deniz.kavzak
 */
public class UniqueEffect {

    private CEG ceg;
    private Effect effect;
    private Set<TestInput> selectedTestInputs;
    private Set<TestInput> mcdc;
    private static int counter = 0;
    
    private ArrayList<TestInput> shuffled;

    public UniqueEffect(CEG ceg, Effect effect) {
        this.ceg = ceg;
        this.effect = effect;
        selectedTestInputs = ceg.getDc().getValidAllPossibleTests(effect);       
        mcdc = new HashSet<>();
        shuffled = getShuffled();
        choseAllTests();
    }

    public Set<TestInput> getSelectedTestInputs() {
        return selectedTestInputs;
    }

    public Set<TestInput> getMcdc() {
        return mcdc;
    }

    public Effect getEffect() {
        return effect;
    }
    
    private ArrayList<TestInput> getShuffled(){
        ArrayList<TestInput> tests = new ArrayList<>(new HashSet(selectedTestInputs));
        
        Collections.shuffle(tests);
        
        return tests;
    }
     
    /**
     * Test inputs are selected for each node in the effects' expression
     */
    private void choseAllTests() {

        for (CEG_Node n : effect.getExpressionNodes()) {
            System.out.println("cause :  "+ n.getLabel());
            choseTests(n);
        }

    }

    /**
     * Chose test inputs by checking the given cause nodes' value and effects'
     * value Chosen test inputs are chosen for given cause node
     *
     * @param cause
     */
    private void choseTests(CEG_Node cause) {

        Set<TestInput> set1 = new HashSet<>();

        for (TestInput ti : shuffled){//selectedTestInputs) {
            set1.add(ti);
        }

        boolean flag = true; //eklenecek
        outerLoop:
        for (TestInput ti : shuffled){//selectedTestInputs) {
            
            //if (flag) { //eklenecek
            //    break; //eklenecek
            //} //eklenecek
            for (TestInput ti2 : set1) {

                flag = true;
                
                if (!ti.getTestInput().get(cause.getLabel()).equals(ti2.getTestInput().get(cause.getLabel()))) {                   

                    if (!ti.getCorrespondingEffectValue(effect).equals(ti2.getCorrespondingEffectValue(effect))) {

                        for (CEG_Node n : effect.getExpressionNodes()) {
                            
                            if (!n.getLabel().equals(cause.getLabel())) {

                                if (!ti.getTestInput().get(n.getLabel()).equals(ti2.getTestInput().get(n.getLabel()))) {
                                    flag = false;
                                    break;
                                }

                            }
                        }

                        if (flag) {
                            
                            mcdc.add(new TestInput(ti)); //eklenecek
                            mcdc.add(new TestInput(ti2)); //eklenecek

                            break outerLoop; //eklenecek
                        }

                    }

                }

            }
        }

    }

    
    /**
     * Fills the effect truth value in the generated test inputs
     */
    /*
    private void fillEffectValues() {

        String exp = effect.getRelation().getExpression();

        for (TestInput ti : selectedTestInputsForEffect) {

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
                    //System.out.println(ti.getEffect() + " esit misin " + effect.getLabel());
                    ti.getEffectMap().put(effect.getLabel(), ti.stringToValue((String.valueOf(be.booleanValue())).toLowerCase()));
                    ti.setEffect(effect.getLabel());
                    //System.out.println(ti.getEffect() + " esit misin2 " + effect.getLabel());
                    ti.setEffectValue(String.valueOf(be.booleanValue()));

                } catch (MalformedBooleanException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

    }*/

}
