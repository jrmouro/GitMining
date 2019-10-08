/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jrmouro.ufjf.dcc099.gitmining.similarity.functions;

import com.jrmouro.ufjf.dcc099.gitmining.project.Project;
import com.jrmouro.ufjf.dcc099.gitmining.similarity.ProjectSimilarityFunction;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.parser.ParseException;

/**
 *
 * @author ronaldo
 */
public class LanguagesSimilarityFunction extends ProjectSimilarityFunction{
    
    private double value = 0;

    public LanguagesSimilarityFunction(Project project, Object listConcern) throws IOException, InterruptedException, ParseException {
        super(project, listConcern);   
        
        try {
            if( listConcern instanceof List<?>)
                if(((List<?>)listConcern).size() > 0)
                    if(((List<?>)listConcern).get(0) instanceof String)  
                        this.value = this.calcValue((List<String>)listConcern, project.mining.getGithubLanguages());
                    else
                        throw new Exception("Empty Object");
                else
                    throw new Exception("Invalid List Item");
            else
                throw new Exception("Invalid Object");
        } catch (Exception ex) {
            Logger.getLogger(LanguagesSimilarityFunction.class.getName()).log(Level.SEVERE, null, ex);
        }
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
    
}
