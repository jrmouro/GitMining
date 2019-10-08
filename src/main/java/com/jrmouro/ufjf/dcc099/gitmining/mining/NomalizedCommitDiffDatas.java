/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jrmouro.ufjf.dcc099.gitmining.mining;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.parser.ParseException;

/**
 *
 * @author ronaldo
 */
public class NomalizedCommitDiffDatas {
    
    private final List<NomalizedCommitDiffData> nomalizedCommitDiffDatas = new ArrayList();
    
    public void add(NomalizedCommitDiffData ncdd){        
        this.nomalizedCommitDiffDatas.add(ncdd);        
    }
    
    public NomalizedCommitDiffData get(Integer index){
        return this.nomalizedCommitDiffDatas.get(index);
    }
    
    public Integer size(){
        return this.nomalizedCommitDiffDatas.size();
    }
    
    public static NomalizedCommitDiffDatas nomalizedCommitDiffDataList(CommitDiffs dcs) throws IOException, InterruptedException, ParseException {
        
        NomalizedCommitDiffDatas ret = new NomalizedCommitDiffDatas();        
        
        for(int i = 0; i < dcs.size(); i++){
            ret.add(new NomalizedCommitDiffData(dcs.get(i), dcs.totalCommitDiff));
        }
                
        return ret;
    }
    
    public static NomalizedCommitDiffDatas nomalizedCommitDiffDataList(Path pathDir, CommitDiff total, boolean onlyMergesCommits) throws IOException, InterruptedException, ParseException {
        
        NomalizedCommitDiffDatas ret = new NomalizedCommitDiffDatas();
        
        CommitDiffs dcs = CommitDiff.gitCommitDiffs(pathDir, total, onlyMergesCommits);
        
        for(int i = 0; i < dcs.size(); i++)
            ret.add(new NomalizedCommitDiffData(dcs.get(i), total));
                
        return ret;
    }
    
}
