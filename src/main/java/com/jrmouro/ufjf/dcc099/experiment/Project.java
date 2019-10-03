/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jrmouro.ufjf.dcc099.experiment;

import java.net.URL;
import java.nio.file.Path;

/**
 *
 * @author ronaldo
 */
public class Project {
    
    public final URL url;
    public final double result; //b
    public Path path = null;

    public Project(URL url, double result, Path path) {
        this.url = url;
        this.result = result;
        this.path = path;
    }
    
    public Project(URL url, double result) {
        this.url = url;
        this.result = result;
    }
    
    public Project(URL url) {
        this.url = url;
        this.result = 0;
    }
    
    
}
