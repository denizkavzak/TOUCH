package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.openide.util.Exceptions;

public class TestSet {

    ArrayList<TestInput> testInputs;
    Set<TestInput> eliminatedTestSet = new HashSet<>();

    public TestSet() {
        testInputs = new ArrayList<>();
        eliminatedTestSet = new HashSet<>();
        eliminatedTestInputs();
    }

    public Set<TestInput> getTestSet() {

        //System.out.println(testInputs.size());
        Set<TestInput> ts = testInputs.stream().collect(Collectors.toSet());
        Set<TestInput> tsfinal = new HashSet<>();

        //System.out.println(ts.size());
        for (TestInput t : ts) {
            tsfinal.add(t);
            //  System.out.println(t);
        }

        for (TestInput ti : ts) {
            for (TestInput ti2 : ts) {
                if (!ti.equals(ti2)) {
                    if (ti.containSame(ti2)) {
                        //System.out.println("ti " + ti);
                        //System.out.println("ti2 " + ti2);
                        ti.getEffectMap().put(ti2.getEffect(), ti2.getEffectValue());
                        if (!ti2.getEffectMap().isEmpty()) {
                            ti.getEffectMap().putAll(ti2.getEffectMap());
                        }
                        if (ti.getEffectMap().containsKey(ti.getEffect())) {
                            ti.getEffectMap().remove(ti.getEffect());
                        }
                        tsfinal.remove(ti2);
                    }
                }
            }
        }
//System.out.println("size " + ts.size());
        //      System.out.println("size " + tsfinal.size());
        return tsfinal;
    }

    public TestSet(ArrayList<TestInput> testSet) {
        this.testInputs.addAll(testSet);
        eliminatedTestSet = new HashSet<>();
        eliminatedTestInputs();
    }

    public TestSet(Set<TestInput> testSet) {
        testInputs = new ArrayList<>();
        for (TestInput ti : testSet) {
            testInputs.add(ti);
        }
        eliminatedTestSet = new HashSet<>();
        eliminatedTestInputs();
    }

    public void addTestInput(TestInput testInput) {
        //if(!containSameTestInput(testInput)){
        testInputs.add(testInput);
        //}     
    }

    public void addTestInputSet(Set<TestInput> testInputs) {
        for (TestInput ti : testInputs) {
            this.testInputs.add(ti);
        }
    }

    public void addTestInputList(ArrayList<TestInput> testInputs) {
        for (TestInput ti : testInputs) {
            this.testInputs.add(ti);
        }
    }

    public ArrayList<TestInput> getTestInputs() {
        return testInputs;
    }

    public Set<TestInput> getEliminatedTestSet() {
        return eliminatedTestSet;
    }
        
    public ArrayList<TestInput> getTestInputsAsSet() {

        ArrayList<TestInput> newList = new ArrayList<>();

        for (TestInput ti : testInputs) {

            if (newList.isEmpty()) {
                newList.add(ti);
            }

            if (!newList.contains(ti)) {
                newList.add(ti);
            }
        }

        return newList;
    }

    @Override
    public String toString() {
        String str = "";

        for (TestInput ti : testInputs) {
            str += ti;
            str += "\n";
        }

        return str;
    }

    public boolean containSameTestInput(TestInput ti) {

        if (testInputs.isEmpty()) {
            return false;
        }

        if (ti.getTestInput().size() != testInputs.size()) {
            return false;
        }

        boolean same = false;

        for (TestInput t : testInputs) {

            /* for(String s : t.getTestInput().keySet()){
                if(!t.getTestInput().get(s).equals(ti.getTestInput().get(s))){
                    //System.out.println(" false dustu dustu");
                    same = false;
                    break;
                }
            }    
            same = true;*/
            if (t.toString().equals(ti.toString())) {
                same = true;
            }

        }

        return same;

    }

    public TestSet importFromFile(File file) {

        TestSet ts = new TestSet();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));

            String line = br.readLine();

            int id = 0;

            while (line != null) {
                line = line.trim();
                String[] testList = line.split(" ");

                TestInput ti = new TestInput(id++);

                for (String s : testList) {

                    String[] sp = s.split(",");

                    String cause = sp[0].substring(1);
                    String value = sp[1].substring(0, sp[1].length() - 1);

                    ti.addTestInput(cause, ti.stringToValue(value));
                }

                ts.addTestInput(ti);
                line = br.readLine();
            }
            
            br.close();

        } catch (FileNotFoundException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        
        ts.eliminatedTestInputs();
        return ts;
    }

    public void eliminatedTestInputs() {

        Set<TestInput> copyTestInputs = new HashSet<>();
        copyTestInputs.addAll(getTestInputsAsSet());

        Set<TestInput> copyTests = new HashSet<>();
        //copyTests.addAll(getTestInputsAsSet());

        /*for(int i=0 ; i<copyTestInputs.size()-1; i++){
            for(int j=i+1; j<copyTestInputs.size(); j++){
            
            } 
        }*/
        for (TestInput ti : copyTestInputs) {
            if (copyTests.isEmpty()) {
                copyTests.add(ti);
            } else {
                if(containsSameTest(copyTests, ti)==null){
                    copyTests.add(ti);
                }
                else if(!containsSameTest(copyTests, ti).equals(ti)){ //the test can be eliminated is not the one we are searching now
                    TestInput t = containsSameTest(copyTests, ti);
                    if(copyTests.contains(t)){
                        copyTests.remove(t);
                    }
                    copyTests.add(ti);
                }
            }
        }

        eliminatedTestSet.addAll(copyTests);

    }

    /*
    Returns the smaller test input, which can be eliminated
     */
    public TestInput containsSameTest(Set<TestInput> set, TestInput test) {
        boolean flag = true;

        for (TestInput ti : set) {
            flag = true;
            if (ti.getTestInput().keySet().size() < test.getTestInput().keySet().size()) {
                for (String s : ti.getTestInput().keySet()) {
                    if(!test.getTestInput().containsKey(s)){
                        flag = false;
                        break;
                    }
                    if (ti.getTestInput().get(s) != test.getTestInput().get(s)) {
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    return ti;
                }
            } else {
                for (String s : test.getTestInput().keySet()) {
                    if(!ti.getTestInput().containsKey(s)){
                        flag = false;
                        break;
                    }
                    if (ti.getTestInput().get(s) != test.getTestInput().get(s)) {
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    return test;
                }
            }
        }     
        return null; //if not found, return null
    }
 
    public Set<TestInput> getTestsWithKilledMutants(int n){
        Set<TestInput> kTest = new HashSet<>();
        
        for(TestInput ti : eliminatedTestSet){
            if(ti.getKilledMutants().size()>=n){
                kTest.add(ti);
            }
        }
        
        return kTest;
    }
    
}
