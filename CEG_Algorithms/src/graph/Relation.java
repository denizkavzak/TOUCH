package graph;

import java.util.ArrayList;

/**
 * Relation is actually the boolean expression forming the node.
 *
 * @author deniz.kavzak
 *
 */
public class Relation {

    private CEG_Node node; //the node which has relation on.
    private ArrayList<CEG_Node> relatedNodes; //list of the nodes which are included in the boolean function.
    private String relationType; //relation type, i.e. AND, OR (XOR and other constraints are included in OR).
    private Constraint constraint; //the constraint node belonging to this relation.
    private String expression = ""; //Expression with only cause nodes.
    private String expressionInter = ""; //Expression with intermediate nodes.

    public Relation(CEG_Node cegNode) {
        node = cegNode;
        relatedNodes = new ArrayList<>();
        constraint = null;
    }

    public String getExpression() {
        return expression;
    }

    public void addConstraint(Constraint constraint) {
        this.constraint = constraint;
    }

    public boolean isConstrainted() {
        if (!constraint.equals(null)) {
            return true;
        } else {
            return false;
        }
    }

    public CEG_Node getNode() {
        return node;
    }

    public Constraint getConstraint() {
        return constraint;
    }

    public ArrayList<CEG_Node> getRelatedNodes() {
        return relatedNodes;
    }

    public void addRelatedNodes(CEG_Node relatedNode) {
        this.relatedNodes.add(relatedNode);
    }

   /* public ArrayList<CEG_Node> getRelatedNodesByType() {

        ArrayList<CEG_Node> nodes = new ArrayList<>();

        for (CEG_Node n : relatedNodes) {
            if (n.getNodeType().equals("Cause")) {
                nodes.add(new Cause(n.getNode()));
            } else if (n.getNodeType().equals("Intermediate")) {
                nodes.add(new Intermediate(n.getNode(), this));
            } else if (n.getNodeType().equals("Effect")) {
                nodes.add(new Effect(n.getNode(), this));
            }
        }
        return nodes;
    }
*/
    public String getRelationType() {
        return relationType;
    }

    public void setRelationType(String relationType) {
        this.relationType = relationType;
    }


    public String toString() {

        String boolFunc = "[ (";

        if (!relatedNodes.isEmpty()) {

            for (int i = 0; i < relatedNodes.size() - 1; i++) {
                if (!relatedNodes.get(i).isNegated()) {
                    boolFunc = boolFunc + relatedNodes.get(i).getLabel() + " , ";
                } else {

                    boolFunc = boolFunc + "~" + relatedNodes.get(i).getLabel() + " , ";
                }

            }

            if (!relatedNodes.get(relatedNodes.size() - 1).isNegated()) {
                boolFunc = boolFunc + relatedNodes.get(relatedNodes.size() - 1).getLabel() + ") , ";
            } else {
                boolFunc = boolFunc + "~" + relatedNodes.get(relatedNodes.size() - 1).getLabel() + ") , ";
            }
        }

        boolFunc = boolFunc + relationType + " , ";

        if (constraint != null) {
            boolFunc = boolFunc + constraint.getLabel() + " , ";
        }

        boolFunc = boolFunc + node.getLabel() + "]";

        return boolFunc;
    }

    public boolean contains(CEG_Node node) {

        if (node.getLabel().equals(this.getNode().getLabel())) {
            return true;
        }

        if (!relatedNodes.isEmpty()) {
            for (CEG_Node relNode : relatedNodes) {
                if (relNode.getLabel().equals(node.getLabel())) {
                    return true;
                }
            }
        }
        return false;
    }

    public void setExpression(String exp) {
        this.expression = exp;
    }

    public String getExpressionInter() {
        return expressionInter;
    }

    public void setExpressionInter(String expressionInter) {
        this.expressionInter = expressionInter;
    }

}
