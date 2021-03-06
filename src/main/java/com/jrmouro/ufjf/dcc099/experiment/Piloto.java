/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jrmouro.ufjf.dcc099.experiment;


import com.jrmouro.genetic.chromosome.ChromosomeAbstract;
import com.jrmouro.genetic.chromosome.ChromosomeDouble;
import com.jrmouro.genetic.evolutionstrategies.chromosome.ChromosomeOne;
import com.jrmouro.genetic.evolutionstrategies.evolution.EvolutionScoutSniffer;
import com.jrmouro.genetic.fitnessfunction.FitnessFunction;
import com.jrmouro.ufjf.dcc099.gitmining.project.Project;
import com.jrmouro.ufjf.dcc099.gitmining.canonicalPath.CanonicalPath;
import com.jrmouro.ufjf.dcc099.gitmining.similarity.FactorySimilarytyFunction;
import com.jrmouro.ufjf.dcc099.gitmining.similarity.LinearSystemSimilarityEquation;
import com.jrmouro.ufjf.dcc099.gitmining.similarity.ParamClassFunction;
import com.jrmouro.ufjf.dcc099.gitmining.similarity.SimilarityEquation;
import com.jrmouro.ufjf.dcc099.gitmining.similarity.SimilarityFunction;
import com.jrmouro.ufjf.dcc099.gitmining.similarity.WeightFunction;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.parser.ParseException;

/**
 *
 * @author ronaldo
 */
public class Piloto implements Experiment {

    private final List<ParamClassFunction> paramclassFunctions;
    public final List<Project> projectRef;
    public final Project project;
    private LinearSystemSimilarityEquation lsse;
    private final double fatorNormalizedDiffs;

    public Piloto(List<ParamClassFunction> paramclassFunctions, List<Project> projectRef, Project project, double fatorNormalizedDiffs) {
        this.paramclassFunctions = paramclassFunctions;
        this.projectRef = projectRef;
        this.project = project;
        this.fatorNormalizedDiffs = fatorNormalizedDiffs;
    }

    public class Fitness implements FitnessFunction<Double> {

        private final LinearSystemSimilarityEquation lsse;

        public Fitness(LinearSystemSimilarityEquation lsse) {
            this.lsse = lsse;
        }

        @Override
        public double fitness(ChromosomeAbstract<Double> ca) {
            
            lsse.setWeights(ca.getRepresentation());

            return Math.abs(lsse.getValue());
        }

        
        


    }

    @Override
    public void run() {
        try {

            //prepara os diretórios temporários para clonagem
            CanonicalPath.deleteDir("temp");
            CanonicalPath.createDir("temp");
            final Path projRef = Paths.get(CanonicalPath.getPath("temp").toString() + "/projRef");
            project.clonePath = Paths.get(CanonicalPath.getPath("temp").toString() + "/proj");
            CanonicalPath.createDir(projRef);

            //Minera os repositórios
            project.mine(true, fatorNormalizedDiffs);

            int i = 1;
            for (Project p : this.projectRef) {
                p.clonePath = Paths.get(projRef.toString(), "ref" + String.valueOf(i++));
                p.mine(true, fatorNormalizedDiffs);
            }

            try {
                // Gera o Sistema de Equações de Similaridade

                lsse = Piloto.getLSSE(projectRef, paramclassFunctions);
                
                System.out.println(lsse);

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
            
            Fitness fitness = new Fitness(lsse);
            

            // um cromossomo inicial
            ChromosomeDouble c = new ChromosomeOne(lsse.getWeights(), fitness, 0.1);

            c = (ChromosomeDouble) new EvolutionScoutSniffer(100, 0.001).evolve(c, 100, false);

            SimilarityEquation res = getSE(project, paramclassFunctions);

            res.setWeights(c.getRepresentation());

            System.out.println("weights: " + c);

            System.out.println("similarity: " + res.getValue());

        } catch (IOException ex) {
            Logger.getLogger(Piloto.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(Piloto.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Piloto.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParseException ex) {
            Logger.getLogger(Piloto.class.getName()).log(Level.SEVERE, null, ex);
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
    }

    public static void main(String[] args) throws MalformedURLException, ClassNotFoundException, IOException, InterruptedException, ParseException {

        
        String deletions = "com.jrmouro.ufjf.dcc099.gitmining.similarity.functions.DeletionsSimilarityFunction";
        String changedFiles = "com.jrmouro.ufjf.dcc099.gitmining.similarity.functions.ChangedFilesSimilarityFunction";
        String insertions = "com.jrmouro.ufjf.dcc099.gitmining.similarity.functions.InsertionsSimilarityFunction";
        
        List<ParamClassFunction> funcoes = new ArrayList();       
        
        
        for(double d = 0.2; d < 1.0; d = d + 0.2){
            funcoes.add(new ParamClassFunction(Class.forName(deletions), d));
            funcoes.add(new ParamClassFunction(Class.forName(insertions), d));
            funcoes.add(new ParamClassFunction(Class.forName(changedFiles), d));
        }
        
        //Projetos de referência
        URL url1 = new URL("https://api.github.com/repos/jrmouro/chatProject");
        URL url2 = new URL("https://api.github.com/repos/jrmouro/provo");
        URL url3 = new URL("https://api.github.com/repos/jrmouro/genetic");

        List<Project> projectList = new ArrayList();
        projectList.add(new Project(url1, 1.0, false, 10.0));
        projectList.add(new Project(url2, 1.0, false, 10.0));
        projectList.add(new Project(url3, 1.0, false, 10.0));

        //Projeto a ser analisado
        URL gitMining = new URL("https://api.github.com/repos/jrmouro/GitMining");

        //Experimento "Piloto"
        Piloto piloto = new Piloto(funcoes, projectList, new Project(gitMining, false, 10.0), 5.0);

        piloto.run();

    }

    //funções auxiliares
    private static LinearSystemSimilarityEquation getLSSE(List<Project> projects, List<ParamClassFunction> functions) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

        LinearSystemSimilarityEquation lsse = new LinearSystemSimilarityEquation();

        for (Project p : projects) {
            lsse.add(Piloto.getSE(p, functions));
        }

        return lsse;

    }

    private static SimilarityEquation getSE(Project project, List<ParamClassFunction> functions) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

        SimilarityEquation ret = new SimilarityEquation(project.result);

        String pack = "com.jrmouro.ufjf.dcc099.gitmining.similarity.";

        for (ParamClassFunction function : functions) {
            SimilarityFunction sf = FactorySimilarytyFunction.getSimilarityFunction(function, project);
            if (sf != null) {
                ret.add(new WeightFunction(sf));
            }
        }

        return ret;
    }

}
