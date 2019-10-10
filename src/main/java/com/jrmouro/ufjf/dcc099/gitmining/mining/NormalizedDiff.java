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

/**
 *
 * @author ronaldo
 */
public class NormalizedDiff extends Diff{
    
    private Diff difRef;    
    private double normalizedTime = 1.0;
    private double normalizedChangedFiles = 1.0;
    private double normalizedDeletions = 1.0;
    private double normalizedInsertions = 1.0;
    
    public NormalizedDiff(Commit c1, Commit c2, Path pathRep) {
        super(c1, c2, pathRep);
        this.difRef = null;                
    }
    
    public NormalizedDiff(Commit c1, Commit c2, Path pathRep, Diff difRef) {
        super(c1, c2, pathRep);
        if(difRef != null)
            this.normalize(difRef);        
    }
    
    @Override
    final public NormalizedDiff normalize(Diff difRef){
        this.difRef = difRef;        
        //double t = this.getNormalizedTime(difRef);
        //int aux = (int) (t * 10.0);
        //this.normalizedTime = (aux / 10.0);
        this.normalizedTime = this.getNormalizedTime(difRef);
        this.normalizedChangedFiles = this.getNormalizedChangedFiles(difRef);
        this.normalizedDeletions = this.getNormalizedDeletions(difRef);
        this.normalizedInsertions = this.getNormalizedInsertions(difRef);
        return this;
    }
    
    @Override
    final public Double getNormalizedChangedFiles(Diff other){
        if(other.changedfiles > 0)
            return Double.valueOf(this.changedfiles)/Double.valueOf(other.changedfiles);
        return 1.0;
    }
    
    @Override
    final public Double getNormalizedDeletions(Diff other){
        if(other.deletions > 0)
            return Double.valueOf(this.deletions)/Double.valueOf(other.deletions);
        return 1.0;
    }
    
    @Override
    final public Double getNormalizedInsertions(Diff other){
        if(other.insertions > 0)
            return Double.valueOf(this.insertions)/Double.valueOf(other.insertions);
        return 1.0;
    }
    
    @Override
    final public Double getNormalizedTime(Diff other){
        Integer i = other.commit1.date, f = this.commit2.date;
        Integer ff = other.commit2.date;        
        return Double.valueOf(f - i)/Double.valueOf(ff - i);
    }

    public Diff getDifRef() {
        return difRef;
    }

    public double getNormalizedTime() {
        return normalizedTime;
    }

    public double getNormalizedChangedFiles() {
        return normalizedChangedFiles;
    }

    public double getNormalizedDeletions() {
        return normalizedDeletions;
    }

    public double getNormalizedInsertions() {
        return normalizedInsertions;
    }
    
    
    
    @Override
    public String toString() {
        return String.valueOf(this.normalizedTime) + "\t"
                + String.valueOf(this.normalizedChangedFiles) + "\t"
                + String.valueOf(this.normalizedInsertions) + "\t"
                + String.valueOf(this.normalizedDeletions);
    }
    
    public static NormalizedDiff parse(Commit c1, Commit c2, String diff, Path pathRep, Diff diffRef) {

        NormalizedDiff ret = new NormalizedDiff(c1, c2, pathRep);

        if (diff != null) {

            String[] aux = diff.split(",");

            for (String string : aux) {

                String a = string.trim();
                String[] b = a.split(" ");
                if (b[1].charAt(0) == 'f') {
                    ret.changedfiles = Integer.parseInt(b[0]);
                }
                if (b[1].charAt(0) == 'i') {
                    ret.insertions = Integer.parseInt(b[0]);
                }
                if (b[1].charAt(0) == 'd') {
                    ret.deletions = Integer.parseInt(b[0]);
                }

            }
        }
        
        if(diffRef != null)
            return ret.normalize(diffRef);

        return ret;
    }

    static public NormalizedDiff gitDiff(Commit c1, Commit c2, Path pathRep, Diff diffRef) throws IOException, InterruptedException {
        
        NormalizedDiff ret = new NormalizedDiff(c1, c2, pathRep);
        
        Process process = Runtime.getRuntime().exec("git diff " + c1.hash + " " + c2.hash + " --shortstat", null, new File(pathRep.toString()));
        //StringBuilder output = new StringBuilder();
        StringBuilder error = new StringBuilder();

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()));

        BufferedReader stdError = new BufferedReader(
                new InputStreamReader(process.getErrorStream()));

        String line = reader.readLine();
        if (line != null) {
            ret = NormalizedDiff.parse(c1, c2, line, pathRep, diffRef);
        }

        while ((line = stdError.readLine()) != null) {
            error.append(line).append("\n");
        }

        int exitVal = process.waitFor();

        if (exitVal == 0 && error.toString().length() > 0) {

            System.out.println("Error: " + error.toString());

        }

        return ret;
    }
    
    
}
