/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jrmouro.gitmining;

import java.io.IOException;
import java.nio.file.Path;
import org.json.simple.parser.ParseException;

/**
 *
 * @author ronaldo
 */
public class MergeConflictsSimilarityFunction extends RepositorySimilarityFunction{
    
    private MergeConflicts mergeConflicts = null;
    private double value = 0;

    public MergeConflictsSimilarityFunction(Path path) throws IOException, InterruptedException, ParseException {
        super(path);
        this.mergeConflicts = MergeConflicts.gitCommitList(path);
        this.value = this.mergeConflicts.getConflictsRate();
    }

    @Override
    public double getValue() {
        return this.value;
    }
    
}
