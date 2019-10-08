/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jrmouro.ufjf.dcc099.gitmining.mining;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.simple.parser.ParseException;

/**
 *
 * @author ronaldo
 */
public class NomalizedCommitDiffData {

    public double ElapsedTime = 0;
    public double changedFiles = 0;
    public double deletions = 0;
    public double insertions = 0;

    public NomalizedCommitDiffData(double time, double changedFiles, double insertions, double deletions) {
        this.ElapsedTime = time;
        this.changedFiles = changedFiles;
        this.insertions = insertions;
        this.deletions = deletions;
    }

    public NomalizedCommitDiffData(CommitDiff commitDiff, CommitDiff commitDiffRef) {

        if (Math.abs(commitDiffRef.c1.date - commitDiffRef.c2.date) > 0) {
            
            this.ElapsedTime = Double.valueOf(Math.abs(commitDiff.c1.date - commitDiff.c2.date)) / Double.valueOf(Math.abs(commitDiffRef.c1.date - commitDiffRef.c2.date));
            int aux = (int) (this.ElapsedTime * 10.0);
            this.ElapsedTime = 0.1 + (aux / 10.0);
            
        }else{
            this.ElapsedTime = 1.0;
        }

        if (commitDiffRef.diff.changedfiles > 0.0) {
            this.changedFiles = Double.valueOf(commitDiff.diff.changedfiles) / Double.valueOf(commitDiffRef.diff.changedfiles);
        } else {
            this.changedFiles = 1.0;
        }

        if (commitDiffRef.diff.deletions > 0.0) {
            this.deletions = Double.valueOf(commitDiff.diff.deletions) / Double.valueOf(commitDiffRef.diff.deletions);
        } else {
            this.deletions = 1.0;
        }
        
        if (commitDiffRef.diff.insertions > 0.0) {
            this.insertions = Double.valueOf(commitDiff.diff.insertions) / Double.valueOf(commitDiffRef.diff.insertions);
        } else {
            this.insertions = 1.0;
        }       
        

    }

    @Override
    public String toString() {
        return String.valueOf(this.ElapsedTime) + "\t"
                + String.valueOf(this.changedFiles) + "\t"
                + String.valueOf(this.insertions) + "\t"
                + String.valueOf(this.deletions);
    }

    public static List<NomalizedCommitDiffData> nomalizedCommitDiffDataList(CommitDiffs dcs) throws IOException, InterruptedException, ParseException {
        
        List<NomalizedCommitDiffData> ret = new ArrayList();        
        
        for(int i = 0; i < dcs.size(); i++){
            ret.add(new NomalizedCommitDiffData(dcs.get(i), dcs.totalCommitDiff));
        }
                
        return ret;
    }
    
    public static List<NomalizedCommitDiffData> nomalizedCommitDiffDataList(Path pathDir, CommitDiff total, boolean onlyMergesCommits) throws IOException, InterruptedException, ParseException {
        
        List<NomalizedCommitDiffData> ret = new ArrayList();
        
        CommitDiffs dcs = CommitDiff.gitCommitDiffs(pathDir, total, onlyMergesCommits);
        
        for(int i = 0; i < dcs.size(); i++)
            ret.add(new NomalizedCommitDiffData(dcs.get(i), total));
                
        return ret;
    }

    static public void CommitDiffPlotSmoothBezier(List<NomalizedCommitDiffData> aux, Path script, Path data) throws IOException, InterruptedException {
        CommitDiffDataPlotScriptSmoothBezier(script, data);
        CommitDiffDataPlotFile(aux, data);
        CommitDiffPlot(script);
    }

    static public void CommitDiffPlot(List<NomalizedCommitDiffData> aux, Path script, Path data) throws IOException, InterruptedException {
        CommitDiffDataPlotScript(script, data);
        CommitDiffDataPlotFile(aux, data);
        CommitDiffPlot(script);
    }

    static public void CommitDiffPlot(Path pathDir) throws IOException, InterruptedException {

        Process process = Runtime.getRuntime().exec("gnuplot -p " + pathDir.toString());

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

    public static void CommitDiffDataPlotScriptSmoothBezier(Path path, Path data) throws IOException {

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
        sb.append("plot \"").append(data.toString()).append("\" using 1:2 title 'Changed' smooth bezier, ");
        sb.append("\"").append(data.toString()).append("\" using 1:3 title 'Insertions' smooth bezier, ");
        sb.append("\"").append(data.toString()).append("\" using 1:4 title 'Deletions' smooth bezier\n");

        // Writes the content to the file
        writer.write(sb.toString());
        writer.flush();
        writer.close();

    }

    public static void CommitDiffDataPlotScript(Path path, Path data) throws IOException {

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

    public static void CommitDiffDataPlotFile(List<NomalizedCommitDiffData> aux, Path path) throws IOException {

        File file = new File(path.toString());

        // delete the file if it exists
        file.delete();

        // creates the file
        file.createNewFile();

        // creates a FileWriter Object
        FileWriter writer = new FileWriter(file);

        String s = "";
        for (NomalizedCommitDiffData ncdd : aux) {
            s += ncdd.toString() + "\n";
        }

        // Writes the content to the file
        writer.write(s);
        writer.flush();
        writer.close();

    }

    public static List<NomalizedCommitDiffData> reducednomalizedCommitDiffDataList(List<NomalizedCommitDiffData> aux) throws IOException, InterruptedException {

        List<NomalizedCommitDiffData> ret = new ArrayList();

        if (aux.size() > 0) {

            int i = 0;
            double s1 = 0.0, s2 = 0.0, s3 = 0.0;
            double t1 = aux.get(0).ElapsedTime;
            
            ret.add(new NomalizedCommitDiffData(0, 0, 0, 0));

            for (NomalizedCommitDiffData ncdd : aux) {
                if (t1 != ncdd.ElapsedTime) {

                    ret.add(new NomalizedCommitDiffData(t1, s1 / i, s2 / i, s3 / i));

                    t1 = ncdd.ElapsedTime;
                    s1 = s2 = s3 = 0.0;
                    i = 0;
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

    public static List<Map<Double, Double>> nomalizedCommitDiffDataListMap(Path pathDir, CommitDiff total, boolean onlyMergesCommits) throws IOException, InterruptedException, ParseException {

        List<Map<Double, Double>> ret = new ArrayList();

        for (int i = 0; i < 3; i++) {
            ret.add(new HashMap());
        }

        List<NomalizedCommitDiffData> list = nomalizedCommitDiffDataList(pathDir, total, onlyMergesCommits);

        for (NomalizedCommitDiffData ncdd : list) {

            if (ncdd.ElapsedTime > 0.0) {

                if (ncdd.changedFiles > 0.0) {

                    Double v = ret.get(0).putIfAbsent(ncdd.ElapsedTime, ncdd.changedFiles);
                    if (v != null) {
                        ret.get(0).put(ncdd.ElapsedTime, (ncdd.changedFiles + ret.get(0).get(ncdd.ElapsedTime)) / 2);
                    }
                }

                if (ncdd.insertions > 0.0) {

                    Double v = ret.get(1).putIfAbsent(ncdd.ElapsedTime, ncdd.insertions);
                    if (v != null) {
                        ret.get(1).put(ncdd.ElapsedTime, (ncdd.insertions + ret.get(1).get(ncdd.ElapsedTime)) / 2);
                    }

                }

                if (ncdd.deletions > 0.0) {

                    Double v = ret.get(2).putIfAbsent(ncdd.ElapsedTime, ncdd.deletions);
                    if (v != null) {
                        ret.get(2).put(ncdd.ElapsedTime, (ncdd.deletions + ret.get(2).get(ncdd.ElapsedTime)) / 2);
                    }
                }

            }

        }

        return ret;

    }

}