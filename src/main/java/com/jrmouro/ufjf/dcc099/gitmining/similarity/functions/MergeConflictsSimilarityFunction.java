/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jrmouro.ufjf.dcc099.gitmining.similarity.functions;

import com.jrmouro.ufjf.dcc099.gitmining.mining.MergeConflicts;
import com.jrmouro.ufjf.dcc099.gitmining.project.Project;
import com.jrmouro.ufjf.dcc099.gitmining.similarity.ProjectSimilarityFunction;
import java.io.IOException;
import org.json.simple.parser.ParseException;

/**
 *
 * @author ronaldo
 */
public class MergeConflictsSimilarityFunction extends ProjectSimilarityFunction{
    
   
    private double value = 0;

    public MergeConflictsSimilarityFunction(Project project, Object obj) throws IOException, InterruptedException, ParseException {
        super(project, obj);
        MergeConflicts mergeConflicts = MergeConflicts.gitMergeConflicts(project.getClonePath());
        this.value = mergeConflicts.getNrMergeConflictsNrMergeCommitsRate();
    }

    @Override
    public Double getValue() {
        return this.value;
    }

    @Override
    public Object getParam() {
        return null;
    }
    
    
    
}
