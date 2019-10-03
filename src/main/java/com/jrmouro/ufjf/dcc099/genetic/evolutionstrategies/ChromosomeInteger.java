/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jrmouro.ufjf.dcc099.genetic.evolutionstrategies;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.apache.commons.math3.genetics.AbstractListChromosome;
import org.apache.commons.math3.genetics.InvalidRepresentationException;

/**
 *
 * @author ronaldo
 */
public class ChromosomeInteger extends ChromosomeAbstract<Integer>{
    
    final private FitnessFunction<Integer> fitnessFunction;

    public ChromosomeInteger(FitnessFunction fitnessFunction, List<Integer> representation) throws InvalidRepresentationException {
        super(representation);
        this.fitnessFunction = fitnessFunction;
    }

    public ChromosomeInteger(FitnessFunction fitnessFunction, Integer[] representation) throws InvalidRepresentationException {
        super(representation);
        this.fitnessFunction = fitnessFunction;
    }

    public ChromosomeInteger(FitnessFunction fitnessFunction, List<Integer> representation, boolean copyList) {
        super(representation, copyList);
        this.fitnessFunction = fitnessFunction;
    }
     
    
    public List<Integer> getRepresentation(){
        return super.getRepresentation();
    }
    
        
        
    public FitnessFunction getFitnessFunction() {
        return fitnessFunction;
    }
    
    
    @Override
    public double fitness() {
        return this.fitnessFunction.fitness(this);
    }

    @Override
    protected void checkValidity(List<Integer> chromosomeRepresentation) throws InvalidRepresentationException {
        
    }

    @Override
    public AbstractListChromosome<Integer> newFixedLengthChromosome(List<Integer> chromosomeRepresentation) {
        return new ChromosomeInteger(this.fitnessFunction, chromosomeRepresentation);
    }


    public static ChromosomeInteger getRandom(FitnessFunction fitnessFunction, int size, int leftBound, int rightBound){
        List<Integer> ret = new ArrayList();
        Random r = new Random();
        for (int i = 0; i < size; i++) {
            ret.add(r.nextInt((rightBound - leftBound) + 1) + leftBound);
        }
        return new ChromosomeInteger(fitnessFunction, ret);
    }

    @Override
    public ChromosomeAbstract evolve(boolean max) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ChromosomeAbstract<Integer> normalize() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
