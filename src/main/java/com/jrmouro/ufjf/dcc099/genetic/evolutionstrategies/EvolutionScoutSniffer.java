/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jrmouro.ufjf.dcc099.genetic.evolutionstrategies;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ronaldo
 */
public class EvolutionScoutSniffer implements IevolutionFunction<Double> {

    final private int sniff;
    final double limit;

    public EvolutionScoutSniffer(int sniff, double limit) {
        this.sniff = sniff;
        this.limit = limit;
    }

    @Override
    public ChromosomeAbstract<Double> evolve(ChromosomeAbstract<Double> original, int n, boolean max) {

        if (!(original instanceof ChromosomeSD)) {
            try {
                throw new Exception("EvolutionScoutSniffer works only with ChromosomeSD");
            } catch (Exception ex) {
                Logger.getLogger(EvolutionScoutSniffer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        ChromosomeSD c = (ChromosomeSD) original;
        ChromosomeSD sc = new ChromosomeScout(c.getCopyListUtil(), c.getFitnessFunction(), c.getSd());

        for (int i = 0; i < n; i++) {
            
            c = (ChromosomeSD) c.evolve(max);

            sc = (ChromosomeSD) sc.evolve(max);

            ChromosomeSD sn = new ChromosomeSniffer(sc.getCopyListUtil(), sc.getFitnessFunction(), sc.getSd(), this.sniff);;

            for (int j = 0; j < sniff; j++) {
                sn = (ChromosomeSD) sn.evolve(max);
            }

            int r = c.compareTo(sn);

            if (max) {
                if (r < 0) {
                    c = (ChromosomeSD) c.newFixedLengthChromosome(sn.getCopyListUtil());
                }
            } else {
                if (r > 0) {
                    c = (ChromosomeSD) c.newFixedLengthChromosome(sn.getCopyListUtil());
                }
            }

        }

        return c;

    }


}
