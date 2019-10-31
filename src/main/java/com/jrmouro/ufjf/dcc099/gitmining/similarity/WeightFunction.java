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
public class WeightFunction{

    private Double weight = 1.0;
    final private SimilarityFunction similarityFunction;

    public WeightFunction(SimilarityFunction similarityFunction) {
        this.similarityFunction = similarityFunction;
    }
    
    public WeightFunction(SimilarityFunction similarityFunction, Double weight) {
        this.similarityFunction = similarityFunction;
        this.weight = weight;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }        
    
    public Double getValue() {
        return this.weight * this.similarityFunction.getValue();
    }

    @Override
    public String toString() {
        return this.getValue().toString();
    }
    
    
    
}
