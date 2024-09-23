package org.example;

import org.moeaframework.Executor;
import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.core.Solution;

public class LMPMainV2 {
    public static void main(String[] args) {
        LaborMarketV2 problem = new LaborMarketV2();

        NondominatedPopulation result = new Executor()
                .withProblem(problem)
                .withAlgorithm("NSGAII")
                .withMaxEvaluations(100)
                .run();


        for (Solution solution : result) {
            System.out.println("Objective: " + solution.getObjective(0));
        }
    }
}