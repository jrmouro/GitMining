/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.jrmouro.gitmining.Mining;
import com.jrmouro.gitmining.Polynom;
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
public class PolYnomJUnitTest {
    
    public PolYnomJUnitTest() {
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

            List<Mining.NomalizedCommitDiffData> list = Mining.NomalizedCommitDiffData.nomalizedCommitDiffDataList(path, false);
            List<Mining.NomalizedCommitDiffData> rlist = Mining.NomalizedCommitDiffData.reducednomalizedCommitDiffDataList(list);
            
            double[]f = new double[rlist.size()];
            double[]x = new double[rlist.size()];
            int i = 0;
            
            for (Mining.NomalizedCommitDiffData ncdd : rlist) {
                System.out.println(ncdd);
                f[i] = ncdd.changedFiles;
                x[i++] = ncdd.time;
            }
            
            Mining.NomalizedCommitDiffData.CommitDiffPlot(rlist, Mining.getPath("script.plot"), Mining.getPath("data.txt"));
            
            
            double[] p = Polynom.newton(f, x);
            i = 0;
            for (double d : p) {
                System.out.print("(" + d + "*x^" + String.valueOf(i++) + ") + ");
            }
            

        }
        
    }
}
