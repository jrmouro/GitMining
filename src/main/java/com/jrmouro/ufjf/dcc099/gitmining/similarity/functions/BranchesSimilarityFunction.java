/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jrmouro.ufjf.dcc099.gitmining.similarity.functions;

import com.jrmouro.ufjf.dcc099.gitmining.project.Project;
import com.jrmouro.ufjf.dcc099.gitmining.similarity.ProjectSimilarityFunction;

/**
 *
 * @author ronaldo
 */
public class BranchesSimilarityFunction extends ProjectSimilarityFunction{
    
    private double value = 0;

    public BranchesSimilarityFunction(Project project, Object param) {
        super(project, param);
        
        this.value = (double)project.mining.getBranches().size() / (double)project.mining.getCommits().size();
    }

    @Override
    public Double getValue() {
        return this.value;
    }
    
    
    
}
