package edu.ufjf.dcc099.gitmining;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.jrmouro.ufjf.dcc099.gitmining.MergeConflicts;
import com.jrmouro.ufjf.dcc099.gitmining.Mining;
import java.io.IOException;
import java.nio.file.Path;
import org.json.simple.parser.ParseException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author ronaldo
 */
public class MergeConflictsJUnitTest {
    
    public MergeConflictsJUnitTest() {
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
        
        Path path = Mining.getPath("temp");
        MergeConflicts mc = MergeConflicts.gitCommitList(path);
        
        mc.forEach((t) -> {
            System.out.println(t);
        });
        
        System.out.println(mc.getNrMergeCommits());
        System.out.println(mc.getNrMergeConflicts());
        System.out.println(mc.getConflictsRate());
        
    
    }
}
