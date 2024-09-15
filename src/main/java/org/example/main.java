package org.example;

import org.moeaframework.Executor;
import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.core.Solution;

public class main {
    public static void main(String[] args) {
        int numEmployee = 10;
        int numCompany = 10;

        double[][] salary = {                   // tiền lương của môĩ nhân viên ứng với mỗi công ty
                {5000, 6000, 5500, 4000, 4800, 5200, 6200, 4000, 5300, 5400},
                {4800, 5500, 6000, 5000, 5200, 4800, 5500, 6000, 5000, 5200 },
                {6000, 5500, 4000, 6500, 5000, 5000, 6000, 5500, 4000, 4800},
                {5200, 6200, 4000, 5300, 5400, 6000, 5500, 4000, 6500, 5000},
                {4500, 5000, 5200, 4700, 5800, 5000, 6000, 5500, 4000, 4800},
                {5000, 6000, 5500, 4000, 4800, 5200, 6200, 4000, 5300, 5400},
                {4800, 5500, 6000, 5000, 5200, 4500, 5000, 5200, 4700, 5800},
                {6000, 5500, 4000, 6500, 5000, 4800, 5500, 6000, 5000, 5200},
                {5200, 6200, 4000, 5300, 5400, 4800, 5500, 6000, 5000, 5200},
                {4500, 5000, 5200, 4700, 5800, 5500, 5000, 5200, 4700, 5800}
        };
        // đơn vị là đô la


        NondominatedPopulation result = new Executor()
                .withProblemClass(LaborMarketProblem.class,numEmployee, numCompany, salary)
                .withAlgorithm("PESA2")
                .withMaxEvaluations(1000)
                .run();


        // output :
        for (Solution solution : result) {

            int[] employeeChoice = (int[]) solution.getAttribute("employeeChoice");
            for (int i = 0 ; i < employeeChoice.length; i++){
                System.out.println(i + " comes to " + employeeChoice[i]);   // in ra nhân viên nào vào công ty nào
            }
            System.out.println("Total salary of all employees get: " + (-solution.getObjective(0)) + "dolars");
            System.out.println("----------------------------------------");
        }
    }
}
