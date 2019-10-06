/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jrmouro.ufjf.dcc099.gitmining.similarity;

/**
 *
 * @author ronaldo
 */
public class ParamClassFunction {

    final Object param;

    final Class classFunction;

    public ParamClassFunction(Object param, Class classFunction) {
        this.param = param;
        this.classFunction = classFunction;
    }

    public Object getParam() {
        return param;
    }

    public Class getClassFunction() {
        return classFunction;
    }
}
