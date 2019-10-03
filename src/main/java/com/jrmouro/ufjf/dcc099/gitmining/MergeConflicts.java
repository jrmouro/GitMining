/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jrmouro.ufjf.dcc099.gitmining;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.parser.ParseException;



/**
 *
 * @author ronaldo
 */
public class MergeConflicts extends ArrayList<String>{
    
    private int nrMergeCommits = 0, nrMergeConflicts = 0;

    public MergeConflicts() {}
    
    public void incMergeCommits(){
        nrMergeCommits++;
    }
    
    public void incMergeConflicts(){
        nrMergeConflicts++;
    }

    public Integer getNrMergeCommits() {
        return nrMergeCommits;
    }

    public Integer getNrMergeConflicts() {
        return nrMergeConflicts;
    }
    
    public Double getConflictsRate(){
        if(this.nrMergeCommits == 0)
            return 0.0;
        return Double.valueOf(this.nrMergeConflicts)/Double.valueOf(this.nrMergeCommits);
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
    
    static public MergeConflicts gitCommitList(Path pathDir) throws IOException, InterruptedException, ParseException {
        
        MergeConflicts ret = new MergeConflicts();
        
        String command = "git log --merges";        

        Process process = Runtime.getRuntime().exec(command, null, new File(pathDir.toString()));;
                
        StringBuilder error = new StringBuilder();

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()));

        BufferedReader stdError = new BufferedReader(
                new InputStreamReader(process.getErrorStream()));

        String line;
        String c = null;
        while ((line = reader.readLine()) != null){    
            
            String[] ca = line.split(" ");
            
            if(ca.length > 1 && ca[0].equals("commit")){
                c = ca[1];
                ret.incMergeCommits();
            }else if(c != null && line.contains("Conflicts:")){
                ret.add(c);
                c = null;
                ret.incMergeConflicts();
            }
            
        }
        

        while ((line = stdError.readLine()) != null)
            error.append(line + "\n");
        

        int exitVal = process.waitFor();

        if (exitVal == 0 && error.toString().length() > 0)
            System.out.println("Error: " + error.toString());

        return ret;
        
    }
    
}
