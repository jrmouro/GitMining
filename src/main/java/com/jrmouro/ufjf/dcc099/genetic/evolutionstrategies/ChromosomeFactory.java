/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jrmouro.ufjf.dcc099.genetic.evolutionstrategies;

import java.util.List;
import org.apache.commons.math3.genetics.Chromosome;

/**
 *
 * @author ronaldo
 */
public interface ChromosomeFactory {
    
    public Chromosome get(List<Double> representation, FitnessFunction fitnessFunction);

    public Chromosome get(Double[] representation, FitnessFunction fitnessFunction);

    public Chromosome get(List<Double> representation, boolean copyList, FitnessFunction fitnessFunction);
    
}
