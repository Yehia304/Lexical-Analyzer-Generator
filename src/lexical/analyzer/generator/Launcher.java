/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lexical.analyzer.generator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Stream;

/**
 *
 * @author Crap
 */
public class Launcher {
    
    private static ArrayList<ArrayList<String>> input = new ArrayList<>();
    private static ArrayList<ArrayList<String>> regDef = new ArrayList<>();
    private static ArrayList<ArrayList<String>> regExp = new ArrayList<>();
    private static ArrayList<ArrayList<String>> keywords = new ArrayList<>();
    private static ArrayList<ArrayList<String>> punc = new ArrayList<>();
    private static ArrayList<String> beforeEqu = new ArrayList<>();
    private static ArrayList<ArrayList<String>> sortedRegDef = new ArrayList<>();
    private static ArrayList<ArrayList<String>> finalRegExp = new ArrayList<>();
    private static final String rangeExp = "([a-zA-Z0-9]-[a-zA-Z0-9])";
    
    public Launcher(String filepath) throws IOException{
        File file = new File(filepath);
        readInput(file);
        formulateRegExp();
        formulate();
        addCon();
        //System.out.println(finalRegExp);
    }
    
    public static void readInput(File file) throws FileNotFoundException, IOException{
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            
            
            while ((line = br.readLine()) != null) {
                
               if(line.charAt(0)=='{'){
                   line = line.replace("{", "");
                   line = line.replace("}", "");
                   ArrayList<String> arrayList = new ArrayList<String>(Arrays.asList(line.replaceAll("^\\s", "").split("\\s"))); 
                   keywords.add(arrayList);
               }else if(line.charAt(0)=='['){
                   line = line.replace("[", "");
                   line = line.replace("]", "");
                   ArrayList<String> arrayList = new ArrayList<String>(Arrays.asList(line.replaceAll("^\\s", "").split("\\s"))); 
                   punc.add(arrayList);
                   System.out.println(line);
               }else{
                   ArrayList<String> arrayList = new ArrayList<String>(Arrays.asList(line.replaceAll("^\\s", "").split("\\s"))); 
                   if(arrayList.get(0).charAt(arrayList.get(0).length()-1)==':'){
                       arrayList.set(0, arrayList.get(0).replace(":", ""));
                       regExp.add(arrayList);
                   }
                   
                   else if(arrayList.get(1).equals("=")){
                       arrayList.remove(1);
                       regDef.add(arrayList);
                   }
               }
            }
            System.out.println("------------------regDef---------------");
            System.out.println(regDef);
            System.out.println("------------------regExp---------------");
            System.out.println(regExp);
            System.out.println("------------------Keywords---------------");
            System.out.println(keywords);
            System.out.println("------------------Punctuations---------------");
            System.out.println(punc);
        }
    }
    
    public ArrayList<ArrayList<String>> getRegDef(){
        return regDef;
    }
    public ArrayList<ArrayList<String>> getRegExp(){
        return regExp;
    }
    public ArrayList<ArrayList<String>> getKeywords(){
        return keywords;
    }
    public ArrayList<ArrayList<String>> getpunc(){
        return punc;
    }
    
    public void formulateRegExp(){
        for(int i=0; i<regDef.size();i++){
            beforeEqu.add(regDef.get(i).get(0));
        }
        
        Collections.sort(beforeEqu);
        Collections.reverse(beforeEqu);
        
        for(String str : beforeEqu){
            for(ArrayList<String> arl : regDef){
                if(str.equals(arl.get(0))){
                    sortedRegDef.add(arl);
                }
            }
        }
        
       // System.out.println(sortedRegDef);
        
        
    for(int i=0; i<sortedRegDef.size();i++){
        
            String token = sortedRegDef.get(i).get(0);
            String tokenValue = "";        
            for(int j=1; j<sortedRegDef.get(i).size();j++){
                 tokenValue = tokenValue + sortedRegDef.get(i).get(j);
            }
            tokenValue = "(" + tokenValue + ")";
            for(int j=0; j<regExp.size();j++){
                for(int k=0; k<regExp.get(j).size();k++){
                    if(regExp.get(j).get(k).contains(token)){
                        ArrayList<String> temp1 = regExp.get(j);
                        String str = temp1.get(k);
                        str = str.replace(token, tokenValue);
                        temp1.set(k, str);
                        regExp.set(j, temp1);
                    }
                }
            }
            //System.out.println(tokenValue);

    }

       // System.out.println(regExp);
    }
    
    
    public void formulate(){
        String s = "";  //removing backslash and spaces 
        //System.out.println(regExp);
        for(ArrayList<String> arr : regExp){
            s = s + "" + String.join(" ", arr) + "$";
        }
        //System.out.println("xxx"+s+"xxx");
        char[] charr = s.toCharArray();
        String s2 = "";
        for(int i=0; i<charr.length; i++){
            if(charr[i] == '\\'){
               s2 = s2 + " " + charr[i]; 
            }else if(charr[i]=='(' || charr[i]==')' || (charr[i]=='+' && charr[i-1]!='\\') || charr[i]=='|' || (charr[i]=='*' && charr[i-1]!='\\')){
                s2 = s2 + " " + charr[i] + " ";
            }else{
                s2 = s2 + charr[i];
            }
        }
        //System.out.println(s2);
        //System.out.println("XXXXXXXXX"+s2+"XXXXXXXXXXXX");
        ArrayList<String> temp = new ArrayList<>(Arrays.asList(s2.split("\\$"))); 
        
        for(String str : temp){
            //System.out.println(str);
            /*char[] edit = str.toCharArray();
            String s3 = "";
            for(int i=0; i<edit.length; i++){
                if(edit[i]=='\\' && edit[i+1]!='L'){
                    s3 = s3 + " ";
                }else{
                s3 = s3 + edit[i];
                }
                }*/
            //System.out.println(str);
            ArrayList<String> temp2 = new ArrayList<String>(Arrays.asList(str.replaceAll("^\\s", "").split("\\s")));
            //System.out.println(temp2);
            //System.out.println(temp2);
            finalRegExp.add(temp2);
        }
        
       // System.out.println(finalRegExp);
        
        
        //System.out.println(finalRegExp);
        for(int i =0; i<finalRegExp.size();i++){
            for(int j=0; j<finalRegExp.get(i).size();j++){
                if(finalRegExp.get(i).get(j).equals("")){
                    ArrayList<String> aaa = finalRegExp.get(i);
                    aaa.remove(j);
                    finalRegExp.set(i, aaa);
                    j--;
                }
            }
        }
        
        /*String s4=""; //adding dots
        for(ArrayList<String> arr : finalRegExp){
            s4 = s4 + String.join(" ", arr) + "$";
        }
        char[] dotEdit = s4.toCharArray();
        //System.out.println(dotEdit);
        String s5 = "";
        for(int i=0; i<dotEdit.length; i++){
            if(dotEdit[i]==')' && dotEdit[i+2]=='('){
                s5 = s5 + dotEdit[i] + " .";
            }else{
                s5 = s5 + dotEdit[i];
            }
        }
        System.out.println(s5);*/
        //System.out.println(finalRegExp);
        
        //printArrayListForAshraf();
    }
    
    
    
    public void printArrayListForAshraf(){
        for(ArrayList<String> arr : finalRegExp){
            for(String str : arr){
                System.out.println(str);
            }
        }
        //System.out.println("");
    }
    
    
    public ArrayList<ArrayList<String>> getTransitionTable(){
        return finalRegExp;
    }
    
    
    public void addCon(){
        for(int i=0; i<finalRegExp.size(); i++){
            for(int j=1; j<finalRegExp.get(i).size();j++){
                String a = finalRegExp.get(i).get(j);
                if(j+1 >= finalRegExp.get(i).size())break;
                String b = finalRegExp.get(i).get(j+1);
                
                if(!(a.equals("(")||a.equals(")")||a.equals("\\+")||a.equals("-")||a.equals("\\*")||a.equals("/")||a.equals("\\=")||a.equals("<")||a.equals(">")||a.equals("!")||a.equals("+")||a.equals("*")||a.equals("|")) && !(b.equals("(")||b.equals(")")||b.equals("\\+")||b.equals("-")||b.equals("\\*")||b.equals("/")||b.equals("\\=")||b.equals("<")||b.equals(">")||b.equals("!")||b.equals("+")||b.equals("*")||b.equals("|"))){
                    
                    ArrayList<String> arr = new ArrayList<>();
                    arr = finalRegExp.get(i);
                    arr.add(j+1, " ");
                    finalRegExp.set(i, arr);
                    
                    
                    
                    //System.out.println(a + " " + b);
                }else if((a.equals("\\+")||a.equals("-")||a.equals("\\*")||a.equals("/")||a.equals("\\=")||a.equals("<")||a.equals(">")||a.equals("!")) && (b.equals("\\+")||b.equals("-")||b.equals("\\*")||b.equals("/")||b.equals("\\=")||b.equals("<")||b.equals(">")||b.equals("!"))){
                    ArrayList<String> arr = new ArrayList<>();
                    arr = finalRegExp.get(i);
                    arr.add(j+1, " ");
                    finalRegExp.set(i, arr);
                    j++;
                   // System.out.println(a + " " + b);
                }else if(a.equals(")") && b.equals("(")){
                    ArrayList<String> arr = new ArrayList<>();
                    arr = finalRegExp.get(i);
                    arr.add(j+1, " ");
                    finalRegExp.set(i, arr);
                    j++;
                    //System.out.println(a + " " + b);
                }else if(a.equals("+") && b.equals("(") ){
                    ArrayList<String> arr = new ArrayList<>();
                    arr = finalRegExp.get(i);
                    arr.add(j+1, " ");
                    finalRegExp.set(i, arr);
                    j++;
                    //System.out.println(a + " " + b + "XXXXXXXXXXXXXXXXXXXXXXX");
                }else if(a.equals("*") && b.equals("(")){
                    ArrayList<String> arr = new ArrayList<>();
                    arr = finalRegExp.get(i);
                    arr.add(j+1, " ");
                    finalRegExp.set(i, arr);
                    j++;
                    //System.out.println(a + " " + b);
                }else if(a.equals("E")){
                    ArrayList<String> arr = new ArrayList<>();
                    arr = finalRegExp.get(i);
                    arr.add(j+1, " ");
                    finalRegExp.set(i, arr);
                    j++;
                    //System.out.println(a + " " + b);
                }else if(a.equals(".") || b.equals(".")){
                    ArrayList<String> arr = new ArrayList<>();
                    arr = finalRegExp.get(i);
                    arr.add(j+1, " ");
                    finalRegExp.set(i, arr);
                    j++;
                    //System.out.println(a + " xxx " + b);
                }else{

                }
                
                
            }
        }
    }
}
