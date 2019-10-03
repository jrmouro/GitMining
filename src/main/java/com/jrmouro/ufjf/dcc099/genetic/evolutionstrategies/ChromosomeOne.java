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
public class ChromosomeOne extends ChromosomeSD{
        
    public ChromosomeOne(List<Double> representation, FitnessFunction fitnessFunction, double sd) throws InvalidRepresentationException {
        super(prepare(representation,sd), fitnessFunction, sd);
        
    }
    
    protected ChromosomeOne(double sd, List<Double> representation, FitnessFunction fitnessFunction) throws InvalidRepresentationException {
        super(representation, fitnessFunction, sd);
        
    }

    public ChromosomeOne(Double[] representation, FitnessFunction fitnessFunction, double sd) throws InvalidRepresentationException {
        super(prepare(representation,sd), fitnessFunction, sd);
        
    }
    
    @Override
    public AbstractListChromosome<Double> newFixedLengthChromosome(List<Double> chromosomeRepresentation) {
        return new ChromosomeOne(chromosomeRepresentation, this.getFitnessFunction(), this.getSd());
    }

    

    @Override
    public List<Double> getCopyListUtil() {
        List<Double> ret = super.getCopyListUtil();
        ret.remove(ret.size()-1);        
        return ret;
    }
        
    static private List<Double> prepare(List<Double> representation, double sd){
        
        NormalDistribution n0 = new  NormalDistribution(0, sd);             
        representation.add(Math.abs(n0.inverseCumulativeProbability(new Random().nextDouble())));
                
        return representation;
    }
    
    static private Double[] prepare(Double[] representation, double sd){
        int t = representation.length;
        Double[] ret = new Double[t+1];
        for (int i = 0; i < t; i++)
            ret[i] = representation[i];
        
        NormalDistribution n0 = new  NormalDistribution(0, sd);   
        
        ret[t] = Math.abs(n0.inverseCumulativeProbability(new Random().nextDouble()));
        
        return ret;
    }

    @Override
    public ChromosomeAbstract<Double> evolve(boolean max) {
        
        List<Double> representation = new ArrayList();        
        
        NormalDistribution n0 = new  NormalDistribution(0, sd);    
        
        double z0 = n0.inverseCumulativeProbability(new Random().nextDouble());
        if( z0 + this.getRepresentation().get(this.getRepresentation().size() - 1) < 0)
            z0 *= -1;
        z0 += this.getRepresentation().get(this.getRepresentation().size() - 1);
        NormalDistribution n1 = new  NormalDistribution(0, z0);
        
        for (int i = 0; i < this.getRepresentation().size() - 1; i++) {
            double zi = n1.inverseCumulativeProbability(new Random().nextDouble());
            representation.add(zi + this.getRepresentation().get(i));
        }
        
        representation.add(z0);
        
        ChromosomeDouble child = new ChromosomeOne(sd, representation, this.getFitnessFunction());
        
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

    @Override
    public ChromosomeAbstract<Double> normalize() {
        
        int t = this.getRepresentation().size() - 1;
        List<Double> list = new ArrayList();
        Double min = this.getRepresentation().get(0);
        Double max = min;
        
        for (int i = 0; i < t; i++) {
            double d = this.getRepresentation().get(i);
            if(d < min)
                min = d;
            if(d > max)
                max = d;
        }
        
        Double range = Math.abs(max - min);
        for (int i = 0; i < t; i++) {
            list.add(i, Math.abs((min - this.getRepresentation().get(i)) / range));
        }
        
        //list.add(this.getRepresentation().get(t));
        
        return new ChromosomeOne(list, this.getFitnessFunction(), this.sd);
        
    }
    
    

    
}
