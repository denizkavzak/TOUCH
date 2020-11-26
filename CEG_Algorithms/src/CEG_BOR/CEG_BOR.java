/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CEG_BOR;

import graph.CEG;
import graph.CEG_Node;
import graph.Cause;
import graph.Effect;
import graph.Intermediate;
import java.util.ArrayList;
import java.util.Random;
import test.TestInput;
import test.TestSet;
import test.Value;

/**
 * TODO
 * @author deniz.kavzak
 */
public class CEG_BOR {

    private TestInput ti;

    private TestSet trueSet;
    private TestSet falseSet;
    private TestSet allSet;
    private CEG ceg;

    private static int id = 0;

    public CEG_BOR(CEG ceg) {
        this.ceg = ceg;
        trueSet = new TestSet();
        falseSet = new TestSet();
        allSet = new TestSet();
        //CEG_BOR_Tests();
    }

    public TestSet CEG_BOR_Tests() {
        for (Effect effect : ceg.getEffectNodes()) {
            trueSet = CEG_BOR_t(effect);
            falseSet = CEG_BOR_f(effect);
            allSet.addTestInputList(union(trueSet, falseSet).getTestInputs());
        }

        return allSet;
    }

    public TestSet CEG_BOR_Tests_Effect(Effect effect) {
        return union(CEG_BOR_t(effect), CEG_BOR_f(effect));
    }

    public TestSet getTrueSet() {
        return trueSet;
    }

    public TestSet getFalseSet() {
        return falseSet;
    }

    public TestSet getAllSet() {
        return allSet;
    }

    public TestSet CEG_BOR_t(CEG_Node node) {
        //if (node.isNegated()) {
            //System.out.println("node is negated!");
          //  return CEG_BOR_f(node);
        //}

        TestSet ts = new TestSet();

        System.out.println("Node Type: "+node.getNodeType());
        if (node.getNodeType().equals("Intermediate")) {
            Intermediate intermediate = ceg.getIntermediateNode(node.getLabel());

            String op = intermediate.getRelationType();

            ArrayList<CEG_Node> nodes = intermediate.getRelation().getRelatedNodes();

            if (op.equals("AND")) {
                System.out.println("In AND-I");
                for (int i = 0; i < nodes.size() - 1; i = i + 2) {

                    //if (!nodes.get(i).isNegated() && !nodes.get(i + 1).isNegated()) {
                    ts.addTestInputList(ontoOp(CEG_BOR_t(nodes.get(i)), CEG_BOR_t(nodes.get(i + 1))).getTestInputs());
                    /*} else if (nodes.get(i).isNegated()) {
                        if (nodes.get(i + 1).isNegated()) {
                            ts.addTestInputList(ontoOp(CEG_BOR_f(nodes.get(i)), CEG_BOR_f(nodes.get(i + 1))).getTestInputs());
                        } else {
                            ts.addTestInputList(ontoOp(CEG_BOR_f(nodes.get(i)), CEG_BOR_t(nodes.get(i + 1))).getTestInputs());
                        }
                    } else if (nodes.get(i + 1).isNegated()) {
                        ts.addTestInputList(ontoOp(CEG_BOR_t(nodes.get(i)), CEG_BOR_f(nodes.get(i + 1))).getTestInputs());
                    }*/
                }
                if (nodes.size() % 2 != 0) {
                    TestSet temp = ts;
                    ts.getTestInputs().clear();
                    //if (!(nodes.get(nodes.size() - 1)).isNegated()) {
                    ts = ontoOp(temp, CEG_BOR_t(nodes.get(nodes.size() - 1)));
                    /*} else {
                        ts = ontoOp(temp, CEG_BOR_f(nodes.get(nodes.size() - 1)));
                    }*/
                }
            } else if (op.equals("OR")) {
                System.out.println("In OR-I");
                for (int i = 0; i < nodes.size() - 1; i = i + 2) {

                    //if (!nodes.get(i).isNegated() && !nodes.get(i + 1).isNegated()) {
                    ts.addTestInputList(union(cartesianProduct(CEG_BOR_t(nodes.get(i)), CEG_BOR_f(nodes.get(i + 1))),
                            cartesianProduct(CEG_BOR_f(nodes.get(i)), CEG_BOR_t(nodes.get(i + 1)))).getTestInputs());
                    /*}else if (nodes.get(i).isNegated()) {
                        if (nodes.get(i + 1).isNegated()) {
                            ts.addTestInputList(union(cartesianProduct(CEG_BOR_f(nodes.get(i)), CEG_BOR_t(nodes.get(i + 1))),
                                cartesianProduct(CEG_BOR_t(nodes.get(i)), CEG_BOR_f(nodes.get(i + 1)))).getTestInputs());
                        } else {
                            ts.addTestInputList(union(cartesianProduct(CEG_BOR_f(nodes.get(i)), CEG_BOR_f(nodes.get(i + 1))),
                                cartesianProduct(CEG_BOR_t(nodes.get(i)), CEG_BOR_t(nodes.get(i + 1)))).getTestInputs());
                            //ts.addTestInputList(ontoOp(CEG_BOR_f(nodes.get(i)), CEG_BOR_t(nodes.get(i + 1))).getTestInputs());
                        }
                    } else if (nodes.get(i + 1).isNegated()) {
                        ts.addTestInputList(union(cartesianProduct(CEG_BOR_t(nodes.get(i)), CEG_BOR_t(nodes.get(i + 1))),
                                cartesianProduct(CEG_BOR_f(nodes.get(i)), CEG_BOR_f(nodes.get(i + 1)))).getTestInputs());
                        //ts.addTestInputList(ontoOp(CEG_BOR_t(nodes.get(i)), CEG_BOR_f(nodes.get(i + 1))).getTestInputs());
                    }*/
                }

                if (nodes.size() % 2 != 0) {
                    TestSet temp = ts;
                    ts.getTestInputs().clear();
                    //if (!(nodes.get(nodes.size() - 1)).isNegated()) {
                    ts = union(temp, CEG_BOR_t(nodes.get(nodes.size() - 1)));
                    //}else{
                    //    ts = ontoOp(temp, CEG_BOR_f(nodes.get(nodes.size() - 1)));
                    //}
                }

                //union(cartesianProduct(CEG_BOR_t(n1),CEG_BOR_f(n2)),cartesianProduct(CEG_BOR_f(n1), CEG_BOR_t(n2)));
            }
        } else if (node.getNodeType().equals("Effect")) {
            Effect effect = ceg.getEffectNode(node.getLabel());

            String op = effect.getRelationType();

            ArrayList<CEG_Node> nodes = effect.getRelation().getRelatedNodes();

            if (op.equals("AND")) {
                for (int i = 0; i < nodes.size() - 1; i = i + 2) {

                    //if (!nodes.get(i).isNegated() && !nodes.get(i + 1).isNegated()) {
                    ts.addTestInputList(ontoOp(CEG_BOR_t(nodes.get(i)), CEG_BOR_t(nodes.get(i + 1))).getTestInputs());
                    /*} else if (nodes.get(i).isNegated()) {
                        if (nodes.get(i + 1).isNegated()) {
                            ts.addTestInputList(ontoOp(CEG_BOR_f(nodes.get(i)), CEG_BOR_f(nodes.get(i + 1))).getTestInputs());
                        } else {
                            ts.addTestInputList(ontoOp(CEG_BOR_f(nodes.get(i)), CEG_BOR_t(nodes.get(i + 1))).getTestInputs());
                        }
                    } else if (nodes.get(i + 1).isNegated()) {
                        ts.addTestInputList(ontoOp(CEG_BOR_t(nodes.get(i)), CEG_BOR_f(nodes.get(i + 1))).getTestInputs());
                    }*/
                }
                if (nodes.size() % 2 != 0) {
                    TestSet temp = ts;
                    ts.getTestInputs().clear();
                    //if (!(nodes.get(nodes.size() - 1)).isNegated()) {
                    ts = ontoOp(temp, CEG_BOR_t(nodes.get(nodes.size() - 1)));

                    /*} else {
                        ts = ontoOp(temp, CEG_BOR_f(nodes.get(nodes.size() - 1)));
                    }*/
                }

            } else if (op.equals("OR")) {
                for (int i = 0; i < nodes.size() - 1; i = i + 2) {

                    //if (!nodes.get(i).isNegated() && !nodes.get(i + 1).isNegated()) {
                    ts.addTestInputList(union(cartesianProduct(CEG_BOR_t(nodes.get(i)), CEG_BOR_f(nodes.get(i + 1))),
                            cartesianProduct(CEG_BOR_f(nodes.get(i)), CEG_BOR_t(nodes.get(i + 1)))).getTestInputs());
                    /*}else if (nodes.get(i).isNegated()) {
                        if (nodes.get(i + 1).isNegated()) {
                            ts.addTestInputList(union(cartesianProduct(CEG_BOR_f(nodes.get(i)), CEG_BOR_t(nodes.get(i + 1))),
                                cartesianProduct(CEG_BOR_t(nodes.get(i)), CEG_BOR_f(nodes.get(i + 1)))).getTestInputs());
                        } else {
                            ts.addTestInputList(union(cartesianProduct(CEG_BOR_f(nodes.get(i)), CEG_BOR_f(nodes.get(i + 1))),
                                cartesianProduct(CEG_BOR_t(nodes.get(i)), CEG_BOR_t(nodes.get(i + 1)))).getTestInputs());
                            //ts.addTestInputList(ontoOp(CEG_BOR_f(nodes.get(i)), CEG_BOR_t(nodes.get(i + 1))).getTestInputs());
                        }
                    } else if (nodes.get(i + 1).isNegated()) {
                        ts.addTestInputList(union(cartesianProduct(CEG_BOR_t(nodes.get(i)), CEG_BOR_t(nodes.get(i + 1))),
                                cartesianProduct(CEG_BOR_f(nodes.get(i)), CEG_BOR_f(nodes.get(i + 1)))).getTestInputs());
                        //ts.addTestInputList(ontoOp(CEG_BOR_t(nodes.get(i)), CEG_BOR_f(nodes.get(i + 1))).getTestInputs());
                    }*/
                }

                if (nodes.size() % 2 != 0) {
                    TestSet temp = ts;
                    ts.getTestInputs().clear();
                    //if (!(nodes.get(nodes.size() - 1)).isNegated()) {
                    ts = union(temp, CEG_BOR_t(nodes.get(nodes.size() - 1)));

                    //}else{
                    //    ts = ontoOp(temp, CEG_BOR_f(nodes.get(nodes.size() - 1)));
                    //}
                }
                System.out.println("OR : " + ts.toString());
            }
        } else if (node.getNodeType().equals("Cause")) {
            //System.out.println("Inside Cause: "+node.getLabel());
            TestInput newT = new TestInput(id++);
            //if (!node.isNegated()) {
            newT.addTestInput(node.getLabel(), Value.True);
            //System.out.println("cause part : " + newT);
            //} else {
            //   newT.addTestInput(node.getLabel(), Value.False);
            //}
            
            //ts.addTestInputList();
            ts.addTestInput(newT);
        }
        //System.out.println("Returning: "+ts.toString());
        return ts;
    }

    public TestSet CEG_BOR_f(CEG_Node node) {

        //if (node.isNegated()) {
        //    return CEG_BOR_t(node);
        //}

        TestSet ts = new TestSet();

        if (node.getNodeType().equals("Intermediate")) {
            
            Intermediate intermediate = ceg.getIntermediateNode(node.getLabel());

            String op = intermediate.getRelationType();

            ArrayList<CEG_Node> nodes = intermediate.getRelation().getRelatedNodes();

            if (op.equals("OR")) {
                
                for (int i = 0; i < nodes.size() - 1; i = i + 2) {

                    //if (!nodes.get(i).isNegated() && !nodes.get(i + 1).isNegated()) {
                    ts.addTestInputList(ontoOp(CEG_BOR_f(nodes.get(i)), CEG_BOR_f(nodes.get(i + 1))).getTestInputs());
                    /*} else if (nodes.get(i).isNegated()) {
                        if (nodes.get(i + 1).isNegated()) {
                            ts.addTestInputList(ontoOp(CEG_BOR_t(nodes.get(i)), CEG_BOR_t(nodes.get(i + 1))).getTestInputs());
                        } else {
                            ts.addTestInputList(ontoOp(CEG_BOR_t(nodes.get(i)), CEG_BOR_f(nodes.get(i + 1))).getTestInputs());
                        }
                    } else if (nodes.get(i + 1).isNegated()) {
                        ts.addTestInputList(ontoOp(CEG_BOR_f(nodes.get(i)), CEG_BOR_t(nodes.get(i + 1))).getTestInputs());
                    }*/
                }
                if (nodes.size() % 2 != 0) {
                    TestSet temp = ts;
                    ts.getTestInputs().clear();
                    //if (!(nodes.get(nodes.size() - 1)).isNegated()) {
                    ts = ontoOp(temp, CEG_BOR_f(nodes.get(nodes.size() - 1)));
                    //} else {
                    //   ts = ontoOp(temp, CEG_BOR_t(nodes.get(nodes.size() - 1)));
                    //}
                }
            } else if (op.equals("AND")) {
                for (int i = 0; i < nodes.size() - 1; i = i + 2) {

                    //if (!nodes.get(i).isNegated() && !nodes.get(i + 1).isNegated()) {
                    ts.addTestInputList(union(cartesianProduct(CEG_BOR_f(nodes.get(i)), CEG_BOR_t(nodes.get(i + 1))),
                            cartesianProduct(CEG_BOR_t(nodes.get(i)), CEG_BOR_f(nodes.get(i + 1)))).getTestInputs());
                    /*}else if (nodes.get(i).isNegated()) {
                        if (nodes.get(i + 1).isNegated()) {
                            ts.addTestInputList(union(cartesianProduct(CEG_BOR_t(nodes.get(i)), CEG_BOR_f(nodes.get(i + 1))),
                                cartesianProduct(CEG_BOR_f(nodes.get(i)), CEG_BOR_t(nodes.get(i + 1)))).getTestInputs());
                        } else {
                            ts.addTestInputList(union(cartesianProduct(CEG_BOR_t(nodes.get(i)), CEG_BOR_t(nodes.get(i + 1))),
                                cartesianProduct(CEG_BOR_f(nodes.get(i)), CEG_BOR_f(nodes.get(i + 1)))).getTestInputs());
                            //ts.addTestInputList(ontoOp(CEG_BOR_f(nodes.get(i)), CEG_BOR_t(nodes.get(i + 1))).getTestInputs());
                        }
                    } else if (nodes.get(i + 1).isNegated()) {
                        ts.addTestInputList(union(cartesianProduct(CEG_BOR_f(nodes.get(i)), CEG_BOR_f(nodes.get(i + 1))),
                                cartesianProduct(CEG_BOR_t(nodes.get(i)), CEG_BOR_t(nodes.get(i + 1)))).getTestInputs());
                        //ts.addTestInputList(ontoOp(CEG_BOR_t(nodes.get(i)), CEG_BOR_f(nodes.get(i + 1))).getTestInputs());
                    }*/
                }

                if (nodes.size() % 2 != 0) {
                    TestSet temp = ts;
                    ts.getTestInputs().clear();
                    //if (!(nodes.get(nodes.size() - 1)).isNegated()) {
                    ts = union(temp, CEG_BOR_f(nodes.get(nodes.size() - 1)));
                    //}else{
                    //   ts = ontoOp(temp, CEG_BOR_t(nodes.get(nodes.size() - 1)));
                    // }
                }

                //union(cartesianProduct(CEG_BOR_t(n1),CEG_BOR_f(n2)),cartesianProduct(CEG_BOR_f(n1), CEG_BOR_t(n2)));
            }
        } else if (node.getNodeType().equals("Effect")) {
         
            Effect effect = ceg.getEffectNode(node.getLabel());
            String op = effect.getRelationType();

            ArrayList<CEG_Node> nodes = effect.getRelation().getRelatedNodes();

            if (op.equals("OR")) {
         
                for (int i = 0; i < nodes.size() - 1; i = i + 2) {

                    //if (!nodes.get(i).isNegated() && !nodes.get(i + 1).isNegated()) {
                    ts.addTestInputList(ontoOp(CEG_BOR_f(nodes.get(i)), CEG_BOR_f(nodes.get(i + 1))).getTestInputs());
                    /*} else if (nodes.get(i).isNegated()) {
                        if (nodes.get(i + 1).isNegated()) {
                            ts.addTestInputList(ontoOp(CEG_BOR_t(nodes.get(i)), CEG_BOR_t(nodes.get(i + 1))).getTestInputs());
                        } else {
                            ts.addTestInputList(ontoOp(CEG_BOR_t(nodes.get(i)), CEG_BOR_f(nodes.get(i + 1))).getTestInputs());
                        }
                    } else if (nodes.get(i + 1).isNegated()) {
                        ts.addTestInputList(ontoOp(CEG_BOR_f(nodes.get(i)), CEG_BOR_t(nodes.get(i + 1))).getTestInputs());
                    }*/
                }
                if (nodes.size() % 2 != 0) {
                    TestSet temp = ts;
                    ts.getTestInputs().clear();
                    //if (!(nodes.get(nodes.size() - 1)).isNegated()) {
                    ts = ontoOp(temp, CEG_BOR_f(nodes.get(nodes.size() - 1)));
                    //} else {
                    //   ts = ontoOp(temp, CEG_BOR_t(nodes.get(nodes.size() - 1)));
                    //}
                }
            } else if (op.equals("AND")) {
         
                for (int i = 0; i < nodes.size() - 1; i = i + 2) {

                    //if (!nodes.get(i).isNegated() && !nodes.get(i + 1).isNegated()) {
                    ts.addTestInputList(union(cartesianProduct(CEG_BOR_f(nodes.get(i)), CEG_BOR_t(nodes.get(i + 1))),
                            cartesianProduct(CEG_BOR_t(nodes.get(i)), CEG_BOR_f(nodes.get(i + 1)))).getTestInputs());
                    /*}else if (nodes.get(i).isNegated()) {
                        if (nodes.get(i + 1).isNegated()) {
                            ts.addTestInputList(union(cartesianProduct(CEG_BOR_t(nodes.get(i)), CEG_BOR_f(nodes.get(i + 1))),
                                cartesianProduct(CEG_BOR_f(nodes.get(i)), CEG_BOR_t(nodes.get(i + 1)))).getTestInputs());
                        } else {
                            ts.addTestInputList(union(cartesianProduct(CEG_BOR_t(nodes.get(i)), CEG_BOR_t(nodes.get(i + 1))),
                                cartesianProduct(CEG_BOR_f(nodes.get(i)), CEG_BOR_f(nodes.get(i + 1)))).getTestInputs());
                            //ts.addTestInputList(ontoOp(CEG_BOR_f(nodes.get(i)), CEG_BOR_t(nodes.get(i + 1))).getTestInputs());
                        }
                    } else if (nodes.get(i + 1).isNegated()) {
                        ts.addTestInputList(union(cartesianProduct(CEG_BOR_f(nodes.get(i)), CEG_BOR_f(nodes.get(i + 1))),
                                cartesianProduct(CEG_BOR_t(nodes.get(i)), CEG_BOR_t(nodes.get(i + 1)))).getTestInputs());
                        //ts.addTestInputList(ontoOp(CEG_BOR_t(nodes.get(i)), CEG_BOR_f(nodes.get(i + 1))).getTestInputs());
                    }*/
                }

                if (nodes.size() % 2 != 0) {
                    TestSet temp = ts;
                    ts.getTestInputs().clear();
                    //if (!(nodes.get(nodes.size() - 1)).isNegated()) {
                    ts = union(temp, CEG_BOR_f(nodes.get(nodes.size() - 1)));
                    //}else{
                    //   ts = ontoOp(temp, CEG_BOR_t(nodes.get(nodes.size() - 1)));
                    // }
                }

                //union(cartesianProduct(CEG_BOR_t(n1),CEG_BOR_f(n2)),cartesianProduct(CEG_BOR_f(n1), CEG_BOR_t(n2)));
            }
        } else if (node.getNodeType().equals("Cause")) {
        
            TestInput newT = new TestInput(id++);
            //  if (!node.isNegated()) {
            newT.addTestInput(node.getLabel(), Value.False);
            // } else {
            //    newT.addTestInput(node.getLabel(), Value.True);
            // }
            ts.addTestInput(newT);
        }

        return ts;

    }

    public TestSet ontoOp(TestSet ts1, TestSet ts2) {

        TestSet ts = new TestSet();
        TestSet ts1Copy = ts1;
        TestSet ts2Copy = ts2;

        if (ts1.getTestInputs().size() < ts2.getTestInputs().size()) {
            TestSet temp;
            temp = ts1;
            ts1 = ts2;
            ts2 = temp;
        }

        for (int i = 0; i < ts2.getTestInputs().size(); i++) {
            boolean valid = true;
            TestInput ti = ts1.getTestInputs().get(i);
            TestInput ti2 = ts2.getTestInputs().get(i);
            TestInput newT = new TestInput(id++);

            for (String s : ti.getTestInput().keySet()) {
                newT.addTestInput(s, ti.getTestInput().get(s));
            }

            for (String s : ti2.getTestInput().keySet()) {
                if (!newT.getTestInput().keySet().contains(s)) {
                    newT.addTestInput(s, ti2.getTestInput().get(s));
                } else if (ti.getTestInput().get(s).equals(ti2.getTestInput().get(s))) {
                    newT.addTestInput(s, ti2.getTestInput().get(s));
                } else {
                    valid = false;
                }
            }

            if (valid) {
                ts.addTestInput(newT);
                ts1Copy.getTestInputs().remove(ti);
                ts2Copy.getTestInputs().remove(ti2);
            }
        }

        for (int i = 0; i >= ts2.getTestInputs().size() && i < ts1.getTestInputs().size(); i++) {
            TestInput ti = ts1.getTestInputs().get(i);
            TestInput newT = new TestInput(id++);
            for (String s : ti.getTestInput().keySet()) {
                newT.addTestInput(s, ti.getTestInput().get(s));
            }

            boolean valid = true;

            while (valid) {
                Random rn = new Random();
                int ind = 0;

                if (!ts2.getTestInputs().isEmpty()) {

                    ind = rn.nextInt(ts2.getTestInputs().size());
                    TestInput ti2 = ts2.getTestInputs().get(ind);

                    for (String s : ti2.getTestInput().keySet()) {
                        if (!newT.getTestInput().keySet().contains(s)) {
                            newT.addTestInput(s, ti2.getTestInput().get(s));
                        } else if (ti.getTestInput().get(s).equals(ti2.getTestInput().get(s))) {
                            newT.addTestInput(s, ti2.getTestInput().get(s));
                        } else {
                            valid = false;
                        }
                    }
                }
            }

            ts.addTestInput(newT);
            ts1Copy.getTestInputs().remove(ti);
        }

        if (!ts1Copy.getTestInputs().isEmpty()) {

            for (TestInput ti : ts1Copy.getTestInputs()) {

                TestInput newT = new TestInput(id++);
                for (String s : ti.getTestInput().keySet()) {
                    newT.addTestInput(s, ti.getTestInput().get(s));
                }

                boolean valid = true;

                do {
                    Random rn = new Random();
                    int ind = 0;

                    if (!ts2.getTestInputs().isEmpty()) {
                        ind = rn.nextInt(ts2.getTestInputs().size());
                        TestInput ti2 = ts2.getTestInputs().get(ind);

                        for (String s : ti2.getTestInput().keySet()) {
                            if (!newT.getTestInput().keySet().contains(s)) {
                                newT.addTestInput(s, ti2.getTestInput().get(s));
                            } else if (ti.getTestInput().get(s).equals(ti2.getTestInput().get(s))) {
                                newT.addTestInput(s, ti2.getTestInput().get(s));
                            } else {
                                valid = false;
                            }
                        }
                    }
                } while (!valid);

                ts.addTestInput(newT);
                //ts1Copy.getTestInputs().remove(ti);

            }

        }

        if (!ts2Copy.getTestInputs().isEmpty()) {

            for (TestInput ti : ts2Copy.getTestInputs()) {

                TestInput newT = new TestInput(id++);
                for (String s : ti.getTestInput().keySet()) {
                    newT.addTestInput(s, ti.getTestInput().get(s));
                }

                boolean valid = true;

                do {
                    Random rn = new Random();
                    int ind = 0;

                    if (!ts2.getTestInputs().isEmpty()) {
                        ind = rn.nextInt(ts1.getTestInputs().size());
                        TestInput ti2 = ts1.getTestInputs().get(ind);

                        for (String s : ti2.getTestInput().keySet()) {
                            if (!newT.getTestInput().keySet().contains(s)) {
                                newT.addTestInput(s, ti2.getTestInput().get(s));
                            } else if (ti.getTestInput().get(s).equals(ti2.getTestInput().get(s))) {
                                newT.addTestInput(s, ti2.getTestInput().get(s));
                            } else {
                                valid = false;
                            }
                        }
                    }
                } while (!valid);

                ts.addTestInput(newT);

            }

        }

        System.out.println("ONTO: "+ts.toString());
        return ts;
    }

    public TestSet cartesianProduct(TestSet ts1, TestSet ts2) {

        TestSet ts = new TestSet();

        if (ts1.getTestInputs().size() < ts2.getTestInputs().size()) {
            TestSet temp;
            temp = ts1;
            ts1 = ts2;
            ts2 = temp;
        }

        for (TestInput ti : ts1.getTestInputs()) {
            for (TestInput ti2 : ts2.getTestInputs()) {

                TestInput newT = new TestInput(id++);

                for (String s : ti.getTestInput().keySet()) {
                    newT.addTestInput(s, ti.getTestInput().get(s));
                }

                for (String s : ti2.getTestInput().keySet()) {
                    if (!newT.getTestInput().keySet().contains(s)) {
                        newT.addTestInput(s, ti2.getTestInput().get(s));
                    } else if (ti.getTestInput().get(s).equals(ti2.getTestInput().get(s))) {
                        newT.addTestInput(s, ti2.getTestInput().get(s));
                    } else {
                        newT = null;
                    }
                }

                if (newT != null) {

                    ts.addTestInput(newT);
                }
            }

        }
        
        System.out.println("CARTESIAN: "+ts.toString());
        return ts;
    }

    public TestSet union(TestSet ts1, TestSet ts2) {

        TestSet ts = new TestSet();

        for(TestInput ti : ts1.getTestInputs())
            ts.addTestInput(ti);
        
        for(TestInput ti : ts2.getTestInputs())
            ts.addTestInput(ti);
        
        //ts.addTestInputList(ts1.getTestInputs());
        //ts.addTestInputList(ts2.getTestInputs());

        System.out.println("UNION: "+ts.toString());
        return ts;
    }

}
