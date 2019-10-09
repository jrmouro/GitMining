/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jrmouro.ufjf.dcc099.gitmining.mining;

import com.jrmouro.ufjf.dcc099.gitmining.canonicalPath.CanonicalPath;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author ronaldo
 */
public class Mining {

    private final Path pathDir;

    private final URL url;
    
    private JSONObject githubRepositoryJSONObject = null;

    private Commits mergeCommits = null;

    private Commits commits = null;

    private CommitDiff total = null;

    private CommitDiffs commitDiffs = null;

    private NomalizedCommitDiffDatas ncdd = null;
    
    private NomalizedCommitDiffDatas rNcdd = null;
    
    private UnivariateFunction polynomChangedDeletions = null;
    
    private UnivariateFunction polynomChangedInsertions = null;
    
    private UnivariateFunction polynomChangedChangedFiles = null;

    private MergeConflicts mergeConflicts = null;
    
    private Map<String, Long> githubLanguages = null;
    
    private List<String> branches = null;

    public Path getPathDir() {
        return pathDir;
    }

    public URL getUrl() {
        return url;
    }

    public NomalizedCommitDiffDatas getrNcdd() {
        return rNcdd;
    }

    public UnivariateFunction getPolynomChangedDeletions() {
        return polynomChangedDeletions;
    }

    public UnivariateFunction getPolynomChangedInsertions() {
        return polynomChangedInsertions;
    }

    public UnivariateFunction getPolynomChangedChangedFiles() {
        return polynomChangedChangedFiles;
    }
        
    public void gitCloneRepository() throws IOException, InterruptedException, Exception{
        if(url != null && pathDir != null)
            Mining.gitCloneRepository(url, pathDir);
        else
            throw new Exception("Url or pathDir invalid!");
    }

    public JSONObject getGithubRepositoryJSONObject() {
        return githubRepositoryJSONObject;
    }

    public Commits getMergeCommits() {
        return mergeCommits;
    }

    public Commits getCommits() {
        return commits;
    }

    public CommitDiff getTotal() {
        return total;
    }

    public CommitDiffs getCommitDiffs() {
        return commitDiffs;
    }

    public NomalizedCommitDiffDatas getNcdd() {
        return ncdd;
    }

    public MergeConflicts getMergeConflicts() {
        return mergeConflicts;
    }

    public Map<String, Long> getGithubLanguages() {
        return githubLanguages;
    }

    public List<String> getBranches() {
        return branches;
    }
       
    
    public Long stargazersCount(){
        if(this.githubRepositoryJSONObject != null)
            return (Long)this.githubRepositoryJSONObject.get("stargazers_count");
        return 0L;
    }
    
    public Long subscribersCount(){
        if(this.githubRepositoryJSONObject != null)
            return (Long)this.githubRepositoryJSONObject.get("subscribers_count");
        return 0L;
    }
    
    public Long networkCount(){
        if(this.githubRepositoryJSONObject != null)
            return (Long)this.githubRepositoryJSONObject.get("network_count");
        return 0L;
    }
    
    public Long forksCount(){
        if(this.githubRepositoryJSONObject != null)
            return (Long)this.githubRepositoryJSONObject.get("forks_count");
        return 0L;
    }
    
    public String name(){
        if(this.githubRepositoryJSONObject != null)
            return (String)this.githubRepositoryJSONObject.get("name");
        return "";
    }
    
    public Long openIssuesCount(){
        if(this.githubRepositoryJSONObject != null)
            return (Long)this.githubRepositoryJSONObject.get("open_issues_count");
        return 0L;
    }
    
    public Long watchersCount(){
        if(this.githubRepositoryJSONObject != null)
            return (Long)this.githubRepositoryJSONObject.get("watchers_count");
        return 0L;
    }
    
    
    

    public Mining(Path pathDir, URL url, boolean clone) throws IOException, InterruptedException, ParseException {

        this.pathDir = pathDir;

        this.url = url;
        
        if(url != null){
            
            this.githubRepositoryJSONObject = Mining.githubRepositoryJSONObject(url);
            
            if(clone && pathDir != null){
            
                String htmlUrl = (String) this.githubRepositoryJSONObject.get("html_url");
            
                CanonicalPath.deleteDir(pathDir);
            
                gitCloneRepository(new URL(htmlUrl), pathDir);
            
            }
            
            this.githubLanguages = Mining.githubLanguages(new URL((String)this.githubRepositoryJSONObject.get("languages_url")));
        
            String branches_url = ((String)this.githubRepositoryJSONObject.get("branches_url")).replace("{/branch}", "");
            
            this.branches = Mining.githubBranches(new URL(branches_url));
        
        }

        if (this.pathDir != null) {

            this.mergeCommits = Commits.gitCommits(pathDir, true);

            this.commits = Commits.gitCommits(pathDir, false);

            if (this.commits.size() > 1) {

                this.total = new CommitDiff(pathDir, this.commits.get(0), this.commits.get(this.commits.size() - 1));

                this.commitDiffs = CommitDiffs.gitCommitDiffs(commits);

                this.ncdd = NomalizedCommitDiffDatas.nomalizedCommitDiffDatas(commitDiffs);
                
                this.rNcdd = ncdd.reducednomalizedCommitDiffDatas();
                
                this.polynomChangedDeletions = rNcdd.polynomDeletionsFunction();
    
                this.polynomChangedInsertions = rNcdd.polynomInsertionsFunction();
    
                this.polynomChangedChangedFiles = rNcdd.polynomChangedFilesFunction();

                this.mergeConflicts = MergeConflicts.gitMergeConflicts(pathDir);

                MergeConflicts.setConflictCommits(this.commits, mergeConflicts);

            }

        }

    }
    
    public List<String> githubBranches() throws IOException, InterruptedException, ParseException{
        return Mining.githubBranches(url);
    }

    public Map<String, Long> githubLanguages() throws IOException, InterruptedException, ParseException {
        if (url != null) {
            return Mining.githubLanguages(url);
        }
        return null;
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
            output.append(line).append("\n");
        }

        while ((line = stdError.readLine()) != null) {
            error.append(line).append("\n");
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

    static public Map<String, Long> githubLanguages(URL url) throws IOException, InterruptedException, ParseException {

        Map<String, Long> ret = new HashMap();

        Process process = Runtime.getRuntime().exec("curl " + url.toString());

        StringBuilder output = new StringBuilder();
        StringBuilder error = new StringBuilder();

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()));

        BufferedReader stdError = new BufferedReader(
                new InputStreamReader(process.getErrorStream()));

        String line;
        while ((line = reader.readLine()) != null) {
            output.append(line).append("\n");
        }

        while ((line = stdError.readLine()) != null) {
            error.append(line).append("\n");
        }

        int exitVal = process.waitFor();

        if (exitVal == 0) {

            if (error.toString().length() > 0) {

                System.out.println("Info:\n" + error.toString());

            }

            System.out.println("Languages:\n" + output.toString());

            JSONObject obj = (JSONObject) new JSONParser().parse(output.toString());

            obj.forEach((t, u) -> {
                ret.put((String) t, (Long) u);
            });

        }

        return ret;

    }

    static public List<String> githubBranches(URL url) throws IOException, InterruptedException, ParseException {

        List<String> ret = new ArrayList();

        Process process = Runtime.getRuntime().exec("curl " + url.toString());

        StringBuilder output = new StringBuilder();
        StringBuilder error = new StringBuilder();

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()));

        BufferedReader stdError = new BufferedReader(
                new InputStreamReader(process.getErrorStream()));

        String line;
        
        while ((line = reader.readLine()) != null) {
            output.append(line).append("\n");
        }

        while ((line = stdError.readLine()) != null) {
            error.append(line).append("\n");
        }

        int exitVal = process.waitFor();

        if (exitVal == 0) {

            if (error.toString().length() > 0) {

                System.out.println("Info:\n" + error.toString());

            }

            System.out.println("Branches:\n" + output.toString());

            JSONArray jo = (JSONArray) new JSONParser().parse(output.toString());

            jo.forEach((t) -> {

                JSONObject obj = (JSONObject) t;

                ret.add(((String) obj.get("name")));

            });

        }

        return ret;

    }
    
    static public JSONObject githubRepositoryJSONObject(URL url) throws IOException, InterruptedException, ParseException {

        Process process = Runtime.getRuntime().exec("curl " + url.toString());

        StringBuilder output = new StringBuilder();
        StringBuilder error = new StringBuilder();

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()));

        BufferedReader stdError = new BufferedReader(
                new InputStreamReader(process.getErrorStream()));

        String line;
        while ((line = reader.readLine()) != null) {
            output.append(line).append("\n");
        }

        while ((line = stdError.readLine()) != null) {
            error.append(line).append("\n");
        }

        int exitVal = process.waitFor();

        if (exitVal == 0) {

            if (error.toString().length() > 0) {

                System.out.println("Info:\n" + error.toString());

            }

            System.out.println("Repository JSONObject:\n" + output.toString());

            return (JSONObject) new JSONParser().parse(output.toString());

        }

        return null;

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


}
