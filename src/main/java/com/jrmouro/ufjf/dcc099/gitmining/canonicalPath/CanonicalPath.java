/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jrmouro.ufjf.dcc099.gitmining.canonicalPath;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author ronaldo
 */
public class CanonicalPath {
    
        
    static public String createTempDir() throws IOException {

        return createDir("temp");

    }

    static public String createDir(String name) throws IOException {

        String aux = new File(".").getCanonicalPath();

        Path baseTempPath = Paths.get(aux + "/" + name);

        return createDir(baseTempPath);

    }

    static public String createDir(Path path) throws IOException {

        if (!Files.exists(path)) {
            File file = new File(path.toString());
            boolean result = file.mkdirs();
            if (result) {
                return file.getAbsolutePath();
            }
        }

        return null;

    }
    
    static public Path getPath(String name) throws IOException {
        String aux = new File(".").getCanonicalPath();
        return Paths.get(aux + "/" + name);
    }

    static public boolean deleteTempDir() throws IOException {

        return deleteDir("temp");
    }
    
    static public boolean deleteDir(String name) throws IOException {

        String aux = new File(".").getCanonicalPath();
        Path baseTempPath = Paths.get(aux + "/" + name);

        return deleteDir(baseTempPath);
    }

    static public boolean deleteDir(Path path) throws IOException {

        if (Files.exists(path)) {
            FileUtils.deleteDirectory(new File(path.toString()));
            return true;
        }

        return false;
    }
    
}
