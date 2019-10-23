package edu.ufjf.dcc099.gitmining;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.jrmouro.ufjf.dcc099.gitmining.mining.Mining;
import java.io.IOException;
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
public class GithubPublicRepositoriesUrlJUnitTest {
    
    public GithubPublicRepositoriesUrlJUnitTest() {
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
    public void hello() throws IOException, ParseException, InterruptedException {
    
        Mining.githubPublicRepositoriesUrl(1).forEach((t) -> {
            System.err.println(t.toString());
        });
    
    }
}
