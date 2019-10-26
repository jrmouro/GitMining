/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jrmouro.ufjf.dcc099.gitmining.project;

import com.jrmouro.ufjf.dcc099.gitmining.mining.Mining;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import org.json.simple.parser.ParseException;

/**
 *
 * @author ronaldo
 */
public class Project {
    
    public Mining mining = null;
    public final double result;
    public URL url = null;
    public Path clonePath = null;
    private boolean mined = false;

    public boolean isMining() {
        return mined;
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


    public final void mine(boolean clone, double fatorNormalizedDiffs) throws IOException, InterruptedException, ParseException {
        
        if(!this.mined && this.url != null && this.clonePath != null){
        
            this.mining = new Mining(this.clonePath, this.url, clone, fatorNormalizedDiffs);

            this.mined = true;
            
        }

    }

    public Project(URL url, double result, Path clonePath, boolean mine, boolean clone, double fatorNormalizedDiffs) throws IOException, InterruptedException, ParseException {

        this.url = url;
        this.result = result;
        this.clonePath = clonePath;
        if (mine) {
            this.mine(clone, fatorNormalizedDiffs);
        }

    }

    public Project(URL url, double result, boolean init, double fatorNormalizedDiffs) throws IOException, InterruptedException, ParseException {

        this.url = url;
        this.result = result;
        if (init) {
            this.mine(false, fatorNormalizedDiffs);
        }

    }

    public Project(URL url, boolean init, double fatorNormalizedDiffs) throws IOException, InterruptedException, ParseException {

        this.url = url;
        this.result = 0;
        if (init) {
            this.mine(false, fatorNormalizedDiffs);
        }

    }

}
