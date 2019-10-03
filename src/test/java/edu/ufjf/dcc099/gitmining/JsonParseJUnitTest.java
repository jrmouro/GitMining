package edu.ufjf.dcc099.gitmining;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.FileNotFoundException;
import java.io.IOException;
import org.json.simple.JSONObject;
import org.junit.Test;
import org.json.simple.parser.*; 

/**
 *
 * @author ronaldo
 */
public class JsonParseJUnitTest {
    
    public JsonParseJUnitTest() {
    }
    
    

    @Test
    public void hello() throws FileNotFoundException, IOException, ParseException {
        JSONObject jo = (JSONObject) new JSONParser().parse("{\"teste\":\"teste\"}"); 
        
        System.out.println(jo.get("teste"));
        
    
    }
}
