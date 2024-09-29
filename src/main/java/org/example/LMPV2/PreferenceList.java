package org.example.LMPV2;

import java.util.Arrays;

public class PreferenceList {


    public static String[][] to(String nameOfSet, double[][] propertyOfSet ){
        int size = propertyOfSet.length;
        String[][] a = new String[size][size];
        for (int i =0; i < size; i++){
            double[] each = propertyOfSet[i].clone();
            for (int j=0; j<each.length; j++){
                double max = each[0];
                int position = 0;
                for (int k=0; k< each.length; k++){
                    if (each[k]>=max){
                        max = each[k];
                        position = k;

                    }
                }

                a[i][j] = nameOfSet + (position+1);
                each[position] = 0;
            }
        }
        return a;
    }
    public static void main(String[] args) {
        double[][] salary = {
                //C1 , C2  , C3  , C4  , C5
                /*E1*/{4500, 6000, 3000, 4000, 4800},
                /*E2*/{4800, 5500, 6000, 2000, 5200},
                /*E3*/{6000, 3000, 4000, 6500, 2500},
                /*E4*/{5200, 6200, 4000, 3500, 5400},
                /*E5*/{4500, 5000, 5200, 4700, 5800}
        };
        String[][] employeePref = PreferenceList.to("company" , salary);
        System.out.println(Arrays.deepToString(employeePref));
    }
}