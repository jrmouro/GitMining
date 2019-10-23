/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ufjf.dcc099.gitmining;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author ronaldo
 */
public class LevenshteinJUnitTest {
    
    public LevenshteinJUnitTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    
    public int levenshtein(String s1, String s2){
        
        int l = s1.length() + 1;
        int c = s2.length() + 1;
        
        int[][] m = new int[l][c];
        
        for(int i = 0; i < l; i++)
            m[i][0] = i;
        
        for(int i = 0; i < c; i++)
            m[0][i] = i;
        
        for(int i = 1; i < l; i++)
            for(int j = 1; j < c; j++){
                int cost;
                if(s1.charAt(i-1) == s2.charAt(j-1))
                    cost = 0;
                else
                    cost = 2;
                
                m[i][j] = Math.min(Math.min(m[i-1][j] + 1, m[i][j-1] + 1), m[i-1][j-1]+ cost);
                
                
            }
            
        return m[l - 1][c - 1];
        
    }

    @Test
    public void test() {
    
        System.out.println(this.levenshtein("José Ronaldo Mouro", "José Mouro"));
    
    }
}
