/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lexical.analyzer.generator;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Crap
 */
public class NFA {
    private ArrayList<ArrayList<String>> regExpPostfix;
    private ArrayList<ArrayList<String>> keywords;
    private ArrayList<ArrayList<String>> punc;
    private ArrayList<String> finalStates = new ArrayList<>();
    private Graph graph = new Graph();
    private int stateNum = 0;
    private ArrayList<Network> networkArray = new ArrayList<>();
    private HashMap<String, String> acceptanceStates = new HashMap<>();
    private ArrayList<String> initialStates = new ArrayList<>();
    
    public NFA(ArrayList<ArrayList<String>> regExpPostfix, ArrayList<String> finalStates, ArrayList<ArrayList<String>> keywords, ArrayList<ArrayList<String>> punc, Graph graph){
        this.regExpPostfix = regExpPostfix;
        this.keywords = keywords;
        this.punc = punc;
        this.finalStates = finalStates;
        this.graph = graph;
        
        stateNum++;
        String stateName = String.valueOf(stateNum);
        graph.createState(stateName);
        
        
        generatePunc();
        generateKeywords();
        generateReg();
        createInitialState();
        System.out.println(acceptanceStates);
        
        System.out.println("-----------------------------------------------");
        System.out.println(regExpPostfix);
        System.out.println(keywords);
        System.out.println(punc);
        System.out.println(finalStates);

    }
    
    public void generateKeywords(){
        
        for(int i=0; i<keywords.size(); i++){
            for(int j=0; j<keywords.get(i).size();j++){
                char[] charr = keywords.get(i).get(j).toCharArray();
                for(int k=0; k<charr.length; k++){
                    
                    stateNum++;
                    String s1 = String.valueOf(stateNum);
                    graph.createState(s1);
                    stateNum++;
                    String s2 = String.valueOf(stateNum);
                    graph.createState(s2);
                    graph.connect(s1, s2, String.valueOf(charr[k]));
                    Network network = new Network();
                    network.addToNetwork(s1);
                    network.addToNetwork(s2);
                    network.setInitialState(s1);
                    network.setFinalState(s2);
                    networkArray.add(network);
                    if(k!=0){
                        String initial1 = networkArray.get(networkArray.size()-2).getInitialState();
                        String initial2 = networkArray.get(networkArray.size()-1).getInitialState();
                        String final1 =  networkArray.get(networkArray.size()-2).getFinalState();
                        String final2 =  networkArray.get(networkArray.size()-1).getFinalState();
                        Network tempNetwork = conc(initial1, initial2, final1, final2);
                        tempNetwork.mixNetworks(networkArray.get(networkArray.size()-2), networkArray.get(networkArray.size()-1));
                        networkArray.add(tempNetwork);
                        
                    }  
                }
                System.out.println("Initial: " + networkArray.get(networkArray.size()-1).getInitialState() + " Final: "+networkArray.get(networkArray.size()-1).getFinalState());
                acceptanceStates.put(networkArray.get(networkArray.size()-1).getFinalState(), keywords.get(i).get(j));
                initialStates.add(networkArray.get(networkArray.size()-1).getInitialState());
            }
        }
    }
    
    public void generatePunc(){
        for(int i=0; i<punc.size(); i++){
            for(int j=0; j<punc.get(i).size(); j++){
                
                stateNum++;
                String s1 = String.valueOf(stateNum);
                graph.createState(s1);
                stateNum++;
                String s2 = String.valueOf(stateNum);
                graph.createState(s2);
                graph.connect(s1, s2, punc.get(i).get(j));
                initialStates.add(s1);
                acceptanceStates.put(s2, "punctuation");
            }
        }
    }
    
    public void generateReg(){
        for(int i=0; i<regExpPostfix.size();i++){
            for(int j=0; j<regExpPostfix.get(i).size(); j++){
                if(regExpPostfix.get(i).get(j).equals("|")){
                    String initial1 = networkArray.get(networkArray.size()-2).getInitialState();
                    String initial2 = networkArray.get(networkArray.size()-1).getInitialState();
                    String final1 =  networkArray.get(networkArray.size()-2).getFinalState();
                    String final2 =  networkArray.get(networkArray.size()-1).getFinalState();
                    Network tempNetwork = or(initial1, initial2, final1, final2);
                    tempNetwork.mixNetworks(networkArray.get(networkArray.size()-2), networkArray.get(networkArray.size()-1));
                    tempNetwork.addToNetwork(tempNetwork.getInitialState());
                    tempNetwork.addToNetwork(tempNetwork.getFinalState());
                    for(int k=1;k<=networkArray.size();k++){
                        if(isRedundant(networkArray.get(networkArray.size()-k),tempNetwork)){
                            networkArray.remove(networkArray.size()-k);
                            k--;
                        }
                    }
                    networkArray.add(tempNetwork);
                }else if(regExpPostfix.get(i).get(j).equals(" ")){
                    String initial1 = networkArray.get(networkArray.size()-2).getInitialState();
                    String initial2 = networkArray.get(networkArray.size()-1).getInitialState();
                    String final1 =  networkArray.get(networkArray.size()-2).getFinalState();
                    String final2 =  networkArray.get(networkArray.size()-1).getFinalState();
                    Network tempNetwork = conc(initial1, initial2, final1, final2);
                    tempNetwork.mixNetworks(networkArray.get(networkArray.size()-2), networkArray.get(networkArray.size()-1));
                    for(int k=1;k<=networkArray.size();k++){
                        if(isRedundant(networkArray.get(networkArray.size()-k),tempNetwork)){
                            networkArray.remove(networkArray.size()-k);
                            k--;
                        }
                    }
                    networkArray.add(tempNetwork);
                }else if(regExpPostfix.get(i).get(j).equals("*")){
                    String initial1 = networkArray.get(networkArray.size()-1).getInitialState();
                    String final1 =  networkArray.get(networkArray.size()-1).getFinalState();
                    Network tempNetwork = kleeneClosure(initial1, final1);
                    tempNetwork.addToNetwork(tempNetwork.getInitialState());
                    tempNetwork.addToNetwork(tempNetwork.getFinalState());
                    tempNetwork.addAllToNetwork(networkArray.get(networkArray.size()-1));
                    for(int k=1;k<=networkArray.size();k++){
                        if(isRedundant(networkArray.get(networkArray.size()-k),tempNetwork)){
                            networkArray.remove(networkArray.size()-k);
                            k--;
                        }
                    }
                    networkArray.add(tempNetwork);
                }else if(regExpPostfix.get(i).get(j).equals("+")){
                    String initial1 = networkArray.get(networkArray.size()-1).getInitialState();
                    String final1 =  networkArray.get(networkArray.size()-1).getFinalState();
                    Network tempNetwork = positiveClosure(initial1, final1);
                    tempNetwork.addToNetwork(tempNetwork.getInitialState());
                    tempNetwork.addToNetwork(tempNetwork.getFinalState());
                    tempNetwork.addAllToNetwork(networkArray.get(networkArray.size()-1));
                    for(int k=1;k<=networkArray.size();k++){
                        if(isRedundant(networkArray.get(networkArray.size()-k),tempNetwork)){
                            networkArray.remove(networkArray.size()-k);
                            k--;
                        }
                    }
                    networkArray.add(tempNetwork);
                }else{
                    stateNum++;
                    String s1 = String.valueOf(stateNum);
                    graph.createState(s1);
                    stateNum++;
                    String s2 = String.valueOf(stateNum);
                    graph.createState(s2);
                    graph.connect(s1, s2, regExpPostfix.get(i).get(j));
                    Network network = new Network();
                    network.addToNetwork(s1);
                    network.addToNetwork(s2);
                    network.setInitialState(s1);
                    network.setFinalState(s2);
                    networkArray.add(network);
                }
            }
            System.out.println("Initial: " + networkArray.get(networkArray.size()-1).getInitialState() + " Final: "+networkArray.get(networkArray.size()-1).getFinalState());
            acceptanceStates.put(networkArray.get(networkArray.size()-1).getFinalState(), finalStates.get(i));
            initialStates.add(networkArray.get(networkArray.size()-1).getInitialState());
        }
    }
    
    
    public Network or(String firstInitial, String secondInitial, String firstFinal, String secondFinal){
        stateNum++;
        String s1 = String.valueOf(stateNum);
        graph.createState(s1);
        graph.connect(s1, firstInitial, "\\L");
        graph.connect(s1, secondInitial, "\\L");
        stateNum++;
        String s2 = String.valueOf(stateNum);
        graph.createState(s2);
        graph.connect(firstFinal, s2, "\\L");
        graph.connect(secondFinal, s2, "\\L");
        Network network = new Network();
        network.setInitialState(s1);
        network.setFinalState(s2);
        return network;
    }
    
    public Network conc(String firstInitial, String secondInitial, String firstFinal, String secondFinal){
        
        graph.connect(firstFinal, secondInitial, "\\L");
        Network network = new Network();
        network.setInitialState(firstInitial);
        network.setFinalState(secondFinal);
        return network;
    }
    
    public Network kleeneClosure(String initialState,String finalState){
        
        stateNum++;
        String s1 = String.valueOf(stateNum);
        graph.createState(s1);
        graph.connect(s1, initialState, "\\L");
        stateNum++;
        String s2 = String.valueOf(stateNum);
        graph.createState(s2);
        graph.connect(s1, s2, "\\L");
        graph.connect(finalState, initialState, "\\L");
        graph.connect(finalState, s2, "\\L");
        Network network = new Network();
        network.setInitialState(s1);
        network.setFinalState(s2);
        return network;
    }
    
    public Network positiveClosure(String initialState, String finalState){
        stateNum++;
        String s1 = String.valueOf(stateNum);
        graph.createState(s1);
        graph.connect(s1, initialState, "\\L");
        stateNum++;
        String s2 = String.valueOf(stateNum);
        graph.createState(s2);
        graph.connect(finalState, initialState, "\\L");
        graph.connect(finalState, s2, "\\L");
        Network network = new Network();
        network.setInitialState(s1);
        network.setFinalState(s2);
        return network;
    }
    
    public boolean isRedundant(Network n1, Network n2){
        ArrayList<String> a1 = n1.getStates();
        ArrayList<String> a2 = n2.getStates();
        for(int i=0;i<a1.size();i++){
            for(int j=0;j<a2.size();j++){
               // System.out.println(a1.get(i));
                // System.out.println(a2.get(j));
               if(a1.get(i).equals(a2.get(j))){
                   return true;
               }
            }
                
        }
        return false;
            
        
    }
    
    
    public HashMap<String, String> getAcceptanceStates(){
        return acceptanceStates;
    }
    
    public int getStateNum(){
        return stateNum;
    }
    
    public ArrayList<ArrayList<String>> getTransitionTable(){
        return graph.getTransitionTable();
    }
    
    public Graph getGraph(){
        return graph;
    }
    
    public void createInitialState(){
        
        
        for(String str : initialStates){
            graph.connect("1", str, "\\L");
           
        }
        
    }
    
}
