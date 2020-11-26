/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package graph;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author deniz.kavzak
 */
public class Test {

    public static void main(String[] args) throws FileNotFoundException, IOException {
        
        BufferedReader bf = new BufferedReader(new FileReader("C:\\Users\\deniz.kavzak\\Desktop\\MVV\\myers.xml"));
        //Set<TestInput> h = new HashSet<>();
        
        Set<String> l = new HashSet<>();
        String line = bf.readLine();
        
        while(line!=null){
            
            //String[] s = line.split(line);
            
            l.add(line);
            
            line = bf.readLine();
        }
        
        bf.close();
        System.out.println("size: " + l.size());
        
        BufferedWriter bw = new BufferedWriter(new FileWriter("C:\\Users\\deniz.kavzak\\Desktop\\MVV\\myersFilter.xml"));
        
        for(String s : l){
            bw.write(s);
            bw.newLine();
        }
        
        bw.close();
       

    }

}
