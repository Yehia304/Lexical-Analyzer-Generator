/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lexical.analyzer.generator;

import java.util.ArrayList;

/**
 *
 * @author Crap
 */
public class Network {
    
    private ArrayList<String> states = new ArrayList<>();
    private String initialState;
    private String finalState;
    
    public Network(){
        
    }
    
    
    public void setInitialState(String initialState){
        this.initialState = initialState;
    }
    
    public void setFinalState(String finalState){
        this.finalState = finalState;
    }
    
    public void addToNetwork(String stateName){
        states.add(stateName);
    }
    
    public String getInitialState(){
        return initialState;
    }
    
    public String getFinalState(){
        return finalState;
    }
    
    public ArrayList<String> getStates(){
        return states;
    }
    
    public void addAllToNetwork(Network n1){
        ArrayList<String> a1 = n1.getStates();
        for(String s : a1)addToNetwork(s);
    }
    
    public void mixNetworks(Network n1, Network n2){
        ArrayList<String> a1 = n1.getStates();
        ArrayList<String> a2 = n2.getStates();
        ArrayList<String> sum = new ArrayList<>();
        sum.addAll(a1);
        sum.addAll(a2);
        
        for(String s : sum)addToNetwork(s);
        
    }
    
    
}
