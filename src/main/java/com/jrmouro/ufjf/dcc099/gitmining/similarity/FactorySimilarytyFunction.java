/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jrmouro.ufjf.dcc099.gitmining.similarity;

import com.jrmouro.ufjf.dcc099.gitmining.project.Project;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ronaldo
 */
public class FactorySimilarytyFunction {
    
    public static SimilarityFunction getSimilarityFunction(ParamClassFunction paramClassFunction, Project project) {
        try {
            return getSimilarityFunction(paramClassFunction.getClassFunction(), paramClassFunction.getParam(), project);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(FactorySimilarytyFunction.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(FactorySimilarytyFunction.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(FactorySimilarytyFunction.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(FactorySimilarytyFunction.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(FactorySimilarytyFunction.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvocationTargetException ex) {
            Logger.getLogger(FactorySimilarytyFunction.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

   
    
    public static SimilarityFunction getSimilarityFunction(Class classFunction, Object param, Project project) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

        String pack = "com.jrmouro.ufjf.dcc099.gitmining.similarity.";

        if (classFunction.getSuperclass() == Class.forName(pack + "ProjectSimilarityFunction") && project != null) {
            return get(classFunction, param, project);
        }

        return null;
    }

    
    private static SimilarityFunction get(Class classFunction, Object param, Project project) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

        Class[] cArg = new Class[2];
        cArg[0] = Project.class;
        cArg[1] = Object.class;
        Constructor ct = classFunction.getDeclaredConstructor(cArg);
        ct.setAccessible(true);

        return (SimilarityFunction) ct.newInstance(project, param);

    }

}
