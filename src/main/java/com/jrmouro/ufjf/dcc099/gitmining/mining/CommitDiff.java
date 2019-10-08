/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jrmouro.ufjf.dcc099.gitmining.mining;

import com.jrmouro.ufjf.dcc099.gitmining.mining.CommitDiffs;
import com.jrmouro.ufjf.dcc099.gitmining.mining.Diff;
import com.jrmouro.ufjf.dcc099.gitmining.mining.Commits;
import java.io.IOException;
import java.nio.file.Path;
import org.json.simple.parser.ParseException;

/**
 *
 * @author ronaldo
 */
public class CommitDiff {

    final Path pathDir;
    final protected Commit c1, c2;
    final public Diff diff;
    
    public CommitDiff(Path pathDir, Commit c1, Commit c2) throws IOException, InterruptedException {
        this.c1 = c1;
        this.c2 = c2;
        this.diff = Diff.gitDiff(c1, c2, pathDir);
        this.pathDir = pathDir;
    }

    public CommitDiff(Path pathDir, Commit c1, Commit c2, Diff diff) {
        this.c1 = c1;
        this.c2 = c2;
        this.diff = diff;
        this.pathDir = pathDir;
    }
    
    public Double getMaxDeletionsRate(CommitDiff max){
        return this.diff.getDiffMaxDeletionsRate(max.diff);
    }
    
    public Double getMaxInsertionsRate(CommitDiff max){
        return this.diff.getDiffMaxInsertionsRate(max.diff);
    }
    
    public Double getMaxChangedFilesRate(CommitDiff max){
        return this.diff.getDiffMaxChangedFilesRate(max.diff);
    }
    
    public Double getDiffChangedFilesElapseDateRate(){
        if(Double.valueOf(c1.date - c2.date) == 0)
            return 1.0;
        return Double.valueOf(this.diff.changedfiles)/Math.abs(Double.valueOf(c1.date - c2.date));
    }
    
    public Double getDiffInsertionsElapseDateRate(){
        if(Double.valueOf(c1.date - c2.date) == 0)
            return 1.0;
        return Double.valueOf(this.diff.insertions)/Math.abs(Double.valueOf(c1.date - c2.date));
    }
    
    public Double getDiffDeletionsElapseDateRate(){
        if(Double.valueOf(c1.date - c2.date) == 0)
            return 1.0;
        return Double.valueOf(this.diff.deletions)/Math.abs(Double.valueOf(c1.date - c2.date));
    }

    
    public static CommitDiffs gitCommitDiffs(Path pathDir, Commits commits, CommitDiff total) throws IOException, InterruptedException, ParseException {
        
        CommitDiffs ret = new CommitDiffs(pathDir, total);
        

        for (int i = 0; i < commits.size() - 1; i++) {
            Diff diff = Diff.gitDiff(commits.get(i), commits.get(i + 1), pathDir);
            ret.add(new CommitDiff(pathDir, commits.get(i), commits.get(i + 1), diff));

        }
        return ret;
    }
    
    public static CommitDiffs gitCommitDiffs(Path pathDir, CommitDiff total, boolean onlyMergesCommits) throws IOException, InterruptedException, ParseException {
        
        CommitDiffs ret = new CommitDiffs(pathDir, total);
        
        Commits commits = Commits.gitCommits(pathDir, onlyMergesCommits);

        for (int i = 0; i < commits.size() - 1; i++) {
            Diff diff = Diff.gitDiff(commits.get(i), commits.get(i + 1), pathDir);
            ret.add(new CommitDiff(pathDir, commits.get(i), commits.get(i + 1), diff));

        }
        return ret;
    }

    
}
