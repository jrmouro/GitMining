/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jrmouro.ufjf.dcc099.genetic.evolutionstrategies;

/**
 *
 * @author ronaldo
 */
public interface IevolutionFunction<T> {
    public ChromosomeAbstract<T> evolve(ChromosomeAbstract<T> original, int n, boolean max);
}
