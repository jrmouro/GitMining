/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jrmouro.ufjf.dcc099.genetic.evolutionstrategies;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.genetics.AbstractListChromosome;
import org.apache.commons.math3.genetics.InvalidRepresentationException;

/**
 *
 * @author ronaldo
 */
public class ChromosomeDouble extends ChromosomeAbstract<Double>{
    
    final private FitnessFunction fitnessFunction;

    public ChromosomeDouble(List<Double> representation, FitnessFunction fitnessFunction) throws InvalidRepresentationException {
        super(representation);
        this.fitnessFunction = fitnessFunction;
    }

    public ChromosomeDouble(Double[] representation, FitnessFunction fitnessFunction) throws InvalidRepresentationException {
        super(representation);
        this.fitnessFunction = fitnessFunction;
    }

    public ChromosomeDouble(List<Double> representation, boolean copyList, FitnessFunction fitnessFunction) {
        super(representation, copyList);
        this.fitnessFunction = fitnessFunction;
    }

    public FitnessFunction getFitnessFunction() {
        return fitnessFunction;
    }  
       

    @Override
    public List<Double> getRepresentation() {
        return super.getRepresentation();
    }
    
       
    @Override
    protected void checkValidity(List<Double> chromosomeRepresentation) throws InvalidRepresentationException {}

    @Override
    public AbstractListChromosome<Double> newFixedLengthChromosome(List<Double> chromosomeRepresentation) {
        return new ChromosomeDouble(chromosomeRepresentation, true, this.fitnessFunction);
    }

    @Override
    public double fitness() {
        return this.fitnessFunction.fitness(this);
    }

    @Override
    public ChromosomeAbstract<Double> evolve(boolean max) {
        
        List<Double> representation = new ArrayList();        
        
        NormalDistribution n0 = new  NormalDistribution(0, 1.0);    
             
        for (int i = 0; i < this.getRepresentation().size(); i++) {
            double zi = n0.inverseCumulativeProbability(new Random().nextDouble());
            representation.add(zi + this.getRepresentation().get(i));
        }
                      
        return new ChromosomeDouble(representation, this.getFitnessFunction());
    }

    /*@Override
    public List<Double> getCopyListUtil() {
        List<Double> ret = new ArrayList();   
        for(double d: this.getRepresentation())
            ret.add(d);
        return ret;
    }*/

    @Override
    public ChromosomeAbstract<Double> normalize() {
        List<Double> list = this.getCopyListUtil();
        Double min = list.get(0);
        Double max = list.get(0);
        for (Double d : list) {
            if(d < min)
                min = d;
            if(d > max)
                max = d;
        }
        Double range = Math.abs(max - min);
        for (int i = 0; i < list.size(); i++) {
            list.set(i, Math.abs((min - list.get(i)) / range));
        }
        return new ChromosomeDouble(list, this.getFitnessFunction());
    }

       
    
}
