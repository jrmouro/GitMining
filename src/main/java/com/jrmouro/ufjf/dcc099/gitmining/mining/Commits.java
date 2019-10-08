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
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author ronaldo
 */
public class Commits{
    
    public final Path pathDir;
    private final List<Commit> commits = new ArrayList();
    private final Set<String> authors = new HashSet();
    private Integer firstDate = Integer.MAX_VALUE, lastDate = 0;

    public Commits(Path pathDir) {
        this.pathDir = pathDir;
    }    
            
    public void add(Commit commit){
        
        this.commits.add(commit);
        
        this.authors.add(commit.author);
        
        if(this.firstDate > commit.date)
            this.firstDate = commit.date;
        
        if(this.lastDate < commit.date)
            this.lastDate = commit.date;
    }
    
    public Commit get(Integer index){
        return this.commits.get(index);
    }
    
    public Integer size(){
        return this.commits.size();
    }
    
    public Integer elapseDate(){
        if(this.commits.isEmpty())
            return 0;
        return Math.abs(this.lastDate - this.firstDate);
    }
    
    public Double getNrAuthorsCommitsRate(){
        if(this.commits.isEmpty() )
            return 1.0;
        return Double.valueOf(this.authors.size())/Double.valueOf(this.commits.size());
    } 
    
    public Double getNrAuthorsElapseDateRate(){
        if((lastDate - firstDate) == 0)
            return 1.0;
        return Double.valueOf(this.authors.size())/Math.abs(Double.valueOf(lastDate - firstDate));
    } 
    
    public Double getNrCommitsElapseDateRate(){
        if((lastDate - firstDate) == 0)
            return 1.0;
        return Double.valueOf(this.commits.size())/Math.abs(Double.valueOf(lastDate - firstDate));
    } 
    
    static public Commit parse(Path pathDir, String data) throws ParseException{
        List<String> parents = new ArrayList();
        JSONObject jo = (JSONObject) new JSONParser().parse(data); 
        String[] p = ((String)jo.get("parents")).split(" ");
        parents.addAll(Arrays.asList(p));
        return new Commit(  pathDir,
                            (String)jo.get("hash"), 
                            (String)jo.get("author"), 
                            (String)jo.get("email"), 
                            Integer.parseInt((String)jo.get("date")), parents);
    }
    
    static public Commits gitCommits(Path pathDir, boolean onlyMergesCommits) throws IOException, InterruptedException, ParseException {
        
        Commits ret = new Commits(pathDir);
        
        String command;
        if (onlyMergesCommits) {
            command = "git log --merges --pretty=format:{\"hash\":\"%H\",\"date\":\"%ct\",\"author\":\"%aN\",\"email\":\"%ae\",\"parents\":\"%P\"}";
        } else {
            command = "git log  --pretty=format:{\"hash\":\"%H\",\"date\":\"%ct\",\"author\":\"%aN\",\"email\":\"%ae\",\"parents\":\"%P\"}";
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
            error.append(line).append("\n");
        

        int exitVal = process.waitFor();

        if (exitVal == 0 && error.toString().length() > 0)
            System.out.println("Error: " + error.toString());

        return ret;
        
    }
    
    
    
}
