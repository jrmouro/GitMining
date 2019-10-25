/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jrmouro.ufjf.dcc099.gitmining.vector;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author ronaldo
 */
public class Vector implements Iterable<Double> {

    protected List<Double> vector;

    public Vector() {
        this.vector = new ArrayList();
    }

    public Vector(List<Double> vector) {
        this.vector = vector;
    }

    public void add(Double d) {
        this.vector.add(d);
    }

    public void addAll(Vector other) {
        this.vector.addAll(other.vector);
    }

    @Override
    public Iterator<Double> iterator() {
        return this.vector.iterator();
    }

    public double similarity(Vector other) {
        return cosineSimilarity(this, other);
    }

    public static double cosineSimilarity(Vector r, Vector s) {

        int t = Math.min(r.vector.size(), s.vector.size());

        if (t == 0) {
            return 0.0;
        }

        double aux1 = 0.0;
        double aux2 = 0.0;
        double aux3 = 0.0;

        for (int i = 0; i < t; i++) {
            aux1 += r.vector.get(i) * s.vector.get(i);
            aux2 += r.vector.get(i) * r.vector.get(i);
            aux3 += s.vector.get(i) * s.vector.get(i);
        }

        if (aux2 <= 0.0 || aux3 <= 0.0) {
            return 0.0;
        }

        return aux1 / (Math.sqrt(aux2) * Math.sqrt(aux3));
    }

}
