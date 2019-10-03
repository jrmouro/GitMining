/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jrmouro.ufjf.dcc099.gitmining.similarity;

import java.net.URL;

/**
 *
 * @author ronaldo
 */
public abstract class RemoteSimilarityFunction implements SimilarityFunction{
    
    final protected URL url;

    public RemoteSimilarityFunction(URL url) {
        this.url = url;
    }
        
}
