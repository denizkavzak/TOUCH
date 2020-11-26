/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Fourier;

import graph.CEG;
import graph.Effect;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import jboolexpr.BooleanExpression;
import jboolexpr.MalformedBooleanException;
import mutation.ClauseConjunction;
import mutation.ClauseDisjunction;
import mutation.ExpressionNegation;
import mutation.MissingVariable;
import mutation.Mutant;
import mutation.MutantDetail;
import mutation.OperatorReference;
import mutation.StuckAt0;
import mutation.StuckAt1;
import mutation.VariableNegation;
import mutation.VariableReference;
import org.openide.util.Exceptions;
import test.TestInput;
import test.TestSet;

/**
 *
 * @author deniz.kavzak
 */
public class Mut {

    private static int id = 0;
    private CEG ceg;
    private TestSet testSet;
    private ArrayList<Mutant> allMutants; //holds all the mutants that are created
    // private HashMap<String, ArrayList<String>> mutantsOfTypesString; //holds all the mutants as string for each fault type
    private HashMap<String, ArrayList<Mutant>> mutantsOfTypes; //holds all the mutants for each fault type
    // private HashMap<String, Integer> totalMutants; //holds the total number of mutants for each fault type
    private HashMap<String, Integer> mutationResult; //holds the number of detected faults for each fault type
    private HashMap<String, ArrayList<Mutant>> mutantsOfEffect; //holds the related mutants for each effect node
    private HashMap<String, Integer> noOfMutantsOfTypes; //holds the number of mutants created for each fault type
    public int killerTests = 0;
    public int killedMutants = 0;
    public Mut(CEG ceg) {
        this.ceg = ceg;
        allMutants = new ArrayList<>();
        mutantsOfEffect = new HashMap<>();
        mutantsOfTypes = new HashMap<>();
        mutationResult = new HashMap<>();
        noOfMutantsOfTypes = new HashMap<>();
        fillAllMutants();
    }

    public HashMap<String, Integer> getMutationResult() {
        return mutationResult;
    }

    public ArrayList<Mutant> getAllMutants() {
        return allMutants;
    }

    public HashMap<String, ArrayList<Mutant>> getMutantsOfTypes() {
        return mutantsOfTypes;
    }

    public HashMap<String, ArrayList<Mutant>> getMutantsOfEffect() {
        return mutantsOfEffect;
    }

    public HashMap<String, Integer> getNoOfMutantsOfTypes() {
        return noOfMutantsOfTypes;
    }

    
    private void fillAllMutants() {

        for (Effect effect : ceg.getEffectNodes()) {

            ArrayList<Mutant> effectMutants = new ArrayList<>();

            effectMutants.add(new ClauseConjunction(id++, effect));
            effectMutants.add(new ClauseDisjunction(id++, effect));
            effectMutants.add(new ExpressionNegation(id++, effect));
            effectMutants.add(new MissingVariable(id++, effect));
            effectMutants.add(new OperatorReference(id++, effect));
            effectMutants.add(new StuckAt0(id++, effect));
            effectMutants.add(new StuckAt1(id++, effect));
            effectMutants.add(new VariableNegation(id++, effect));
            effectMutants.add(new VariableReference(id++, effect));

            mutantsOfEffect.put(effect.getLabel(), new ArrayList(effectMutants));

            allMutants.addAll(new ArrayList(effectMutants));
        }

        for (Mutant m : allMutants) {

            String faultType = convertToAbbr(m);
            // System.out.println("fault type: " + faultType);
            if (mutantsOfTypes.keySet().contains(faultType)) {
                ArrayList<Mutant> created = mutantsOfTypes.get(faultType);
                // System.out.println("created size : " + created.size());
                created.add(m);
                mutantsOfTypes.put(faultType, new ArrayList(created));

            } else {
                ArrayList<Mutant> created = new ArrayList<>();
                created.add(m);
                mutantsOfTypes.put(faultType, new ArrayList(created));
            }
           
        }
    }

    private String convertToAbbr(Mutant m) {
        String faultType = null;
        if (m instanceof ClauseConjunction) {
            faultType = "CCF";
        } else if (m instanceof ClauseDisjunction) {
            faultType = "CDF";
        } else if (m instanceof ExpressionNegation) {
            faultType = "ENF";
        } else if (m instanceof MissingVariable) {
            faultType = "MVF";
        } else if (m instanceof OperatorReference) {
            faultType = "ORF";
        } else if (m instanceof StuckAt0) {
            faultType = "SA0";
        } else if (m instanceof StuckAt1) {
            faultType = "SA1";
        } else if (m instanceof VariableNegation) {
            faultType = "VNF";
        } else if (m instanceof VariableReference) {
            faultType = "VRF";
        }

        return faultType;
    }

    public boolean isDetected(String mutant, String mutantType, Effect effect, TestSet testSet) {

        String exp = effect.getRelation().getExpression();
        String modifiedExp = exp;
        String modifiedMutant = mutant;
        boolean flag = false;
        
        Set<TestInput> set = testSet.getEliminatedTestSet();
        for (TestInput ti : set) {
            modifiedExp = exp;
            modifiedMutant = mutant;

            for (String s : ti.getTestInput().keySet()) {
                while (modifiedExp.contains(s + " ")) {
                    String check = s + " ";
                    modifiedExp = modifiedExp.replace(check, ti.getTestInput().get(s).toString().toLowerCase() + " ");

                }

                while (modifiedMutant.contains(s + " ")) {
                    String check = s + " ";
                    modifiedMutant = modifiedMutant.replace(check, ti.getTestInput().get(s).toString().toLowerCase() + " ");
                }

            }

            if (!modifiedMutant.contains("C") && !modifiedExp.contains("C")) {
                try {

                    BooleanExpression be = BooleanExpression.readLeftToRight(modifiedExp);
                    BooleanExpression be2 = BooleanExpression.readLeftToRight(modifiedMutant);
                    
                    if (be.booleanValue() != be2.booleanValue()) {
                        killedMutants++;
                        ti.killer = true;
                        ti.addKilledMutant(new MutantDetail(effect, mutantType, mutant));
                        flag = true;
                    }

                } catch (MalformedBooleanException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        }

        return flag;
    }

    public String getMutantAnalysisResults(Effect effect, TestSet testSet) {

        double all = 0;

        double i = 0;

        System.out.println("Effect : " + effect.getLabel() + " = " + effect.getRelation().getExpression());
        for (Mutant m : mutantsOfEffect.get(effect.getLabel())) {
            
            double type = 0;
            double alltype = 0;
            System.out.println("Mutant type : " + m.getClass());
            for (String mut : m.getMutants()) {
                all++;
                alltype++;

                if (isDetected(mut, m.getClass().getSimpleName(), effect, testSet)) {
                    
                    i++;
                    type++;
                }
            }
            System.out.println("Type success: " + type + "/" + alltype + "= " + type / alltype);

            if (mutationResult.keySet().contains(convertToAbbr(m))) {
                int old = mutationResult.get(convertToAbbr(m));
                mutationResult.put(convertToAbbr(m), (old + (int) type));
            } else {
                mutationResult.put(convertToAbbr(m), (int) type);
            }

        }

        return i + "/" + all + "= " + (i / all);
    }

    public String getMutantAnalysisResultsForAllEffects(TestSet testSet, int n) throws IOException {

        String s = "";

        for (Effect effect : ceg.getEffectNodes()) {
            s += getMutantAnalysisResults(effect, testSet) + "\n";
        }

        for (String t : mutantsOfTypes.keySet()) {
            int count = 0;
            for (Mutant m : mutantsOfTypes.get(t)) {
                count += m.getMutants().size();
            }
            noOfMutantsOfTypes.put(t, count);
        }

        //System.out.println("SIZE OF ELIMINATED TEST SET: " + testSet.getTestsWithKilledMutants(n).size());
        
        //for(TestInput ti :testSet.getTestsWithKilledMutants(n))
        //    System.out.println(ti);
        
        
        //getEliminatedMutantsSuccess(testSet, n, 10);
        
        return s;
    }

    public Set<MutantDetail> getEliminatedMutants(TestSet testSet, int n) {

        Set<MutantDetail> mutantsInDetail = new HashSet<>();
        Set<TestInput> tests = testSet.getTestsWithKilledMutants(n);
        for (TestInput ti : tests) {
            for (MutantDetail s : ti.getKilledMutants()) {
                if (!mutantsInDetail.contains(s)) {
                    mutantsInDetail.add(s);
                }
            }
        }
        return mutantsInDetail;
    }

    public void getEliminatedMutantsSuccess(TestSet testSet, int n, int m) throws IOException {

        int count = 0;
        Set<MutantDetail> killedMutants = new HashSet<>();

        Set<MutantDetail> set = new HashSet<>();//getEliminatedMutants(testSet, n);
        
        //set.addAll(getRandomMutants(set, m));
        
        for(Mutant mut : allMutants){
            for(int id=0;id<mut.getMutants().size();id++){
                set.add(new MutantDetail(mut.getEffect(), mut.getClass().getSimpleName(), mut.getMutants().get(id)));
            }
        }
        
        writeMutantsToFile(set, "mutants.txt");

        Set<TestInput> testWKilledMut = testSet.getTestsWithKilledMutants(n);
        for (MutantDetail mutant : set) {
            outerloop:

            for (TestInput ti : testWKilledMut) {

                String modifiedMutant = mutant.getExpression();
                String modifiedExp = mutant.getEffect().getRelation().getExpression();

                for (String s : ti.getTestInput().keySet()) {

                    while (modifiedExp.contains(s + " ")) {
                        String check = s + " ";
                        modifiedExp = modifiedExp.replace(check, ti.getTestInput().get(s).toString().toLowerCase() + " ");
                    }

                    while (modifiedMutant.contains(s + " ")) {
                        String check = s + " ";
                        modifiedMutant = modifiedMutant.replace(check, ti.getTestInput().get(s).toString().toLowerCase() + " ");
                    }

                }

                if (!modifiedMutant.contains("C") && !modifiedExp.contains("C")) {
                    try {

                        BooleanExpression be = BooleanExpression.readLeftToRight(modifiedExp);
                        BooleanExpression be2 = BooleanExpression.readLeftToRight(modifiedMutant);
                        
                        if (be.booleanValue() != be2.booleanValue()) {
                           
                            count++;
                            killedMutants.add(mutant);
                            break outerloop;
                        }

                    } catch (MalformedBooleanException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                }
            }

        }

        System.out.println("Success on all mutants : " + killedMutants.size() + " / " + set.size() + " = " + (double) killedMutants.size() / (double) set.size());

    }

    public void writeMutantsToFile(Set<MutantDetail> mutants, String file) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            for (MutantDetail s : mutants) {
                bw.write(s.toString());
                bw.newLine();
            }
        }
    }
    
    public void writeMutantsToFile(Set<MutantDetail> mutants, File file) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            for (MutantDetail s : mutants) {
                bw.write(s.toString());
                bw.newLine();
            }
        }
    }    

    public void analysisOfImportedMutants(Set<MutantDetail> mutants, TestSet testSet) {

        int count = 0;
        Set<MutantDetail> killedMutants = new HashSet<>();
        HashMap<String, Set<MutantDetail>> allMutantsOfTypes = new HashMap<>();
        HashMap<String, Set<MutantDetail>> killedTypes = new HashMap<>();

        for (MutantDetail mutant : mutants) {
            Set<MutantDetail> cur = new HashSet<>();

            if (allMutantsOfTypes.containsKey(mutant.getType())) {
                cur = allMutantsOfTypes.get(mutant.getType());

            }

            cur.add(mutant);
            allMutantsOfTypes.put(mutant.getType(), cur);

            for (TestInput ti : testSet.getEliminatedTestSet()) {

                String modifiedMutant = mutant.getExpression();
                String modifiedExp = mutant.getEffect().getRelation().getExpression();

                for (String s : ti.getTestInput().keySet()) {

                    while (modifiedExp.contains(s + " ")) {
                        String check = s + " ";
                        modifiedExp = modifiedExp.replace(check, ti.getTestInput().get(s).toString().toLowerCase() + " ");
                    }

                    while (modifiedMutant.contains(s + " ")) {
                        String check = s + " ";
                        modifiedMutant = modifiedMutant.replace(check, ti.getTestInput().get(s).toString().toLowerCase() + " ");
                    }

                }

                if (!modifiedMutant.contains("C") && !modifiedExp.contains("C")) {
                    try {

                        BooleanExpression be = BooleanExpression.readLeftToRight(modifiedExp);
                        BooleanExpression be2 = BooleanExpression.readLeftToRight(modifiedMutant);

                        
                        if (be.booleanValue() != be2.booleanValue()) {
                           
                            count++;
                            killedMutants.add(mutant);

                            Set<MutantDetail> cur2 = new HashSet<>();

                            if (killedTypes.containsKey(mutant.getType())) {
                                cur2 = killedTypes.get(mutant.getType());

                            }

                            cur2.add(mutant);
                            killedTypes.put(mutant.getType(), cur2);

                            break;
                        }

                    } catch (MalformedBooleanException ex) {
                        Exceptions.printStackTrace(ex);
                    }
                }
            }

        }

        System.out.println("Success on all mutants : " + killedMutants.size() + " / " + mutants.size() + " = " + (double) killedMutants.size() / (double) mutants.size());

        for (String s : killedTypes.keySet()) {
            System.out.println(s + " = " + killedTypes.get(s).size() + " / " + allMutantsOfTypes.get(s).size() + " = " + (double) killedTypes.get(s).size() / (double) allMutantsOfTypes.get(s).size());
        }
    }

    /*   public String getMutantAnalysisResultsMutantTypes(TestSet testSet) {

        double all = 0;

        double i = 0;

        for (String type : mutantsOfTypes.keySet()) {

            System.out.println("Mutant type : " + type);
            double typeCount = 0;
            double alltype = 0;
            
            //System.out.println("ALL DIF MUTS OF TYPE:" + mutantsOfTypes.get(type).size());
            
            for (Mutant m : mutantsOfTypes.get(type)) {
                //typeCount = 0;
                //alltype = 0;

                //System.out.println("ALL MUTS:" + m.getMutants().size());
                //for (Effect effect : ceg.getEffectNodes()) {
                        
                    for (String mut : m.getMutants()) {
                        //System.out.println("mutant : " + mut);
                        all++;
                        alltype++;
                        if (isDetected(mut, m.getEffect(), testSet)) {
                            System.out.println(mut + " is detected");
                            i++;
                            typeCount++;
                            break;
                        }
                    }
                //}
            }
            //if(mutationResult.keySet().contains(type)){
              //  int cur = mutationResult.get(type);
               // mutationResult.put(type, (cur+(int)typeCount));
            //}else
            mutationResult.put(type, (int)typeCount);
            noOfMutantsOfTypes.put(type, (int)alltype);
            System.out.println("Type success: " + typeCount + "/" + alltype + "= " + typeCount / alltype);
        }
        
        System.out.println("All: " + i + "/" + all + "= " + (i / all));
        return i + "/" + all + "= " + (i / all);
    }
     */

    private Set<MutantDetail> getRandomMutants(Set<MutantDetail> set, int n) {
        
        Set<MutantDetail> extraMutants = new HashSet<>();
        
        Random rnd = new Random();
        
        while(extraMutants.size()!=n){
            int j = rnd.nextInt(allMutants.size());
            
            int k = rnd.nextInt(allMutants.get(j).getMutants().size());
            MutantDetail md = new MutantDetail(allMutants.get(j).getEffect(), allMutants.get(j).getClass().getSimpleName(), allMutants.get(j).getMutants().get(k));
            
            if(!set.contains(md)){
                extraMutants.add(md);
            }
        }               
        return extraMutants;
        
    }
}
