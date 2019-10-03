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
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.InvalidRepresentationException;

/**
 *
 * @author ronaldo
 */
public class ChromosomeSniffer extends ChromosomeOne{
    
    
    final private double limit;

    public ChromosomeSniffer(List<Double> representation, FitnessFunction fitnessFunction, double sd, double limit) throws InvalidRepresentationException {
        super(representation, fitnessFunction, sd);
        this.limit = limit;
    }
    
    protected ChromosomeSniffer(double sd, double limit, List<Double> representation, FitnessFunction fitnessFunction) throws InvalidRepresentationException {
        super(sd, representation, fitnessFunction);
        this.limit = limit;
    }

    public ChromosomeSniffer(Double[] representation, FitnessFunction fitnessFunction, double sd, double limit) throws InvalidRepresentationException {
        super(representation, fitnessFunction, sd);
        this.limit = limit;
    }
    
    @Override
    public AbstractListChromosome<Double> newFixedLengthChromosome(List<Double> chromosomeRepresentation) {
        return new ChromosomeSniffer(chromosomeRepresentation, this.getFitnessFunction(), this.getSd(), this.limit);
    }
       

    @Override
    public ChromosomeDouble evolve(boolean max) {
        
        List<Double> representation = new ArrayList();        
        
        NormalDistribution n0 = new  NormalDistribution(0, sd);    
        
        double z0 = Math.abs(n0.inverseCumulativeProbability(new Random().nextDouble()));
        //if( z0 + this.getRepresentation().get(this.getRepresentation().size() - 1) < 0)
            //z0 *= -1;
        z0 -= this.getRepresentation().get(this.getRepresentation().size() - 1);
        if(z0 < limit)
            z0 = limit;
        NormalDistribution n1 = new  NormalDistribution(0, z0);
        
        for (int i = 0; i < this.getRepresentation().size() - 1; i++) {
            double zi = n1.inverseCumulativeProbability(new Random().nextDouble());
            representation.add(zi + this.getRepresentation().get(i));
        }
        
        representation.add(z0);
        
        ChromosomeDouble child = new ChromosomeSniffer(sd, limit, representation, this.getFitnessFunction());
        
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
