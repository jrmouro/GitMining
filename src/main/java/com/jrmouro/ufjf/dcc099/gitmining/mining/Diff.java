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
public class Diff {
    final Path pathRep;
    final public Commit commit1, commit2;
    public int changedfiles = 0, insertions = 0, deletions = 0;

    public Diff(Commit c1, Commit c2, Path pathRep) {
        this.commit1 = c1;
        this.commit2 = c2;
        this.pathRep = pathRep;
    }
    
    public Double getNormalizedChangedFiles(Diff other){
        if(other.changedfiles > 0)
            return Double.valueOf(this.changedfiles)/Double.valueOf(other.changedfiles);
        return 1.0;
    }
    
    public Double getNormalizedDeletions(Diff other){
        if(other.deletions > 0)
            return Double.valueOf(this.deletions)/Double.valueOf(other.deletions);
        return 1.0;
    }
    
    public Double getNormalizedInsertions(Diff other){
        if(other.insertions > 0)
            return Double.valueOf(this.insertions)/Double.valueOf(other.insertions);
        return 1.0;
    }
    
    public Double getNormalizedTime(Diff other){
        Integer i = other.commit1.date, f = this.commit2.date;
        Integer ff = other.commit2.date;        
        return Double.valueOf(f - i)/Double.valueOf(ff - i);
    }
      
    
    public Double getDiffMaxDeletionsRate(Diff max){
        if(max.deletions == 0 || max == null)
            return 1.0;
        return Double.valueOf(this.deletions)/Double.valueOf(max.deletions);
    }
    
    public Double getDiffMaxInsertionsRate(Diff max){
        if(max.insertions == 0 || max == null)
            return 1.0;
        return Double.valueOf(this.insertions)/Double.valueOf(max.insertions);
    }
    
    public Double getDiffMaxChangedFilesRate(Diff max){
        if(max.changedfiles == 0 || max == null)
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

    public static Diff parse(Commit c1, Commit c2, String diff, Path pathRep) {

        Diff ret = new Diff(c1, c2, pathRep);

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

        return ret;
    }

    static public Diff gitDiff(Commit c1, Commit c2, Path pathRep) throws IOException, InterruptedException {
        
        Diff ret = new Diff(c1, c2, pathRep);
        
        Process process = Runtime.getRuntime().exec("git diff " + c1.hash + " " + c2.hash + " --shortstat", null, new File(pathRep.toString()));
        //StringBuilder output = new StringBuilder();
        StringBuilder error = new StringBuilder();

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()));

        BufferedReader stdError = new BufferedReader(
                new InputStreamReader(process.getErrorStream()));

        String line = reader.readLine();
        if (line != null) {
            ret = Diff.parse(c1, c2, line, pathRep);
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

    static public List<Diff> gitDiff(List<Commit> commits, Path pathDir) throws IOException, InterruptedException {

        List<Diff> ret = new ArrayList();

        for (int i = 0; i < commits.size() - 1; i++) {
            ret.add(Diff.gitDiff(commits.get(i), commits.get(i + 1), pathDir));
        }

        return ret;
    }

}
