package graph;

import org.gephi.graph.api.Node;

/**
 * Represents the nodes in CEG
 *
 * @author deniz.kavzak
 *
 */
public class CEG_Node {

    Node node;
    int grade = 0; //level of the node, i.e. cause nodes have 0 grade.
    String id = null; //id of the node, needed for graphml transformation.
    String label = null; //name of the node, i.e. C1, C2.
    String nodeType = null; //Cause, Intermediate, Constraint or Effect.
    //boolean truthValue; //boolean truth value. IT MAY BE NEEDED WHEN WE USE DECISION TABLE?
    boolean isNegated = false; //used in instances in hashMap. IS IT NEEDED??? 

    public CEG_Node(Node node) {
        this.node = node;
        //System.out.println(node.getNodeData().getId());
        this.id = node.getNodeData().getId();
        this.label = node.getNodeData().getLabel();
        //System.out.println(this.label);
        this.grade = (int) node.getAttributes().getValue("grade");
        this.nodeType = (String) node.getAttributes().getValue("nodeType");
    }

    public CEG_Node() {

    }

    public void setNegated() {
        isNegated = true;
    }

    public boolean isNegated() {
        return isNegated;
    }

    /*
	public boolean isTruthValue() {
		return truthValue;
	}

	public void setTruthValue(boolean truthValue) {
		this.truthValue = truthValue;
	}
     */
    public Node getNode() {
        return node;
    }

    public String getNodeType() {
        return nodeType;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public boolean equals(Object other) {

        if (this.getLabel().equals(((CEG_Node) other).getLabel())) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "CEG_Node [node=" + node + ", grade=" + grade + ", id=" + id + ", label=" + label + ", nodeType="
                + nodeType + ", isNegated=" + isNegated + "]";
    }

    /*
	@Override
	public int compare(CEG_Node o1, CEG_Node o2) {
		System.out.println(o1.getLabel().compareTo(o2.getLabel()));
		return o1.getLabel().compareTo(o2.getLabel());
	}
	
     */
}
