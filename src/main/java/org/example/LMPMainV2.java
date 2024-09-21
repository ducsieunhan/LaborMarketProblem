package org.example;

import org.moeaframework.core.Solution;

public class LMPMainV2 {
    public static void main(String[] args) {
        LaborMarketV2 laborMarket = new LaborMarketV2();

        int populationSize = 100;
        for (int i = 0; i < populationSize; i++) {
            System.out.println("Danh sách phân bổ nhân viên và công ty: ");
            Solution solution = laborMarket.newSolution();
            laborMarket.evaluate(solution);
        }
    }
}
