/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lexical.analyzer.generator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 *
 * @author Crap
 */
public class DFA {
    private ArrayList<ArrayList<String>> nfaTransitionTable = new ArrayList<>();
    private HashMap<String, String> nfaAcceptanceStates = new HashMap<>();
    private Graph graph;
    private Graph nfaGraph;
    private NFA nfa;
    private int stateNum = 0;
    private HashMap<String, String> stateTranslationTable = new HashMap();
    private ArrayList<ArrayList<String>> arrayOfTotalNextStates = new ArrayList<>();
    private ArrayList<String> noTransitions = new ArrayList<>();
    private ArrayList<String> noTransitionsInput = new ArrayList<>();
    
    public DFA(NFA nfa, Graph graph){
        this.nfa = nfa;
        nfaTransitionTable = nfa.getTransitionTable();
        nfaAcceptanceStates = nfa.getAcceptanceStates();
        this.graph = graph;
        nfaGraph = nfa.getGraph();
        generateDFA();
        addNullState();
        System.out.println("Hash Table: " + stateTranslationTable);
    }
    
    public void generateDFA(){
       /* for(int i=1; i<nfaTransitionTable.size();i++){
            
        }*/
       ArrayList<String> list = new ArrayList<String>();
       stateNum++;
       String stateName = String.valueOf(stateNum);
       graph.createState(stateName);
       E_Closure(list, "1");
       Collections.sort(list);
       stateTranslationTable.put(String.join(",", list), stateName);
       
       arrayOfTotalNextStates.add(list);
       //ArrayList<String> nextStates = getTotalNextStates(list, "o");
       
       for(int i=0; i<arrayOfTotalNextStates.size(); i++){
          
           for(int j=1; j<nfaTransitionTable.get(0).size(); j++){
               boolean alreadyExists = false;
              // System.out.println("AAAAAAAAAA: " + arrayOfTotalNextStates);
               ArrayList<String> totalNextStates = getTotalNextStates(arrayOfTotalNextStates.get(i), nfaTransitionTable.get(0).get(j));
               
               if(totalNextStates.isEmpty()){
                        ArrayList<String> arrlst = arrayOfTotalNextStates.get(i);
                        Collections.sort(arrlst);
                        noTransitions.add(stateTranslationTable.get(String.join(",", arrlst)));
                        
                        noTransitionsInput.add(nfaTransitionTable.get(0).get(j));
                    }else{
                        for(ArrayList<String> arr : arrayOfTotalNextStates){
                             if(isDuplicate(arr, totalNextStates)){
                                 alreadyExists = true;
                                 
                             }
                         }
                         if(alreadyExists){
                             
                             String targetState = stateTranslationTable.get(String.join(",", totalNextStates));
                             graph.connect(stateTranslationTable.get(String.join(",", arrayOfTotalNextStates.get(i))), targetState, nfaTransitionTable.get(0).get(j));
                             alreadyExists = false;
                         }else{
                             stateNum++;
                             stateName = String.valueOf(stateNum);
                             graph.createState(stateName);
                             
                             graph.connect(stateTranslationTable.get(String.join(",", arrayOfTotalNextStates.get(i))), stateName, nfaTransitionTable.get(0).get(j));

                             Collections.sort(totalNextStates);
                             stateTranslationTable.put(String.join(",", totalNextStates), stateName);
                             arrayOfTotalNextStates.add(totalNextStates);
                         }

                    }
               
               
               
               
           }
       }
       
       
       /*for(int i=2; i<nfaTransitionTable.size(); i++){
           E_Closure(list,nfaTransitionTable.get(i).get(0));
       }*/
       //E_Closure(list, "68");
       //System.out.println("DFA: " + list);
       
    }
    
    public void E_Closure(ArrayList<String> eClosureTransitions, String state){
        if(!eClosureTransitions.contains(state))
            eClosureTransitions.add(state);
        for(int i=0; i<nfaTransitionTable.size(); i++){
            if(nfaTransitionTable.get(i).get(0).equals(state)){
                //System.out.println("state: " + state );
                for(int j=0; j<nfaTransitionTable.get(i).size(); j++){
                    if((!nfaTransitionTable.get(i).get(j).equals(" "))&&nfaTransitionTable.get(0).get(j).equals("\\L")){
                        //System.out.println("Found: " + nfaTransitionTable.get(0).get(j));
                        String[] inputs = nfaTransitionTable.get(i).get(j).split(",");
                        for(String nextState : inputs)
                        E_Closure(eClosureTransitions, nextState);
                    }
                }
            }
        }
    }
    
    
    public boolean isDuplicate(ArrayList<String> list1, ArrayList<String> list2){
        if(list1.size() != list2.size()){
            return false;
        }
        
        Collections.sort(list1);
        Collections.sort(list2);
            
        for(int i=0; i<list1.size(); i++){
            if(!list1.get(i).equals(list2.get(i))){
                return false;
            }
        }
        
        return true;
    }
    
    public ArrayList<String> getTotalNextStates(ArrayList<String> currentStates, String input){
        //System.out.println("INPUTS " + inputs);
       // System.out.println("BAAAAAAAAAAAAAA: " + currentStates);
        ArrayList<String> totalNextStates = new ArrayList<>();
        
            
                //System.out.println("Input: " + inputs.get(i));
                for(int j=0; j<currentStates.size(); j++){
                    if(!nfaGraph.getNextState(currentStates.get(j), input).equals(" ")){
                        //System.out.println("State: " + currentStates.get(j) + " Input: " + input + " Next State: " + nfaGraph.getNextState(currentStates.get(j), input));
                        String[] states = nfaGraph.getNextState(currentStates.get(j), input).split(",");
                        //System.out.println("STATES: " + nfaGraph.getNextState(currentStates.get(j), inputs.get(i)));
                        for(int k=0; k<states.length;k++){
                            E_Closure(totalNextStates, states[k]);
                        }
                    }
                }
            
            
        
        
        return totalNextStates;
    }
    
    
    public void addNullState(){

        System.out.println(noTransitions);
        System.out.println(noTransitionsInput);
        stateNum++;
        String stateName = String.valueOf(stateNum);
        graph.createState(stateName);
        
        
        
        for(int i=0; i<noTransitions.size(); i++){
            graph.connect(noTransitions.get(i), stateName, noTransitionsInput.get(i));
        }
        
        
        for(int i=1; i<nfaTransitionTable.get(0).size(); i++){
            graph.connect(stateName, stateName, nfaTransitionTable.get(0).get(i));
        }
    }
    
}