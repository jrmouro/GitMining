/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jrmouro.ufjf.dcc099.gitmining.mining;

import com.jrmouro.ufjf.dcc099.gitmining.canonicalPath.CanonicalPath;
import com.jrmouro.ufjf.dcc099.gitmining.mining.plot.Plottable;
import com.jrmouro.ufjf.dcc099.gitmining.mining.plot.Plottables;
import com.jrmouro.ufjf.dcc099.gitmining.mining.plot.PolynomPlottable;
import com.jrmouro.ufjf.dcc099.gitmining.polynom.Polynom;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.analysis.interpolation.UnivariateInterpolator;
import org.json.simple.parser.ParseException;

/**
 *
 * @author ronaldo
 */
public class NormalizedDiffs implements Iterable<NormalizedDiff>, Plottable{
    
    final private List<NormalizedDiff> nomalizedDiffs = new ArrayList();
    final private Plottables plottables; 

    public NormalizedDiffs(String title) {
        this.plottables = new Plottables(title); 
    }
    
    public void add(NormalizedDiff diff){
        this.nomalizedDiffs.add(diff);
    }
    
    public Integer size(){
        return this.nomalizedDiffs.size();
    }

    @Override
    public String title() {
        return this.plottables.title();
    }
    
    

    @Override
    public Iterator<NormalizedDiff> iterator() {
        return this.nomalizedDiffs.iterator();
    }
    
    public double[] polynomChangedFiles() throws IOException, InterruptedException {

       double[] x = new double[this.nomalizedDiffs.size()];
        double[] y = new double[this.nomalizedDiffs.size()];
        int i = 0;
        for (NormalizedDiff d : this.nomalizedDiffs) {
            x[i] = d.getNormalizedTime();
            y[i++] = d.getNormalizedChangedFiles();
        }
        return Polynom.polynom(y, x);
    }

    public double[] polynomDeletions() throws IOException, InterruptedException {
        double[] x = new double[this.nomalizedDiffs.size()];
        double[] y = new double[this.nomalizedDiffs.size()];
        int i = 0;
        for (NormalizedDiff d : this.nomalizedDiffs) {
            x[i] = d.getNormalizedTime();
            y[i++] = d.getNormalizedDeletions();
        }
        return Polynom.polynom(y, x);
    }

    public double[] polynomInsertions() throws IOException, InterruptedException {
        
        double[] x = new double[this.nomalizedDiffs.size()];
        double[] y = new double[this.nomalizedDiffs.size()];
        int i = 0;
        for (NormalizedDiff d : this.nomalizedDiffs) {
            x[i] = d.getNormalizedTime();
            y[i++] = d.getNormalizedInsertions();
        }
        return Polynom.polynom(y, x);
    }
    
    public UnivariateFunction polynomChangedFilesFunction() {
        UnivariateInterpolator interpolator = new SplineInterpolator();
        double[] x = new double[this.nomalizedDiffs.size()];
        double[] y = new double[this.nomalizedDiffs.size()];
        int i = 0;
        for (NormalizedDiff d : this.nomalizedDiffs) {
            x[i] = d.getNormalizedTime();
            y[i++] = d.getNormalizedChangedFiles();
        }
        return interpolator.interpolate(x, y);
    }
    
    public UnivariateFunction polynomInsertionsFunction() {
        UnivariateInterpolator interpolator = new SplineInterpolator();
        double[] x = new double[this.nomalizedDiffs.size()];
        double[] y = new double[this.nomalizedDiffs.size()];
        int i = 0;
        for (NormalizedDiff d : this.nomalizedDiffs) {
            x[i] = d.getNormalizedTime();
            y[i++] = d.getNormalizedInsertions();
        }
        return interpolator.interpolate(x, y);
    }
    
    public UnivariateFunction polynomDeletionsFunction() {
        UnivariateInterpolator interpolator = new SplineInterpolator();
        double[] x = new double[this.nomalizedDiffs.size()];
        double[] y = new double[this.nomalizedDiffs.size()];
        int i = 0;
        for (NormalizedDiff d : this.nomalizedDiffs) {
            x[i] = d.getNormalizedTime();
            y[i++] = d.getNormalizedDeletions();
        }
        return interpolator.interpolate(x, y);
    }

    

    @Override
    public void plot() {
        
        try {
            Plottable p1 = new PolynomPlottable(this.polynomChangedFiles(),CanonicalPath.getPath("changedFiles.plot"), this.title() + " - ChangedFiles");
            Plottable p2 = new PolynomPlottable(this.polynomInsertions(),CanonicalPath.getPath("insertions.plot"), this.title() + " - Insertions");
            Plottable p3 = new PolynomPlottable(this.polynomDeletions(),CanonicalPath.getPath("deletions.plot"), this.title() + " - Deletions");
            this.plottables.add(p1);
            this.plottables.add(p2);
            this.plottables.add(p3);
        } catch (IOException ex) {
            Logger.getLogger(NormalizedDiffs.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(NormalizedDiffs.class.getName()).log(Level.SEVERE, null, ex);
        }
        
                
        this.plottables.plot();
    }

    @Override
    public Path script(Path data) {
        return null;       
        
    }
    
    static public NormalizedDiffs getNormalizedDiffs(Path pathRep, boolean onlyMergesCommits, String title) throws IOException, InterruptedException, ParseException {
                
        Diff diffRef = null;
        
        Commits commits = Commits.gitCommits(pathRep, onlyMergesCommits);        
        
        if(commits.size() > 1)
            diffRef = NormalizedDiff.gitDiff(commits.get(0), commits.get(commits.size() - 1), pathRep, null);
        
        return getNormalizedDiffs(commits, pathRep, diffRef, title);
    }
    
    static public NormalizedDiffs getNormalizedDiffs(Commits commits, Path pathRep, String title) throws IOException, InterruptedException {
                
        Diff diffRef = null;
        
        if(commits.size() > 1)
            diffRef = NormalizedDiff.gitDiff(commits.get(0), commits.get(commits.size() - 1), pathRep, null);
        
        return getNormalizedDiffs(commits, pathRep, diffRef, title);
    }
    
    static public NormalizedDiffs getNormalizedDiffs(Commits commits, Path pathRep, Diff diffRef, String title) throws IOException, InterruptedException {

        NormalizedDiffs ret = new NormalizedDiffs(title);

        for (int i = 0; i < commits.size() - 1; i++) {
            ret.add(NormalizedDiff.gitDiff(commits.get(i), commits.get(i + 1), pathRep, diffRef));
        }

        return ret;
    }
    
    static public NormalizedDiffs getNormalizedDiffs(Commits commits, Integer indexFirst, Integer indexLast, double pass, Path pathRep, String title) throws IOException, InterruptedException {

        
        Diff diffRef = null;
        
        if(commits.size() > 1)
            diffRef = NormalizedDiff.gitDiff(commits.get(0), commits.get(commits.size() - 1), pathRep, null);
        
        
        NormalizedDiffs ret = new NormalizedDiffs(title);
        int a = 1;
        int f = indexFirst;
        int i = f + (int)(a*pass);
        
        for (; i < commits.size() - 1 && i <= indexLast; i = (int)(a*pass)) {
            ret.add(NormalizedDiff.gitDiff(commits.get(f), commits.get(i), pathRep, diffRef));
            f = i;
            a++;
        }
        
        if(indexLast < commits.size() && f < indexLast)
           ret.add(NormalizedDiff.gitDiff(commits.get(f), commits.get(indexLast), pathRep, diffRef));
            
        return ret;
    }
    
    
  
    static public NormalizedDiffs getNormalizedDiffs(Commits commits, Integer indexFirst, Integer indexLast, Integer pass, Path pathRep, Diff diffRef, String title) throws IOException, InterruptedException {

        
        NormalizedDiffs ret = new NormalizedDiffs(title);
        
        int f = indexFirst;
        int i = f + pass;
        
        for (; i < commits.size() - 1 && i <= indexLast; i = i + pass) {
            ret.add(NormalizedDiff.gitDiff(commits.get(f), commits.get(i), pathRep, diffRef));
            f = i;
        }
        
        if(indexLast < commits.size() && f < indexLast)
           ret.add(NormalizedDiff.gitDiff(commits.get(f), commits.get(indexLast), pathRep, diffRef));
            
        return ret;
    }
    
}
