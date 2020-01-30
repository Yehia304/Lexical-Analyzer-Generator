/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lexical.analyzer.generator;

import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Crap
 */
public class Test {
    public static void main(String args[]) throws IOException{
        
        Launcher launcher = new Launcher ("Regex Example.txt");
        ArrayList<ArrayList<String>> transitionTable = launcher.getTransitionTable();
        InfixToPostfix itp = new InfixToPostfix(transitionTable);
        Graph graph = new Graph();
        NFA nfa = new NFA(itp.getPostfix(), itp.getFinalStates(), launcher.getKeywords(), launcher.getpunc(), graph);
        
        graph.printMatrix();
        
        new Table(graph.getTransitionTable(), "NFA");
        Graph graph2 = new Graph();
        DFA dfa = new DFA(nfa, graph2);
        new Table(graph2.getTransitionTable(), "DFA");
        //System.out.println(transitionTable);
    }
}
