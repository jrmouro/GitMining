/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jrmouro.ufjf.dcc099.genetic.evolutionstrategies;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ronaldo
 */
public class RepresentationTransformIntegerDouble implements RepresentationTransform<Integer,Double>{

    @Override
    public List<Integer> get(ChromosomeAbstract<Double> chromosome, int ini, int fim) {
        List<Integer> ret = new ArrayList();
        int t = chromosome.getRepresentation().size();
        int s = fim - ini;
        for (int i = ini; i < fim && i < t; i++) {
            ret.add((int)(s*chromosome.getRepresentation().get(i)));
        }
        return ret;
    }

       
    
}
