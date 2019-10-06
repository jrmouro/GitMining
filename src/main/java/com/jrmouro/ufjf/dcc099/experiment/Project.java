/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jrmouro.ufjf.dcc099.experiment;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author ronaldo
 */
public class Project {
    
    public final URL url;
    public final double result; //b
    public Path clonePath = null;
    protected JSONObject jsonObject = null;
    
    
    public void initialize(boolean clone){
        
        try {  
            
            if(url != null)
                this.jsonObject = githubRepositoryJSONObject(url);
            else
                this.jsonObject = null;
            
        } catch (IOException ex) {
            Logger.getLogger(Project.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Project.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(Project.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(this.jsonObject != null && clone){
            
            try {
                
                Piloto.deleteDir(clonePath);
                
                String htmlUrl = (String) this.jsonObject.get("html_url");
                
                try {
                    
                    if(htmlUrl != null && clonePath != null)
                        gitCloneRepository(new URL(htmlUrl), clonePath);
                    
                } catch (IOException ex) {
                    Logger.getLogger(Project.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Project.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            } catch (IOException ex) {
                Logger.getLogger(Project.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }       
        
    }

    public Project(URL url, double result, Path clonePath) {
        
        this.url = url;
        this.result = result;
        this.clonePath = clonePath;   
        
    }
    
    public Project(URL url, double result) {
        
        this.url = url;
        this.result = result;        
        
    }
    
    public Project(URL url) {
        
        this.url = url;
        this.result = 0;
               
    }
    
    static public void gitCloneRepository(URL url, Path pathDir) throws IOException, InterruptedException {

        Process process = Runtime.getRuntime().exec("git clone " + url.toString() + " " + pathDir.toString());

        StringBuilder output = new StringBuilder();
        StringBuilder error = new StringBuilder();

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()));

        BufferedReader stdError = new BufferedReader(
                new InputStreamReader(process.getErrorStream()));

        String line;
        while ((line = reader.readLine()) != null) {
            output.append(line + "\n");
        }

        while ((line = stdError.readLine()) != null) {
            error.append(line + "\n");
        }

        int exitVal = process.waitFor();

        if (exitVal == 0 && error.toString().length() > 0) {

            System.out.println("Info: " + error.toString());
            System.out.println("Output: " + output.toString());

        }

    }
    
    static public JSONObject githubRepositoryJSONObject(URL url) throws IOException, InterruptedException, ParseException {
        

        Process process = Runtime.getRuntime().exec("curl " + url.toString());

        StringBuilder output = new StringBuilder();
        StringBuilder error = new StringBuilder();

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()));

        BufferedReader stdError = new BufferedReader(
                new InputStreamReader(process.getErrorStream()));

        String line;
        while ((line = reader.readLine()) != null) {
            output.append(line + "\n");
        }

        while ((line = stdError.readLine()) != null) {
            error.append(line + "\n");
        }

        int exitVal = process.waitFor();

        if (exitVal == 0) {

            if (error.toString().length() > 0) {

                System.out.println("Info:\n" + error.toString());

            }
            
            System.out.println("Repository JSONObject:\n" + output.toString());
            
            return (JSONObject) new JSONParser().parse(output.toString());

        }

        return null;

    }
    
    static public boolean deleteDir(String name) throws IOException {

        String aux = new File(".").getCanonicalPath();
        Path baseTempPath = Paths.get(aux + "/" + name);

        return deleteDir(baseTempPath);
    }

    static public boolean deleteDir(Path path) throws IOException {

        if (Files.exists(path)) {
            FileUtils.deleteDirectory(new File(path.toString()));
            return true;
        }

        return false;
    }
    
    
}
