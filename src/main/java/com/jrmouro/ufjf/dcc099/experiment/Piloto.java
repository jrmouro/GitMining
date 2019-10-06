/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jrmouro.ufjf.dcc099.experiment;


import com.jrmouro.genetic.evolutionstrategies.chromosome.ChromosomeAbstract;
import com.jrmouro.genetic.evolutionstrategies.chromosome.ChromosomeDouble;
import com.jrmouro.genetic.evolutionstrategies.chromosome.ChromosomeOne;
import com.jrmouro.genetic.evolutionstrategies.evolution.EvolutionScoutSniffer;
import com.jrmouro.genetic.evolutionstrategies.fitnessfunction.FitnessFunction;
import com.jrmouro.ufjf.dcc099.gitmining.similarity.FactorySimilarytyFunction;
import com.jrmouro.ufjf.dcc099.gitmining.similarity.LinearSystemSimilarityEquation;
import com.jrmouro.ufjf.dcc099.gitmining.similarity.ParamClassFunction;
import com.jrmouro.ufjf.dcc099.gitmining.similarity.SimilarityEquation;
import com.jrmouro.ufjf.dcc099.gitmining.similarity.SimilarityFunction;
import com.jrmouro.ufjf.dcc099.gitmining.similarity.WeightFunction;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author ronaldo
 */
public class Piloto implements Experiment{
    
    private final List<ParamClassFunction> paramclassFunctions;
    public final List<Project> projectRef;
    public final Project project;
    private LinearSystemSimilarityEquation lsse;

    public Piloto(List<ParamClassFunction> paramclassFunctions, List<Project> projectRef, Project project) {
        this.paramclassFunctions = paramclassFunctions;
        this.projectRef = projectRef;
        this.project = project;
    }
        
    

    @Override
    public void run() {
        try {
            
            //prepara os diretórios temporários para clonagem
            Piloto.deleteDir("temp");
            Piloto.createDir("temp");
            final Path projRef = Paths.get(Piloto.getPathFromCanonical("temp").toString() + "/projRef");
            final Path proj = Paths.get(Piloto.getPathFromCanonical("temp").toString() + "/proj");
            Piloto.createDir(projRef);
            
            
            
            //Clona os repositórios
            int i = 1;
            for (Project p : this.projectRef){      
                Path path = Piloto.getPath(projRef, "ref" + String.valueOf(i++));
                p.path = path;
                Piloto.gitCloneRepository(p.url, p.path);
            }
             
            
            Piloto.gitCloneRepository(this.project.url, proj);
            this.project.path = proj;
            
            
            try {
                // Gera o Sistema de Equações de Similaridade

                lsse = Piloto.getLSSE(projectRef, paramclassFunctions);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Piloto.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NoSuchMethodException ex) {
                Logger.getLogger(Piloto.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InstantiationException ex) {
                Logger.getLogger(Piloto.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(Piloto.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvocationTargetException ex) {
                Logger.getLogger(Piloto.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            // FitnessFunction
            
            FitnessFunction<Double> fitness = new FitnessFunction<Double>(){ 
                @Override
                public Double fitness(ChromosomeAbstract<Double> chromosome) {
                    
                    lsse.setWeights(chromosome.getRepresentation());
                    
                    return Math.abs(lsse.getValue());
                    
                }
            
            } ;    
            
            // um cromossomo inicial
            
            ChromosomeDouble c = new ChromosomeOne(lsse.getWeights(), fitness, 0.1);
            
            c = (ChromosomeDouble) new EvolutionScoutSniffer(100, 0.001).evolve(c, 100, false);
            
            System.out.println(c);
            
            
        } catch (IOException ex) {
            Logger.getLogger(Piloto.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Piloto.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(Piloto.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void main(String[] args) throws MalformedURLException, ClassNotFoundException {
        
        //Funções de Sililaridade
        
        String mergeConflicts = "com.jrmouro.ufjf.dcc099.gitmining.similarity.functions.MergeConflictsSimilarityFunction";
        
        String languages = "com.jrmouro.ufjf.dcc099.gitmining.similarity.functions.LanguagesSimilarityFunction";
        List<String> languagesParam = new ArrayList();
        languagesParam.add("C");
        languagesParam.add("Python");
        
        List<ParamClassFunction> funcoes = new ArrayList();
        funcoes.add(new ParamClassFunction(null, Class.forName(mergeConflicts)));
        funcoes.add(new ParamClassFunction(languagesParam, Class.forName(languages)));
        
        //Projetos de referência
        
        URL spark = new URL("https://github.com/apache/spark");
        URL gson = new URL("https://github.com/google/gson");        
        URL image = new URL("https://github.com/google/image-compression");
        
        List<Project> projectList = new ArrayList();
        //projectList.add(new Project(spark, 1.0)); //comentei pq é um projeto grande e demora baixar
        projectList.add(new Project(gson, 0.8));
        projectList.add(new Project(image, 0.7));
        
        
        
        //Projeto a ser analisado
        
        URL gitMining = new URL("https://github.com/jrmouro/GitMining");
        
        
        //Experimento "Piloto"
        
        Piloto piloto = new Piloto(funcoes, projectList, new Project(gitMining));
        piloto.run();
        
        
    }
    
        
    //funções auxiliares
    
    private static LinearSystemSimilarityEquation getLSSE(List<Project> projects, List<ParamClassFunction> functions) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
        
        LinearSystemSimilarityEquation lsse = new LinearSystemSimilarityEquation();
                
        for (Project p : projects) {
            lsse.add(Piloto.getSE(p.result, p.url, p.path, functions));
        }
        
        return lsse;
        
    }
    
    private static SimilarityEquation getSE(double b, URL url, Path path, List<ParamClassFunction> functions) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
        
        SimilarityEquation ret = new SimilarityEquation(b); 
        
        String pack = "com.jrmouro.ufjf.dcc099.gitmining.similarity.";
        
        for (ParamClassFunction function : functions) {
            SimilarityFunction sf = FactorySimilarytyFunction.getSimilarityFunction(function, url, path);
            if(sf != null)
                ret.add(new WeightFunction(sf));
        }
        
        return ret;
    }
    
        
    static public Path getPathFromCanonical(String name) throws IOException {
        String aux = new File(".").getCanonicalPath();
        return Paths.get(aux + "/" + name);
    }
    
    static public Path getPath(Path path, String name) throws IOException {
        return Paths.get(path.toString() + "/" + name);
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
    
    static public void gitCloneRepository(URL url, Path pathDir) throws IOException, InterruptedException {

        Process process = Runtime.getRuntime().exec("git clone " + url.toString() + " " + pathDir.toString());

        StringBuilder output = new StringBuilder();
        StringBuilder error = new StringBuilder();

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()));

        BufferedReader stdError = new BufferedReader(
                new InputStreamReader(process.getErrorStream()));

        String line;
        while ((line = reader.readLine()) != null) {
            output.append(line + "\n");
        }

        while ((line = stdError.readLine()) != null) {
            error.append(line + "\n");
        }

        int exitVal = process.waitFor();

        if (exitVal == 0 && error.toString().length() > 0) {

            System.out.println("Info: " + error.toString());
            System.out.println("Output: " + output.toString());

        }

    }
    
}
