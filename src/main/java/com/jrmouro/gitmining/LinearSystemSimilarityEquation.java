/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jrmouro.gitmining;

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
    
    public double getValue(){
        if(valued)
            return value;
        value = 0;
        for (SimilarityEquation thi : this.list) {
            value += thi.getValue();
        }
        return value;
    }
    
    public void setWeight(double[] weights){
        this.valued = false;
        int i = 0;
        for (double weight : weights) {
            if(i < this.list.size())
                this.list.get(i++).setWeight(weights);
        }
    }

    public boolean add(SimilarityEquation e) {
        this.valued = false;
        return this.list.add(e);
    }
    
    
}
