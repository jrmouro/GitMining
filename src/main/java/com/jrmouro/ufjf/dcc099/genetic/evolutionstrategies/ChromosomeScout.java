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
public class ChromosomeScout extends ChromosomeOne{
        

    public ChromosomeScout(List<Double> representation, FitnessFunction fitnessFunction, double sd) throws InvalidRepresentationException {
        super(representation, fitnessFunction,sd);
        
    }
    
    protected ChromosomeScout(double sd, List<Double> representation, FitnessFunction fitnessFunction) throws InvalidRepresentationException {
        super(sd, representation, fitnessFunction);
        
    }

    public ChromosomeScout(Double[] representation, FitnessFunction fitnessFunction, double sd) throws InvalidRepresentationException {
        super(representation, fitnessFunction, sd);
        
    }
    
    @Override
    public AbstractListChromosome<Double> newFixedLengthChromosome(List<Double> chromosomeRepresentation) {
        return new ChromosomeScout(chromosomeRepresentation, this.getFitnessFunction(), this.getSd());
    }
           

    @Override
    public ChromosomeDouble evolve(boolean max) {
        
        List<Double> representation = new ArrayList();        
        
        NormalDistribution n0 = new  NormalDistribution(0, sd);    
        
        double z0 = Math.abs(n0.inverseCumulativeProbability(new Random().nextDouble()));
        //if( z0 + this.getRepresentation().get(this.getRepresentation().size() - 1) < 0)
            //z0 *= -1;
        z0 += this.getRepresentation().get(this.getRepresentation().size() - 1);
        NormalDistribution n1 = new  NormalDistribution(0, z0);
        
        for (int i = 0; i < this.getRepresentation().size() - 1; i++) {
            double zi = n1.inverseCumulativeProbability(new Random().nextDouble());
            representation.add(zi + this.getRepresentation().get(i));
        }
        
        representation.add(z0);
        
        ChromosomeDouble child = new ChromosomeScout(sd, representation, this.getFitnessFunction());
        
        int r = this.compareTo(child);
        
        if(max){
            if(r < 0)
                return child;
        }else{
            if(r > 0)
                return child;
        }   
        
        return this;
        
    }

    
}
