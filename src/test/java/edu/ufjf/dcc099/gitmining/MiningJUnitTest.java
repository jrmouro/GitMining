/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ufjf.dcc099.gitmining;

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
public class MiningJUnitTest {
    
    public MiningJUnitTest() {
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
        Mining mining = new Mining(path, url, true);
        
        
        
        
    
        
        
    }
}
