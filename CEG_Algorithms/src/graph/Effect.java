package graph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.gephi.graph.api.Node;
import test.TestInput;
import test.Value;

/**
 * Represents the effect node in the CEG
 *
 * @author deniz.kavzak
 *
 */
public class Effect extends CEG_Node {

    private Relation relation; //the relation this node is formed by.
    private Set<Set<CEG_Node>> DNF; //DNF format of the effect expression with node informations

    private Set<TestInput> DNFasInputs;

    private String DNFasExp = ""; //String format of DNF with "AND" "OR"
    private String reformedDNFexp = ""; //Contains the DNF with "&&" and "||" to use boolean expression handler to calculate truth values
    private ArrayList<ArrayList<CEG_Node_in_Exp>> DNF_Exp; //DNF of effect formed by truth table algorithm
    private String minDNF; //This version is used for the DNF conversion tool
    private ArrayList<ArrayList<CEG_Node_in_Exp>> min_DNF_Exp; //This version is used for the DNF conversion tool
    private ArrayList<CEG_Node> expressionCauseNodes;

    public Effect(Node node, Relation relation) {
        super(node);
        this.relation = relation;
        DNF_Exp = new ArrayList<>();
        min_DNF_Exp = new ArrayList<>();
        expressionCauseNodes = new ArrayList<>();
        DNFasInputs = new HashSet<>();
    }

    public ArrayList<ArrayList<CEG_Node_in_Exp>> getMin_DNF_Exp() {
        return min_DNF_Exp;
    }

    public void setExpressionCauseNodes(CEG ceg) {
        ArrayList<CEG_Node> list = new ArrayList<>();
        String exp = relation.getExpression();

        for (CEG_Node n : ceg.getCauseNodes()) {

            String key = n.getLabel() + " ";
            if (exp.contains(key)) {
                list.add(n);
            }
        }
        expressionCauseNodes.addAll(list);
    }

    /**
     * Get the cause nodes in expression
     *
     * @param ceg
     * @return
     */
    public ArrayList<CEG_Node> getExpressionNodes() {
        return expressionCauseNodes;
    }

    public String getRelationType() {
        return relation.getRelationType();
    }

    public void setRelation(Relation relation) {
        this.relation = relation;
    }

    public Relation getRelation() {
        return relation;
    }

    public Set<Set<CEG_Node>> getDNF() {
        return DNF;
    }

    public void setDNF(Set<Set<CEG_Node>> DNF) {
        this.DNF = DNF;
    }

    public String getMinDNF() {
        return minDNF;
    }

    public void setMinDNF(String DNF) {
        this.minDNF = DNF;
    }

    public ArrayList<ArrayList<CEG_Node_in_Exp>> getDNF_Exp() {
        return DNF_Exp;
    }

    public void setDNF_Exp(ArrayList<ArrayList<CEG_Node_in_Exp>> dNF_Exp) {
        DNF_Exp = dNF_Exp;
    }

    public Set<TestInput> getDNFasInputs() {
        return DNFasInputs;
    }

    public void setDNFasInputs(Set<TestInput> DNFasInputs) {
        this.DNFasInputs = DNFasInputs;
    }

    /**
     * String format of DNF with "AND" "OR"
     *
     * @param ceg
     */
    public void setDNFasExp(CEG ceg) {

        for (TestInput ti : DNFasInputs) {

            DNFasExp = DNFasExp + "(";

            for (String s : ti.getTestInput().keySet()) {

                if (ti.getTestInput().get(s) == Value.True) {
                    DNFasExp = DNFasExp + " " + s + " AND";
                } else {
                    DNFasExp = DNFasExp + " ~" + s + " AND";
                }
            }

            DNFasExp = DNFasExp.substring(0, DNFasExp.lastIndexOf("AND"));

            DNFasExp = DNFasExp + ") " + " OR ";

        }

        if (DNFasExp.contains("OR")) {
            DNFasExp = DNFasExp.substring(0, DNFasExp.lastIndexOf("OR"));
        }

    }

    public String getDNFasExp() {
        return DNFasExp;
    }

    public String getReformedDNFexp() {
        return reformedDNFexp;
    }

    public void setReformedDNFexp(String reformedDNFexp) {
        this.reformedDNFexp = reformedDNFexp;
    }

    /**
     * Parses the string DNF in order to split into components and then to
     * convert CEG_Node_in_Exp
     *
     * @param ceg
     */
    public void setDNFExpression(CEG ceg) {
        String exp = DNFasExp;

        exp = exp.replaceAll("AND", "&&");
        exp = exp.replaceAll("OR", "||");
        exp = exp.replaceAll("~", "!");

        String[] splitted = exp.split("\\|\\|");

        for (String component : splitted) {
            if (!component.isEmpty() && DNF_Parser(ceg, component) != null) {
                //exp = exp.replaceAll("&", "&&");
                DNF_Exp.add(DNF_Parser(ceg, component));
            }
        }

    }

    /**
     * Converts given string component into the form of CEG_Node_in_Exp list
     *
     * @param ceg
     * @param comp
     * @return
     */
    private ArrayList<CEG_Node_in_Exp> DNF_Parser(CEG ceg, String comp) {
        String exp = comp;

        ArrayList<CEG_Node_in_Exp> component = new ArrayList<>();

        exp = exp.trim();

        while (exp.contains("(")) {
            int i = exp.indexOf('(');
            exp = exp.substring(i + 1, exp.length());
        }
        while (exp.contains(")")) {
            int j = exp.indexOf(')');
            exp = exp.substring(0, j);
        }
        exp = exp.trim();

        if (!exp.equals("")) {
            String[] nodes = exp.split("\\&\\&");

            for (String str : nodes) {
                str = str.trim();

                if (!str.contains("!")) {
                    CEG_Node_in_Exp node = new CEG_Node_in_Exp(ceg.getCauseLabelNode(str));
                    component.add(node);
                } else {
                    str = str.substring(1);
                    CEG_Node_in_Exp node = new CEG_Node_in_Exp(ceg.getCauseLabelNode(str));
                    node.setNegated(true);
                    component.add(node);
                }

            }

            return component;
        } else {

            return null;
        }
    }

    /**
     * This version is used for the DNF conversion tool
     *
     * @param ceg
     */
    public void setDNFExpressionMin(CEG ceg) {
        String exp = minDNF;

        if (exp.contains("|")) {
            String[] splitted = exp.split("\\|");
            for (String component : splitted) {
                min_DNF_Exp.add(DNF_ParserMin(ceg, component));
            }
        } else {
            min_DNF_Exp.add(DNF_ParserMin(ceg, exp));
        }

    }

    /**
     * This version is used for the DNF conversion tool
     *
     * @param ceg
     * @param comp
     * @return
     */
    private ArrayList<CEG_Node_in_Exp> DNF_ParserMin(CEG ceg, String comp) {
        String exp = comp;

        ArrayList<CEG_Node_in_Exp> component = new ArrayList<>();

        exp = exp.trim();

        while (exp.contains("(")) {
            int i = exp.indexOf('(');
            exp = exp.substring(i + 1, exp.length());
        }
        while (exp.contains(")")) {
            int j = exp.indexOf(')');
            exp = exp.substring(0, j);
        }
        exp = exp.trim();

        if (exp.contains("&")) {
            String[] nodes = exp.split("\\&");

            for (String str : nodes) {
                str = str.trim();

                if (!str.contains("!")) {
                    CEG_Node_in_Exp node = new CEG_Node_in_Exp(ceg.getCauseLabelNode(str));
                    component.add(node);
                } else {
                    str = str.substring(1);
                    CEG_Node_in_Exp node = new CEG_Node_in_Exp(ceg.getCauseLabelNode(str));
                    node.setNegated(true);
                    component.add(node);
                }

            }
        } else {
            if (exp.contains("!")) {
                CEG_Node_in_Exp node = new CEG_Node_in_Exp(ceg.getCauseLabelNode(exp.substring(1)));
                node.setNegated(true);
                component.add(node);
            } else {
                CEG_Node_in_Exp node = new CEG_Node_in_Exp(ceg.getCauseLabelNode(exp));
                component.add(node);
            }
        }
        return component;
    }

}
