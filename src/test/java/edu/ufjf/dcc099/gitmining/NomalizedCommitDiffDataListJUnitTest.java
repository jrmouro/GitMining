package edu.ufjf.dcc099.gitmining;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.jrmouro.ufjf.dcc099.gitmining.canonicalPath.CanonicalPath;
import com.jrmouro.ufjf.dcc099.gitmining.mining.Mining;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import org.json.simple.parser.ParseException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author ronaldo
 */
public class NomalizedCommitDiffDataListJUnitTest {
    
    public NomalizedCommitDiffDataListJUnitTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void test() throws IOException, InterruptedException, ParseException {
    
        Path path = CanonicalPath.getPath("temp");
        URL url = new URL("https://api.github.com/repos/jrmouro/GitMining");        
        CanonicalPath.deleteDir(path);
        
        Mining mining = new Mining(path, url, true);
        
        mining.getNcdd().polynomChangedFilesPlot(CanonicalPath.getPath("ChangedFilesPolynom.plot"));
        
        mining.getNcdd().dataChangedFilesPlot(CanonicalPath.getPath("ChangedFiles.plot"), CanonicalPath.getPath("ChangedFiles.txt"));
        
        
        mining.getNcdd().polynomDeletionsPlot(CanonicalPath.getPath("DeletionsPolynom.plot"));
        
        mining.getNcdd().dataDeletionsPlot(CanonicalPath.getPath("Deletions.plot"), CanonicalPath.getPath("Deletions.txt"));
        
        
        mining.getNcdd().polynomInsertionsPlot(CanonicalPath.getPath("InsertionsPolynom.plot"));
        
        mining.getNcdd().dataInsertionsPlot(CanonicalPath.getPath("Insertions.plot"), CanonicalPath.getPath("Insertions.txt"));
        
        
        
    }
}
