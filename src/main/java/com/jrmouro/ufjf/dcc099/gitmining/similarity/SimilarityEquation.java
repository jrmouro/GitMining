/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jrmouro.ufjf.dcc099.gitmining.similarity;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ronaldo
 */
public class SimilarityEquation{
        
    private Double b = 0.0;
    private boolean valued = false;
    private double value = 0;
    private final List<WeightFunction> list = new ArrayList<>();

    public SimilarityEquation() {}
    
    public SimilarityEquation(Double b) {
        this.b = b;
    }
    
    public Double getValue(){
        if(valued)
            return value;
        value = 0;
        for (WeightFunction thi : this.list) {
            value += thi.getValue();
        }
        return value - b;
    }
    
    public void setWeights(Double[] weights){
        this.valued = false;
        int i = 0;
        for (double weight : weights) {
            if(i < this.list.size())
                this.list.get(i++).setWeight(weight);
        }
    }
    
    public void setWeights(List<Double> weights){
        this.valued = false;
        int i = 0;
        for (double weight : weights) {
            if(i < this.list.size())
                this.list.get(i++).setWeight(weight);
        }
    }

    public boolean add(WeightFunction e) {
        this.valued = false;
        return this.list.add(e);
    }
    
    
    public Double[] getWeights(){
        Double[] ret = new Double[this.list.size()];
        int i = 0;
        for (WeightFunction weightFunction : this.list) {
            ret[i++] = weightFunction.getWeight();
        }
        return ret;
    }

    @Override
    public String toString() {
        return list.toString();
    }
    
    
    
}
