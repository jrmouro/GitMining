/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jrmouro.ufjf.dcc099.gitmining.mining;

import com.jrmouro.ufjf.dcc099.gitmining.mining.Diff;
import com.jrmouro.ufjf.dcc099.gitmining.mining.Commits;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.parser.ParseException;

/**
 *
 * @author ronaldo
 */
public class CommitDiffs {

    public final Path pathDir;
    final private List<CommitDiff> commitDiffs = new ArrayList();
    private Integer firstDate = Integer.MAX_VALUE, lastDate = 0;
    final public Diff max;
    final public CommitDiff totalCommitDiff;

    public CommitDiffs(Path pathRep, CommitDiff total) {
        this.pathDir = pathRep;
        this.totalCommitDiff = total;
        this.max = new Diff(null, null, pathRep);
    }
    

    public Double getMaxDeletionsRate() {
        return this.max.getDiffMaxDeletionsRate(this.totalCommitDiff.diff);
    }

    public Double getMaxInsertionsRate() {
        return this.max.getDiffMaxInsertionsRate(this.totalCommitDiff.diff);
    }

    public Double getMaxChangedFilesRate() {
        return this.max.getDiffMaxChangedFilesRate(this.totalCommitDiff.diff);
    }

    public Double getNrCommitsDiffsElapseDateRate() {
        if (Math.abs(Double.valueOf(lastDate - firstDate)) == 0.0) {
            return 1.0;
        }
        return Double.valueOf(this.commitDiffs.size()) / Math.abs(Double.valueOf(lastDate - firstDate));
    }

    public void add(CommitDiff commitDiff) {

        this.commitDiffs.add(commitDiff);

        if (this.firstDate > commitDiff.c1.date) {
            this.firstDate = commitDiff.c1.date;
        }

        if (this.lastDate < commitDiff.c2.date) {
            this.lastDate = commitDiff.c2.date;
        }

        if (commitDiff.diff.changedfiles > max.changedfiles) {
            max.changedfiles = commitDiff.diff.changedfiles;
        }
        if (commitDiff.diff.deletions > max.deletions) {
            max.deletions = commitDiff.diff.deletions;
        }
        if (commitDiff.diff.insertions > max.insertions) {
            max.insertions = commitDiff.diff.insertions;
        }

    }
    
    public CommitDiff get(Integer index){
        return this.commitDiffs.get(index);
    }
    
    public Integer size(){
        return this.commitDiffs.size();
    }

    public static CommitDiffs gitCommitDiffs(Commits commits) throws IOException, InterruptedException, ParseException {

        Diff diff = null;
        CommitDiff total = null;
        Path pathDir = null;
        CommitDiffs ret = null;

        if (commits.size() > 0) {
            
            pathDir = commits.get(0).pathDir;
            
            diff = Diff.gitDiff(commits.get(0), commits.get(commits.size() - 1), pathDir);
            
            total = new CommitDiff(pathDir, commits.get(0), commits.get(commits.size() - 1), diff);

            ret = new CommitDiffs(pathDir, total);

            for (int i = 0; i < commits.size() - 1; i++) {
                diff = Diff.gitDiff(commits.get(i), commits.get(i + 1), pathDir);
                ret.add(new CommitDiff(pathDir, commits.get(i), commits.get(i + 1), diff));
            }

        }

        return ret;

    }

    public static CommitDiffs gitCommitDiffs(Path pathDir, boolean onlyMergesCommits) throws IOException, InterruptedException, ParseException {

        Commits commits = Commits.gitCommits(pathDir, onlyMergesCommits);

        return gitCommitDiffs(commits);

    }

}
