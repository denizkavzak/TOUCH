/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package random;

import graph.CEG;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import test.TestInput;
import test.TestSet;
import test.Value;

/**
 *
 * @author deniz.kavzak
 */
public class RandomTest {

    private CEG ceg;
    private TestSet tests;
    private Set<TestInput> selectedTestInputs;
    private static int MAX;
    private static int MIN = 100;

    public RandomTest(CEG ceg) {
        this.ceg = ceg;
        //tests = new TestSet();
        ceg.getDc().setAllPossibleTestInputs();
        selectedTestInputs = ceg.getDc().getAllPossibleTestInputs();
        MAX = selectedTestInputs.size();
        tests = new TestSet(selectedTestInputs);
        //generateAllTests();
    }

    public TestSet getTests(){
        return tests;
    }
    
    private void generateAllTests() {

        ArrayList<TestInput> copySelectedTestInputs = new ArrayList<>();
        copySelectedTestInputs.addAll(selectedTestInputs);
        System.out.println("size of all tests: " + selectedTestInputs.size());
        Set<TestInput> generatedSet = new HashSet<>();

        Random rn = new Random();
        int lim = 1;//rn.nextInt(MAX - MIN + 1) + MIN;
        System.out.println("lim: " + lim);
        int next = 0;
        int count = 0;

        while (count < lim) {
            boolean flag = false;

            do {
                next = rn.nextInt(copySelectedTestInputs.size());
                if (!generatedSet.contains(copySelectedTestInputs.get(next))) {
                    TestInput t = copySelectedTestInputs.get(next);
                    for(int i=0; i<ceg.getCauseNodes().size(); i++){
                        if(!t.getTestInput().keySet().contains(ceg.getCauseNodes().get(i).getLabel()))
                            t.addTestInput(ceg.getCauseNodes().get(i).getLabel(), Value.False);
                    }
                    generatedSet.add(copySelectedTestInputs.get(next));
                    flag = true;
                }
            } while (!flag);

            count++;
        }

        System.out.println("size" + generatedSet.size());
        
        for (TestInput ti : generatedSet) {
                tests.addTestInput(ti);
            }

    }

}
