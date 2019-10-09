/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jrmouro.ufjf.dcc099.gitmining.mining;

import com.jrmouro.ufjf.dcc099.gitmining.polynom.Polynom;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.interpolation.SplineInterpolator;
import org.apache.commons.math3.analysis.interpolation.UnivariateInterpolator;
import org.json.simple.parser.ParseException;

/**
 *
 * @author ronaldo
 */
public class NomalizedCommitDiffDatas {

    public final List<NomalizedCommitDiffData> nomalizedCommitDiffDatas = new ArrayList();

    public void add(NomalizedCommitDiffData ncdd) {
        this.nomalizedCommitDiffDatas.add(ncdd);
    }

    public NomalizedCommitDiffData get(Integer index) {
        return this.nomalizedCommitDiffDatas.get(index);
    }

    public Integer size() {
        return this.nomalizedCommitDiffDatas.size();
    }

    public NomalizedCommitDiffDatas reducednomalizedCommitDiffDatas() throws IOException, InterruptedException {
        return NomalizedCommitDiffDatas.reducednomalizedCommitDiffDatas(this);
    }
    
    public void dataChangedFilesPlot(Path script, Path data) throws IOException, InterruptedException {
        dataPlotFile(this, data);
        dataChangedFilesPlotScript(script, data);
        gnuplot(script);
    }
    
    public void dataDeletionsPlot(Path script, Path data) throws IOException, InterruptedException {
        dataPlotFile(this, data);
        dataDeletionsPlotScript(script, data);
        gnuplot(script);
    }
    
    public void dataInsertionsPlot(Path script, Path data) throws IOException, InterruptedException {
        dataPlotFile(this, data);
        dataInsertionsPlotScript(script, data);
        gnuplot(script);
    }

    public void polynomChangedFilesPlot(Path path) throws IOException, InterruptedException {
        polynomPlotScript(path, "ChangedFiles", polynomChangedFiles());
        gnuplot(path);
    }

    public void polynomDeletionsPlot(Path path) throws IOException, InterruptedException {
        polynomPlotScript(path, "Deletions", polynomDeletions());
        gnuplot(path);
    }

    public void polynomInsertionsPlot(Path path) throws IOException, InterruptedException {
        polynomPlotScript(path, "Insertions", polynomInsertions());
        gnuplot(path);
    }

    public UnivariateFunction polynomChangedFilesFunction() {
        UnivariateInterpolator interpolator = new SplineInterpolator();
        double[] x = new double[this.nomalizedCommitDiffDatas.size()];
        double[] y = new double[this.nomalizedCommitDiffDatas.size()];
        int i = 0;
        for (NomalizedCommitDiffData ncdd : this.nomalizedCommitDiffDatas) {
            x[i] = ncdd.time;
            y[i++] = ncdd.changedFiles;
        }
        return interpolator.interpolate(x, y);
    }

    public UnivariateFunction polynomInsertionsFunction() {
        UnivariateInterpolator interpolator = new SplineInterpolator();
        double[] x = new double[this.nomalizedCommitDiffDatas.size()];
        double[] y = new double[this.nomalizedCommitDiffDatas.size()];
        int i = 0;
        for (NomalizedCommitDiffData ncdd : this.nomalizedCommitDiffDatas) {
            x[i] = ncdd.time;
            y[i++] = ncdd.insertions;
        }
        return interpolator.interpolate(x, y);
    }

    public UnivariateFunction polynomDeletionsFunction() {
        UnivariateInterpolator interpolator = new SplineInterpolator();
        double[] x = new double[this.nomalizedCommitDiffDatas.size()];
        double[] y = new double[this.nomalizedCommitDiffDatas.size()];
        int i = 0;
        for (NomalizedCommitDiffData ncdd : this.nomalizedCommitDiffDatas) {
            x[i] = ncdd.time;
            y[i++] = ncdd.deletions;
        }
        return interpolator.interpolate(x, y);
    }

    public double[] polynomChangedFiles() throws IOException, InterruptedException {

        NomalizedCommitDiffDatas rNCDD = this.reducednomalizedCommitDiffDatas();
        double[] x = new double[rNCDD.nomalizedCommitDiffDatas.size()];
        double[] y = new double[rNCDD.nomalizedCommitDiffDatas.size()];

        int i = 0;
        for (NomalizedCommitDiffData ncdd : rNCDD.nomalizedCommitDiffDatas) {
            x[i] = ncdd.time;
            y[i++] = ncdd.changedFiles;
        }

        return Polynom.polynom(y, x);
    }

    public double[] polynomDeletions() throws IOException, InterruptedException {
        NomalizedCommitDiffDatas rNCDD = this.reducednomalizedCommitDiffDatas();
        double[] x = new double[rNCDD.nomalizedCommitDiffDatas.size()];
        double[] y = new double[rNCDD.nomalizedCommitDiffDatas.size()];
        int i = 0;
        for (NomalizedCommitDiffData ncdd : rNCDD.nomalizedCommitDiffDatas) {
            x[i] = ncdd.time;
            y[i++] = ncdd.deletions;
        }
        return Polynom.polynom(y, x);
    }

    public double[] polynomInsertions() throws IOException, InterruptedException {
        NomalizedCommitDiffDatas rNCDD = this.reducednomalizedCommitDiffDatas();
        double[] x = new double[rNCDD.nomalizedCommitDiffDatas.size()];
        double[] y = new double[rNCDD.nomalizedCommitDiffDatas.size()];
        int i = 0;
        for (NomalizedCommitDiffData ncdd : rNCDD.nomalizedCommitDiffDatas) {
            x[i] = ncdd.time;
            y[i++] = ncdd.insertions;
        }
        return Polynom.polynom(y, x);
    }

    public static NomalizedCommitDiffDatas nomalizedCommitDiffDatas(CommitDiffs dcs) throws IOException, InterruptedException, ParseException {

        NomalizedCommitDiffDatas ret = new NomalizedCommitDiffDatas();

        for (int i = 0; i < dcs.size(); i++) {
            ret.add(new NomalizedCommitDiffData(dcs.get(i), dcs.totalCommitDiff));
        }

        return ret;
    }

    public static NomalizedCommitDiffDatas nomalizedCommitDiffDatas(Path pathDir, CommitDiff total, boolean onlyMergesCommits) throws IOException, InterruptedException, ParseException {

        NomalizedCommitDiffDatas ret = new NomalizedCommitDiffDatas();

        CommitDiffs dcs = CommitDiff.gitCommitDiffs(pathDir, total, onlyMergesCommits);

        for (int i = 0; i < dcs.size(); i++) {
            ret.add(new NomalizedCommitDiffData(dcs.get(i), total));
        }

        return ret;
    }

    public static NomalizedCommitDiffDatas reducednomalizedCommitDiffDatas(NomalizedCommitDiffDatas orig) throws IOException, InterruptedException {

        NomalizedCommitDiffDatas ret = new NomalizedCommitDiffDatas();

        if (orig.size() > 0) {

            int i = 1;
            double s1 = 0.0, s2 = 0.0, s3 = 0.0;
            double t1 = orig.get(0).time;

            //ret.add(new NomalizedCommitDiffData(0, 0, 0, 0));
            for (NomalizedCommitDiffData ncdd : orig.nomalizedCommitDiffDatas) {
                if (t1 != ncdd.time) {

                    ret.add(new NomalizedCommitDiffData(t1, s1 / i, s2 / i, s3 / i));

                    t1 = ncdd.time;
                    s1 = s2 = s3 = 0.0;
                    i = 1;
                } else {
                    s1 += ncdd.changedFiles;
                    s2 += ncdd.insertions;
                    s3 += ncdd.deletions;
                    i++;
                }
            }

            ret.add(new NomalizedCommitDiffData(t1, s1 / i, s2 / i, s3 / i));
        }

        return ret;
    }

    static public void gnuplot(Path path) throws IOException, InterruptedException {

        Process process = Runtime.getRuntime().exec("gnuplot -p " + path.toString());

        StringBuilder error = new StringBuilder();

        BufferedReader stdError = new BufferedReader(
                new InputStreamReader(process.getErrorStream()));

        String line;
        while ((line = stdError.readLine()) != null) {
            error.append(line).append("\n");
        }

        int exitVal = process.waitFor();

        if (exitVal == 0 && error.toString().length() > 0) {

            System.out.println("Error: " + error.toString());

        }

    }

    public static void polynomPlotScript(Path path, String title, double[] polynom) throws IOException {

        String fx = Polynom.toString(polynom);

        File file = new File(path.toString());

        // delete the file if it exists
        file.delete();

        // creates the file
        file.createNewFile();

        // creates a FileWriter Object
        FileWriter writer = new FileWriter(file);

        StringBuilder sb = new StringBuilder();

        sb.append("set title \"").append(title).append("\"\n");
        sb.append("set xlabel \"elapse time\"\n");
        sb.append("set ylabel \"volume\"\n");
        sb.append("set grid\n");
        sb.append(fx).append("\n");
        sb.append("set xrange [0:1]\n");
        sb.append("set yrange [0:1]\n");
        sb.append("plot f(x)\n");

        // Writes the content to the file
        writer.write(sb.toString());
        writer.flush();
        writer.close();

    }

    public static void dataPlotScript(Path path, Path data) throws IOException {

        File file = new File(path.toString());

        // delete the file if it exists
        file.delete();

        // creates the file
        file.createNewFile();

        // creates a FileWriter Object
        FileWriter writer = new FileWriter(file);

        StringBuilder sb = new StringBuilder();

        sb.append("set title \"CommitDiff\"\n");
        sb.append("set xlabel \"tempo\"\n");
        sb.append("set ylabel \"volume\"\n");
        sb.append("set grid\n");
        sb.append("plot \"").append(data.toString()).append("\" using 1:2 title 'Changed' with lines, ");
        sb.append("\"").append(data.toString()).append("\" using 1:3 title 'Insertions' with lines, ");
        sb.append("\"").append(data.toString()).append("\" using 1:4 title 'Deletions' with lines\n");

        // Writes the content to the file
        writer.write(sb.toString());
        writer.flush();
        writer.close();

    }
    
    public static void dataChangedFilesPlotScript(Path path, Path data) throws IOException {

        File file = new File(path.toString());

        // delete the file if it exists
        file.delete();

        // creates the file
        file.createNewFile();

        // creates a FileWriter Object
        FileWriter writer = new FileWriter(file);

        StringBuilder sb = new StringBuilder();

        sb.append("set title \"ChangedFiles\"\n");
        sb.append("set xlabel \"tempo\"\n");
        sb.append("set ylabel \"volume\"\n");
        sb.append("set grid\n");
        sb.append("plot \"").append(data.toString()).append("\" using 1:2 title 'Changed' with lines\n");
        
        // Writes the content to the file
        writer.write(sb.toString());
        writer.flush();
        writer.close();

    }
    
    public static void dataDeletionsPlotScript(Path path, Path data) throws IOException {

        File file = new File(path.toString());

        // delete the file if it exists
        file.delete();

        // creates the file
        file.createNewFile();

        // creates a FileWriter Object
        FileWriter writer = new FileWriter(file);

        StringBuilder sb = new StringBuilder();

        sb.append("set title \"Deletions\"\n");
        sb.append("set xlabel \"tempo\"\n");
        sb.append("set ylabel \"volume\"\n");
        sb.append("set grid\n");
        sb.append("plot \"").append(data.toString()).append("\" using 1:4 title 'Deletions' with lines\n");

        // Writes the content to the file
        writer.write(sb.toString());
        writer.flush();
        writer.close();

    }
    
    public static void dataInsertionsPlotScript(Path path, Path data) throws IOException {

        File file = new File(path.toString());

        // delete the file if it exists
        file.delete();

        // creates the file
        file.createNewFile();

        // creates a FileWriter Object
        FileWriter writer = new FileWriter(file);

        StringBuilder sb = new StringBuilder();

        sb.append("set title \"Insertions\"\n");
        sb.append("set xlabel \"tempo\"\n");
        sb.append("set ylabel \"volume\"\n");
        sb.append("set grid\n");
        sb.append("plot \"").append(data.toString()).append("\" using 1:3 title 'Insertions' with lines\n");

        // Writes the content to the file
        writer.write(sb.toString());
        writer.flush();
        writer.close();

    }
    
    public static void dataPlotFile(NomalizedCommitDiffDatas aux, Path path) throws IOException {

        File file = new File(path.toString());

        // delete the file if it exists
        file.delete();

        // creates the file
        file.createNewFile();

        // creates a FileWriter Object
        FileWriter writer = new FileWriter(file);

        String s = "";
        for (NomalizedCommitDiffData ncdd : aux.nomalizedCommitDiffDatas) {
            s += ncdd.toString() + "\n";
        }

        // Writes the content to the file
        writer.write(s);
        writer.flush();
        writer.close();

    }

}
