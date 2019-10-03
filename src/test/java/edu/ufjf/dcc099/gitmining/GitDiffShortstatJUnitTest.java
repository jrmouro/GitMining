package edu.ufjf.dcc099.gitmining;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.jrmouro.ufjf.dcc099.gitmining.Mining;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.List;
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
public class GitDiffShortstatJUnitTest {

    public GitDiffShortstatJUnitTest() {
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
        Mining.deleteDir(path);
        List<URL> urls = Mining.githubPublicRepositoriesUrl(1);
        if (urls.size() > 0) {

            Mining.gitCloneRepository(urls.get(10), path);

            List<Mining.Commit> commits = Mining.Commit.gitCommitHashList(path, false);
            List<Mining.Diff> diffs = Mining.Diff.gitDiffShortstat(commits, path);

            for (Mining.Diff diff : diffs) {
                System.out.println(diff);
            }

        }

    }
}
