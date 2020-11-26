/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Fourier;

import graph.CEG;
import graph.Cause;
import graph.Effect;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import jboolexpr.MalformedBooleanException;
import mcdc.UniqueEffect;
import mcdc.UniqueMCDC;
import mi.MI;
import mi.MITest;
import mutation.Mutant;
import org.gephi.graph.api.GraphController;
import org.gephi.graph.api.GraphModel;
import org.gephi.graph.api.UndirectedGraph;
import org.gephi.io.importer.api.Container;
import org.gephi.io.importer.api.ImportController;
import org.gephi.io.processor.plugin.DefaultProcessor;
import org.gephi.project.api.ProjectController;
import org.openide.util.Lookup;
import test.TestInput;
import test.TestSet;
import test.Value;

/**
 *
 * @author deniz.kavzak
 */
public class testFourier {

    public static void main(String[] args) throws IOException, MalformedBooleanException {

        ProjectController projectController = Lookup.getDefault().lookup(ProjectController.class);
        projectController.newProject();
        projectController.newWorkspace(projectController.getCurrentProject());
        ImportController importController = Lookup.getDefault().lookup(ImportController.class);
        Container container;
        String fileName = "tcas_all.graphml";
        //String fileName = "graph-exampleLONG.graphml";
        //String fileName = "exampleFromThesis.graphml";
        //String fileName = "papergraph-example.graphml";

        //String fileName = "denizv1.graphml";
        //String fileName = "sample.graphml";
        File file = new File(fileName);
        container = importController.importFile(file);

        importController.process(container, new DefaultProcessor(), projectController.getCurrentWorkspace());
        GraphController graphController = Lookup.getDefault().lookup(GraphController.class);
        GraphModel graphModel = graphController.getModel();

        UndirectedGraph graph = graphModel.getUndirectedGraph();
        CEG ceg = new CEG(graph);

        for (Effect e : ceg.getEffectNodes()) {
            System.out.println(e.getLabel() + "= " + e.getRelation().getExpression());
        }

        // System.out.println("size : " + ceg.getDc().getValidAllPossibleTests(ceg.getEffectNodes().get(3)).size());
        Mut m = new Mut(ceg);
        
        //TestSet ts2 = new TestSet();
        //ts2 = ts2.importFromFile(new File("C:\\Users\\Deniz\\Desktop\\New folder\\Myers"));
        
        //for (TestInput ti : ts2.getTestInputs()) {
          //  System.out.println(ti);
        //}
        // UniqueMCDC umcdc = new UniqueMCDC(ceg);
        //System.out.println(ceg.getEffectNodes().get(6).getRelation().getExpression());
        //UniqueEffect ue = new UniqueEffect(ceg, ceg.getEffectNodes().get(6));
        //System.out.println("Effect: " + ceg.getEffectNodes().get(3).getRelation().getExpression());

//(ArrayList<Mutant> mutants = m.getMutantsOfEffect().get(ceg.getEffectNodes().get(0));
        /*  TestInput ti1 = new TestInput(0);
        TestInput ti2 = new TestInput(1);
        
        ti1.addTestInput("C1", Value.False);
        ti1.addTestInput("C2", Value.False);
        ti1.addTestInput("C3", Value.False);
        ti1.addTestInput("C4", Value.True);
        ti1.addTestInput("C5", Value.False);
        
        ti2.addTestInput("C1", Value.True);
        ti2.addTestInput("C2", Value.False);
        ti2.addTestInput("C3", Value.False);
        ti2.addTestInput("C4", Value.True);
        ti2.addTestInput("C5", Value.False);
        
        System.out.println("t1 = " + ti1.getCorrespondingEffectValue(ceg.getEffectNodes().get(3)));
        System.out.println("t2 = " + ti2.getCorrespondingEffectValue(ceg.getEffectNodes().get(3)));
         */
//for(Cause n : ceg.getCauseNodes())
        //   System.out.println(n);
        Fourier f = new Fourier(ceg);
        //System.out.println("Effect: " + ceg.getEffectNodes().get(3).getRelation().getExpression());
        //TestSet ts = f.findAllSpectralTestsForEffect(ceg.getEffectNodes().get(3));
        TestSet ts = f.getTestsForAllEffectsbyFunction(m);
        //System.out.println("all possible tests: " + ue.getSelectedTestInputs().size());
        //for(TestInput t : ue.getMcdc()){
        //System.out.println("total: " + ue.getSelectedTestInputs().size());
        //for (TestInput t : ue.getMcdc()) {
        //  System.out.println(t);
        //}
        //System.out.println("size: " + ue.getMcdc().size());
        //System.out.println("-------------------");
        //TestSet ts =  umcdc.getAllTests();    
        //TestSet ts = new TestSet(ue.getMcdc());
        //TestSet ts = f.findAllSpectralTestsForEffectbyFunction(ceg.getEffectNodes().get(3), m);

        //TestSet ts = f.getTestsForAllEffectsbyFunction(m);
//System.out.println("Effect: " + ceg.getEffectNodes().get(3).getRelation().getExpression());
//TestSet ts = f.findSpectralTest(ceg.getEffectNodes().get(3), m.getMutantsOfEffect().get(ceg.getEffectNodes().get(3)).get(0));
        
        //MI mi = new MI(ceg);
        
        //TestSet ts = new TestSet(mi.getAllTests());
        
        //MITest mi = new MITest(ceg);
        //TestSet ts = mi.getTestSet();
        
        for (TestInput ti : ts.getTestInputs()) {
            System.out.println(ti);
        }
        
        //  System.out.println("number of tests: " + ts.getTestInputs().size());
        //System.out.println("Analysis : " + m.getMutantAnalysisResultsForAllEffects(ts));
        //System.out.println("mutant based analysis: ");
        //System.out.println("Analysis : " + m.getMutantAnalysisResultsMutantTypes(ts));
        //int say = 0;
        //System.out.println("Effect: " + ceg.getEffectNodes().get(3).getRelation().getExpression());
        /*for(Effect e : ceg.getEffectNodes()){
            for(Mutant muta : m.getMutantsOfEffect().get(e)){
            System.out.println("Mutant type : " + muta.getClass());
            for(String st: muta.getMutants()){
                System.out.println("mutant: " + st);
                say++;
            }
        }
        }
         */
        //System.out.println("Toplam mutant sayisi = " + say);
        //System.out.println("Toplam test  "  + Fourier.toplamSay);
        //System.out.println("Unique test set : " + ts.getTestInputs().size());
        // for(TestInput ti  : ts.getTestInputs())
        //    System.out.println(ti);
        //TestSet ts = f.findSpectralTest(ceg.getEffectNodes().get(0), m.getMutantsOfEffect().get(ceg.getEffectNodes().get(0)).get(0));
//TestSet ts = f.findSpectralTest(ceg.getEffectNodes().get(0), m.getMutantsOfEffect().get(ceg.getEffectNodes().get(0)).get(0));        
        //TestSet ts = f.findAllSpectralTestsForEffect(ceg.getEffectNodes().get(0));
        //TestSet ts = f.getTestsForAllEffects();
        //System.out.println("ti : " + ts);
        //System.out.println("original effect: " + ceg.getEffectNodes().get(0).getRelation().getExpression());
        /*TestSet ts = f.findAllSpectralTestsForEffect(ceg.getEffectNodes().get(0));
        
        for(Mutant mut : mutants){
            System.out.println("mutant: " + mut);
        }
        
        for(TestInput ti : ts.getTestInputs()){
            System.out.println("test : " + ti);
        }
         */
        //for(CEG_Node n : ceg.getEffectNodes().get(0).getExpressionNodes(ceg)){
        //    System.out.println("expression nodes : " + n.getLabel());
        //}
        //System.out.println("test : " + ti);
    }
}
