package graph;

import java.util.ArrayList;
import org.gephi.graph.api.Node;

/**
 * Represents the intermediate node in CEG
 * @author deniz.kavzak
 *
 */
public class Intermediate extends CEG_Node{
	
	String relationType; //relation type, i.e. AND, OR.
	Relation relation=null; //the relation this node is formed by.
	
	public Intermediate(Node node, Relation relation) {
		super(node);
		this.relationType = relation.getRelationType();
	}

	public String getRelationType() {
		return relationType;
	}

	public Relation getRelation() {
		return relation;
	}
	
	public void setRelation(Relation relation){
		this.relation = relation;
		this.relationType = relation.getRelationType();
	}
        
        /**
	 * Get the cause nodes in expression
	 * @param ceg
	 * @return
	 */
	public ArrayList<CEG_Node> getExpressionNodes(CEG ceg){
		
		ArrayList<CEG_Node> list = new ArrayList<>();
		String exp = relation.getExpression();
		
		for(CEG_Node n : ceg.getCauseNodes()){
			String key = n.getLabel() + " ";
			if(exp.contains(key)){
				list.add(n);
			}
		}
		return list;
	}
}
