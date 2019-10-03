/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jrmouro.ufjf.dcc099.genetic.evolutionstrategies;

import org.apache.commons.math3.genetics.AbstractListChromosome;

/**
 *
 * @author ronaldo
 */
public interface FitnessFunction<T> {
    public double fitness(ChromosomeAbstract<T> chromosome);
}
