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
public abstract class ScriptPlottable extends DataScriptPlottable{

    public ScriptPlottable() {
        super(null);
    }
    
    abstract public Path script();
    
    @Override
    public abstract String title();
    

    @Override
    final public Path script(Path data) {
        return this.script();
    }

    
    
}
