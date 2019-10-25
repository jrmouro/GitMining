/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.ufjf.dcc099.gitmining;

import com.jrmouro.ufjf.dcc099.gitmining.vector.TokenVector;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author ronaldo
 */
public class StringTokenizerJUnitTest {

    public StringTokenizerJUnitTest() {
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

    

    private static double cosineSimilarity(double[] r, double[] s) {

        int t = Math.min(r.length, s.length);

        if (t == 0) {
            return 0.0;
        }

        double aux1 = 0.0;
        double aux2 = 0.0;
        double aux3 = 0.0;

        for (int i = 0; i < t; i++) {
            aux1 += r[i] * s[i];
            aux2 += r[i] * r[i];
            aux3 += s[i] * s[i];
        }

        if (aux2 <= 0.0 || aux3 <= 0.0) {
            return 0.0;
        }

        return aux1 / (Math.sqrt(aux2) * Math.sqrt(aux3));
    }

    @Test
    public void test() {

        TokenVector tm1 = new TokenVector("Mouro Jo.sé Ro\nnaldo, Mouro Mouro", " ,.\n");

        TokenVector tm2 = new TokenVector(tm1, "José Ronaldo Mouro");

        System.out.println(tm1);
        System.out.println(tm2);

        System.out.println(tm1.similarity(tm2));
        System.out.println(tm1.similarity(tm1));
        System.out.println(tm2.similarity(tm1));

    }
}
