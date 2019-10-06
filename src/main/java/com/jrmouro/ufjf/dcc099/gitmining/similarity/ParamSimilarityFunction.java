/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jrmouro.ufjf.dcc099.gitmining.similarity;

/**
 *
 * @author ronaldo
 */
public abstract class ParamSimilarityFunction implements SimilarityFunction{
    
    final private Object param;

    public ParamSimilarityFunction(Object param) {
        this.param = param;
    }

    @Override
    public Object getParam() {
        return this.param;
    }
}
