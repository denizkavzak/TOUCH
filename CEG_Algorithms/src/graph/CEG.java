package graph;

import java.util.ArrayList;
import java.util.HashMap;

import org.gephi.graph.api.Edge;
import org.gephi.graph.api.Node;
import org.gephi.graph.api.UndirectedGraph;

//The Cause-Effect Graph formed from a given graph in graphml format
public class CEG {

    UndirectedGraph graph; //The graph from gephi.
    ArrayList<CEG_Node> nodes; //All nodes in CEG.
    ArrayList<CEG_Edge> edges; //All edges in CEG.

    private ArrayList<Cause> causeNodes; //Cause nodes in CEG.
    private ArrayList<Effect> effectNodes; //Effect nodes in CEG.
    private ArrayList<Intermediate> interNodes; //Intermediate nodes in CEG.
    private ArrayList<Constraint> consNodes; //Constraint nodes in CEG.
    private ArrayList<Relation> relations; //Relations in CEG.
    private DNF_Converter dc;
    HashMap<String, ArrayList<Relation>> effectMap;
    HashMap<String, ArrayList<Relation>> interMap;

    public CEG(UndirectedGraph graph) {
        this.graph = graph;
        relations = null;
        init(graph);
    }

    //Fills the lists of nodes and relations from the graph.
    private void init(UndirectedGraph graph) {

        nodes = new ArrayList<>();
        edges = new ArrayList<>();
        causeNodes = new ArrayList<>();
        effectNodes = new ArrayList<>();
        interNodes = new ArrayList<>();
        consNodes = new ArrayList<>();

        for (Node n : graph.getNodes()) {
            nodes.add(new CEG_Node(n));
        }

        for (Edge e : graph.getEdges()) {
            edges.add(new CEG_Edge(e));
        }

        for (CEG_Node cegNode : nodes) {
            if (cegNode.getNodeType().equals("Cause")) {
                Cause cNode = new Cause(cegNode.getNode());
                //System.out.println("Cause : " + cNode);
                causeNodes.add(cNode);
            } else if (cegNode.getNodeType().equals("Effect")) {
                Effect eNode = new Effect(cegNode.getNode(), new Relation(cegNode));
                effectNodes.add(eNode);
            } else if (cegNode.getNodeType().equals("Intermediate")) {
                Intermediate iNode = new Intermediate(cegNode.getNode(), new Relation(cegNode));
                interNodes.add(iNode);
            } else if (cegNode.getNodeType().equals("Constraint")) {
                Constraint cNode = new Constraint(cegNode.getNode());
                consNodes.add(cNode);
            }
        }

        relations = new ArrayList<>();
        setRelations(this);
        createMinMaps();
        createExp();
        createInnerExp();
        reformExp();
        setExpressionCauseNodes();
        dc = new DNF_Converter(this);
        setMin();

        //reformDNF(); 
        //setDNF_Exp();
    }

    public DNF_Converter getDNFConverter(){
        return this.dc;
    }
    
    /**
     * Creates the boolean expression for all relations with intermediate nodes
     * in it
     */
    private void createExp() {

        String exp;

        for (Relation rel : relations) {
            exp = "";
            int i = 0;

            if (!rel.getRelatedNodes().get(i).isNegated()) {
                exp = exp + "( " + rel.getRelatedNodes().get(i).getLabel();
            } else {
                exp = exp + "( " + "~" + rel.getRelatedNodes().get(i).getLabel();
            }
            i++;

            while (i < rel.getRelatedNodes().size() - 1) {
                if (!rel.getRelatedNodes().get(i).isNegated()) {
                    exp = exp + " " + rel.getRelationType() + " " + rel.getRelatedNodes().get(i).getLabel();
                } else {
                    exp = exp + " " + rel.getRelationType() + " " + "~" + rel.getRelatedNodes().get(i).getLabel();
                }
                i++;
            }

            if (rel.getRelatedNodes().size() == 1) {
                exp = exp + " )";
            } else {
                if (!rel.getRelatedNodes().get(i).isNegated()) {
                    exp = exp + " " + rel.getRelationType() + " " + rel.getRelatedNodes().get(i).getLabel() + " )";
                } else {
                    exp = exp + " " + rel.getRelationType() + " " + "~" + rel.getRelatedNodes().get(i).getLabel() + " )";
                }
            }
            rel.setExpression(exp);
            rel.setExpressionInter(exp);
        }

    }

    public DNF_Converter getDc() {
        return dc;
    }

    /**
     * Gets the expression with intermediate nodes in it, converts it to the
     * expression with only cause nodes
     */
    private void createInnerExp() {
        for (Relation rel : relations) {

            String exp = rel.getExpression();

            while (exp.contains("I")) {

                int st = exp.indexOf("I");

                int end = exp.indexOf(" ", st);

                String target = exp.substring(st, end);
                //System.out.println(this.getInterMap().keySet().toString());   
                //System.out.println(target);
                String replacement = this.getInterMap().get(target).get(0).getExpression();

                exp = exp.replaceFirst(target, replacement);
                rel.setExpression(exp);
            }

        }
    }

    /**
     * Reforms the expression with "&&" and "||" and "!" in order to evaluate
     * them.
     *
     * @param exp
     * @return
     */
    private String reformBoolExp(String exp) {
        String e = exp;
        e = e.replaceAll("AND", "&&");
        e = e.replaceAll("OR", "||");
        e = e.replaceAll("~", "!");
        return e;
    }

    private void reformExp() {

        String exp;

        for (Effect e : effectNodes) {
            exp = e.getRelation().getExpression();
            exp = reformBoolExp(exp);
            e.getRelation().setExpression(exp);
        }
    }

    /**
     *
     */
    public void reformDNF() {
        String exp;

        for (Effect e : effectNodes) {
            exp = e.getDNFasExp();
            exp = reformBoolExp(exp);
            e.setReformedDNFexp(exp);
            e.setDNFExpression(this);
        }
    }

    /**
     * This version is used for the other DNF conversion tool
     */
    private void setMin() {
        for (Effect e : effectNodes) {
            e.setDNFExpressionMin(this);
        }
    }

    /*
	public void setDNF_Exp() {
		
		for(Effect e : effectNodes){
			DNF_Expression de = new DNF_Expression(this, e);
		}
	}
     */
    
    public UndirectedGraph getGraph() {
        return graph;
    }

    public ArrayList<CEG_Node> getNodes() {
        return nodes;
    }

    public ArrayList<CEG_Edge> getEdges() {
        return edges;
    }

    public ArrayList<Relation> getRelations() {
        return relations;
    }

    public ArrayList<Cause> getCauseNodes() {
        return causeNodes;
    }

    public ArrayList<Effect> getEffectNodes() {
        return effectNodes;
    }

    public ArrayList<Intermediate> getInterNodes() {
        return interNodes;
    }

    public ArrayList<Constraint> getConsNodes() {
        return consNodes;
    }

    public HashMap<String, ArrayList<Relation>> getEffectMap() {
        return effectMap;
    }

    public HashMap<String, ArrayList<Relation>> getInterMap() {
        return interMap;
    }

    public Effect getEffectNode(String label) {
        for (Effect e : this.getEffectNodes()) {
            if (e.getLabel().equals(label)) {
                return e;
            }
        }
        return null;
    }

    public Intermediate getIntermediateNode(String label) {
        for (Intermediate i : this.getInterNodes()) {
            if (i.getLabel().equals(label)) {
                return i;
            }
        }
        return null;
    }

    public Node getNodeByLabel(String label) {

        for (CEG_Node n : nodes) {
            if (n.getLabel().equals(label)) {
                return this.getGraph().getNode(n.getId());
            }
        }

        return null;
    }

    /**
     * Sets the relations and fills them in the list from the graph by parsing
     * the relation attribute of nodes.
     */
    private void setRelations(CEG ceg) {

        String rel;
        Relation relation = null;

        for (CEG_Node n : ceg.getNodes()) {

            relation = new Relation(n);
            rel = (String) n.getNode().getAttributes().getValue("relation");

            if (!rel.equals("null")) {

                CEG_Node cegNode;

                if (rel.contains("-")) {

                    String[] rel_sp = rel.split("\\-");
                    String neighbors = rel_sp[0];
                    String relationType = rel_sp[1];

                    relation.setRelationType(relationType.trim());

                    String[] neighbor_nodes = neighbors.split("\\+");

                    for (Constraint cons : consNodes) {
                        String constraintInter = cons.getResultNodeLabel();
                        if (n.getLabel().equals(constraintInter)) {
                            relation.addConstraint(cons);
                        }
                    }
                    for (int i = 0; i < neighbor_nodes.length; i++) {
                        if (!neighbor_nodes[i].startsWith("~")) {
                            //System.out.println(n.getNode().getNodeData().getLabel());
                            cegNode = new CEG_Node(ceg.getNodeByLabel(neighbor_nodes[i].trim()));
                            relation.addRelatedNodes(cegNode);
                        } else {
                            cegNode = new CEG_Node(ceg.getNodeByLabel(neighbor_nodes[i].substring(1).trim()));
                            cegNode.setNegated();
                            relation.addRelatedNodes(cegNode);
                        }
                    }

                } else {
                    if (rel.contains("~")) {
                        relation.setRelationType("NOT");
                        cegNode = new CEG_Node(ceg.getNodeByLabel(rel.substring(1).trim()));
                        cegNode.setNegated();
                        relation.addRelatedNodes(cegNode);
                    } else {
                        cegNode = new CEG_Node(ceg.getNodeByLabel(rel.trim()));
                        relation.addRelatedNodes(cegNode);
                    }
                }

                for (Intermediate inter : this.interNodes) {
                    if (inter.getLabel().equals(n.getLabel())) {
                        inter.setRelation(relation);
                    }
                }
                for (Effect effect : this.effectNodes) {
                    if (effect.getLabel().equals(n.getLabel())) {
                        effect.setRelation(relation);
                    }
                }

                relations.add(relation);
            }
        }
    }

    /*
	 * Creates a hash map for all nodes with the relations they occur in.

	public HashMap<String, ArrayList<Relation>> createMap(){
		
		HashMap<String, ArrayList<Relation>> map = new HashMap<>();
		
		ArrayList<Relation> nodeRelations;
		
		for (CEG_Node node : nodes) {
			
			if(!(node instanceof Constraint)){
				
				nodeRelations = new ArrayList<>();
				
				for (Relation relation : relations) {
					if(relation.contains(node)){
						nodeRelations.add(relation);
					}
				}
				
				map.put(node.getLabel(), nodeRelations);
			}
		}
		
		return map;
		
	}
     */
    
    /**
     * Fills the effect and intermediate node hash maps to directly get their
     * relations by their labels
     */
    private void createMinMaps() {

        effectMap = new HashMap<>();
        interMap = new HashMap<>();

        ArrayList<Relation> nodeRelations;

        for (Effect effect : effectNodes) {

            nodeRelations = new ArrayList<>();

            for (Relation relation : relations) {
                if (relation.getNode().getLabel().equals(effect.getLabel())) {
                    nodeRelations.add(relation);
                }
            }
            if (!nodeRelations.equals(null)) {
                effectMap.put(effect.getLabel(), nodeRelations);
            }
        }

        for (Intermediate intermediate : interNodes) {

            nodeRelations = new ArrayList<>();

            for (Relation relation : relations) {
                if (relation.getNode().getLabel().equals(intermediate.getLabel())) {
                    nodeRelations.add(relation);
                }
            }
            if (!nodeRelations.equals(null)) {
                interMap.put(intermediate.getLabel(), nodeRelations);
            }
        }

    }

    public CEG_Node getCauseLabelNode(String label) {
        for (CEG_Node node : causeNodes) {
            if (node.getLabel().equalsIgnoreCase(label)) {
                return node;
            }
        }
        return null;
    }

    /*
	public CEG_Node getLabelNode(String label){
		for (CEG_Node node : getNodes()) {
			if(node.getLabel() == label){
				return node;
			}	
		}
		
		return null;
	}
     */
    private void setExpressionCauseNodes() {
        for (Effect e : effectNodes) {
            e.setExpressionCauseNodes(this);
        }
    }
}
