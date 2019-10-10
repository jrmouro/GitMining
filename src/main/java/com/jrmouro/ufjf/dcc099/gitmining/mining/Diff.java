/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jrmouro.ufjf.dcc099.gitmining.mining;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ronaldo
 */
public abstract class Diff {
    
    final Path pathRep;
    final public Commit commit1, commit2;
    public int changedfiles = 0, insertions = 0, deletions = 0;

    public Diff(Commit c1, Commit c2, Path pathRep) {
        this.commit1 = c1;
        this.commit2 = c2;
        this.pathRep = pathRep;
    }
    
    abstract public Diff normalize(Diff difRef);
    
    abstract public Double getNormalizedChangedFiles(Diff other);
    
    abstract public Double getNormalizedDeletions(Diff other);
    
    abstract public Double getNormalizedInsertions(Diff other);
    
    abstract public Double getNormalizedTime(Diff other);
      
    
    public Double getDiffMaxDeletionsRate(Diff max){
        if(max.deletions == 0)
            return 1.0;
        return Double.valueOf(this.deletions)/Double.valueOf(max.deletions);
    }
    
    public Double getDiffMaxInsertionsRate(Diff max){
        if(max.insertions == 0)
            return 1.0;
        return Double.valueOf(this.insertions)/Double.valueOf(max.insertions);
    }
    
    public Double getDiffMaxChangedFilesRate(Diff max){
        if(max.changedfiles == 0)
            return 1.0;
        return Double.valueOf(this.changedfiles)/Double.valueOf(max.changedfiles);
    }
    
    public Double getNrChangedFilesNrInsertionsRate(){
        if(insertions == 0)
            return 1.0;
        return Double.valueOf(this.changedfiles)/Double.valueOf(this.insertions);
    }
    
    public Double getNrChangedFilesNrDeletionsRate(){
        if(deletions == 0)
            return 1.0;
        return Double.valueOf(this.changedfiles)/Double.valueOf(this.deletions);
    }

    @Override
    public String toString() {
        
        return  "Commit1: " + this.commit1.hash + "\n" +
                "Commit2: " + this.commit2.hash + "\n" +
                "ChangedFiles: " + String.valueOf(this.changedfiles)
                + " - Insertions: " + String.valueOf(this.insertions)
                + " - Deletions: " + String.valueOf(this.deletions);
        
    }

    public Diff(Commit c1, Commit c2, int changedfiles, int insertions, int deletions, Path pathRep) {
        this.changedfiles = changedfiles;
        this.insertions = insertions;
        this.deletions = deletions;
        this.commit1 = c1;
        this.commit2 = c2;
        this.pathRep = pathRep;
    }
    
    public static Diff parse(Diff diff, String diffStr) {

        //Diff ret = new Diff(c1, c2);

        if (diff != null) {

            String[] aux = diffStr.split(",");

            for (String string : aux) {

                String a = string.trim();
                String[] b = a.split(" ");
                if (b[1].charAt(0) == 'f') {
                    diff.changedfiles = Integer.parseInt(b[0]);
                }
                if (b[1].charAt(0) == 'i') {
                    diff.insertions = Integer.parseInt(b[0]);
                }
                if (b[1].charAt(0) == 'd') {
                    diff.deletions = Integer.parseInt(b[0]);
                }

            }
        }

        return diff;
    }   

}
