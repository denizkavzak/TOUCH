/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BORMI;

/**
 * TODO
 * @author deniz.kavzak
 */
public class Singularity {
    public static boolean isSingular(String expression){
        
            String modifiedExpression = expression;
            
            while(modifiedExpression.contains("C")){
                int i = modifiedExpression.indexOf("C");
                int j = modifiedExpression.indexOf(" ", i);
                String node = modifiedExpression.substring(i,j);
                
                modifiedExpression = modifiedExpression.replaceFirst(node, "");
                if(modifiedExpression.contains(node)){
                    return false;
                }
                
            }
            
            return true;   
        }
        
        public static boolean isMutuallySingular(String expression1, String expression2){
            
            String modifiedExpression1 = expression1;
            String modifiedExpression2 = expression2;
            
            while(modifiedExpression1.contains("C")){
                int i = modifiedExpression1.indexOf("C");
                int j = modifiedExpression1.indexOf(" ", i);
                String node = modifiedExpression1.substring(i,j);
                                
                modifiedExpression1 = modifiedExpression1.replaceFirst(node, "");
                
                if(modifiedExpression2.contains(node)){
                    return false;
                }
                
            }
            
            return true;
        }
      
}
