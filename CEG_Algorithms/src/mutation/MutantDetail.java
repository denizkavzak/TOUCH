/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mutation;

import graph.Effect;
import java.util.Objects;

/**
 *
 * @author Deniz
 */
public class MutantDetail {
    
    private Effect effect;
    private String type;
    private String expression;

    public MutantDetail(Effect effect, String type, String expression) {
        this.effect = effect;
        this.type = type;
        this.expression = expression;
    }

    public Effect getEffect() {
        return effect;
    }

    public String getType() {
        return type;
    }

    public String getExpression() {
        return expression;
    }

    @Override
    public String toString() {
        return effect.getLabel() + ", " + type + ", " + expression;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MutantDetail other = (MutantDetail) obj;
        if (!this.type.equals(other.type)) {
            return false;
        }
        if (!this.expression.equals(other.expression)) {
            return false;
        }
        if (!this.effect.getLabel().equals(other.effect.getLabel())) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.effect);
        hash = 89 * hash + Objects.hashCode(this.type);
        hash = 89 * hash + Objects.hashCode(this.expression);
        return hash;
    }
    
    
}
