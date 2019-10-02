/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jrmouro.gitmining;

/**
 *
 * @author ronaldo
 */
public class WeightFunction{

    private double weight = 1;
    final private SimilarityFunction similarityFunction;

    public WeightFunction(SimilarityFunction similarityFunction) {
        this.similarityFunction = similarityFunction;
    }
    
    public WeightFunction(SimilarityFunction similarityFunction, double weight) {
        this.similarityFunction = similarityFunction;
        this.weight = weight;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }        
    
    public double getValue() {
        return this.weight * this.similarityFunction.getValue();
    }
    
}
