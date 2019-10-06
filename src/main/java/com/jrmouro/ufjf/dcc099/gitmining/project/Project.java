/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jrmouro.ufjf.dcc099.gitmining.project;

import com.jrmouro.ufjf.dcc099.experiment.Piloto;
import com.jrmouro.ufjf.dcc099.gitmining.canonicalPath.CanonicalPath;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author ronaldo
 */
public class Project {

    public final double result; //b
    private URL url = null;
    private Path clonePath = null;
    private JSONObject jsonObject = null;
    private boolean inicialized = false;

    public boolean isInicialized() {
        return inicialized;
    }

    public double getResult() {
        return result;
    }

    public URL getUrl() {
        return url;
    }

    public Path getClonePath() {
        return clonePath;
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }
    

    public final void clone(Path clonePath) {
        
        if (this.url != null) {

            if (this.jsonObject != null) {
                String htmlUrl = (String) this.jsonObject.get("html_url");
                try {
                    
                    if (htmlUrl != null && clonePath != null) {
                        CanonicalPath.deleteDir(clonePath);
                        gitCloneRepository(new URL(htmlUrl), clonePath);
                        this.clonePath = clonePath;
                    }

                } catch (IOException ex) {
                    Logger.getLogger(Project.class.getName()).log(Level.SEVERE, null, ex);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Project.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                this.initialize(true);
            }

        }
    }

    public final void initialize(boolean clone) {

        try {

            if (url != null) {
                this.jsonObject = githubRepositoryJSONObject(url);
            } else {
                this.jsonObject = null;
            }

        } catch (IOException ex) {
            Logger.getLogger(Project.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Project.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(Project.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (this.jsonObject != null && clone) {

            String htmlUrl = (String) this.jsonObject.get("html_url");
            try {
                
                if (htmlUrl != null && clonePath != null) {
                    CanonicalPath.deleteDir(clonePath);
                    gitCloneRepository(new URL(htmlUrl), clonePath);
                    
                }

            } catch (IOException ex) {
                Logger.getLogger(Project.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InterruptedException ex) {
                Logger.getLogger(Project.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        this.inicialized = true;

    }

    public Project(URL url, double result, Path clonePath, boolean init, boolean clone) {

        this.url = url;
        this.result = result;
        this.clonePath = clonePath;
        if (init) {
            this.initialize(clone);
        }

    }

    public Project(URL url, double result, boolean init) {

        this.url = url;
        this.result = result;
        if (init) {
            this.initialize(false);
        }

    }

    public Project(URL url, boolean init) {

        this.url = url;
        this.result = 0;
        if (init) {
            this.initialize(false);
        }

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

    

}
