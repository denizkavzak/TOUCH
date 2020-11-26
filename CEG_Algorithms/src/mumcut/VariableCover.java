package mumcut;

import test.Value;

/**
 *
 * @author deniz.kavzak
 */
public class VariableCover {
    
    private String label;
    private boolean trueVal = false;
    private boolean falseVal = false;
    //private boolean covered = false;
    
    public VariableCover(String label){
        this.label = label;
    }
    
    public void setValue(Value val){
        if(val == Value.True){
            setTrueVal();
        }else if(val == Value.False){
            setFalseVal();
        }
    }
    
    public Value missingValue(){
        if(trueVal == false){
            return Value.True;
        }else if(falseVal == false){
            return Value.False;
        }
        return Value.NA;
    }
    
    public void setTrueVal(){
        trueVal = true;
    }
    
    public void setFalseVal(){
        falseVal = true;
    }

    public String getLabel() {
        return label;
    }

    public boolean isTrueVal() {
        return trueVal;
    }

    public boolean isFalseVal() {
        return falseVal;
    }

    public boolean isCovered() {
        
        if(trueVal == true && falseVal == true)
            return true;
        else
            return false;
    }

  //  public void setCovered(boolean covered) {
        //this.covered = covered;
  //  }

}
