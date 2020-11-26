package graph;

import org.gephi.graph.api.Edge;

/**
 * Represents the edges in the CEG
 *
 * @author deniz.kavzak
 *
 */
public class CEG_Edge {

    Edge edge;
    CEG_Node source;
    CEG_Node target;
    boolean negated = false;

    public CEG_Edge(Edge edge) {
        this.edge = edge;
        this.source = new CEG_Node(edge.getSource());
        this.target = new CEG_Node(edge.getTarget());

        if (edge.getAttributes().getValue("neg") != null) {
            if (edge.getAttributes().getValue("neg").equals("NOT")) {
                this.negated = true;
            }
        }
    }

}
