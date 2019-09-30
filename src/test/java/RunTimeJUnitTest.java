/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Test;

/**
 *
 * @author ronaldo
 */
public class RunTimeJUnitTest {

    public RunTimeJUnitTest() {
    }

    @Test
    public void hello() throws IOException, InterruptedException, ParseException {
                
        
        Process process = Runtime.getRuntime().exec("curl https://api.github.com/repositories");

        StringBuilder output = new StringBuilder();

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()));

        String line;
        while ((line = reader.readLine()) != null) {
            output.append(line + "\n");
        }

        int exitVal = process.waitFor();
        
        if (exitVal == 0) {
            
            JSONArray jo =  (JSONArray) new JSONParser().parse(output.toString()); 
            
            jo.forEach((t) -> {
                JSONObject obj = (JSONObject) t;
                System.out.println(obj.get("id"));
            });
            
            
            
            System.out.println("Success!");
            System.out.println(output);
            System.exit(0);
        } 

    }

}
