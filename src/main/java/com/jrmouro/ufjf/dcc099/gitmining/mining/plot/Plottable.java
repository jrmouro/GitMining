/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jrmouro.ufjf.dcc099.gitmining.mining.plot;

import java.nio.file.Path;

/**
 *
 * @author ronaldo
 */
public interface Plottable {
    public void plot();
    public Path script(Path data);
}
