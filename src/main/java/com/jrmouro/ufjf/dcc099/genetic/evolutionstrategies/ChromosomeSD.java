/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jrmouro.ufjf.dcc099.genetic.evolutionstrategies;

import java.util.List;
import org.apache.commons.math3.genetics.InvalidRepresentationException;

/**
 *
 * @author ronaldo
 */
public abstract class ChromosomeSD  extends ChromosomeDouble{
    
    final protected double sd;

    public ChromosomeSD(List<Double> representation, FitnessFunction fitnessFunction, double sd) throws InvalidRepresentationException {
        super(representation, fitnessFunction);
        this.sd = sd;
    }

    public ChromosomeSD(Double[] representation, FitnessFunction fitnessFunction, double sd) throws InvalidRepresentationException {
        super(representation, fitnessFunction);
        this.sd = sd;
    }
           
    public double getSd() {
        return sd;
    }
    
}
