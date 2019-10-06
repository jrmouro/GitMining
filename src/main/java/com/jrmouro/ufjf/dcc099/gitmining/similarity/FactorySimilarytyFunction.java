/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jrmouro.ufjf.dcc099.gitmining.similarity;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ronaldo
 */
public class FactorySimilarytyFunction {

    public static SimilarityFunction getSimilarityFunction(ParamClassFunction paramClassFunction, Path path) {
        try {
            return getSimilarityFunction(paramClassFunction.getClassFunction(), paramClassFunction.getParam(), path);
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

    public static SimilarityFunction getSimilarityFunction(ParamClassFunction paramClassFunction, URL url) {
        try {
            return getSimilarityFunction(paramClassFunction.getClassFunction(), paramClassFunction.getParam(), url);
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

    public static SimilarityFunction getSimilarityFunction(ParamClassFunction paramClassFunction, URL url, Path path) {
        try {
            return getSimilarityFunction(paramClassFunction.getClassFunction(), paramClassFunction.getParam(), url, path);
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

    public static SimilarityFunction getSimilarityFunction(Class classFunction, Object param, Path path) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

        String pack = "com.jrmouro.ufjf.dcc099.gitmining.similarity.";

        if (classFunction.getSuperclass() == Class.forName(pack + "RepositorySimilarityFunction") && path != null) {
            return get(classFunction, param, path);
        }

        return null;
    }

    public static SimilarityFunction getSimilarityFunction(Class classFunction, Object param, URL url) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

        String pack = "com.jrmouro.ufjf.dcc099.gitmining.similarity.";

        if (classFunction.getSuperclass() == Class.forName(pack + "RemoteSimilarityFunction") && url != null) {
            return get(classFunction, param, url);
        }

        return null;

    }

    public static SimilarityFunction getSimilarityFunction(Class classFunction, Object param, URL url, Path path) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

        String pack = "com.jrmouro.ufjf.dcc099.gitmining.similarity.";

        if (classFunction.getSuperclass() == Class.forName(pack + "RepositorySimilarityFunction") && path != null) {
            return get(classFunction, param, path);
        } else if (classFunction.getSuperclass() == Class.forName(pack + "RemoteSimilarityFunction") && url != null) {
            return get(classFunction, param, url);
        } else if (classFunction.getSuperclass() == Class.forName(pack + "RemoteRepositorySimilarityFunction") && path != null && url != null) {
            return get(classFunction, param, url, path);
        }

        return null;

    }

    private static SimilarityFunction get(Class classFunction, Object param, URL url) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

        Class[] cArg = new Class[2];
        cArg[0] = URL.class;
        cArg[1] = Object.class;
        Constructor ct = classFunction.getDeclaredConstructor(cArg);
        ct.setAccessible(true);

        return (SimilarityFunction) ct.newInstance(url, param);

    }

    private static SimilarityFunction get(Class classFunction, Object param, Path path) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

        Class[] cArg = new Class[2];
        cArg[0] = Path.class;
        cArg[1] = Object.class;
        Constructor ct = classFunction.getDeclaredConstructor(cArg);
        ct.setAccessible(true);

        return (SimilarityFunction) ct.newInstance(path, param);
    }

    private static SimilarityFunction get(Class classFunction, Object param, URL url, Path path) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

        Class[] cArg = new Class[3];
        cArg[0] = URL.class;
        cArg[1] = Path.class;
        cArg[2] = Object.class;
        Constructor ct = classFunction.getDeclaredConstructor(cArg);
        ct.setAccessible(true);

        return (SimilarityFunction) ct.newInstance(url, path, param);
    }

}
