/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jrmouro.gitmining;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author ronaldo
 */
public class Mining {

    public static class CommitDiff {

        final protected Commit c1, c2;
        final public Diff diff;

        public CommitDiff(Commit c1, Commit c2, Diff diff) {
            this.c1 = c1;
            this.c2 = c2;
            this.diff = diff;
        }

        public static List<CommitDiff> listCommitDiff(Path pathDir, boolean onlyMergesCommits) throws IOException, InterruptedException {
            List<CommitDiff> ret = new ArrayList();
            List<Commit> commits = Commit.gitCommitHashList(pathDir, onlyMergesCommits);

            for (int i = 0; i < commits.size() - 1; i++) {
                Diff diff = Diff.gitDiffShortstat(commits.get(i).hash, commits.get(i + 1).hash, pathDir);
                ret.add(new CommitDiff(commits.get(i), commits.get(i + 1), diff));

            }
            return ret;
        }

        public static List<CommitDiff> listCommitDiff(Path pathDir, Diff max, AtomicInteger first, AtomicInteger last, boolean onlyMergesCommits) throws IOException, InterruptedException {
            List<CommitDiff> ret = new ArrayList();
            List<Commit> commits = Commit.gitCommitHashList(pathDir, onlyMergesCommits);

            if (commits.size() > 0) {
                last.set(commits.get(0).timestamp);
                first.set(commits.get(0).timestamp);
            }

            for (int i = 0; i < commits.size() - 1; i++) {
                Diff diff = Diff.gitDiffShortstat(commits.get(i).hash, commits.get(i + 1).hash, pathDir);
                if (diff != null) {
                    ret.add(new CommitDiff(commits.get(i), commits.get(i + 1), diff));
                    if (diff.changedfiles > max.changedfiles) {
                        max.changedfiles = diff.changedfiles;
                    }
                    if (diff.deletions > max.deletions) {
                        max.deletions = diff.deletions;
                    }
                    if (diff.insertions > max.insertions) {
                        max.insertions = diff.insertions;
                    }
                    if (commits.get(i).timestamp < first.get()) {
                        first.set(commits.get(i).timestamp);
                    }
                    if (commits.get(i).timestamp > last.get()) {
                        last.set(commits.get(i).timestamp);
                    }
                }

            }

            return ret;
        }

    }

    public static class NomalizedCommitDiffData {

        public double time = 0;
        public double changedFiles = 0;
        public double deletions = 0;
        public double insertions = 0;

        public NomalizedCommitDiffData(double time, double changedFiles, double insertions, double deletions) {
            this.time = time;
            this.changedFiles = changedFiles;
            this.insertions = insertions;
            this.deletions = deletions;
        }

        public NomalizedCommitDiffData(AtomicInteger firstTime, AtomicInteger lastTime, Diff maxDiff, Commit c1, Commit c2, Diff diff) {

            if (lastTime.get() - firstTime.get() > 0) {
                this.time = Double.valueOf(c2.timestamp - firstTime.get()) / Double.valueOf(lastTime.get() - firstTime.get());

                int aux = (int) (this.time * 10.0);
                this.time = aux / 10.0;
            } else {
                this.time = -1.0;
            }

            if (maxDiff.changedfiles > 0.0) {
                this.changedFiles = Double.valueOf(diff.changedfiles) / Double.valueOf(maxDiff.changedfiles);
            } else {
                this.changedFiles = -1.0;
            }

            if (maxDiff.deletions > 0.0) {
                this.deletions = Double.valueOf(diff.deletions) / Double.valueOf(maxDiff.deletions);
            } else {
                this.deletions = -1.0;
            }

            if (maxDiff.insertions > 0.0) {
                this.insertions = Double.valueOf(diff.insertions) / Double.valueOf(maxDiff.insertions);
            } else {
                this.insertions = -1.0;
            }

        }

        @Override
        public String toString() {
            return String.valueOf(this.time) + "\t"
                    + String.valueOf(this.changedFiles) + "\t"
                    + String.valueOf(this.insertions) + "\t"
                    + String.valueOf(this.deletions);
        }

        public static List<NomalizedCommitDiffData> nomalizedCommitDiffDataList(Path pathDir, boolean onlyMergesCommits) throws IOException, InterruptedException {
            List<NomalizedCommitDiffData> ret = new ArrayList();
            Diff max = new Diff();
            AtomicInteger first = new AtomicInteger(0), last = new AtomicInteger(0);
            List<CommitDiff> dc = CommitDiff.listCommitDiff(pathDir, max, first, last, onlyMergesCommits);
            for (CommitDiff commitDiff : dc) {
                //if(commitDiff.c1.timestamp > commitDiff.c2.timestamp)
                ret.add(new NomalizedCommitDiffData(first, last, max, commitDiff.c1, commitDiff.c2, commitDiff.diff));
            }

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
                error.append(line + "\n");
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
            sb.append("plot \"" + data.toString() + "\" using 1:2 title 'Changed' smooth bezier, ");
            sb.append("\"" + data.toString() + "\" using 1:3 title 'Insertions' smooth bezier, ");
            sb.append("\"" + data.toString() + "\" using 1:4 title 'Deletions' smooth bezier\n");

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
            sb.append("plot \"" + data.toString() + "\" using 1:2 title 'Changed' with lines, ");
            sb.append("\"" + data.toString() + "\" using 1:3 title 'Insertions' with lines, ");
            sb.append("\"" + data.toString() + "\" using 1:4 title 'Deletions' with lines\n");

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
                double t1 = aux.get(0).time;

                for (NomalizedCommitDiffData ncdd : aux) {
                    if (t1 != ncdd.time) {

                        ret.add(new NomalizedCommitDiffData(t1, s1 / i, s2 / i, s3 / i));

                        t1 = ncdd.time;
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

        public static List<Map<Double, Double>> nomalizedCommitDiffDataListMap(Path pathDir, boolean onlyMergesCommits) throws IOException, InterruptedException {

            List<Map<Double, Double>> ret = new ArrayList();

            for (int i = 0; i < 3; i++) {
                ret.add(new HashMap());
            }

            List<NomalizedCommitDiffData> list = nomalizedCommitDiffDataList(pathDir, onlyMergesCommits);

            for (NomalizedCommitDiffData ncdd : list) {

                if (ncdd.time > 0.0) {

                    if (ncdd.changedFiles > 0.0) {

                        Double v = ret.get(0).putIfAbsent(ncdd.time, ncdd.changedFiles);
                        if (v != null) {
                            ret.get(0).put(ncdd.time, (ncdd.changedFiles + ret.get(0).get(ncdd.time)) / 2);
                        }
                    }

                    if (ncdd.insertions > 0.0) {

                        Double v = ret.get(1).putIfAbsent(ncdd.time, ncdd.insertions);
                        if (v != null) {
                            ret.get(1).put(ncdd.time, (ncdd.insertions + ret.get(1).get(ncdd.time)) / 2);
                        }

                    }

                    if (ncdd.deletions > 0.0) {

                        Double v = ret.get(2).putIfAbsent(ncdd.time, ncdd.deletions);
                        if (v != null) {
                            ret.get(2).put(ncdd.time, (ncdd.deletions + ret.get(2).get(ncdd.time)) / 2);
                        }
                    }

                }

            }

            return ret;

        }

    }

    public static class Commit {

        final public String hash;
        final public int timestamp;

        public Commit(String hash, int timestamp) {
            this.hash = hash;
            this.timestamp = timestamp;
        }

        @Override
        public String toString() {
            String ret = "hash: " + this.hash + " - timestamp: " + timestamp;
            return ret; //To change body of generated methods, choose Tools | Templates.
        }

        public static Commit parse(String diff) {

            String[] aux = diff.split("-");

            return new Commit(aux[0], Integer.parseInt(aux[1]));
        }

        static public List<Commit> gitCommitHashList(Path pathDir, boolean onlyMergesCommits) throws IOException, InterruptedException {
            List<Commit> ret = new ArrayList();
            Process process = null;
            if (onlyMergesCommits) {
                process = Runtime.getRuntime().exec("git log --merges --pretty=format:%H-%ct", null, new File(pathDir.toString()));
            } else {
                process = Runtime.getRuntime().exec("git log --pretty=format:%H-%ct", null, new File(pathDir.toString()));
            }
            StringBuilder output = new StringBuilder();
            StringBuilder error = new StringBuilder();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            BufferedReader stdError = new BufferedReader(
                    new InputStreamReader(process.getErrorStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line + "\n");
                ret.add(Commit.parse(line));
            }

            while ((line = stdError.readLine()) != null) {
                error.append(line + "\n");
            }

            int exitVal = process.waitFor();

            if (exitVal == 0 && error.toString().length() > 0) {

                System.out.println("Error: " + error.toString());

            }
            return ret;
        }

    }

    public static class Diff {

        public int changedfiles = 0, insertions = 0, deletions = 0;

        public Diff() {
        }

        @Override
        public String toString() {
            return "ChangedFiles: " + String.valueOf(this.changedfiles)
                    + " - Insertions: " + String.valueOf(this.insertions)
                    + " - Deletions: " + String.valueOf(this.deletions);
        }

        public Diff(int changedfiles, int insertions, int deletions) {
            this.changedfiles = changedfiles;
            this.insertions = insertions;
            this.deletions = deletions;
        }

        public static Diff parse(String diff) {

            Diff ret = new Diff();

            if (diff != null) {

                String[] aux = diff.split(",");

                for (String string : aux) {

                    String a = string.trim();
                    String[] b = a.split(" ");
                    if (b[1].charAt(0) == 'f') {
                        ret.changedfiles = Integer.parseInt(b[0]);
                    }
                    if (b[1].charAt(0) == 'i') {
                        ret.insertions = Integer.parseInt(b[0]);
                    }
                    if (b[1].charAt(0) == 'd') {
                        ret.deletions = Integer.parseInt(b[0]);
                    }

                }
            }

            //System.out.println(ret);
            return ret;
        }

        static public Diff gitDiffShortstat(String h1, String h2, Path pathDir) throws IOException, InterruptedException {
            Diff ret = null;
            Process process = Runtime.getRuntime().exec("git diff " + h1 + " " + h2 + " --shortstat", null, new File(pathDir.toString()));
            //StringBuilder output = new StringBuilder();
            StringBuilder error = new StringBuilder();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            BufferedReader stdError = new BufferedReader(
                    new InputStreamReader(process.getErrorStream()));

            String line = reader.readLine();
            if (line != null) {
                ret = Diff.parse(line);
            }
            
            while ((line = stdError.readLine()) != null) {
                error.append(line + "\n");
            }

            int exitVal = process.waitFor();

            if (exitVal == 0 && error.toString().length() > 0) {

                System.out.println("Error: " + error.toString());

            }

            return ret;
        }

        static public List<Diff> gitDiffShortstat(List<Commit> commits, Path pathDir) throws IOException, InterruptedException {

            List<Diff> ret = new ArrayList();

            for (int i = 0; i < commits.size() - 1; i++) {
                ret.add(Diff.gitDiffShortstat(commits.get(i).hash, commits.get(i + 1).hash, pathDir));
            }

            return ret;
        }

    }

    static public List<String> gitCommitHashList(Path pathDir, boolean onlyMergesCommits) throws IOException, InterruptedException {
        List<String> ret = new ArrayList();
        Process process = null;
        if (onlyMergesCommits) {
            process = Runtime.getRuntime().exec("git log --merges --pretty=format:%H", null, new File(pathDir.toString()));
        } else {
            process = Runtime.getRuntime().exec("git log --pretty=format:%H", null, new File(pathDir.toString()));
        }

        StringBuilder output = new StringBuilder();
        StringBuilder error = new StringBuilder();

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()));

        BufferedReader stdError = new BufferedReader(
                new InputStreamReader(process.getErrorStream()));

        String line;
        while ((line = reader.readLine()) != null) {
            output.append(line + "\n");
            ret.add(line);
        }

        while ((line = stdError.readLine()) != null) {
            error.append(line + "\n");
        }

        int exitVal = process.waitFor();

        if (exitVal == 0 && error.toString().length() > 0) {

            System.out.println("Error: " + error.toString());

        }

        return ret;
    }

    static public void gitCloneRepository(URL url, Path pathDir) throws IOException, InterruptedException {

        Process process = Runtime.getRuntime().exec("git clone " + url.toString() + " " + pathDir.toString());

        StringBuilder output = new StringBuilder();
        StringBuilder error = new StringBuilder();

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()));

        BufferedReader stdError = new BufferedReader(
                new InputStreamReader(process.getErrorStream()));

        String line;
        while ((line = reader.readLine()) != null) {
            output.append(line + "\n");
        }

        while ((line = stdError.readLine()) != null) {
            error.append(line + "\n");
        }

        int exitVal = process.waitFor();

        if (exitVal == 0 && error.toString().length() > 0) {

            System.out.println("Info: " + error.toString());
            System.out.println("Output: " + output.toString());

        }

    }

    static public List<URL> githubPublicRepositoriesUrl(int since) throws IOException, InterruptedException, ParseException {

        List<URL> ret = new ArrayList();

        Process process = Runtime.getRuntime().exec("curl https://api.github.com/repositories?since=" + String.valueOf(since));

        StringBuilder output = new StringBuilder();
        StringBuilder error = new StringBuilder();

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()));

        BufferedReader stdError = new BufferedReader(
                new InputStreamReader(process.getErrorStream()));

        String line;
        while ((line = reader.readLine()) != null) {
            output.append(line + "\n");
        }

        while ((line = stdError.readLine()) != null) {
            error.append(line + "\n");
        }

        int exitVal = process.waitFor();

        if (exitVal == 0) {

            if (error.toString().length() > 0) {

                System.out.println("Info:\n" + error.toString());

            }

            JSONArray jo = (JSONArray) new JSONParser().parse(output.toString());

            jo.forEach((t) -> {

                JSONObject obj = (JSONObject) t;

                if (!(boolean) obj.get("fork")) {
                    try {
                        ret.add(new URL((String) obj.get("html_url")));
                    } catch (MalformedURLException ex) {
                        Logger.getLogger(Mining.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            });

        }

        return ret;

    }

    static public Path getPath(String name) throws IOException {
        String aux = new File(".").getCanonicalPath();
        return Paths.get(aux + "/" + name);
    }

    static public boolean deleteTempDir() throws IOException {

        return deleteDir("temp");
    }

    static public boolean deleteDir(String name) throws IOException {

        String aux = new File(".").getCanonicalPath();
        Path baseTempPath = Paths.get(aux + "/" + name);

        return deleteDir(baseTempPath);
    }

    static public boolean deleteDir(Path path) throws IOException {

        if (Files.exists(path)) {
            FileUtils.deleteDirectory(new File(path.toString()));
            return true;
        }

        return false;
    }

    static public String createTempDir() throws IOException {

        return createDir("temp");

    }

    static public String createDir(String name) throws IOException {

        String aux = new File(".").getCanonicalPath();

        Path baseTempPath = Paths.get(aux + "/" + name);

        return createDir(baseTempPath);

    }

    static public String createDir(Path path) throws IOException {

        if (!Files.exists(path)) {
            File file = new File(path.toString());
            boolean result = file.mkdirs();
            if (result) {
                return file.getAbsolutePath();
            }
        }

        return null;

    }

}
