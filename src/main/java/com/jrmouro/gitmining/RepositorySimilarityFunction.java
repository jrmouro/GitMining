/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jrmouro.gitmining;

import java.nio.file.Path;

/**
 *
 * @author ronaldo
 */
public abstract class RepositorySimilarityFunction implements SimilarityFunction{
    
    final protected Path path;

    public RepositorySimilarityFunction(Path path) {
        this.path = path;
    }
    
}
