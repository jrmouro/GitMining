/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jrmouro.ufjf.dcc099.gitmining.mining.plot;

import com.jrmouro.ufjf.dcc099.gitmining.polynom.Polynom;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ronaldo
 */
public class PolynomPlottable extends ScriptPlottable{
    
    final double[] polynom;
    final Path scriptPath;
    final String title;

    public PolynomPlottable(double[] polynom, Path scriptPath, String title) {
        this.polynom = polynom;
        this.scriptPath = scriptPath;
        this.title = title;
    }
    
    @Override
    public Path script() {
        
        String fx = Polynom.toString(polynom);

        File file = new File(scriptPath.toString());

        // delete the file if it exists
        file.delete();

        try {
            // creates the file
            file.createNewFile();
        } catch (IOException ex) {
            Logger.getLogger(PolynomPlottable.class.getName()).log(Level.SEVERE, null, ex);
        }

        // creates a FileWriter Object
        FileWriter writer = null;
        try {
            writer = new FileWriter(file);
        } catch (IOException ex) {
            Logger.getLogger(PolynomPlottable.class.getName()).log(Level.SEVERE, null, ex);
        }

        StringBuilder sb = new StringBuilder();

        sb.append("set title \"").append(title).append("\"\n");
        sb.append("set xlabel \"elapse time\"\n");
        sb.append("set ylabel \"volume\"\n");
        sb.append("set grid\n");
        sb.append(fx).append("\n");
        sb.append("set xrange [0:1]\n");
        sb.append("set yrange [0:1]\n");
        sb.append("plot f(x)\n");

        try {
            // Writes the content to the file
            writer.write(sb.toString());
        } catch (IOException ex) {
            Logger.getLogger(PolynomPlottable.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            writer.flush();
        } catch (IOException ex) {
            Logger.getLogger(PolynomPlottable.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(PolynomPlottable.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return scriptPath;
        
    }
    
}
