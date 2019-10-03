/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jrmouro.ufjf.dcc099.genetic.evolutionstrategies;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.genetics.AbstractListChromosome;
import org.apache.commons.math3.genetics.InvalidRepresentationException;

/**
 *
 * @author ronaldo
 */
public abstract class ChromosomeAbstract<T> extends AbstractListChromosome<T> implements IevolutionStrategy, ChromosomeUtil<T>, ChromosomeNormalize<T>{

    public ChromosomeAbstract(List<T> representation) throws InvalidRepresentationException {
        super(representation);
    }

    public ChromosomeAbstract(T[] representation) throws InvalidRepresentationException {
        super(representation);
    }

    public ChromosomeAbstract(List<T> representation, boolean copyList) {
        super(representation, copyList);
    }

    @Override
    public List<T> getRepresentation() {
        return super.getRepresentation(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<T> getCopyListUtil() {
        List<T> ret = new ArrayList();   
        for(T d: this.getRepresentation())
            ret.add(d);
        return ret;
    }
    
}
