/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jrmouro.ufjf.dcc099.gitmining.similarity.functions;

import com.jrmouro.ufjf.dcc099.gitmining.similarity.RemoteSimilarityFunction;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author ronaldo
 */
public class LanguagesSimilarityFunction extends RemoteSimilarityFunction{
    
    private double value = 0;

    public LanguagesSimilarityFunction(List<String> concern, URL url) throws IOException, InterruptedException, ParseException {
        super(url, concern);        
        this.value = this.calcValue(concern, githubLanguages(url));        
    }
    
    private double calcValue(List<String> concern, Map<String,Long> languages){
        
        long divider = 0;
        long dividend = 0;
        
        Collection<Long> coll = languages.values();
                
        for (Long v : coll) {
            divider += v;
        }
        
        for (String l : concern) {
            Long n = languages.get(l);
            if(n != null)
                dividend += n;
        }
        
        if(divider == 0)
            return 0.0;
        
        return Double.valueOf(dividend)/Double.valueOf(divider);
    }

    @Override
    public Double getValue() {
        return this.value;
    }
    
    
    static public Map<String, Long> githubLanguages(URL url) throws IOException, InterruptedException, ParseException {

        Map<String, Long> ret = new HashMap();

        Process process = Runtime.getRuntime().exec("curl " + url.toString());

        StringBuilder output = new StringBuilder();
        StringBuilder error = new StringBuilder();

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()));

        BufferedReader stdError = new BufferedReader(
                new InputStreamReader(process.getErrorStream()));

        String line;
        while ((line = reader.readLine()) != null) {
            output.append(line + "\n");
        }

        while ((line = stdError.readLine()) != null) {
            error.append(line + "\n");
        }

        int exitVal = process.waitFor();

        if (exitVal == 0) {

            if (error.toString().length() > 0) {

                System.out.println("Info:\n" + error.toString());

            }
            
            JSONObject obj = (JSONObject) new JSONParser().parse(output.toString());

            obj.forEach((t, u) -> {
                ret.put((String)t, (Long)u);
            });

            

        }

        return ret;

    }
    
}
