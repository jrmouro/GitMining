/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jrmouro.ufjf.dcc099.gitmining.similarity;

import com.jrmouro.ufjf.dcc099.gitmining.project.Project;

/**
 *
 * @author ronaldo
 */
public abstract class ProjectSimilarityFunction  implements SimilarityFunction{

    final protected Project project;
    final private Object param;

    public ProjectSimilarityFunction(Project project, Object param) {
        this.project = project;
        this.param = param;
    }

    @Override
    public Object getParam() {
        return this.param;
    }

    @Override
    public Project getProject() {
        return this.project;
    }
    
}
