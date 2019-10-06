/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jrmouro.ufjf.dcc099.gitmining.similarity.functions;

import com.jrmouro.ufjf.dcc099.gitmining.MergeConflicts;
import com.jrmouro.ufjf.dcc099.gitmining.similarity.RepositorySimilarityFunction;
import java.io.IOException;
import java.nio.file.Path;
import org.json.simple.parser.ParseException;

/**
 *
 * @author ronaldo
 */
public class MergeConflictsSimilarityFunction extends RepositorySimilarityFunction{
    
   
    private double value = 0;

    public MergeConflictsSimilarityFunction(Path path) throws IOException, InterruptedException, ParseException {
        super(path, null);
        MergeConflicts mergeConflicts = MergeConflicts.gitCommitList(path);
        this.value = mergeConflicts.getConflictsRate();
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
