/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jrmouro.ufjf.dcc099.gitmining.similarity;

import java.net.URL;
import java.nio.file.Path;

/**
 *
 * @author ronaldo
 */
public abstract class RemoteRepositorySimilarityFunction extends RepositorySimilarityFunction{
    
    protected URL url;

    public RemoteRepositorySimilarityFunction(URL url, Path path, Object param) {
        super(path, param);
        this.url = url;
    }

    
    
}