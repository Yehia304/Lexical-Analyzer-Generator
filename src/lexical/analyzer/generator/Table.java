/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lexical.analyzer.generator;

import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;

/**
 *
 * @author Crap
 */
public class Table {    
    private JFrame f;    
    private ArrayList<ArrayList<String>> transitionTable = new ArrayList<>();
    private ArrayList<String> dataList = new ArrayList<>();
    private String[][] data = new String[10000][10000];
    private String[] columns = new String[10000];
            
            
    public Table(ArrayList<ArrayList<String>> transitionTable, String title){    
        
        
        this.transitionTable = transitionTable;
        columns = new String[transitionTable.get(0).size()];
        data = new String[transitionTable.size()][transitionTable.get(0).size()];
        data = generateData();
        columns = generateColumns();
        
        
        f=new JFrame();     
        //JPanel panel = new JPanel ();
       // panel.setBorder (BorderFactory.createTitledBorder (BorderFactory.createEtchedBorder (),
       //                                                     "WHATEVER",
       //                                                     TitledBorder.CENTER,
       //                                                     TitledBorder.TOP));
        ArrayList<String> s = new ArrayList<>();
        //System.out.println("DATA: "+ data + " Columns: "+ columns);
        JTable jt=new JTable(data, columns);    
        jt.setBounds(30,40,200,300);          
        JScrollPane sp=new JScrollPane(jt);    
        
        //panel.add(jt);
        //f.add(panel);
        f.add(sp);  
        f.setTitle(title);
        f.setSize(300,400);    
        f.setVisible(true);    
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        
    }
    
    public String[][] generateData(){
        int k=0;
        for(int i=1; i<transitionTable.size();i++){
            for(int j=0;j<transitionTable.get(i).size();j++){
                data[i][j] = transitionTable.get(i).get(j);
            }
        }
        
        //System.out.println(data);
        return data;
    }
    
    public String[] generateColumns(){
        columns = (String[]) transitionTable.get(0).toArray(new String[transitionTable.get(0).size()]);
        return columns;
    }
    
    
    
}