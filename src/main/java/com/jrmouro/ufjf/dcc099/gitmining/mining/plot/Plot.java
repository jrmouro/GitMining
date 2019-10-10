/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jrmouro.ufjf.dcc099.gitmining.mining.plot;

import com.jrmouro.ufjf.dcc099.gitmining.polynom.Polynom;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;

/**
 *
 * @author ronaldo
 */
public class Plot {
    
    static public void plotScript(Path scriptPath) throws IOException, InterruptedException {

        Process process = Runtime.getRuntime().exec("gnuplot -p " + scriptPath.toString());

        StringBuilder error = new StringBuilder();

        BufferedReader stdError = new BufferedReader(
                new InputStreamReader(process.getErrorStream()));

        String line;
        while ((line = stdError.readLine()) != null) {
            error.append(line).append("\n");
        }

        int exitVal = process.waitFor();

        if (exitVal == 0 && error.toString().length() > 0) {

            System.out.println("Error: " + error.toString());

        }

    }

    public static Path polynomScript(Path scriptPath, String title, double[] polynom) throws IOException {

        String fx = Polynom.toString(polynom);

        File file = new File(scriptPath.toString());

        // delete the file if it exists
        file.delete();

        // creates the file
        file.createNewFile();

        // creates a FileWriter Object
        FileWriter writer = new FileWriter(file);

        StringBuilder sb = new StringBuilder();

        sb.append("set title \"").append(title).append("\"\n");
        sb.append("set xlabel \"elapse time\"\n");
        sb.append("set ylabel \"volume\"\n");
        sb.append("set grid\n");
        sb.append(fx).append("\n");
        sb.append("set xrange [0:1]\n");
        sb.append("set yrange [0:1]\n");
        sb.append("plot f(x)\n");

        // Writes the content to the file
        writer.write(sb.toString());
        writer.flush();
        writer.close();
        
        return scriptPath;

    }
}
