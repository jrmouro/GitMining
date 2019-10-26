/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jrmouro.ufjf.dcc099.gitmining.similarity.functions;

import com.jrmouro.ufjf.dcc099.gitmining.mining.levenshtein.LevenshteinDistance;
import com.jrmouro.ufjf.dcc099.gitmining.project.Project;
import com.jrmouro.ufjf.dcc099.gitmining.similarity.ProjectSimilarityFunction;

/**
 *
 * @author ronaldo
 */
public class LevenshteinNameSimilarityFunction extends ProjectSimilarityFunction{
    
    private final double value;

    public LevenshteinNameSimilarityFunction(Project project, Object param) {
        super(project, param);
        
        if(param instanceof String)
            this.value = LevenshteinDistance.get((String)project.mining.getGithubRepositoryJSONObject().get("name"), param.toString());
        else
            this.value = 0.0;
    }

    @Override
    public Double getValue() {
        return this.value;
    }
    
    
    
}
