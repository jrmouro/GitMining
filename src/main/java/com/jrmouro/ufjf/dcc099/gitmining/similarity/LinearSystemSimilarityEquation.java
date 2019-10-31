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
public class LinearSystemSimilarityEquation{
    
    private boolean valued = false;
    private double value = 0;
    final private List<SimilarityEquation> list = new ArrayList<>();
    
    public Double getValue(){
        if(valued)
            return value;
        value = 0;
        for (SimilarityEquation thi : this.list) {
            value += thi.getValue();
        }
        return value;
    }
    
    public void setWeights(Double[] weights){
        this.valued = false;
        int i = 0;
        for (Double weight : weights) {
            if(i < this.list.size())
                this.list.get(i++).setWeights(weights);
        }
    }
    
    public void setWeights(List<Double> weights){
        this.valued = false;
        int i = 0;
        for (Double weight : weights) {
            if(i < this.list.size())
                this.list.get(i++).setWeights(weights);
        }
    }

    public boolean add(SimilarityEquation e) {
        this.valued = false;
        return this.list.add(e);
    }
    
    public Double[] getWeights(){
        if(this.list.size() > 0)
            return this.list.get(0).getWeights();
        return null;
    }

    @Override
    public String toString() {
        
        String ret = "SimilarityMatrix\n";
        
        for (SimilarityEquation similarityEquation : list) {
            ret += similarityEquation + "\n";
        }
        
        return ret;
    }
    
    
    
}
