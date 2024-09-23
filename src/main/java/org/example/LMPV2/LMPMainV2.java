package org.example.LMPV2;

import org.example.LMPV1.LaborMarketProblem;
import org.example.LMPV2.LaborMarketV2;
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
            System.out.println("Total satisfy both side: " + -solution.getObjective(0));
            System.out.println("Matched pairs:");
            String[][] matches = (String[][]) solution.getAttribute("matches");
            for (int i = 0; i < matches[0].length; i++) {
                System.out.println(matches[0][i] + " -> " + matches[1][i]);
            }
            System.out.println("------------------------------------------");
        }
    }
}


