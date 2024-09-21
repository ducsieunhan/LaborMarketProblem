package org.example;

import org.moeaframework.Executor;
import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.core.Solution;

public class LMPMainV2 {
    public static void main(String[] args) {
        // Create an instance of the problem
        LaborMarketV2 problem = new LaborMarketV2();

        // Run the optimization algorithm
        NondominatedPopulation result = new Executor()
                .withProblem(problem)
                .withAlgorithm("NSGAII")
                .withMaxEvaluations(100)
                .run();

        // Output the results


        for (Solution solution : result) {
            System.out.println("Objective: " + -solution.getObjective(0));
        }
    }
}
