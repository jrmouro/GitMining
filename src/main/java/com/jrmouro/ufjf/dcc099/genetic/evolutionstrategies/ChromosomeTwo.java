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
public class ChromosomeTwo extends ChromosomeSD{    
    
    public ChromosomeTwo(List<Double> representation, FitnessFunction fitnessFunction, double sd) throws InvalidRepresentationException {
        super(prepare(representation,sd), fitnessFunction, sd);
    }
    
    private ChromosomeTwo(double sd, List<Double> representation, FitnessFunction fitnessFunction) throws InvalidRepresentationException {
        super(representation, fitnessFunction, sd);
    }

    public ChromosomeTwo(Double[] representation, FitnessFunction fitnessFunction, double sd) throws InvalidRepresentationException {
        super(prepare(representation, sd), fitnessFunction, sd);
    }
    
    @Override
    public AbstractListChromosome<Double> newFixedLengthChromosome(List<Double> chromosomeRepresentation) {
        return new ChromosomeTwo(chromosomeRepresentation, this.getFitnessFunction(), this.getSd());
    }
    
    
    @Override
    public List<Double> getCopyListUtil() {
        List<Double> ret = new ArrayList();
        int x = this.getRepresentation().size()/2;
        for (int i = 0; i < x; i++) {
            ret.add(this.getRepresentation().get(i));
        }               
        return ret;
    }
 

    static private List<Double> prepare(List<Double> representation, double sd){
        int t = representation.size();
        NormalDistribution n0 = new  NormalDistribution(0, sd);         
        for (int i = 0; i < t; i++)
            representation.add(Math.abs(n0.inverseCumulativeProbability(new Random().nextDouble())));
                
        return representation;
    }
    
    static private Double[] prepare(Double[] representation, double sd){
        int t = representation.length;
        Double[] ret = new Double[2*t];
        for (int i = 0; i < t; i++)
            ret[i] = representation[i];
        
        NormalDistribution n0 = new  NormalDistribution(0, sd);         
        for (int i = t; i < 2*t; i++)
            ret[i] = Math.abs(n0.inverseCumulativeProbability(new Random().nextDouble()));
        
        return ret;
    }
        

    @Override
    public ChromosomeAbstract<Double> evolve(boolean max) {
        
        int t = this.getRepresentation().size();
        
        List<Double> representation = new ArrayList(); 
        NormalDistribution n0 = new  NormalDistribution(0, sd); 
        double [] z = new double[t/2];
        
        
        for (int u = t/2, i = 0; u < t; u++, i++) {
            double a = n0.inverseCumulativeProbability(new Random().nextDouble());
            if( a + this.getRepresentation().get(u) < 0)
                a *= -1;
            z[i] = a + this.getRepresentation().get(u);
        }
           
        
        for (int i = 0; i < t/2; i++) {            
            NormalDistribution n1 = new  NormalDistribution(0, z[i]);        
            double zi = n1.inverseCumulativeProbability(new Random().nextDouble());
            representation.add(zi + this.getRepresentation().get(i));
        }
        
        
        for (int i = 0; i < t/2; i++) {
            representation.add(z[i]);        
        }
        
        ChromosomeDouble child = new ChromosomeTwo(sd, representation, this.getFitnessFunction());
        
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
        int t = this.getRepresentation().size()/2;
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
        
        for (int i = 0; i < t; i++) {
            list.add(this.getRepresentation().get(t + i));
        }
                
        return new ChromosomeTwo(list, this.getFitnessFunction(), this.sd);
    }
    
    
    
    
    
        
    
    
        
    
}
