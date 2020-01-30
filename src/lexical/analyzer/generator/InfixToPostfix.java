/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lexical.analyzer.generator;

import java.util.ArrayList;
import java.util.Stack;

/**
 *
 * @author Crap
 */
public class InfixToPostfix {
    private ArrayList<ArrayList<String>> regExp = new ArrayList<>();
    private ArrayList<String> priorities = new ArrayList<>();
    private ArrayList<String> finalStates = new ArrayList<>();
    private ArrayList<ArrayList<String>> postfix = new ArrayList<>();
    
    public InfixToPostfix(ArrayList<ArrayList<String>> regExp){
        this.regExp = regExp;
        generate();
        
    }
    private int priority(String token){
        if(token.equals("*")  || token.equals("+") ){
            return 3;
        }else if(token.equals(" ")){
            return 2;
        }else if (token.equals("|")){
            return 1;
        }else 
            return 0;
                   
    }
    private boolean isOperator(String str){
        if(str.equals("|")  || str.equals("*")  || str.equals("+") || str.equals("(") || str.equals(")") || str.equals(" ") )
            return true;
        else
            return false;
        
    }
    
    private void generate(){
        Stack<String> stack = new Stack();
        
        
        for(int i =0; i<regExp.size();i++){
            finalStates.add(regExp.get(i).get(0));
            ArrayList<String> output = new ArrayList<>();
            for(int j=1; j<regExp.get(i).size();j++){
               //System.out.println("Input: "+regExp.get(i).get(j));
                if(!isOperator(regExp.get(i).get(j))){       
                    output.add(regExp.get(i).get(j)); //still there is a problem here
                    
                    //System.out.println("output first if " + output);
                }else if(regExp.get(i).get(j).equals("(")){
                    //System.out.println("wrong stack1 " + stack);
                    stack.push(regExp.get(i).get(j));
                    //System.out.println("wrong stack2 " + stack);
                }else if(regExp.get(i).get(j).equals(")")){
                    
                    while (!stack.isEmpty() && !stack.peek().equals("(")) {
                       output.add(stack.pop());  
                    }
                    if(j+1 <regExp.get(i).size() ){
                        if(regExp.get(i).get(j+1).equals("*") || regExp.get(i).get(j+1).equals("+")){
                        output.add(regExp.get(i).get(j+1));
                        j++;
                        } 
                    }
                     
                    //System.out.println(stack);
                    if(!stack.isEmpty()){
                        if(stack.peek().equals("("))
                            stack.pop();
                    }
                   // System.out.println(stack);
                }else{
                    
                    while (!stack.isEmpty() && priority(regExp.get(i).get(j)) <= priority(stack.peek())) 
                        
                    
                         output.add(stack.pop()); 
                         //System.out.println(" Token: "+ regExp.get(i).get(j) + " output: " + output);
                   
                   
                   stack.push(regExp.get(i).get(j)); 
                    
                }
                    
                
            }
           // System.out.println("stack test0 " + stack);
            while(!stack.isEmpty())
               output.add(stack.pop()); 
            //System.out.println("stack test1 " + stack);
            //postfix.add(output);
            //System.out.println("Output: " + output); 
            
            postfix.add(i, output);
            
        }
        
        
        //System.out.println("xxx " + postfix);
    }
    
    /*public boolean isCon(String a, String b){
        if(!(a.equals("(")||a.equals(")")||a.equals("\\+")||a.equals("-")||a.equals("\\*")||a.equals("/")||a.equals("\\=")||a.equals("<")||a.equals(">")||a.equals("!")||a.equals("+")||a.equals("*")||a.equals("|")) && !(b.equals("(")||b.equals(")")||b.equals("\\+")||b.equals("-")||b.equals("\\*")||b.equals("/")||b.equals("\\=")||b.equals("<")||b.equals(">")||b.equals("!")||b.equals("+")||b.equals("*")||b.equals("|"))){
            System.out.println(a + " " + b);
            return true;
        }else if((a.equals("\\+")||a.equals("-")||a.equals("\\*")||a.equals("/")||a.equals("\\=")||a.equals("<")||a.equals(">")||a.equals("!")) && (b.equals("\\+")||b.equals("-")||b.equals("\\*")||b.equals("/")||b.equals("\\=")||b.equals("<")||b.equals(">")||b.equals("!"))){
            System.out.println(a + " " + b);
            return true;
        }else if(a.equals(")") && b.equals("(")){
            System.out.println(a + " " + b);
            return true;
        }else if(a.equals("+") && b.equals("(") ){
            System.out.println(a + " " + b + "XXXXXXXXXXXXXXXXXXXXXXX");
            return true;
        }else if(a.equals("*") && b.equals("(")){
            System.out.println(a + " " + b);
            return true;
        }else if(a.equals("E")){
            System.out.println(a + " " + b);
            return true;
        }else if(a.equals(".") || b.equals(".")){
            System.out.println(a + " xxx " + b);
            return true;
        }else{
            return false;
        }
    }*/
    
    public ArrayList<ArrayList<String>> getPostfix(){
        return postfix;
    }
    
    public ArrayList<String> getFinalStates(){
        return finalStates;
    }
}
