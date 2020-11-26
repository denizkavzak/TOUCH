package graph;

import CEG_BOR.CEG_BOR;
import com.bpodgursky.jbool_expressions.Expression;
import com.bpodgursky.jbool_expressions.parsers.ExprParser;
import com.bpodgursky.jbool_expressions.rules.RuleSet;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.UndirectedGraph;
import org.gephi.io.importer.api.Container;
import org.gephi.io.importer.api.ImportController;
import org.gephi.io.processor.plugin.DefaultProcessor;
import org.gephi.project.api.ProjectController;
import org.openide.util.Lookup;

import jboolexpr.BooleanExpression;
import jboolexpr.MalformedBooleanException;
import mcdc.MaskingMCDC;
import mcdc.UniqueMCDC;
import mi.*;
import mumcut.CUTPNFP;
import mumcut.MNFP;
import mumcut.MNFPe;
import mumcut.MUMCUT;
import mumcut.MUTP;
import mumcut.MUTPe;
import mumcut.NFP;
import mumcut.NFP_Min;
import mumcut.UTP;
import mumcut.UTP_Min;
import mumcut.UTPi;
import mutation.*;
import myers.EffectValues;
import myers.InterValues;
import myers.Myers;
import org.gephi.layout.plugin.multilevel.Test;
import random.RandomTest;
import test.TestInput;
import test.TestSet;
import test.Value;

public class Main {

    public static void main(String[] args) throws IOException, MalformedBooleanException {

        ProjectController projectController = Lookup.getDefault().lookup(ProjectController.class);
        projectController.newProject();
        projectController.newWorkspace(projectController.getCurrentProject());
        ImportController importController = Lookup.getDefault().lookup(ImportController.class);
        Container container;
        //String fileName = "C:/Users/deniz.kavzak/Desktop/Thesis/dot/graph-example.graphml";
        //String fileName = "graph-exampleLONG.graphml";
        String fileName = "graph-example.graphml";

        //String fileName = "denizv1.graphml";
        //String fileName = "sample.graphml";
        File file = new File(fileName);
        container = importController.importFile(file);

        importController.process(container, new DefaultProcessor(), projectController.getCurrentWorkspace());
        GraphController graphController = Lookup.getDefault().lookup(GraphController.class);
        GraphModel graphModel = graphController.getModel();

        UndirectedGraph graph = graphModel.getUndirectedGraph();
        CEG ceg = new CEG(graph);
        
        BufferedReader bff = new BufferedReader(new FileReader("C:\\Users\\deniz.kavzak\\Desktop\\deneme3.xml"));
                
        Set<String> lis = new HashSet<>();
        String l = bff.readLine();
        
        while(l!=null){
            lis.add(l);
            l = bff.readLine();
        }
        
        bff.close();
        
        BufferedWriter bff2 = new BufferedWriter(new FileWriter("C:\\Users\\deniz.kavzak\\Desktop\\deneme4.xml"));
        
        for(String ss: lis){
            bff2.write(ss);
            bff2.newLine();
        }
        
        bff2.close();
        
        /*for(Effect e : ceg.getEffectNodes()){
            //System.out.println(e.getLabel() + " = " + e.getRelation().getExpressionInter());
            System.out.println(e.getLabel() + " = " + e.getRelation().getExpression());
        }
        
        
        
        //RandomTest rt = new RandomTest(ceg);
        //TestSet tes = rt.getTests();
        
        //for(TestInput ti : tes.getTestInputs()){
            
        //}
        
/*
        TestSet ts = new TestSet();

        BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\deniz.kavzak\\workspace\\CEG_Algorithms\\Yeni klasör\\UTP-uyms.txt"));
        BufferedReader br2 = new BufferedReader(new FileReader("C:\\Users\\deniz.kavzak\\workspace\\CEG_Algorithms\\Yeni klasör\\NFP-uyms.txt"));

        String l = br.readLine();
        int idd = 0;

        while (l != null) {
            //System.out.println(line);
            TestInput ti = new TestInput(idd);
            l = l.substring(0, l.lastIndexOf(","));
            l = l.trim();
            String[] nodeList = l.split(" , ");

            for (String s : nodeList) {

                //System.out.println("addsa : " + s);
                //if (!s.contains("E")) {
                //System.out.println("sdfsdf   " + s);
                //System.out.println("node " + s);
                s = s.substring(s.indexOf("(") + 1, s.lastIndexOf(")"));

                String[] node = s.split(",");
                Value v;
                if (node[1].equals("True")) {
                    v = Value.True;
                } else {
                    v = Value.False;
                }
                ti.addTestInput(node[0], v);

            }
            ts.addTestInput(ti);
            l = br.readLine();
        }

        br.close();

        l = br2.readLine();

        while (l != null) {
            //System.out.println(line);
            TestInput ti = new TestInput(idd);
            l = l.substring(0, l.lastIndexOf(","));
            l = l.trim();
            String[] nodeList = l.split(" , ");

            for (String s : nodeList) {

                //System.out.println("addsa : " + s);
                //if (!s.contains("E")) {
                //System.out.println("sdfsdf   " + s);
                s = s.substring(s.indexOf("(") + 1, s.lastIndexOf(")"));

                String[] node = s.split(",");
                Value v;
                if (node[1].equals("True")) {
                    v = Value.True;
                } else {
                    v = Value.False;
                }
                ti.addTestInput(node[0], v);

            }
            ts.addTestInput(ti);
            l = br2.readLine();
        }

        br2.close();

        //for(TestInput t : ts.getTestSet()){
        //    System.out.println(t);
        //}
        //System.out.println("set " + ts.getTestSet().size());
        //System.out.println("set " + ts.getTestInputs().size());
        BufferedReader bf3 = new BufferedReader(new FileReader("VariableReference.txt"));

        l = bf3.readLine();

        HashMap<String, ArrayList<String>> mutants1 = new HashMap<>();
        ArrayList<String> m1 = null;

        while (l != null) {
            if (l.contains("E")) {
                //System.out.println("l " + line);
                m1 = new ArrayList<>();
                mutants1.put(l, m1);
            } else {
                m1.add(l);
            }
            l = bf3.readLine();
        }

        //  for (String s : mutants.keySet()) {
        //    System.out.println("effect: " + s);
        //  for (String se : mutants.get(s)) {
        //    System.out.println(se);
        //    }
        //}
        bf3.close();

        for (String st : mutants1.keySet()) {
            int count = 0;
            for (String mut : mutants1.get(st)) {
                for (TestInput ti : ts.getTestSet()) {

                    //if (ti.getEffectMap().containsKey(st)) {
                    String modMut = mut;
                    for (String s : ti.getTestInput().keySet()) {
                        if (modMut.contains(s + " ")) {
                            modMut = modMut.replace(s + " ", ti.getTestInput().get(s).toString().toLowerCase());
                        }
                    }
                    if (!modMut.contains("C")) {
                        String modEx = ceg.getEffectNode(st).getRelation().getExpression();
                        for (String s : ti.getTestInput().keySet()) {
                            if (modEx.contains(s + " ")) {
                                modEx = modEx.replace(s + " ", ti.getTestInput().get(s).toString().toLowerCase());
                            }
                        }
                        if (!modEx.contains("C")) {
                            BooleanExpression be = BooleanExpression.readLeftToRight(modMut);
                            BooleanExpression be2 = BooleanExpression.readLeftToRight(modEx);

                            if (be.booleanValue() != be2.booleanValue()) {
                                count++;
                                break;
                            }
                        }
                    }
                    //}
                }
            }
            System.out.println(st + " : " + count + " / " + mutants1.get(st).size());
        }

        System.out.println("");

        /*
        TestSet te1 = new TestSet();
        TestSet te2 = new TestSet();
        int i = 0;
        TestInput ti1 = new TestInput(i++);
        ti1.addTestInput("C1", Value.False);
        ti1.addTestInput("C2", Value.False);
        ti1.addTestInput("C3", Value.True);
        te1.addTestInput(ti1);
        
        TestInput ti2 = new TestInput(i++);
        ti2.addTestInput("C1", Value.True);
        ti2.addTestInput("C2", Value.True);
        ti2.addTestInput("C3", Value.False);
        te1.addTestInput(ti2);
        
        TestInput ti3 = new TestInput(i++);
        ti3.addTestInput("C4", Value.False);
        ti3.addTestInput("C5", Value.False);
        ti3.addTestInput("C6", Value.True);
        ti3.addTestInput("C7", Value.True);
        te2.addTestInput(ti3);
        
        TestInput ti4 = new TestInput(i++);
        ti4.addTestInput("C4", Value.True);
        ti4.addTestInput("C5", Value.True);
        ti4.addTestInput("C6", Value.False);
        ti4.addTestInput("C7", Value.False);
        te2.addTestInput(ti4);
        
        for(TestInput t : cb.cartesianProduct(te1, te2).getTestInputs()){
            System.out.println("union op : " + t);
        }
         */
 /*CEG_BOR cb = new CEG_BOR(ceg);

        System.out.println("effect : " + ceg.getEffectNodes().get(13).getRelation().getExpression());
        System.out.println("effect : " + ceg.getEffectNodes().get(13).getRelation().getExpressionInter());

        TestSet ts = cb.CEG_BOR_t(ceg.getEffectNodes().get(13));
        System.out.println("---> "+ts.toString());
        System.out.println("trues : ");
        for (TestInput tis : ts.getTestSet()) {
            System.out.println(tis);
        }

        //for(TestInput t : ceg.getDc().getSelectedTestInputs())
        //   System.out.println(t);
        // UniqueMCDC mc = new UniqueMCDC(ceg);
        // RandomTest mc = new RandomTest(ceg);
//System.out.println("unique");
        //MI mc = new MI(ceg);
        //     for (TestInput ti : mc.getAllTests().getTestSet()) {
        //         System.out.println(ti);
        //}
//MaskingMCDC mcd = new MaskingMCDC(ceg);
        //     System.out.println("masking");
        //   for (TestInput ti : mcd.getAllTests().getTestSet()) {
        //      System.out.println(ti);
        //}
        // System.out.println("size " + mc.getTests().getTestSet().size());
        //System.out.println("size " + mc.getTests().getTestSet().size());
        //System.out.println("effect : " + ceg.getEffectNodes().get(1).getRelation().getExpression());
        /* Myers m = new Myers(ceg);
        for(TestInput ti : m.getTs().getTestSet()){
            System.out.println(ti);
        }
        
        /*MI mi = new MI(ceg);
        for(TestInput ti : mi.getAllTests()){
            System.out.println(ti);
        }
        
        RandomTest rt = new RandomTest(ceg);
        
        
        Set<TestInput> t = rt.generateTestsForEffect(ceg.getEffectNodes().get(1));
        System.out.println("random");
        for(TestInput ti : t){
            
            System.out.println(ti);
        }
        System.out.println("random bitti");
        
        /*
        CEG_BOR cb = new CEG_BOR(ceg);
        
        
        System.out.println(ceg.getIntermediateNode("I1").getRelation().getExpressionInter());
        System.out.println(ceg.getIntermediateNode("I2").getRelation().getExpressionInter());
        
        System.out.println(ceg.getEffectNodes().get(1).getRelation().getExpressionInter());
        
        TestSet ts = cb.CEG_BOR_t(ceg.getEffectNodes().get(1));
        
        for(TestInput ti : ts.getTestInputs()){
            System.out.println("ti " + ti);
        }
        
       /* for(Effect effect : ceg.getEffectNodes()){
            System.out.println("effect " + effect.getLabel());
            System.out.println("expression : " + effect.getRelation().getExpressionInter());
            System.out.println("expression : " + effect.getRelation().getExpression());
            System.out.println("dnf expression : " + effect.getMinDNF());
            
            String s = effect.getRelation().getExpression();
            
            s = s.replaceAll("\\&\\&", "\\&");
            s = s.replaceAll("\\|\\|", "\\|");
            
            Expression<String> nonStandard = ExprParser.parse(s);

            Expression<String> sopForm;
            sopForm = RuleSet.simplify(nonStandard);
            
            System.out.println("Simplified expression: " + sopForm);
            
            System.out.println("");
        }
        /*
        BOR my = new BOR(ceg);
        
        System.out.println("generated test cases : ");
         //BufferedWriter bf10 = new BufferedWriter(new FileWriter("MYERSWITHDIF.txt"));
       // for(TestInput ti : my.getTs().getTestSet()){
            //System.out.println(ti);
            //bf10.write(ti.toString());
           // bf10.newLine();
           // bf10.flush();
       // }
        
        /*CUTPNFP cut = new CUTPNFP(ceg);
        
        BufferedWriter bf10 = new BufferedWriter(new FileWriter("CUT.txt"));
        
        
        System.out.println("cutpnfp: " + cut.getAllTests().getTestSet().size());
        
        for(TestInput t : cut.getAllTests().getTestSet()){
            System.out.println(t);
            bf10.write(t.toString());
            bf10.newLine();
            bf10.flush();
        }
        
       /* BufferedWriter bf10 = new BufferedWriter(new FileWriter("MissingVariable.txt"));
        
        for(Effect effect : ceg.getEffectNodes()){
            MissingVariable mv = new MissingVariable(0, effect);
            bf10.write(effect.getLabel());
            bf10.newLine();
            for(String s : mv.getMutants()){
                System.out.println(s);
                bf10.write(s);
                bf10.newLine();
            }
            bf10.flush();
        }
        
        /*
        MaskingMCDC umcdc = new MaskingMCDC(ceg);
        
        BufferedWriter bf10 = new BufferedWriter(new FileWriter("mcdcMaskingDeneme2.txt"));
        
        
        System.out.println("masking size: " + umcdc.getMcdc().size());
        
        for(TestInput t : umcdc.getMcdc()){
            System.out.println(t);
            bf10.write(t.toString());
            bf10.newLine();
            bf10.flush();
        }
        
       /* TestInput ti3 = new TestInput(0);
		TestInput ti4 = new TestInput(1);
		
		ti3.addTestInput("C1", Value.True);
		ti3.addTestInput("C2", Value.True);
		ti3.addTestInput("C3", Value.True);
		ti3.addTestInput("C4", Value.True);
                
		
		ti4.addTestInput("C3", Value.True);
		ti4.addTestInput("C4", Value.True);
		ti4.addTestInput("C2", Value.True);
		ti4.addTestInput("C1", Value.True);
		//ti2.addTestInput("C5", Value.True);
		//ti2.addTestInput("C6", Value.True);
		
		System.out.println(ti4.isSameExceptKey(ti3, "C2"));
        
        
        
        
        /*	UniqueMCDC mcdc = new UniqueMCDC(ceg);
		
		BufferedWriter bf = new BufferedWriter(new FileWriter("mcdc.txt"));
		
		for(TestInput ti : mcdc.getMcdc()){
			bf.write(ti.toString());
			bf.newLine();
		}
		
		bf.close();*/
 /* System.out.println("dfadfadf");
        for (Effect e : ceg.getEffectNodes()) {
            System.out.println(e.getMin_DNF_Exp());
            UTP_Min utpimin = new UTP_Min(ceg, e);
            for (TestInput ti : utpimin.getTests()){
                System.out.println(ti);
            }
        }
        System.out.println("dfadfadf");
         */
        // MUTP mutp = new MUTP(ceg);
//TestSet testSet = new TestSet();
        //            System.out.println("MUTP basladı");
        //System.out.println(ceg.getEffectNodes().get(0).getMin_DNF_Exp());
        // MUMCUT mumcut = new MUMCUT(ceg);
//MUTP mutp = new MUTP(ceg);
        // MUTP mutp = new MUTP(ceg);
        //   MNFP mnfp = new MNFP(ceg);
        //               CUTPNFP cutpnfp = new CUTPNFP(ceg);
        //     for(TestInput t : cutpnfp.getAllTests().getTestSet()){
        //      testSet.addTestInput(t);
        // }
        //TestSet ts2 = new TestSet();
        //for(TestInput t : testSet.getTestSet()){
        //   ts2.addTestInput(t);
        // }
        // testSet = new TestSet();
        //       for(TestInput t : cutpnfp.getAllTests().getTestSet()){
        //         testSet.addTestInput(t);
        //   }
        // for(TestInput t : testSet.getTestSet()){
        //    ts2.addTestInput(t);
        // }
        // testSet = new TestSet();
        // for(TestInput t : cutpnfp.getAllTests().getTestSet()){
        //   testSet.addTestInput(t);
        //}
        // 
        // for(TestInput t : testSet.getTestSet()){
        //   ts2.addTestInput(t);
        //}
        //  testSet.addTestInputList(mutp.getTestSet().getTestInputs());
        //System.out.println(mutp.getTestSet().getTestSet().size());
        // testSet.addTestInputList(mnfp.getTestSet().getTestInputs());
        //System.out.println(mnfp.getTestSet().getTestSet().size());
        // testSet.addTestInputList(cutpnfp.getAllTests().getTestInputs());
        //System.out.println(cutpnfp.getAllTests().getTestSet().size());
        // TestSet ts = new TestSet();
        //UTP_Min utpmin = new UTP_Min(ceg, ceg.getEffectNodes().get(0));
        /*     FileWriter f = new FileWriter("CUTPNFPBool.txt");
        BufferedWriter bf8 = new BufferedWriter(f);
        for (TestInput ti : testSet.getTestSet()) {
         //   ts.addTestInput(ti);
            bf8.write(ti.toString());
            bf8.newLine();
            bf8.flush();
            System.out.println(ti);
        }
        
        //for (TestInput ti : ts.getTestInputs()) {
        //  System.out.println(ti);
        // }
        System.out.println("MUTP bitti");

        /* for (Effect e : ceg.getEffectNodes()) {
            System.out.println(e.getLabel());
            System.out.println(e.getRelation().getExpression());
            System.out.println(e.getMinDNF());
            System.out.println(e.getMin_DNF_Exp());
        }*/
//		for(Effect e : ceg.getEffectNodes()){
//			System.out.println(e.getLabel());
//			System.out.println(e.getMinDNF());
//			for(ArrayList<CEG_Node_in_Exp> l : e.getMin_DNF_Exp()){
//				System.out.println(l);
//			}
//		}
        /*		
//		BufferedReader bf = new BufferedReader(new FileReader("MIwithToolTrue.txt"));
//		BufferedReader bf2 = new BufferedReader(new FileReader("MIwithToolFalse.txt"));
//		
//		BufferedWriter bf3 = new BufferedWriter(new FileWriter("MIwithToolTrueFiltered.txt"));
		BufferedWriter bf4 = new BufferedWriter(new FileWriter("MIwithToolAll.txt"));
		
		MI mi = new MI(ceg);
		
		
		Set<TestInput> set1 = new HashSet<>();
		Set<TestInput> set2 = new HashSet<>();
		
		for(TestInput ti : mi.getAllTests()){
			set1.add(ti);
			set2.add(ti);
		}
		
		int count = 0;
		for(TestInput ti : mi.getAllTests()){
			for(TestInput ti2 : set1){
				if(!ti.isSame(ti2)){
					if(ti.containSame(ti2) && set2.contains(ti2)){
						set2.remove(ti2);
						count++;
						//System.out.println(set3.getTestInputs().size());
						System.out.println("removed " + count++);
					}
				}
			}
		}
		
		System.out.println(set2.size());

		for(TestInput ti : set2){
			bf4.write(ti.toString());
			bf4.newLine();
		}
		
		
//		bf.close();
//		bf2.close();
//		bf3.close();
		bf4.close();
         */
 /*	
		
	//	BufferedWriter bf3 = new BufferedWriter(new FileWriter("MIwithToolTrueFiltered.txt"));
	//	BufferedWriter bf4 = new BufferedWriter(new FileWriter("MIwithToolFalseFilteredWithTrue.txt"));
	
		Set<String> set1 = new HashSet<>();
		
		String line = bf.readLine();
		
		while(line!=null){
			set1.add(line);
			line = bf.readLine();
		}

		Set<String> set2 = new HashSet<>();
		
		line = bf2.readLine();
		
		while(line!=null){
			set2.add(line);
			line = bf2.readLine();
		}

		
		
		bf.close();
		bf2.close();
		//bf3.close();
		//bf4.close();
         */
        BufferedReader bf = new BufferedReader(new FileReader("C:\\Users\\deniz.kavzak\\Desktop\\tests\\u.txt"));
        //      BufferedReader bf2 = new BufferedReader(new FileReader("NFP-uyms.txt"));
        //     BufferedReader bf4 = new BufferedReader(new FileReader("MNFPThesis.txt"));

        Set<TestInput> tSet = new HashSet<>();
        //ArrayList<TestInput> fSet = new ArrayList<>();
        //Set<TestInput> allSet = new HashSet<>();

        String line = bf.readLine();
        int id = 0;
        while (line != null) {
            //System.out.println(line);
            TestInput ti = new TestInput(id);
            line = line.trim();
            //line = line.substring(0, line.lastIndexOf(","));
            String[] nodeList = line.split(" ");

            for (String s : nodeList) {

                //System.out.println("addsa : " + s);
                if (!s.contains("E")) {
                    //System.out.println("sdfsdf   " + s);
                    s = s.substring(s.indexOf("(") + 1, s.lastIndexOf(")"));

                    String[] node = s.split(",");
                    Value v;
                    if (node[1].equals("True")) {
                        v = Value.True;
                    } else {
                        v = Value.False;
                    }
                    ti.addTestInput(node[0], v);
                } else {
                    s = s.substring(s.indexOf("(") + 1, s.lastIndexOf(")"));

                    String[] node = s.split(",");

                    Value v;
                    if (node[1].equals("True")) {
                        v = Value.True;
                    } else {
                        v = Value.False;
                    }
                    ti.addEffectToMap(node[0], v);

                }
            }
            tSet.add(ti);
            line = bf.readLine();
        }

        bf.close();

        BufferedReader bf2 = new BufferedReader(new FileReader("VariableNegation.txt"));

        line = bf2.readLine();

        HashMap<String, ArrayList<String>> mutants = new HashMap<>();
        ArrayList<String> m = null;

        while (line != null) {
            if (line.contains("E")) {
                //System.out.println("l " + line);
                m = new ArrayList<>();
                mutants.put(line, m);
            } else {
                m.add(line);
            }
            line = bf2.readLine();
        }

        //  for (String s : mutants.keySet()) {
        //    System.out.println("effect: " + s);
        //  for (String se : mutants.get(s)) {
        //    System.out.println(se);
        //    }
        //}
        bf2.close();

        for (String st : mutants.keySet()) {
            int count = 0;
            for (String mut : mutants.get(st)) {
                for (TestInput ti : tSet) {

                    if (ti.getEffectMap().containsKey(st)) {
                        String modMut = mut;
                        for (String s : ti.getTestInput().keySet()) {
                            if (modMut.contains(s + " ")) {
                                modMut = modMut.replace(s + " ", ti.getTestInput().get(s).toString().toLowerCase());
                            }
                        }
                        if (!modMut.contains("C")) {
                            BooleanExpression be = BooleanExpression.readLeftToRight(modMut);

                            if (be.booleanValue() != Boolean.parseBoolean(ti.getEffectMap().get(st).toString().toLowerCase())) {
                                count++;
                                break;
                            }
                        }
                    }
                }
            }
            System.out.println(st + " : " + count + " / " + mutants.get(st).size());
        }

        System.out.println("");
        // System.out.println("size after cutpnfp: " + tSet.size());

        /*line = bf2.readLine();
        //id = 0;
        while (line != null) {
            //System.out.println(line);
            TestInput ti = new TestInput(id);

            line = line.substring(0, line.lastIndexOf(","));
            String[] nodeList = line.split(" , ");

            for (String s : nodeList) {

                //System.out.println("addsa : " + s);
                if (!s.contains("E")) {
                    //System.out.println("sdfsdf   " + s);
                    s = s.substring(s.indexOf("(") + 1, s.lastIndexOf(")"));

                    String[] node = s.split(",");
                    Value v;
                    if (node[1].equals("True")) {
                        v = Value.True;
                    } else {
                        v = Value.False;
                    }

                    ti.addTestInput(node[0], v);
                }
            }
            tSet.add(ti);
            line = bf2.readLine();
        }
         */
 /*    System.out.println("size before mnfp: " + tSet.size());
        
        line = bf4.readLine();
        //int id = 0;
        while (line != null) {
            //System.out.println(line);
            TestInput ti = new TestInput(id);

            //line = line.substring(0, line.lastIndexOf(","));
            String[] nodeList = line.split(" , ");

            for (String s : nodeList) {

                //System.out.println("addsa : " + s);
                if (!s.contains("E")) {
                    //System.out.println("sdfsdf   " + s);
                    s = s.substring(s.indexOf("(") + 1, s.lastIndexOf(")"));

                    String[] node = s.split(",");
                    Value v;
                    if (node[1].equals("True")) {
                        v = Value.True;
                    } else {
                        v = Value.False;
                    }

                    ti.addTestInput(node[0], v);
                }
            }
            tSet.add(ti);
            line = bf4.readLine();
        }
        
        System.out.println("size after mnfp: " + tSet.size());
        
        bf.close();
bf2.close();
bf4.close();
         */
 /*
BufferedWriter bw = new BufferedWriter(new FileWriter("MAX-A.txt"));
        System.out.println("MAXa size: " + tSet.size());
        for(TestInput ti : tSet){
            
            bw.write(ti.toString());
            bw.newLine();
            bw.flush();
        }

        bw.close();
        
        
         */
 /*
        System.out.println("size : " + tSet.size());
        for (Effect e : ceg.getEffectNodes()) {
            FileReader f3 = new FileReader("MissingVariable.txt");
            BufferedReader bf3 = new BufferedReader(f3);
            ArrayList<String> trvalues = new ArrayList<>();
            //ArrayList<String> flvalues = new ArrayList<>();

            String exp = e.getRelation().getExpression();

            for (TestInput ti : tSet) {
                String md = exp;
                for (String ss : ti.getTestInput().keySet()) {
                    md = md.replace(ss + " ", ti.getTestInput().get(ss).toString().toLowerCase());
                }
                if (!md.contains("C")) {
                    BooleanExpression be = BooleanExpression.readLeftToRight(md);
                    trvalues.add(String.valueOf(be.booleanValue()));
                }else{
                    trvalues.add(null);
                }
            }

            //for(String sss: values)
            //System.out.println(sss);
            String s = e.getLabel();
            System.out.println(s);
            //f.write("\n");
            //TestSet t = m.getEv().getMap().get(s);
            //System.out.println("size " + t.getTestInputs().size());

            int count = 0;

            line = bf3.readLine();

            //line = bf.readLine();
            while (!line.contains(e.getLabel())) {
                line = bf3.readLine();
            }

            line = bf3.readLine();

            while (line != null) {
                //boolean flag = false;

                if (line.contains("E")) {
                    break;
                }

                int i = 0;
                int cnt =0;
                int out=0;
                for (TestInput ti : tSet) {
                    //TestInput ti = trSet.get(i);
                    String md = line;

                    for (String ss : ti.getTestInput().keySet()) {
                        md = md.replace(ss + " ", ti.getTestInput().get(ss).toString().toLowerCase() + " ");
                    }

                    if (!md.contains("C")) {
                        BooleanExpression be = BooleanExpression.readLeftToRight(md);
                        //System.out.println("Else:"+out+", tSet:"+tSet.size()+", trvalues: "+i+"-"+cnt+"/"+trvalues.size());
                        
                        if (trvalues.get(cnt) != null && !String.valueOf(be.booleanValue()).equals(trvalues.get(cnt))) {
                            count++;
                            //flag=true;
                            break;
                        }
                       
                        cnt++;
                        i++;
                    }
                    
                    //f.write(ti.toString());
                    //System.out.println(ti);
                    //f.write("\n");
                    //i++;
                }

                line = bf3.readLine();
            }
            bf3.close();
            f3.close();
            System.out.println(count);
        }

        /*
		BufferedReader bf = new BufferedReader(new FileReader("UTP.txt"));
		BufferedReader bf2 = new BufferedReader(new FileReader("NFP.txt"));

		ArrayList<TestInput> tSet = new ArrayList<>();
		ArrayList<TestInput> fSet = new ArrayList<>();
		Set<TestInput> allSet = new HashSet<>();
		ArrayList<String> tSets = new ArrayList<>();
		ArrayList<String> fSets = new ArrayList<>();

		String line = bf.readLine();
		int id = 0;
		while(line!=null){
			tSets.add(line);
			//System.out.println(line);
				TestInput ti = new TestInput(id);
				
				line = line.substring(0,line.lastIndexOf(","));
				String[] nodeList = line.split(" , ");
				
				for(String s : nodeList){
					
					s = s.substring(s.indexOf("(")+1, s.lastIndexOf(")"));
					
					String[] node = s.split(",");
					Value v;
					if(node[1].equals("True")){
						v = Value.True;
					}else{
						v = Value.False;
					}
					
					ti.addTestInput(node[0], v);
				}
				tSet.add(ti);
			line = bf.readLine();
		}
		
		bf.close();
		

		line = bf2.readLine();
		while(line!=null){
			fSets.add(line);
			//System.out.println(line);
				TestInput ti = new TestInput(id);
				
				line = line.substring(0,line.lastIndexOf(","));
				String[] nodeList = line.split(" , ");
				
				for(String s : nodeList){
					s = s.substring(s.indexOf("(")+1, s.lastIndexOf(")"));
					String[] node = s.split(",");
					Value v;
					
					if(node[1].equals("True")){
						v = Value.True;
					}else{
						v = Value.False;
					}
					
					ti.addTestInput(node[0], v);
				}
				
				fSet.add(ti);
				
			line = bf2.readLine();
		}
		
		bf2.close();
		
		int co = 0;
		for(String ti : fSets){
			if(tSets.contains(ti)){
				co++;
			}
		}
		System.out.println(co);*/
        //System.out.println(tSet.size());
        //System.out.println(fSet.size());
//		for(TestInput ti : tSet)
//			allSet.add(ti);
//		
        //System.out.println(allSet.size());
//		Set<TestInput> fSetcopy = new HashSet<>();
//		
//		for(TestInput ti : fSet){
//			allSet.add(ti);
//			//fSetcopy.add(ti);
//		}
        /*
		
		for(Effect effect : ceg.getEffectNodes()){
			
			int comp = (int) Math.log(effect.getDNF_Exp().size());
			
			ArrayList<TestInput> ti = new ArrayList<>(); 
			ArrayList<TestInput> fTi = new ArrayList<>();
			
			String exp = effect.getRelation().getExpression();
			
			for(TestInput t : tSet){
				String md = exp;
				for(String s : t.getTestInput().keySet()){
					String key = s + " ";
					md = md.replace(key, t.getTestInput().get(s).toString().toLowerCase());
				}
				
				BooleanExpression be = BooleanExpression.readLeftToRight(md);
				if(be.booleanValue())
					ti.add(t);
			}
			
			
			
			for(TestInput t : fSet){
				String md = exp;
				for(String s : t.getTestInput().keySet()){
					String key = s + " ";
					md = md.replace(key, t.getTestInput().get(s).toString().toLowerCase());
				}
				
				BooleanExpression be = BooleanExpression.readLeftToRight(md);
				if(!be.booleanValue())
					fTi.add(t);
			}
			
			
			ArrayList<TestInput> select = new ArrayList<>();
			boolean flag = false;
			for(Cause c : ceg.getCauseNodes()){
				int count = 0;
				for(TestInput t1 : ti){
					for(TestInput t2 : fTi){
//						System.out.println(ti.getTestInput().keySet());
//						System.out.println(ti.getTestInput().values());
//						System.out.println(ti2.getTestInput().keySet());
//						System.out.println(ti2.getTestInput().values());
//						System.out.println("==============================");
//						System.out.println(ti.getTestInput().get(c.getLabel()));
//						System.out.println(ti2.getTestInput().get(c.getLabel()));
//						System.out.println("-------------------");
							if(t1.getTestInput().get(c.getLabel())!=t2.getTestInput().get(c.getLabel())){
								//System.out.println("hehe");
								flag = true;
								for(String s : t1.getTestInput().keySet()){
									if(!s.equals(c.getLabel())){
										if(!t1.getTestInput().get(s).equals(t2.getTestInput().get(s))){
											flag = false;
											break;
										}
									}
								}
								
								if(flag){
									select.add(t2);
									select.add(t1);
									count++;
									if(count==comp){
										break;
									}
								}
							}
						}
						if(count==comp){
							break;
						}
					}
						
			}
			
			allSet.addAll(select);
		}
		
		System.out.println(allSet.size());
		
		BufferedWriter bf4 = new BufferedWriter(new FileWriter("cutdeneme2.txt"));
		
		for(TestInput t : allSet){
			bf4.write(t.toString());
			bf4.newLine();
		}
		
		bf4.close();
         */
 /*
		ArrayList<TestInput> select = new ArrayList<>();
		boolean flag = false;
		for(Cause c : ceg.getCauseNodes()){
			int count = 0;
			for(TestInput ti : fSet){
				for(TestInput ti2 : tSet){
//					System.out.println(ti.getTestInput().keySet());
//					System.out.println(ti.getTestInput().values());
//					System.out.println(ti2.getTestInput().keySet());
//					System.out.println(ti2.getTestInput().values());
//					System.out.println("==============================");
//					System.out.println(ti.getTestInput().get(c.getLabel()));
//					System.out.println(ti2.getTestInput().get(c.getLabel()));
//					System.out.println("-------------------");
						if(ti.getTestInput().get(c.getLabel())!=ti2.getTestInput().get(c.getLabel())){
							//System.out.println("hehe");
							flag = true;
							for(String s : ti.getTestInput().keySet()){
								if(!s.equals(c.getLabel())){
									if(!ti.getTestInput().get(s).equals(ti2.getTestInput().get(s))){
										flag = false;
										break;
									}
								}
							}
							
							if(flag){
								select.add(ti2);
								select.add(ti);
								count++;
							}
						}
					}
					if(count==ceg.getEffectNodes().size()){
						break;
					}
				}
					
		}
		System.out.println(select.size());
		
		BufferedWriter bf3 = new BufferedWriter(new FileWriter("CUTPNFP2.txt"));
		
		//Set<TestInput> s = new HashSet<>();
		
		for(TestInput ti : select){
			bf3.write(ti.toString());
			bf3.newLine();
			//s.add(ti);
		}
		
		bf3.close();
		
		//System.out.println(s.size());
		System.out.println(allSet.size());
         */
        //MUMCUT mum = new MUMCUT(ceg);
        /*
		ArrayList<TestInput> set1 = new ArrayList<>();
		ArrayList<TestInput> set2 = new ArrayList<>();
		ArrayList<TestInput> set3 = new ArrayList<>();
		
		BufferedReader bf = new BufferedReader(new FileReader("MUMCUTOUTFF.txt"));
		
		String line = bf.readLine();
		int id = 0;
		while(line!=null){
			//System.out.println(line);
				TestInput ti = new TestInput(id);
				
				line = line.substring(0,line.lastIndexOf(","));
				String[] nodeList = line.split(" , ");
				
				for(String s : nodeList){
					String[] node = s.split(",");
					Value v;
					
					if(node[1].equals("True")){
						v = Value.True;
					}else{
						v = Value.True;
					}
					
					ti.addTestInput(node[0], v);
				}
				
			set1.add(ti);
			set2.add(ti);
			set3.add(ti);
				
			line = bf.readLine();
		}
		
		bf.close();
		
		id = 0;
		System.out.println(set1.size());
		System.out.println(set2.size());
		System.out.println(set3.size());
		
		for(int i = 0 ; i<set1.size()-1; i++){
			for(int j = i+1; j<set2.size(); j++){
				if(set1.get(i).isSame(set2.get(j))){
					set3.remove(set2.get(j));
					System.out.println("removed" + ++id);
				}
			}
		}
		
		System.out.println(set3.size());
		
		
		
/*		MUMCUT mum = new MUMCUT(ceg);
		
		//Set<TestInput> set = new HashSet<>();
		Set<TestInput> set2 = new HashSet<>();
		Set<TestInput> set3 = new HashSet<>();
		
		for(TestInput ti : mum.getAllTests()){
			set2.add(ti);
			set3.add(ti);
		}
		
		System.out.println(set3.size());
		
		//int count=0;
		for(TestInput ti : mum.getAllTests()){
			for(TestInput ti2 : set2){
				if(!ti.isSame(ti2)){
					if(ti.containSame(ti2) && set3.contains(ti2)){
						set3.remove(ti2);
						//System.out.println(set3.getTestInputs().size());
						//System.out.println("removed " + count++);
					}
				}
			}
		}
		
		BufferedWriter bf = new BufferedWriter(new FileWriter("MUMCUTOUTFF.txt"));
		
		System.out.println(set3.size());
		
		for(TestInput ti : set3){
			bf.write(ti.toString());
			bf.newLine();
		}
		
		bf.close();
         */
        //MI mi = new MI(ceg);
        /*
		FileReader fr = new FileReader("MIOUTFF.txt");
		BufferedReader bf2 = new BufferedReader(fr);
		
		Set<String> s = new HashSet<>();
		
		String line = bf2.readLine();
		
		while(line!=null){
			
			s.add(line);
			
			line = bf2.readLine();
		}
		
		System.out.println(s.size());
		
		TestSet set1 = new TestSet();
		TestSet set2 = new TestSet();
		TestSet set3 = new TestSet();
		
		String line = bf2.readLine();
		int id = 0;
		
		String key = line.substring(0, line.indexOf("="));// = line.substring(0, line.indexOf('='));
		List<CEG_Node> nodes = ceg.getEffectNode(key).getExpressionNodes(ceg);// = ceg.getEffectNode(key).getExpressionNodes(ceg);
		line = bf2.readLine();
		
		while(line!=null){
			
			if(line.contains("E")){
				key = line.substring(0, line.indexOf("="));
				nodes.clear();
				nodes = ceg.getEffectNode(key).getExpressionNodes(ceg);
			}else{
				
				TestInput ti = new TestInput(id);
				
				line = line.substring(0,line.lastIndexOf(","));
				String[] nodeList = line.split(",");
				
				for(String s : nodeList){
					ti.addTestInput(s, Value.True);
				}
				
				for(CEG_Node n : nodes){
					if(!ti.getTestInput().containsKey(n.getLabel())){
						ti.addTestInput(n.getLabel(), Value.False);
					}
				}
				
				set1.addTestInput(ti);
			}
			
			line = bf2.readLine();
		}
		
		FileReader fr3 = new FileReader("outMIFalse.txt");
		BufferedReader bf3 = new BufferedReader(fr3);
		
		//System.out.println(line);
		line = bf3.readLine();
		id = 0;
		
		key = line.substring(0, line.indexOf("="));
		//nodes.clear();
		//nodes = ceg.getEffectNode(key).getExpressionNodes(ceg);
		line = bf3.readLine();
		//System.out.println(line);
		while(line!=null){
			
			if(line.contains("E")){
				key = line.substring(0, line.indexOf("="));
				nodes.clear();
				nodes = ceg.getEffectNode(key).getExpressionNodes(ceg);
			}else{
				
				TestInput ti = new TestInput(id);
				
				line = line.substring(0,line.lastIndexOf(","));
				String[] nodeList = line.split(",");
				
				for(String s : nodeList){
					ti.addTestInput(s, Value.True);
				}
				
				for(CEG_Node n : nodes){
					if(!ti.getTestInput().containsKey(n.getLabel())){
						ti.addTestInput(n.getLabel(), Value.False);
					}
				}
				
				set1.addTestInput(ti);
			}
			
			line = bf3.readLine();
		}
		
		bf2.close();
		bf3.close();
		fr.close();
		fr3.close();
		
		for(TestInput ti : set1.getTestInputs()){
			set2.addTestInput(ti);
			set3.addTestInput(ti);
		}
		
		
		int count=0;
		for(TestInput ti : set1.getTestInputs()){
			for(TestInput ti2 : set2.getTestInputs()){
				if(!ti.isSame(ti2)){
					if(ti.containSame(ti2) && set3.getTestInputs().contains(ti2)){
						set3.getTestInputs().remove(ti2);
						//System.out.println(set3.getTestInputs().size());
						//System.out.println("removed " + count++);
					}
				}
			}
		}
		
		System.out.println(set3.getTestInputs().size());
		
		
		Set<TestInput> sett = new HashSet<>();
		
		sett.addAll(set3.getTestInputs());
		System.out.println(sett.size());
		
		
		
		BufferedWriter bf4 = new BufferedWriter(new FileWriter("MIOUTFF.txt"));
		
		for(TestInput t : sett){
			bf4.write(t.toString());
			bf4.newLine();
		}
		
		bf4.close();
         */
 /*for(TestInput ti :mi.getAllTests()){

			set1.addTestInput(ti);
			set2.addTestInput(ti);
			//bf.write(ts.toString());
			//bf.newLine();
		}
				
		int count=0;
		for(TestInput ti : mi.getAllTests()){
			for(TestInput ti2 : set1.getTestInputs()){
				if(!ti.isSame(ti2)){
					if(ti.containSame(ti2) && set2.getTestInputs().contains(ti2)){
						set2.getTestInputs().remove(ti2);
						System.out.println(set2.getTestInputs().size());
						System.out.println("removed " + count++);
					}
				}
			}
		}
		
		BufferedWriter bf = new BufferedWriter(new FileWriter("MIdeneme.txt"));
		
		System.out.println(set2.getTestInputs().size());
		
		for(TestInput ti : set2.getTestInputs()){
			bf.write(ti.toString());
			bf.newLine();
		}
		
		bf.close();*/
 /*
		FileReader f = new FileReader("UTP.txt");
		BufferedReader bf = new BufferedReader(f);
		
		/*String line = bf.readLine();
		
		Set<String> set = new HashSet<>();
		
		while(line!=null){
			set.add(line);
			line = bf.readLine();
		}
		
		System.out.println(set.size());*/
 /*
		FileWriter f2 = new FileWriter("MyersOUTFF.txt");
		BufferedWriter bf2 = new BufferedWriter(f2);
		
		String line = bf.readLine();
		
		Set<TestInput> set = new HashSet<>();
		int id = 0;
		while(line!=null){
			TestInput ti = new TestInput(id++);
			
			line = line.substring(0,line.lastIndexOf(","));
			
			String[] nodes = line.split(" , ");
			
			for(String s : nodes){
				
				s = s.substring(s.indexOf("(")+1,s.lastIndexOf(")"));
				String[] val = s.split(",");
				Value v;
				if(val[1].equals("True")){
					v = Value.True;
				}else{
					v = Value.False;
				}
				ti.addTestInput(val[0], v);
			}

			set.add(ti);
			
			line = bf.readLine();
		}
		
		System.out.println(set.size());
		
		
		
		
		
		bf.close();
		bf2.close();
		f.close();
		f2.close();
		
		/*Myers m = new Myers(ceg);
		
		FileWriter f = new FileWriter("MyersOUTFilt.txt");
		BufferedWriter bf = new BufferedWriter(f);
		
		TestSet set1 = new TestSet();
		TestSet set2 = new TestSet();
		
		for(TestInput ts : m.getTs().getTestInputs()){
			set1.addTestInput(ts);
			set2.addTestInput(ts);
			//bf.write(ts.toString());
			//bf.newLine();
		}
		int count=0;
		for(TestInput ti : m.getTs().getTestInputs()){
			for(TestInput ti2 : set1.getTestInputs()){
				if(!ti.isSame(ti2)){
					if(ti.containSame(ti2) && set2.getTestInputs().contains(ti2)){
						set2.getTestInputs().remove(ti2);
						System.out.println(set2.getTestInputs().size());
						System.out.println("removed " + count++);
					}
				}
			}
		}
		
		System.out.println(set2.getTestInputs().size());
		
		for(TestInput ti : set2.getTestInputs()){
			bf.write(ti.toString());
			bf.newLine();
		}
		
		
		bf.close();
		f.close();
		//MUMCUT mum = new MUMCUT(ceg);
/*
		FileReader f = new FileReader("MIPosTestFilter.txt");
		FileReader f2 = new FileReader("MINegTestFilter.txt");
		BufferedReader bf = new BufferedReader(f);
		BufferedReader bf2 = new BufferedReader(f2);
		
		
		
	//	String line = bf.readLine();
		
		
			//Effect e = ceg.getEffectNodes().get(1); ///////////////////////////
			
			Set<TestInput> trSet = new HashSet<>();
			Set<TestInput> falseSet = new HashSet<>();
			
			String line = bf.readLine();
			while(line!=null){
				//line = line.substring(0, line.lastIndexOf(","));
				String[] nodes = line.split(",");
				
				int count = 0;
				TestInput ti = new TestInput(count++);
				for(String s : nodes){
					ti.addTestInput(s, Value.True);
				}
				for(Cause c : ceg.getCauseNodes()){
					if(!ti.getTestInput().containsKey(c.getLabel())){
						ti.addTestInput(c.getLabel(), Value.False);
					}
				}
				
				trSet.add(ti);
				line = bf.readLine();
			}
			
			line = bf2.readLine();
			while(line!=null){
				//line = line.substring(0, line.lastIndexOf(","));
				String[] nodes = line.split(",");
				
				int count = 0;
				TestInput ti = new TestInput(count++);
				for(String s : nodes){
					ti.addTestInput(s, Value.True);
				}
				for(Cause c : ceg.getCauseNodes()){
					if(!ti.getTestInput().containsKey(c.getLabel())){
						ti.addTestInput(c.getLabel(), Value.False);
					}
				}
				
				falseSet.add(ti);
				line = bf2.readLine();
			}
			
			bf.close();
			bf2.close();
			f.close();
			f2.close();
			
			for(Effect e : ceg.getEffectNodes()){
				FileReader f3 = new FileReader("StuckAt1.txt");
				BufferedReader bf3 = new BufferedReader(f3);
			ArrayList<String> trvalues = new ArrayList<>();
			ArrayList<String> flvalues = new ArrayList<>();
			
			String exp = e.getRelation().getExpression();
			
			for(TestInput ti : trSet){
				String md = exp;
				for(String ss : ti.getTestInput().keySet()){
					md = md.replace(ss+" ",ti.getTestInput().get(ss).toString().toLowerCase());
				}
				if(!md.contains("C")){
					BooleanExpression be = BooleanExpression.readLeftToRight(md);
					trvalues.add(String.valueOf(be.booleanValue()));
				}
			}
			
			for(TestInput ti : falseSet){
				String md = exp;
				for(String ss : ti.getTestInput().keySet()){
					md = md.replace(ss+" ",ti.getTestInput().get(ss).toString().toLowerCase());
				}
				if(!md.contains("C")){
					BooleanExpression be = BooleanExpression.readLeftToRight(md);
					flvalues.add(String.valueOf(be.booleanValue()));
				}
			}
			
			
			//for(String sss: values)
			//System.out.println(sss);
			
			String s = e.getLabel();
			System.out.println(s);
			//f.write("\n");
			//TestSet t = m.getEv().getMap().get(s);
			//System.out.println("size " + t.getTestInputs().size());
			
		
			int count = 0;
			
			line = bf3.readLine();
			
			//line = bf.readLine();
			
			while(!line.contains(e.getLabel())){
				line = bf3.readLine();
			}
			
			line = bf3.readLine();
			
			while(line!=null){
				boolean flag = false;
				
				if(line.contains("E")){
					break;
				}
				
				int i = 0;
				for(TestInput ti : trSet){
					//TestInput ti = trSet.get(i);
					String md = line;
					
					for(String ss : ti.getTestInput().keySet()){
						md = md.replace(ss+" ",ti.getTestInput().get(ss).toString().toLowerCase());
					}
					
					if(!md.contains("C")){
						BooleanExpression be = BooleanExpression.readLeftToRight(md);
						if(!String.valueOf(be.booleanValue()).equals(trvalues.get(i))){
							count++;
							flag=true;
							break;
						}
					}
				//f.write(ti.toString());
					//System.out.println(ti);
				//f.write("\n");
						i++;
				}
				
				
				if(flag==false){
					i=0;
					for(TestInput ti : falseSet){
						
						String md = line;
						
						for(String ss : ti.getTestInput().keySet()){
							md = md.replace(ss+" ",ti.getTestInput().get(ss).toString().toLowerCase());
						}
						
						if(!md.contains("C")){
							BooleanExpression be = BooleanExpression.readLeftToRight(md);
							if(!String.valueOf(be.booleanValue()).equals(flvalues.get(i))){
								count++;
								flag=true;
								break;
							}
						}
						i++;
					}	
				}
				line = bf3.readLine();
			}
			bf3.close();
			f3.close();	
			System.out.println(count);
	}
			
			
			
		

		/*
		FileReader f = new FileReader("UTP.txt");
		FileReader f2 = new FileReader("NFP.txt");
		FileWriter f3 = new FileWriter("UTPModified.txt");
		FileWriter f4 = new FileWriter("NFPModified.txt");
		
		BufferedReader bf = new BufferedReader(f);
		BufferedReader bf2 = new BufferedReader(f2);
		BufferedWriter bf3 = new BufferedWriter(f3);	
		BufferedWriter bf4 = new BufferedWriter(f4);
		
		ArrayList<TestInput> utps = new ArrayList<>();
		ArrayList<TestInput> nfps = new ArrayList<>();
		
		String line = bf.readLine();
		
		int id = 0;
		while(line!=null){
			line = line.substring(0, line.lastIndexOf(","));
			String[] nodes = line.split(",");
			
			TestInput ti = new TestInput(id++, ceg);
			for(String s : nodes){
				ti.addTestInput(s, Value.True);
			}
			
			for(Cause c : ceg.getCauseNodes()){
				if(!ti.getTestInput().containsKey(c.getLabel())){
					ti.addTestInput(c.getLabel(), Value.False);
				}
			}
			
			utps.add(ti);
			bf3.write(ti.toString());
			bf3.newLine();
			line = bf.readLine();
		}
		
		line = bf2.readLine();
		
		while(line!=null){
			TestInput ti = new TestInput(id++, ceg);
			
			if(!line.trim().equals("")){
				line = line.substring(0, line.lastIndexOf(","));
				String[] nodes = line.split(",");
			
				
				for(String s : nodes){
					ti.addTestInput(s, Value.True);
				}
			}
			for(Cause c : ceg.getCauseNodes()){
				if(!ti.getTestInput().containsKey(c.getLabel())){
					ti.addTestInput(c.getLabel(), Value.False);
				}
			}
			
			nfps.add(ti);
			bf4.write(ti.toString());
			bf4.newLine();
			line = bf2.readLine();
		}
			
         */
 /*
		FileWriter f = new FileWriter("UTP.txt");
		BufferedWriter bf = new BufferedWriter(f);
		
		for(UTP utp : mum.getUtps()){
			for(List<CEG_Node> l : utp.getUtpSet()){
				for(CEG_Node n : l){
					bf.write(n.getLabel()+",");
				}
				bf.newLine();
			}
			
		}
		bf.close();f.close();
		FileWriter f2 = new FileWriter("NFP.txt");
		BufferedWriter bf2 = new BufferedWriter(f2);
		
		for(NFP nfp : mum.getNfps()){
			for(List<CEG_Node> l : nfp.getNFP()){
				for(CEG_Node n : l){
					bf2.write(n.getLabel()+",");	
				}
				bf2.newLine();
			}
			
		}
		bf2.close();	f2.close();
		FileWriter f3 = new FileWriter("CUTPNFP.txt");
		BufferedWriter bf3 = new BufferedWriter(f3);
		
		for(List<CEG_Node> l : mum.getCutpnfpAll()){
			for(CEG_Node n : l){
				bf3.write(n.getLabel()+",");
			}
			bf3.newLine();
		}
		bf3.close();
		f3.close();
		//bf4.close();
		//f4.close();
		
		/*FileReader f = new FileReader("MUMCUTfilter.txt");
		BufferedReader bf = new BufferedReader(f);
		
		Set<String> tests = new HashSet<>();
		
		String line = bf.readLine();
		
		while(line!=null){
			//if(line.contains("E") || line.contains("UTP") || line.contains("NFP")){
			//}else{
				tests.add(line);
			//}
			line = bf.readLine();
		}
		bf.close();
		f.close();
		
		FileWriter f2 = new FileWriter("MUMCUTfilter.txt");
		BufferedWriter bf2 = new BufferedWriter(f2);
		
		for(String s : tests){
			bf2.write(s);
			bf2.newLine();	
			bf2.close();
			f2.close();
			
		}
	
		
		System.out.println("tests : " + tests.size());
		
		
		for(Effect e : ceg.getEffectNodes()){

		System.out.println(e.getLabel());
		ArrayList<String> values = new ArrayList<>();
		String exp = e.getRelation().getExpression();
		
		for(String s : tests){
			String md = exp;
			s = s.substring(0,s.lastIndexOf(","));
			String[] nodes = s.split(",");
			
			for(String ss : nodes){
				String check = ss+" ";
				
				while(md.contains(check)){
					md = md.replace(check, "true ");
				}
			}
			
			while(md.contains("C")){
				int st = md.indexOf("C");
				int en = md.indexOf(" ", st);
				md = md.replaceFirst(md.substring(st, en),"false");
			}
				
	
			BooleanExpression be = BooleanExpression.readLeftToRight(md);
			values.add(String.valueOf(be.booleanValue()));
		
		}
		//System.out.println("values : " + values.size());
		FileReader f2 = new FileReader("StuckAt0.txt");
		BufferedReader bf2 = new BufferedReader(f2);
		int count = 0;
		line = bf2.readLine();
		
		//line = bf.readLine();
		
		while(!line.contains(e.getLabel())){
			line = bf2.readLine();
		}
		
		line = bf2.readLine();
		
		while(line!=null){
			
			if(line.contains("E")){
				break;
			}
			int i = 0;
			for(String ti : tests){
				String md = line;

				ti = ti.substring(0,ti.lastIndexOf(","));
				String[] nodes = ti.split(",");
				
				for(String ss : nodes){
					String check = ss+" ";
					
					while(md.contains(check)){
						md = md.replace(check, "true ");
					}
				}
				
				while(md.contains("C")){
					int st = md.indexOf("C");
					int en = md.indexOf(" ", st);
					md = md.replaceFirst(md.substring(st, en),"false");
				}
				
				
				BooleanExpression be = BooleanExpression.readLeftToRight(md);
				if(!String.valueOf(be.booleanValue()).equals(values.get(i))){
					count++;
					break;
				}
				i++;
			}
			//f.write(ti.toString());
				//System.out.println(ti);
			//f.write("\n");
			
			line = bf2.readLine();
		}

		bf2.close();
		f2.close();
		System.out.println(count);

		}*/
 /*
		ArrayList<String> set = new ArrayList<>();
		
		try {
			FileReader f = new FileReader("outMIFilter.txt");
			BufferedReader bf = new BufferedReader(f);
			String line = bf.readLine();
			
			while(line!=null){ 
				set.add(line);
				line = bf.readLine();
			}	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
         */
 /*
		Effect effect = ceg.getEffectNodes().get(3);
		TE te = new TE(ceg, effect, ceg.getDc().getSelectedSet());
		FE fe = new FE(ceg, effect, ceg.getDc().getSelectedSet());
		Set<List<CEG_Node>> trues = new HashSet<>();
		
		Set<List<CEG_Node>> falses = fe.getFSE();
		Set<List<CEG_Node>> falseFilter = fe.getFSE();
		
		for(TEi tei : te.getTEis()){
			trues.addAll(tei.getTrueSet());
		}
			 	
		for(List<CEG_Node> l1 : falses){
			
			for(List<CEG_Node> l2 : trues){
				
				if(l1.size() == l2.size()){
					
				
				int count = 0;
				for(CEG_Node c1 : l1){
					for(CEG_Node c2 : l2){
						if(c1.getLabel().equals(c2.getLabel())){
							count++;
							break;
						}
					}
				}
					if(count == l1.size()){
						return true;
					}
				}
				
			}
			
			
		}
		
         */
 /*
		EffectValues ev = new EffectValues(ceg);
		for(String s : ev.getMap().keySet()){
			System.out.println(s);
			for(TestInput ti: ev.getMap().get(s).getTestInputs()){
				System.out.println(ti);
			}
		}
		System.out.println();
		InterValues iv = new InterValues(ceg);
		for(String s : iv.getMap().keySet()){
			System.out.println(s);
			for(TestInput ti: iv.getMap().get(s).getTestInputs()){
				System.out.println(ti);
			}
		}
         */
        //Myers m = new Myers(ceg);
        //m.fillTestInputs(ceg.getEffectNodes().get(3).getLabel());
        //t = m.fillTestInputs(ceg.getEffectNodes().get(0).getLabel());
        //t = m.fillTestInputs(ceg.getEffectNodes().get(0).getLabel());
        //try {
        //FileWriter f = new FileWriter("Myers.txt");
        //BufferedWriter bf = new BufferedWriter(f);
        /*
		TestSet t = m.getTs();
		
		System.out.println(t.getTestInputs().size());
		ArrayList<TestInput> orj = new ArrayList<>();
		orj.addAll(t.getTestInputs());
		ArrayList<TestInput> tests = new ArrayList<>();
		
		for(TestInput ti: orj){
			for(TestInput ti2 : orj){
				if(!ti.isSame(ti2)){
					if(ti.containSame(ti2)){
						System.out.println("eliminating: ");
						System.out.println(ti);
						System.out.println(ti2);
						continue;
					}
				}
			}
			tests.add(ti);
		}
		
		System.out.println(tests.size());
         */
 /*
		for(int i=0 ; i<tests.size(); i++){
			for(int j=i+1; j<tests.size();  j++)
			{
				if(tests.get(j).getTestInput().keySet().containsAll(tests.get(i).getTestInput().keySet())
						|| tests.get(i).getTestInput().keySet().containsAll(tests.get(j).getTestInput().keySet())){
					boolean flag = true; 
					for(String s : tests.get(i).getTestInput().keySet()){
						for(String s2 : tests.get(j).getTestInput().keySet()){
							if(s.equals(s2) && tests.get(i).getTestInput().get(s)!=tests.get(j).getTestInput().get(s2)){
								flag = false;
							}
						}
					}
					
					if(flag){
						if(tests.get(i).getTestInput().size()<tests.get(j).getTestInput().size()){
							t.getTestInputs().remove(tests.get(i).getTestInput());
						}else{
							t.getTestInputs().remove(tests.get(j).getTestInput());
						}
					}
				}
				
				
			}
			
		}
		System.out.println(t.getTestInputs().size());
		
		//Set<TestInput> set = new HashSet<>();
		
		//set.addAll(t.getTestInputs());
		
		//System.out.println(set.size());
		
		/*
		for(Effect e : ceg.getEffectNodes()){
				
				//Effect e = ceg.getEffectNodes().get(1); ///////////////////////////
				
				ArrayList<String> values = new ArrayList<>();
				String exp = e.getRelation().getExpression();
				for(TestInput ti : t.getTestInputs()){
					String md = exp;
					for(String ss : ti.getTestInput().keySet()){
						md = md.replace(ss+" ",ti.getTestInput().get(ss).toString().toLowerCase());
					}
					if(!md.contains("C")){
						BooleanExpression be = BooleanExpression.readLeftToRight(md);
						values.add(String.valueOf(be.booleanValue()));
					}else{
						values.add("NA");
					}
				}
				
				//for(String sss: values)
				//System.out.println(sss);
				
				String s = e.getLabel();
				System.out.println(s);
				//f.write("\n");
				//TestSet t = m.getEv().getMap().get(s);
				System.out.println("size " + t.getTestInputs().size());
				
				FileReader f = new FileReader("VariableReference.txt");
				BufferedReader bf = new BufferedReader(f);
				int count = 0;
				String line = bf.readLine();
				
				//line = bf.readLine();
				
				while(!line.contains(e.getLabel())){
					line = bf.readLine();
				}
				
				line = bf.readLine();
				
				while(line!=null){
					
					if(line.contains("E")){
						break;
					}
					
					for(int i = 0; i< t.getTestInputs().size() ; i++){
						TestInput ti = t.getTestInputs().get(i);
						String md = line;
						
						for(String ss : ti.getTestInput().keySet()){
							md = md.replace(ss+" ",ti.getTestInput().get(ss).toString().toLowerCase());
						}
						
						if(!md.contains("C")){
							BooleanExpression be = BooleanExpression.readLeftToRight(md);
							if(!String.valueOf(be.booleanValue()).equals(values.get(i))){
								count++;
								break;
							}
						}
					//f.write(ti.toString());
						//System.out.println(ti);
					//f.write("\n");
					}
					line = bf.readLine();
				}
				bf.close();
				f.close();
				System.out.println(count);
		}
         */
        //}
        /*
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
         */
 /*
		MI mi = new MI(ceg);
		
		HashMap<String,ArrayList<String>> map = new HashMap<>();
		HashMap<String,ArrayList<String>> mapF = new HashMap<>();
		
		for(SE se : mi.getSEs()){
			ArrayList<String> cs = new ArrayList<>();
			TE t = se.getTe();
			FE f = se.getFe();
			
			for(FEi fi : f.getFEis()){
				
				for(List<CEG_Node> n :fi.getFalseSet()){
					String s = "";
					for(CEG_Node c : n){
						s+=c.getLabel()+",";
					}
					cs.add(s);
				}
			}
			
			map.put(se.getEffect().getLabel(), cs);
		}
		
		//mi.printTSE();
		
		for(String s: map.keySet()){
			System.out.println(s);
			for(String sa : map.get(s)){
				System.out.println(sa);
			}
		}
         */
 /*
		HashMap<String,ArrayList<String>> map = new HashMap<>();		
		
		try {
			FileReader f = new FileReader("outMITrue.txt");
			BufferedReader bf = new BufferedReader(f);
			
			String line = bf.readLine();
			
			String key = line.substring(0, line.indexOf("="));
			
			line = bf.readLine();
			ArrayList<String> cs = new ArrayList<>();
			while(line!=null){
				if(line.contains("E")){
					map.put(key, cs);
					cs.clear();
					key = line.substring(0, line.indexOf("="));
				}else{
					cs.add(line);
				}
				line = bf.readLine();
			}
			map.put(key, cs);
			cs.clear();
			
			HashMap<String,ArrayList<String>> cln = new HashMap<>();
			
			for(String s : map.keySet()){
				cln.put(s, map.get(s));
			}

			boolean flag = false;
			boolean oflag = false;
			
			ArrayList<String> keys = new ArrayList<>();			
			for(String sa : map.keySet()){
				System.out.println(sa);
			}
			
			for(int i=0; i<keys.size()-1;i++){
				for(String s : map.get(keys.get(i))){
					oflag = false;
					for(int j=i+1; j<keys.size();j++){
						
						if(map.get(keys.get(j)).contains(s)){
							flag = true;
							oflag = true;
						}
						
						if(flag){
							cln.get(keys.get(j)).remove(s);
							flag = false;
						}
					}
					if(oflag){
						cln.get(keys.get(i)).remove(s);
					}
				}
			}
		
			map.clear();
			map = cln;
			
			FileWriter ff = new FileWriter("mideneme.txt");
			BufferedWriter bff = new BufferedWriter(ff);
			
			for(String k : map.keySet()){
				bff.write(k);
				bff.newLine();
				System.out.println(k);
				System.out.println(map.get(k).size());
				for(String s : map.get(k)){
					bff.write(s);
					bff.newLine();
					System.out.println(s);
				}
			}
			
			bf.close();
			f.close();
			bff.close();
			ff.close();
		}catch(IOException e){
			
		}
		
		
		
         */
 /*
		Set<String> set = new HashSet<>();
		Set<String> falseSet = new HashSet<>();
		
		try {
			FileReader f = new FileReader("outMITrueFilter.txt");
			BufferedReader bf = new BufferedReader(f);
			
			FileReader f3 = new FileReader("outMIFalseFilter.txt");
			BufferedReader bf3 = new BufferedReader(f3);
			
			FileWriter f2 = new FileWriter("outMITrueModified.txt");
			BufferedWriter bf2 = new BufferedWriter(f2);
		
			String line = bf.readLine();
			int count=0;
			
			while(line!=null){
				TestInput ti = new TestInput(++count);
				ti.createMap(ceg);
				
				int e = line.lastIndexOf(',');
				line.substring(0,e);
				String[] causes = line.split("\\,");
				
				for(String s : causes){
					ti.changeValue(s, Value.True);
				}
				bf2.write(ti.toString());
				bf2.newLine();
				
				line = bf.readLine();
			}
			
         */
 /*
			while(line!=null){
				set.add(line);
				line = bf.readLine();
			}
			
			line = bf3.readLine();
			
			while(line!=null){
				//if(!line.contains("E")){
					falseSet.add(line);
				//}
				line = bf3.readLine();
			}

			for(String s : falseSet){
				if(!set.contains(s)){
					bf2.write(s);
					bf2.newLine();
				}
			}
         */
 /*
			bf3.close();
			f3.close();
			bf.close();
			bf2.close();
			f.close();
			f2.close();
			
		}catch(IOException e){
			
		}
		
         */
        //MI mi = new MI(ceg);
        /*
		TestSet trSet = new TestSet();
		//TestSet flSet = new TestSet();
		//ArrayList<String> effect = new ArrayList<>();
		
		
		try {
			FileReader f = new FileReader("MyersWithoutNA.txt");
			BufferedReader bf = new BufferedReader(f);
		
			FileReader f2 = new FileReader("VariableReference.txt");
			BufferedReader bf2 = new BufferedReader(f2);
			
			//FileReader f3 = new FileReader("outMIFalseModified.txt");
			//BufferedReader bf3 = new BufferedReader(f3);
			int count = 0;
			String line = bf.readLine();
			line = bf.readLine();
			
			while(line != null){
				
				if(line.contains("E"))
					line = bf.readLine();
				
				line = line.substring(0, line.lastIndexOf(','));
				String[] values = line.split(" , ");
				TestInput ti = new TestInput(0,ceg);
				for(String s: values){
					s = s.substring(s.indexOf('(')+1,s.lastIndexOf(')'));
					String[] comp = s.split(",");
					Value v;
					if(comp[1].equals("True")){
						v = Value.True;
					}else{
						v = Value.False;
					}
					ti.addTestInput(comp[0], v);
					//System.out.println(comp[0]);
					//System.out.println(comp[1]);
					//modifiedExp = modifiedExp.replaceAll(comp[0],comp[1].toLowerCase());
					//System.out.println(modifiedExp);
				}
				
				trSet.addTestInput(ti);
				
				line = bf.readLine();
			}
			
			bf.close();
			f.close();
			
			/*
			line = bf3.readLine();
			line = bf3.readLine();
			
			while(line != null){
				
				if(line.contains("E"))
					line = bf3.readLine();
				
				line = line.substring(0, line.lastIndexOf(','));
				String[] values = line.split(" , ");
				TestInput ti = new TestInput(0);
				for(String s: values){
					s = s.substring(s.indexOf('(')+1,s.lastIndexOf(')'));
					String[] comp = s.split(",");
					Value v;
					if(comp[1].equals("True")){
						v = Value.True;
					}else{
						v = Value.False;
					}
					ti.addTestInput(comp[0], v);
					//System.out.println(comp[0]);
					//System.out.println(comp[1]);
					//modifiedExp = modifiedExp.replaceAll(comp[0],comp[1].toLowerCase());
					//System.out.println(modifiedExp);
				}
				
				flSet.addTestInput(ti);
				
				line = bf3.readLine();
			}
			
			bf3.close();
			f3.close();
         *//*
			ArrayList<String> trval = new ArrayList<>();
			//ArrayList<String> flval = new ArrayList<>();
			
			Effect ef = ceg.getEffectNodes().get(1);
			System.out.println(ef.getLabel());
			String exp = ef.getRelation().getExpression();
			
			for(TestInput t : trSet.getTestInputs()){
				String md = exp;
				for(String c : t.getTestInput().keySet()){
					String value = t.getTestInput().get(c).toString().toLowerCase();
					md = md.replace(c, value);
				}
				if(!md.contains("C")){
					BooleanExpression be = BooleanExpression.readLeftToRight(md);
					trval.add(String.valueOf(be.booleanValue()));
				}
				//System.out.println(be.booleanValue());
			}
			/*
				for(TestInput t : flSet.getTestInputs()){
					String md = exp;
					for(String c : t.getTestInput().keySet()){
						String value = t.getTestInput().get(c).toString().toLowerCase();
						md = md.replace(c, value);
					}
					BooleanExpression be = BooleanExpression.readLeftToRight(md);
					flval.add(String.valueOf(be.booleanValue()));
					
					//System.out.println(be.booleanValue());
				}
         */
        //boolean flag = false;
        /*
				line = bf2.readLine();
				line = bf2.readLine();

				while(!line.contains(ef.getLabel())){
					line = bf2.readLine();
				}
				line = bf2.readLine();
				while(!line.contains("E")){
				//while(line!=null){
					//flag = false;
					for(int i=0; i< trSet.getTestInputs().size(); i++){
						TestInput t = trSet.getTestInputs().get(i);
						String md = line;
						for(String c : t.getTestInput().keySet()){
							String value = t.getTestInput().get(c).toString();
							md = md.replace(c, value.toLowerCase());
						}
						if(!md.contains("C")){
							BooleanExpression be = BooleanExpression.readLeftToRight(md);
							if(!String.valueOf(be.booleanValue()).equals(trval.get(i))){
								count++;
								//flag = true;
								break;
							}
						}
						//System.out.println(be.booleanValue());
					}
						/*if(flag==false){
							for(int i=0; i< flSet.getTestInputs().size(); i++){
								TestInput t = flSet.getTestInputs().get(i);
								String md = line;
								for(String c : t.getTestInput().keySet()){
									String value = t.getTestInput().get(c).toString().toLowerCase();
									md = md.replace(c, value);
								}
								BooleanExpression be = BooleanExpression.readLeftToRight(md);
								if(!String.valueOf(be.booleanValue()).equals(flval.get(i))){
									count++;
									flag = true;
									break;
								}
							}
						}
         */
        //line = bf2.readLine();
        //}

        /*
			boolean flag = false;
			line = bf2.readLine();
			line = bf2.readLine();
			
			while(!line.contains("E")){
				flag = false;
				
				for(TestInput t : trSet.getTestInputs()){
					String md = line;
					for(String c : t.getTestInput().keySet()){
						String value = t.getTestInput().get(c).toString().toLowerCase();
						md = md.replace(c, value);
					}
					BooleanExpression be = BooleanExpression.readLeftToRight(md);
					
					System.out.println(be.booleanValue());
				}
				
					for(TestInput t : flSet.getTestInputs()){
						String md = line;
						for(String c : t.getTestInput().keySet()){
							String value = t.getTestInput().get(c).toString().toLowerCase();
							md = md.replace(c, value);
						}
						BooleanExpression be = BooleanExpression.readLeftToRight(md);
						if(be.booleanValue() == true){
							count++;
							flag = true;
							break;
						}
					}
				
				
				line = bf2.readLine();
			}
         *//*
			bf2.close();
			f2.close();
			
			System.out.println(count);
			
		}catch(IOException e){
			e.printStackTrace();
		} catch (MalformedBooleanException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/*
		try {
			FileReader f = new FileReader("outMITrueModified.txt");
			BufferedReader bf = new BufferedReader(f);
		
			FileReader f2 = new FileReader("ClauseConjunction.txt");
			BufferedReader bf2 = new BufferedReader(f2);
			
			FileReader f3 = new FileReader("outMIFalseModified.txt");
			BufferedReader bf3 = new BufferedReader(f3);
			int count = 0;
			String line = bf.readLine();
			line = bf.readLine();
			
			while(!line.contains("E20")){
				line = bf.readLine();
			}
			line = bf.readLine();
			
			while(line != null){
				
				line = line.substring(0, line.lastIndexOf(','));
				String[] values = line.split(" , ");
				TestInput ti = new TestInput(0);
				for(String s: values){
					s = s.substring(s.indexOf('(')+1,s.lastIndexOf(')'));
					String[] comp = s.split(",");
					Value v;
					if(comp[1].equals("True")){
						v = Value.True;
					}else{
						v = Value.False;
					}
					ti.addTestInput(comp[0], v);
					//System.out.println(comp[0]);
					//System.out.println(comp[1]);
					//modifiedExp = modifiedExp.replaceAll(comp[0],comp[1].toLowerCase());
					//System.out.println(modifiedExp);
				}
				
				trSet.addTestInput(ti);
				
				line = bf.readLine();
			}
			
			bf.close();
			f.close();
			
			line = bf3.readLine();
			line = bf3.readLine();
			
			while(!line.contains("E20")){
				line = bf3.readLine();
			}
			line = bf3.readLine();
			
			while(line != null){
				
				line = line.substring(0, line.lastIndexOf(','));
				String[] values = line.split(" , ");
				TestInput ti = new TestInput(0);
				for(String s: values){
					s = s.substring(s.indexOf('(')+1,s.lastIndexOf(')'));
					String[] comp = s.split(",");
					Value v;
					if(comp[1].equals("True")){
						v = Value.True;
					}else{
						v = Value.False;
					}
					ti.addTestInput(comp[0], v);
					//System.out.println(comp[0]);
					//System.out.println(comp[1]);
					//modifiedExp = modifiedExp.replaceAll(comp[0],comp[1].toLowerCase());
					//System.out.println(modifiedExp);
				}
				
				flSet.addTestInput(ti);
				
				line = bf3.readLine();
			}
			
			bf3.close();
			f3.close();
			
			line = bf2.readLine();
			line = bf2.readLine();
			
			while(!line.contains("E20")){
				line = bf2.readLine();
			}
			line = bf2.readLine();
			
			boolean flag = false;
			
			while(line != null){		
				//System.out.println(line);
				flag = false;
				for(TestInput t : trSet.getTestInputs()){
					String md = line;
					for(String c : t.getTestInput().keySet()){
						String value = t.getTestInput().get(c).toString().toLowerCase();
						md = md.replace(c, value);
					}
					BooleanExpression be = BooleanExpression.readLeftToRight(md);
					if(be.booleanValue() == false){
						flag = true;
						count++;
						break;
					}
				}
				if(flag==false){
					for(TestInput t : flSet.getTestInputs()){
						String md = line;
						for(String c : t.getTestInput().keySet()){
							String value = t.getTestInput().get(c).toString().toLowerCase();
							md = md.replace(c, value);
						}
						BooleanExpression be = BooleanExpression.readLeftToRight(md);
						if(be.booleanValue() == true){
							flag = true;
							count++;
							break;
						}
					}
					/*
					if(flag == false){
						System.out.println("yakalanamad�");
					}
					
				}
				line = bf2.readLine();
			}
			bf2.close();
			f2.close();
			System.out.println(count);

			/*
			int count = 0;
			
			String line = bf2.readLine();
			boolean flag = false;
			String ef1 = line.trim();
			String exp = ceg.getEffectMap().get(ef1).get(0).getExpression();
			line = bf2.readLine();

			while(line != null){
				if(line.contains("E")){
					ef1 = line.trim();
					exp = ceg.getEffectMap().get(ef1).get(0).getExpression();
				}else{
					flag = false;
					String line2 = bf.readLine();
					 
					while(line2 != null){
						if(line2.contains("E")){
							String ef2 = line2.trim();
							if(!ef1.equals(ef2)){
								if(flag == false){
									line2 = bf.readLine();
									break;
								}
							}
						}else{
							String modifiedExp = exp;
							line2 = line2.substring(0, line2.lastIndexOf(','));
							String[] values = line2.split(" , ");
							
							for(String s: values){
								s = s.substring(s.indexOf('(')+1,s.lastIndexOf(')'));
								String[] comp = s.split(",");
								//System.out.println(comp[0]);
								//System.out.println(comp[1]);
								modifiedExp = modifiedExp.replaceAll(comp[0],comp[1].toLowerCase());
								//System.out.println(modifiedExp);
							}
							
							//System.out.println(modifiedExp);
							BooleanExpression be = BooleanExpression.readLeftToRight(modifiedExp);
							if(be.booleanValue()==false){
								flag = true;
								count++;
								//System.out.print(count);
								//System.out.println();
								line2 = bf.readLine();
								break;
							}
							
						}
					
					
						
						line2 = bf.readLine();
					}
					
					String line3 = bf.readLine();
					
					while(line3 !=null){
						if(line3.contains("E")){
							String ef2 = line3.trim();
							if(!ef1.equals(ef2)){
								if(flag == false){
									line3 = bf.readLine();
									break;
								}
							}
						}else{
							String modifiedExp = exp;
							line3 = line3.substring(0, line3.lastIndexOf(','));
							String[] values = line3.split(" , ");
							
							for(String s: values){
								s = s.substring(s.indexOf('(')+1,s.lastIndexOf(')'));
								String[] comp = s.split(",");
								//System.out.println(comp[0]);
								//System.out.println(comp[1]);
								modifiedExp = modifiedExp.replaceAll(comp[0],comp[1].toLowerCase());
								//System.out.println(modifiedExp);
							}
							
							//System.out.println(modifiedExp);
							BooleanExpression be = BooleanExpression.readLeftToRight(modifiedExp);
							if(be.booleanValue()==true){
								flag = true;
								count++;
								//System.out.print(count);
								//System.out.println();
								line3 = bf.readLine();
								break;
							}
							
						}
					
					
						
						line3 = bf.readLine();
						
						
					}
					
				}
				
				
				
				line = bf2.readLine();
			}
			
			System.out.println(count);
			
			bf.close();
			f.close();
			bf2.close();
			f2.close();
         */
 /*
			FileWriter f2 = new FileWriter("MITrueValues.txt");
			BufferedWriter bw = new BufferedWriter(f2);
			
			String line = bf.readLine();
			String eff = line.trim();
			bw.write(eff + " = ");
			String exp = ceg.getEffectMap().get(eff).get(0).getExpression();
			line = bf.readLine();
			
			while(line!=null){
				
				if(line.contains("E")){
					eff = line.trim();
					bw.newLine();
					bw.write(eff + " = ");
					exp = ceg.getEffectMap().get(eff).get(0).getExpression();
					
				}else{
					String modifiedExp = exp;
					line = line.substring(0, line.lastIndexOf(','));
					String[] values = line.split(" , ");
					
					for(String s: values){
						s = s.substring(s.indexOf('(')+1,s.lastIndexOf(')'));
						String[] comp = s.split(",");
						//System.out.println(comp[0]);
						//System.out.println(comp[1]);
						modifiedExp = modifiedExp.replaceAll(comp[0],comp[1].toLowerCase());
						//System.out.println(modifiedExp);
					}
					System.out.println(modifiedExp);
					BooleanExpression be = BooleanExpression.readLeftToRight(modifiedExp);
					bw.write(be.booleanValue() + " , ");
				}
	
				line = bf.readLine();
			}
			
			
			
			BooleanExpression be = BooleanExpression.readLeftToRight(" ");
			
			if(be.booleanValue()){
				
			}		

			bw.close();
			f2.close();
			bf.close();
			f.close();
         */
 /*
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (MalformedBooleanException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
         */
 /*
		try {
			FileWriter f = new FileWriter("Effects.txt");
			BufferedWriter bf = new BufferedWriter(f);
			
			for(Effect e  : ceg.getEffectNodes()){
				System.out.println(e.getRelation().getExpression());
				bf.write(e.getLabel() + "=");
				bf.write(e.getRelation().getExpression());
				bf.newLine();
			}
			
			bf.close();
			f.close();
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
         */
 /*
		EffectValues ev = new EffectValues(ceg);
		System.out.println(ev.getMap().keySet());
		
		for(String s : ev.getMap().keySet()){
			System.out.println(s + ": ");
			System.out.println(ev.getMap().get(s));
		}
         */
 /*
		InterValues iv = new InterValues(ceg);
		System.out.println(iv.getMap().keySet());
		
		for(String s : iv.getMap().keySet()){
			System.out.println(s + ": ");
			System.out.println(iv.getMap().get(s));
		}
		
         */

 /*
		FileWriter f;
		BufferedWriter bw;
		
		ArrayList<ClauseConjunction> cc = new ArrayList<>();
		ArrayList<ClauseDisjunction> cd = new ArrayList<>();
		ArrayList<ExpressionNegation> en = new ArrayList<>();
		ArrayList<OperatorReference> or = new ArrayList<>();
		ArrayList<StuckAt0> s0 = new ArrayList<>();
		ArrayList<StuckAt1> s1 = new ArrayList<>();
		ArrayList<VariableNegation> vn = new ArrayList<>();
		ArrayList<VariableReference> vr = new ArrayList<>();
		
		//ArrayList<Mutant> allMutants = new ArrayList<>(); 
		
		
		int count = 0;
		try {
			for(Effect e : ceg.getEffectNodes()){
				//ArrayList<Mutant> mutants = new ArrayList<>();
				
				cc.add(new ClauseConjunction(count++, e));
				cd.add(new ClauseDisjunction(count++, e));
				en.add(new ExpressionNegation(count++, e));
				or.add(new OperatorReference(count++, e));
				s0.add(new StuckAt0(count++, e));
				s1.add(new StuckAt1(count++, e));
				vn.add(new VariableNegation(count++, e));
				vr.add(new VariableReference(count++, e));
				
				//allMutants.addAll(mutants);

			}
			
			
			String s = "ClauseConjunction.txt";
			
			f = new FileWriter(s);
			bw = new BufferedWriter(f);
			
			for(Mutant m : cc){
				bw.write(m.getEffect().getLabel() + " = ");
				for(String st : m.createMutant()){
					bw.write(st);
					bw.newLine();
				}

			}
			
			bw.close();
			f.close();
			
			s = "ClauseDisjunction.txt";
			
			f = new FileWriter(s);
			bw = new BufferedWriter(f);
			
			for(Mutant m : cd){
				bw.write(m.getEffect().getLabel() + " = ");
				for(String st : m.createMutant()){
					bw.write(st);
					bw.newLine();
				}

			}
			
			bw.close();
			f.close();
			
			s = "ExpressionNegation.txt";
			
			f = new FileWriter(s);
			bw = new BufferedWriter(f);
			
			for(Mutant m : en){
				bw.write(m.getEffect().getLabel() + " = ");
				for(String st : m.createMutant()){
					bw.write(st);
					bw.newLine();
				}

			}
			
			bw.close();
			f.close();
			
			s = "OperatorReference.txt";
			
			f = new FileWriter(s);
			bw = new BufferedWriter(f);
			
			for(Mutant m : or){
				bw.write(m.getEffect().getLabel() + " = ");
				for(String st : m.createMutant()){
					bw.write(st);
					bw.newLine();
				}
				
			}
			
			bw.close();
			f.close();
			
			s = "StuckAt0.txt";
			
			f = new FileWriter(s);
			bw = new BufferedWriter(f);
			
			for(Mutant m : s0){
				bw.write(m.getEffect().getLabel() + " = ");
				for(String st : m.createMutant()){
					bw.write(st);
					bw.newLine();
				}
	
			}
			
			bw.close();
			f.close();
			
			s = "StuckAt1.txt";
			
			f = new FileWriter(s);
			bw = new BufferedWriter(f);
			
			for(Mutant m : s1){
				bw.write(m.getEffect().getLabel() + " = ");
				for(String st : m.createMutant()){
					bw.write(st);
					bw.newLine();
				}
			}
			
			
			bw.close();
			f.close();
			
			
			s = "VariableNegation.txt";
			
			f = new FileWriter(s);
			bw = new BufferedWriter(f);
			
			for(Mutant m : vn){
				bw.write(m.getEffect().getLabel() + " = ");
				for(String st : m.createMutant()){
					bw.write(st);
					bw.newLine();
				}	
			}
			
			bw.close();
			f.close();
			
			s = "VariableReference.txt";
			
			f = new FileWriter(s);
			bw = new BufferedWriter(f);
			
			for(Mutant m : vr){
				bw.write(m.getEffect().getLabel() + " = ");
				for(String st : m.createMutant()){
					bw.write(st);
					bw.newLine();
				}

			}
			
			bw.close();
			f.close();		
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
         */
 /*
		System.out.println();
		System.out.println("Effect nodes with their relations:");
		System.out.println(ceg.getEffectMap().keySet());
		System.out.println(ceg.getEffectMap().get("E3").get(0).getRelatedNodes().get(1).isNegated());
		System.out.println(ceg.getEffectMap().entrySet());
		
		System.out.println();
		System.out.println("Intermediate nodes with their relations:");
		System.out.println(ceg.getInterMap().keySet());
		System.out.println(ceg.getInterMap().entrySet());
		
		System.out.println();
		System.out.println("An initialized test input example:");
		TestInput ti = new TestInput(1);
		System.out.println(ti.getTestInput().keySet());
		System.out.println(ti.getTestInput().entrySet());
		
         */
 /*

		DNF_Converter dc = new DNF_Converter(ceg);
         */
 /*
		for(Relation r : ceg.getRelations()){
			System.out.print(r.getNode().getLabel() + " = ");
			System.out.println(r.getExpression());
		}
         */
        //DNF_Converter dc = new DNF_Converter(ceg);
        /*
		
		for(Effect e : ceg.getEffectNodes()){
			System.out.println(e.getLabel());
			for(Set<CEG_Node> s : e.getDNF()){
				for(CEG_Node n : s){
					System.out.print(n.getLabel() + " ");
				}
				System.out.println();
			}
		}
		
         */
 /*
		System.out.println();
		System.out.println("Effectlerin boolean expressionlar�: ");
		
		for(Effect e : ceg.getEffectNodes()){
			
			System.out.print(e.getLabel() + " = ");
			System.out.println(e.getRelation().getExpressionInter());
			System.out.println(e.getRelation().getExpression());
			System.out.println(e.getDNF_Exp().size());
		}
         */
 /*
		ClauseConjunction m = new ClauseConjunction(1, ceg.getEffectNodes().get(10));
		System.out.println(m.getExpression());
		System.out.println("Clause Conjunction mutants: ");
		for(String s : m.createMutant()){
			System.out.println(s);
		}
		
		ClauseDisjunction n = new ClauseDisjunction(1, ceg.getEffectNodes().get(10));
		System.out.println(n.getExpression());
		System.out.println("Clause Disjunction mutants: ");
		for(String s : n.createMutant()){
			System.out.println(s);
		}
         */
 /*
		StuckAt0 m = new StuckAt0(1, ceg.getEffectNodes().get(10));
		System.out.println(m.getExpression());
		System.out.println("Stuck0 mutants: ");
		for(String s : m.createMutant()){
			System.out.println(s);
		}
		
		StuckAt1 n = new StuckAt1(1, ceg.getEffectNodes().get(10));
		System.out.println(m.getExpression());
		System.out.println("Stuck0 mutants: ");
		for(String s : n.createMutant()){
			System.out.println(s);
		}
         */
 /*
		MissingVariable m = new MissingVariable(1, ceg.getEffectNodes().get(10));
		System.out.println(m.getExpression());
		System.out.println("Missing Variable mutants: ");
		for(String s : m.createMutant()){
			System.out.println(s);
		}
         */
 /*
		VariableReference m = new VariableReference(1, ceg.getEffectNodes().get(10));
		System.out.println(m.getExpression());
		System.out.println("Variable Reference mutants: ");
		for(String s : m.createMutant()){
			System.out.println(s);
		}
         */
 /*
		VariableNegation v = new VariableNegation(1, ceg.getEffectNodes().get(10));
		System.out.println(v.getExpression());
		System.out.println("VariableNegation Mutants:");
		for(String s : v.createMutant()){
			System.out.println(s);
		}
         */
 /*
		OperatorReference f = new OperatorReference(1, ceg.getEffectNodes().get(10));
		System.out.println(f.getExpression());
		System.out.println(f.getNodes().keySet());
		System.out.println(f.getNodes().values());
         */
 /*
		ExpressionNegation e = new ExpressionNegation(1, ceg.getEffectNodes().get(10));
		System.out.println(e.getExpression());
		System.out.println("Exp neg mutants: ");
		for(String s : e.createMutant()){
			System.out.println(s);
		}
		
		OperatorReference f = new OperatorReference(1, ceg.getEffectNodes().get(10));
		System.out.println("exp: " + f.getExpression());
		System.out.println("And mutants: ");
		for(String s : f.createMutantAnd() ){
			System.out.println(s);
		}
		System.out.println("Or mutants: ");
		for(String s : f.createMutantOr() ){
			System.out.println(s);
		}
         */

 /*
		String s = "( C1 && C2 && !C3 && !C4 && C5 && !C6 && C7 && !C8 && C9 )  || ( C1 && C2 && !C3 && !C4 && C5 && !C6 && !C7 && C8 && C9 )  || ( C1 && C2 && C3 && C4 && C5 && !C6 && !C7 && C8 && C9 )  || ( C1 && C2 && C3 && !C4 && !C5 && !C6 && C7 && !C8 && C9 )  || ( C1 && C2 && C3 && C4 && C5 && !C6 && C7 && !C8 && C9 )  || ( C1 && C2 && !C3 && C4 && !C5 && !C6 && C7 && !C8 && C9 )  || ( C1 && C2 && !C3 && C4 && !C5 && !C6 && !C7 && C8 && C9 )  || ( C1 && C2 && C3 && C4 && C5 && C6 && C7 && C8 && C9 )  || ( C1 && C2 && !C3 && !C4 && C5 && C6 && !C7 && !C8 && C9 )  || ( C1 && C2 && C3 && !C4 && !C5 && C6 && C7 && C8 && C9 )  || ( C1 && C2 && !C3 && C4 && !C5 && C6 && C7 && C8 && C9 )  || ( C1 && C2 && !C3 && C4 && !C5 && C6 && !C7 && !C8 && C9 )  || ( C1 && C2 && C3 && C4 && C5 && C6 && !C7 && !C8 && C9 )  || ( C1 && C2 && C3 && !C4 && !C5 && !C6 && !C7 && C8 && C9 )  || ( C1 && C2 && C3 && !C4 && !C5 && C6 && !C7 && !C8 && C9 )  || ( C1 && C2 && !C3 && !C4 && C5 && C6 && C7 && C8 && C9 )  ( C1 && C2 && !C3 && !C4 && C5 && !C6 && C7 && !C8 && C9 )  || ( C1 && C2 && !C3 && !C4 && C5 && !C6 && !C7 && C8 && C9 )  || ( C1 && C2 && C3 && C4 && C5 && !C6 && !C7 && C8 && C9 )  || ( C1 && C2 && C3 && !C4 && !C5 && !C6 && C7 && !C8 && C9 )  || ( C1 && C2 && C3 && C4 && C5 && !C6 && C7 && !C8 && C9 )  || ( C1 && C2 && !C3 && C4 && !C5 && !C6 && C7 && !C8 && C9 )  || ( C1 && C2 && !C3 && C4 && !C5 && !C6 && !C7 && C8 && C9 )  || ( C1 && C2 && C3 && C4 && C5 && C6 && C7 && C8 && C9 )  || ( C1 && C2 && !C3 && !C4 && C5 && C6 && !C7 && !C8 && C9 )  || ( C1 && C2 && C3 && !C4 && !C5 && C6 && C7 && C8 && C9 )  || ( C1 && C2 && !C3 && C4 && !C5 && C6 && C7 && C8 && C9 )  || ( C1 && C2 && !C3 && C4 && !C5 && C6 && !C7 && !C8 && C9 )  || ( C1 && C2 && C3 && C4 && C5 && C6 && !C7 && !C8 && C9 )  || ( C1 && C2 && C3 && !C4 && !C5 && !C6 && !C7 && C8 && C9 )  || ( C1 && C2 && C3 && !C4 && !C5 && C6 && !C7 && !C8 && C9 )  || ( C1 && C2 && !C3 && !C4 && C5 && C6 && C7 && C8 && C9 )  ";
		s = s.replaceAll("\\&\\&", "\\&");
		s = s.replaceAll("\\|\\|", "\\|");
		Expression<String> parsedExpression = RuleSet.simplify(ExprParser.parse(s));
	    System.out.println("simplyfied: ");
		System.out.println(parsedExpression);
		String str = parsedExpression.toString();
		System.out.println(str);
		String[] sp = str.split("\\|");
		System.out.println(sp.length);
	    //System.out.println(parsedExpression.equals(simplified));
		
         */
 /*
		System.out.println();
		System.out.println("DNF formattaki effectler: ");
		
		for(Effect e : ceg.getEffectNodes()){
			
			System.out.print(e.getLabel() + " = ");
			System.out.println(e.getDNFasExp());
			System.out.println(e.getReformedDNFexp());
			System.out.println(e.getDNF_Exp());
			System.out.println(e.getDNF_Exp().size());
		}
         */
 /*
		for(CUTPNFP c : mum.getCutpnfps()){
			for(CUTPNFPi ci : c.getCutpnfpis()){
				System.out.println(ci.getCutpnfpis());
			}
		}*/
        //System.out.println("size " + mum.getUtps().get(1).getVarSize(4));
        //MUMCUT mum = new MUMCUT(ceg);
        /*
		try {
			FileReader f = new FileReader("outMIFalse.txt");
			BufferedReader bf = new BufferedReader(f);
			//FileWriter fw = new FileWriter("outMIFalseModified.txt");
			//BufferedWriter w = new BufferedWriter(fw);
			int count = 0;
			String line = bf.readLine();
			//w.write(line);
			//w.newLine();
			
			//line = bf.readLine();

			int st = line.indexOf('E');
			int end = line.indexOf('=');
			
			String key = line.substring(st, end);
			//w.write(key);
			line = bf.readLine();
			
			while(line!=null){
				
				if(line.trim().equals("")){
					line = bf.readLine();
					break;
				}
				
				if(line.contains("E")){
					st = line.indexOf('E');
					end = line.indexOf("=");
					
					key = line.substring(st, end);
					//w.write(key);
					//line = bf.readLine();
										
				}else{
					TestInput ti = new TestInput(++count);
					ti.createMap(ceg);
					
					int e = line.lastIndexOf(',');
					line.substring(0,e);
					String[] causes = line.split("\\,");
					
					for(String s : causes){
						ti.changeValue(s, Value.True);
					}
					//w.write(ti.toString());
					//w.newLine();
				}

				line = bf.readLine();
			}
			
		bf.close();
		//w.close();
		f.close();
		//fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
			
		
		
         */
        //System.out.println(mum.getUtps().size());
        //System.out.println(mum.getNfps().size());
        /*
		for(UTP utp : mum.getUtps()){
			System.out.println(utp);
			//for(List<CEG_Node> n : utp.getUtpSet())
			System.out.println("utp size:" + utp.getComponentSize());
			for(int i=0; i<utp.getComponentSize();i++){
				System.out.println(utp.getUTPi(i).getUtpAllSet());
			}
		}
		
		for(NFP nfp : mum.getNfps()){
			System.out.println(nfp);
			System.out.println("nfp size:" + nfp.getComponentSize());
			for(int i=0; i<nfp.getComponentSize();i++){
				System.out.println(nfp.getNFPi(i).getFalseSet());
			}		
		}
         */
        //System.out.println(mum.getUtps());
        /*
		for(DNF_Expression de : ceg.getDNFExpressions()){
			for(ArrayList<CEG_Node_in_Exp> cne : de.getDNF_Expression()){
				System.out.println(cne);
			}
		}
         */
        //Set<List<CEG_Node>> set = new HashSet<>();;
        /*
		SE se = new SE(ceg, ceg.getEffectNodes().get(0), ceg.getDc().getSelectedSet());
		
		TE t =se.getTe();
		System.out.println(t.getTSE().size());
		//TE t = new TE(ceg, ceg.getEffectNodes().get(0), ceg.getDc().getSelectedSet());
		for(List<CEG_Node> c : t.getTSE()){
			for(CEG_Node n : c){
				System.out.print(n.getLabel() + ", ");
			}System.out.println();
		}
		System.out.println("sdfafdfsdf");
         */
 /*
		TE t = new TE(ceg, ceg.getEffectNodes().get(0), ceg.getDc().getSelectedSet());
		System.out.println(t.getTSE().size());
		FE fe = new FE(ceg, ceg.getEffectNodes().get(0), ceg.getDc().getSelectedSet());
		fe.eliminatePosFSE(t);
		System.out.println(fe.getFSE().size());
		for(List<CEG_Node> l : fe.getFSE()){
			for(CEG_Node n : l){
				System.out.print(n.getLabel() + " ,");
			}System.out.println();
		}
         */
 /*
		SE se = new SE(ceg, ceg.getEffectNodes().get(0), ceg.getDc().getSelectedSet());
		TE t = se.getTe();
		System.out.println(t.getTSE().size());
		FE fe = se.getFe();
		fe.eliminatePosFSE(t);
		
		System.out.println(fe.getFSE().size());
		for(List<CEG_Node> l : fe.getFSE()){
			for(CEG_Node n : l){
				System.out.print(n.getLabel() + " ,");
			}System.out.println();
		}
         */
 /*try {
			FileWriter f = new FileWriter("outMITrue2.txt");
			
			BufferedWriter bf = new BufferedWriter(f);
			
			FileWriter f2 = new FileWriter("outMIFalse2.txt");
			
			BufferedWriter bf2 = new BufferedWriter(f2);
		
			MI mi = new MI(ceg);
			for(SE se : mi.getSEs()){
			//System.out.println(" " +se.getTes().size());
				TE te = se.getTe();
				//System.out.println(te.getTSE().size());
				bf.newLine();
				bf.write(te.getEffect().getLabel() + "= ");
				bf.newLine();
				//bf.write("TSE: ");
				for(List<CEG_Node> n : te.getTSE()){
					//System.out.println(n.size());
					for(CEG_Node c : n){
						bf.write(c.getLabel() + ",");
						//System.out.print(c.getLabel() + ",");
					}
					//System.out.println();
					bf.newLine();
				}
				
				FE fe = se.getFe();
				//System.out.println(fe.getFSE().size());
				bf2.newLine();
				bf2.write(fe.getEffect().getLabel() + "= ");
				bf2.newLine();
				//bf2.write("FSE: ");
				for(List<CEG_Node> n : fe.getFSE()){
					//System.out.println(n.size());
					//bf2.newLine();
					
					for(CEG_Node c : n){
						bf2.write(c.getLabel() + ",");
						//System.out.print(c.getLabel() + ",");
					}
					bf2.newLine();
					//System.out.println();
				}
				
			}
		
			bf.close();
			f.close();
			bf2.close();
			f2.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
         */
        //MUMCUT mum = new MUMCUT(ceg);
        //set.addAll(te.getTSE());
        /*}
		for(FE fe : se.getFes()){
			//set.addAll(fe.getFSE());
		}
		
		for(List<CEG_Node> l : set){
			for(CEG_Node n : l){
				System.out.print(n.getLabel() + ",");
			}
			System.out.println();
		}
		
         */
 /*
		System.out.println();
		SE se = new SE(ceg, ceg.getEffectNodes().get(3));
		
		System.out.println("True Set for E1: ");
		for(List<CEG_Node> s : se.getTes().get(0).getTSE()){
			for(CEG_Node c : s){
				System.out.print(c.getLabel() + " ");
			}
			System.out.println();
		}
		System.out.println(se.getTes().get(0).getTSE().size());
		System.out.println();
		System.out.println("False Set for E1: ");
		for(List<CEG_Node> c : se.getFes().get(0).getFSE()){
			for(CEG_Node n : c){
				System.out.print(n.getLabel() + " ");	
			}
			System.out.println();
		}
		System.out.println(se.getFes().get(0).getFSE().size());
		
         */

 /*
		MITest mt = new MITest(ceg);

		System.out.println("MI ile olusanlar: ");
		for(TestInput tim : mt.getTestSet().getTestInputs()){
			System.out.println(tim.getTestInput().entrySet());
		}
		
         */
 /*
		DNF_Expression de = new DNF_Expression(ceg, ceg.getEffectNodes().get(0));
		
		ArrayList<ArrayList<CEG_Node_in_Exp>> ex =  de.getDNF_Expression();
		
		for(ArrayList<CEG_Node_in_Exp> e : ex){
			for (CEG_Node_in_Exp n : e) {
				System.out.println(n.getNode().getLabel());
			}
		}
		
		
		
         */
 /*
		Set<CEG_Node> originalSet = ceg.getCauseNodes().stream().collect(Collectors.toSet());
		Set<Set<CEG_Node>> selectedSet = dc.selectValid(dc.powerSet(originalSet));
		
		for(Set<CEG_Node> c : selectedSet){
			for(CEG_Node a : c)
				System.out.print(a.getLabel() + " ");
			System.out.println();
		}
         */
 /*
		File outFile = new File("C:/Users/deniz.kavzak/Desktop/out.txt");
		
		if(!outFile.exists()){
			try {
				outFile.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		PrintWriter pw = new PrintWriter(outFile);
		
		for(Entry<String, ArrayList<Relation>> entry : ceg.getEffectMap().entrySet()){
			pw.println(entry.getKey());
		}
		
         */
 /*for(Effect effect : ceg.getEffectNodes()){
			System.out.println(effect.getLabel());
			for(Relation relation : map.get(effect.getLabel())){
				System.out.println(relation.toString());
			}
		}
         */
 /*for(Cause c : ceg.getCauseNodes()){
			System.out.println(c.toString());
		}
         */
 /*for(Effect e : ceg.getEffectNodes()){
			System.out.println(e.getLabel());
			System.out.println(e.getRelationType());
		}
         */
 /*
		TableHelper th = new TableHelper(ceg);
		DecisionTable dt = th.prepareTable();
		
		System.out.println("\nConditions\n");
		
		for(Condition cond : dt.getConditions()){
			for(CEG_Node cause : cond.getCauses()){
				System.out.println(cond.getID());
				System.out.println(cause.getLabel());
				System.out.println(" - ");
			}
		}
		
		System.out.println("\nActions\n");
		
		for(Action act : dt.getActions()){
			System.out.println(act.getLabel() + act.getRelationType());
			for(CEG_Node n : act.getNodes()){
				System.out.print(n.getLabel());
			}
			
			System.out.println(" - ");
		}
         */
 /*
		for(Relation relation : ceg.getRelations()){
			System.out.println(relation.toString());
		}
		
		for(Relation relation : map.get("I3"))
		{
			System.out.println(relation.toString());
		}
         */
    }

}
