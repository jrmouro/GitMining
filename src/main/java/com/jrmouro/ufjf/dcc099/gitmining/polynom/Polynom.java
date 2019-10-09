/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jrmouro.ufjf.dcc099.gitmining.polynom;

/**
 *
 * @author ronaldo
 */
/* 27.10.2007: UVa 11319 OK */
public class Polynom {

    static final double EPS = 1e-8;
    double p[];
    
    Polynom(double a[]) {
        p = (double[]) a.clone();
        reduce();
    }
    
    final void reduce() {
        int size;
        for (size = p.length - 1; size > 0 && Math.abs(p[size]) < EPS; size--);
        if (++size < p.length) {
            double q[] = new double[size];
            for (int i = 0; i < size; i++) {
                q[i] = p[i];
            }
            p = q;
        }
    }

    Polynom add(Polynom b) {
        int n = Math.max(this.p.length, b.p.length);
        double q[] = new double[n];
        for (int i = 0; i < n; i++) {
            if (i < this.p.length) {
                q[i] += this.p[i];
            }
            if (i < b.p.length) {
                q[i] += b.p[i];
            }
        }
        return new Polynom(q);
    }

    Polynom multiply(Polynom b) {
        double q[] = new double[this.p.length + b.p.length - 1];
        for (int i = 0; i < this.p.length; i++) {
            for (int j = 0; j < b.p.length; j++) {
                q[i + j] += this.p[i] * b.p[j];
            }
        }
        return new Polynom(q);
    }

  
    
    /* newton's interpolation method: for n pairs (x,f(x)), return n-degree polynomial
           lowest degree earliest in returned array */
    public static double[] polynom(double f[], double x[]) {
        Polynom r = new Polynom(new double[]{f[0]});
        int n = f.length;
        double a[][] = new double[n][n];
        for (int i = 0; i < n; i++) {
            a[0][i] = f[i];
        }
        for (int i = 1; i < n; i++) {
            for (int j = 0; j < n - i; j++) {
                a[i][j] = (a[i - 1][j + 1] - a[i - 1][j]) / (x[j + i] - x[j]);
            }
            Polynom m = new Polynom(new double[]{a[i][0]});
            for (int j = 0; j < i; j++) {
                m = m.multiply(new Polynom(new double[]{-x[j], 1}));
            }
            r = r.add(m);
        }
        return r.p;
    }
    
        
    public static double value(double x, double polynom[]) {
        double ret = 0.0;
        int t = 0;
        for (double c : polynom) {
            ret =+ (c * Math.pow(x, t++));
        }
        return ret;
    }

    public static String toString(double p[]) {
        String ret = "f(x)=(";
        int t = 0;
        for (double c : p) {
            
            ret += (String.valueOf(c)+"*"+"x**"+String.valueOf(t)+")");
            if(t < p.length - 1)
                ret += "+(";
            t++;
        }
        
        return ret;
    }
    
    
}
