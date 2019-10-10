/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jrmouro.ufjf.dcc099.gitmining.mining;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.json.simple.parser.ParseException;

/**
 *
 * @author ronaldo
 */
public class Diffs implements Iterable<Diff>{
    
    final public Path dirRep;
    final private List<Diff> diffs;

    public Diffs(Path dirRep) {
        this.dirRep = dirRep;
        this.diffs = new ArrayList();
    }
    
    public Diffs(Path dirRep, List<Diff> diffs) {
        this.dirRep = dirRep;
        this.diffs = diffs;
    }

    public Diffs(Path dirRep, boolean onlyMergesCommits) throws IOException, InterruptedException, ParseException {
        this.dirRep = dirRep;
        this.diffs = Diffs.gitDiffsList(dirRep, onlyMergesCommits);
    }
    
    public Diff max() throws IOException, InterruptedException{
        if(this.diffs.isEmpty())
            return null;
        else if(this.diffs.size() == 1)
            return this.diffs.get(0);
        return Diff.gitDiff(this.diffs.get(0).commit1, this.diffs.get(this.diffs.size() - 1).commit2, dirRep);
    }
    
    

    @Override
    public Iterator<Diff> iterator() {
        return this.diffs.iterator();
    }
    
    private void add(Diff diff){
        this.diffs.add(diff);
    }
        
    static public Diffs gitDiffs(Commits commits, Integer indexFirst, Integer indexLast, Path pathRep) throws IOException, InterruptedException {

        Diffs ret = new Diffs(pathRep);

        for (int i = indexFirst; i < commits.size() - 1 && i < indexLast; i++) {
            ret.add(Diff.gitDiff(commits.get(i), commits.get(i + 1), pathRep));
        }

        return ret;
    }
    
    static public Diffs gitDiffs(Commits commits, Integer indexFirst, Integer indexLast, double pass, Path pathRep) throws IOException, InterruptedException {

        int p = (int)(commits.size() * pass);       
            
        return Diffs.gitDiffs(commits, indexFirst, indexLast, p, pathRep);
    }
    
    static public Diffs gitDiffs(Commits commits, Integer indexFirst, Integer indexLast, Integer pass, Path pathRep) throws IOException, InterruptedException {

        
        Diffs ret = new Diffs(pathRep);
        
        int f = indexFirst;
        int i = f + pass;
        
        for (; i < commits.size() - 1 && i <= indexLast; i = i + pass) {
            ret.add(Diff.gitDiff(commits.get(f), commits.get(i), pathRep));
            f = i;
        }
        
        if(indexLast < commits.size() && f < indexLast)
           ret.add(Diff.gitDiff(commits.get(f), commits.get(indexLast), pathRep));
            
        return ret;
    }
    
    static public Diffs gitDiffs(Commits commits, Path pathRep) throws IOException, InterruptedException {

        Diffs ret = new Diffs(pathRep);

        for (int i = 0; i < commits.size() - 1; i++) {
            ret.add(Diff.gitDiff(commits.get(i), commits.get(i + 1), pathRep));
        }

        return ret;
    }
    
    static public List<Diff> gitDiffsList(Commits commits, Path pathRep) throws IOException, InterruptedException {

        List<Diff> ret = new ArrayList();

        for (int i = 0; i < commits.size() - 1; i++) {
            ret.add(Diff.gitDiff(commits.get(i), commits.get(i + 1), pathRep));
        }

        return ret;
    }
    
    static public List<Diff> gitDiffsList(Path pathRep, boolean onlyMergesCommits) throws IOException, InterruptedException, ParseException {

        List<Diff> ret = new ArrayList();
        
        Commits commits = Commits.gitCommits(pathRep, onlyMergesCommits);

        for (int i = 0; i < commits.size() - 1; i++) {
            ret.add(Diff.gitDiff(commits.get(i), commits.get(i + 1), pathRep));
        }

        return ret;
    }
}
