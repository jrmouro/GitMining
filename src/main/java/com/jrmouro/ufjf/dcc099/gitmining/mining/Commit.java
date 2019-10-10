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
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author ronaldo
 */
public class Commit {
    final public Path pathDir;
    final public String hash, author, email;
    final public Integer date;
    final public List<String> parents;
    private boolean conflict = false;

    public Commit(Path pathDir, String hash, String author, String email, Integer date, List<String> parents) {
        this.hash = hash;
        this.author = author;
        this.email = email;
        this.date = date;
        this.parents = parents;
        this.pathDir = pathDir;
    }
    
    public Double getDiffCommitDeletionsRate(Commit commit) throws IOException, InterruptedException{
        Diff diff = NormalizedDiff.gitDiff(this, commit, pathDir, null);
        return diff.getNrChangedFilesNrDeletionsRate();
    }
    
    public Double getDiffCommitInsertionsRate(Commit commit) throws IOException, InterruptedException{
        Diff diff = NormalizedDiff.gitDiff(this, commit, pathDir, null);
        return diff.getNrChangedFilesNrInsertionsRate();
    }
    
    public void setConflict(MergeConflicts conflicts){
        for (String conflict : conflicts) {
            if(conflicts.equals(this.hash)){
                this.conflict = true;
                return;
            }
        }
        this.conflict = false;
    }
    
    public void setConflict(boolean flag){
        this.conflict = flag;
    }

    public boolean isConflict() {
        return conflict;
    }
    
    
    @Override
    public Object clone() throws CloneNotSupportedException {
        List<String> parents = new ArrayList();
        for (String parent : this.parents) {
            parents.add(parent);
        }        
        return new Commit(this.pathDir, this.hash, this.author, this.email, this.date, parents);
    }
    
    
    static public Commit parse(Path pathDir, String data) throws ParseException{
        List<String> parents = new ArrayList();
        JSONObject jo = (JSONObject) new JSONParser().parse(data); 
        String[] p = ((String)jo.get("parents")).split(" ");
        for (String s : p)
            parents.add(s);
        return new Commit(  pathDir,
                            (String)jo.get("hash"), 
                            (String)jo.get("author"), 
                            (String)jo.get("email"), 
                            Integer.parseInt((String)jo.get("date")), parents);
    }
    
    static public Commits gitCommitList(Path pathDir, boolean onlyMergesCommits) throws IOException, InterruptedException, ParseException {
        
        Commits ret = new Commits(pathDir);
        
        String command;
        if (onlyMergesCommits) {
            command = "git log --merges --pretty=format:{\"hash\":\"%H\",\"date\":\"%ct\",\"author\":\"%aN\",\"email\":\"%ae\",\"parents\":\"%P\"}";
        } else {
            command = "git log  --pretty=format:{\"hash\":\"%H\",\"date\":%ct,\"author\":\"%aN\",\"email\":\"%ae\",\"parents\":\"%P\"}";
        }

        Process process = Runtime.getRuntime().exec(command, null, new File(pathDir.toString()));;
                
        StringBuilder error = new StringBuilder();

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()));

        BufferedReader stdError = new BufferedReader(
                new InputStreamReader(process.getErrorStream()));

        String line;
        
        while ((line = reader.readLine()) != null)            
            ret.add(parse(pathDir, line));
        

        while ((line = stdError.readLine()) != null)
            error.append(line + "\n");
        

        int exitVal = process.waitFor();

        if (exitVal == 0 && error.toString().length() > 0)
            System.out.println("Error: " + error.toString());

        return ret;
    }
    
    static public void setConflictCommitList(List<Commit> commitList, MergeConflicts conflicts){
                
        for (Commit commit : commitList) {
            commit.setConflict(conflicts);
        }
        
    }
            
    static public List<Commit> getConflictCommitList(List<Commit> commitList, MergeConflicts conflicts) throws CloneNotSupportedException{
        
        List<Commit> ret = new ArrayList();
        
        for (Commit commit : commitList) {
            for (String conflict : conflicts) {
                if(commit.hash.equals(conflict)){
                    Commit c = (Commit) commit.clone();
                    c.setConflict(true);
                    ret.add(c);
                }
            }

        }        
        
        return ret;
    }
    
}
