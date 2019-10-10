/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jrmouro.ufjf.dcc099.gitmining.mining.plot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ronaldo
 */
public abstract class DataScriptPlottable implements Plottable{
    
    final Path data;

    public DataScriptPlottable(Path data) {
        this.data = data;
    }
    
    @Override
    final public void plot() {
        
        Process process = null;
        try {
            process = Runtime.getRuntime().exec("gnuplot -p " + this.script(data).toString());
        } catch (IOException ex) {
            Logger.getLogger(DataScriptPlottable.class.getName()).log(Level.SEVERE, null, ex);
        }

        StringBuilder error = new StringBuilder();

        BufferedReader stdError = new BufferedReader(
                new InputStreamReader(process.getErrorStream()));

        String line;
        try {
            while ((line = stdError.readLine()) != null) {
                error.append(line).append("\n");
            }
        } catch (IOException ex) {
            Logger.getLogger(DataScriptPlottable.class.getName()).log(Level.SEVERE, null, ex);
        }

        int exitVal = 0;
        try {
            exitVal = process.waitFor();
        } catch (InterruptedException ex) {
            Logger.getLogger(DataScriptPlottable.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (exitVal == 0 && error.toString().length() > 0) {

            System.out.println("Error: " + error.toString());

        }
        
    }
   
    
}
