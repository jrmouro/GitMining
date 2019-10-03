/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jrmouro.ufjf.dcc099.genetic.evolutionstrategies;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.genetics.Chromosome;
import org.apache.commons.math3.genetics.MutationPolicy;

/**
 *
 * @author ronaldo
 */
public class DoubleMutation  implements MutationPolicy{
    
        
    @Override
    public Chromosome mutate(Chromosome original) throws MathIllegalArgumentException {
        if (!(original instanceof ChromosomeDouble)) {
            try {
                throw new Exception("DoubleMutation works only with DoubleChromosome");
            } catch (Exception ex) {
                Logger.getLogger(DoubleMutation.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return ((ChromosomeDouble)original).evolve(false);
    }
    
}
