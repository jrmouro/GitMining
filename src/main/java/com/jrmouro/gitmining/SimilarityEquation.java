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
public class SimilarityEquation{
        
    private double b = 0;
    private boolean valued = false;
    private double value = 0;
    private final List<WeightFunction> list = new ArrayList<>();

    public SimilarityEquation() {}
    
    public SimilarityEquation(double b) {
        this.b = b;
    }
    
    public double getValue(){
        if(valued)
            return value;
        value = 0;
        for (WeightFunction thi : this.list) {
            value += thi.getValue();
        }
        return value - b;
    }
    
    public void setWeight(double[] weights){
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
    
}
