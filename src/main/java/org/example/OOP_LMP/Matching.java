package org.example.OOP_LMP;

import java.util.Arrays;

public class Matching {
    final static int PADDING = 5;  //padding
    final static int SIZE = 15;
    final static String[] POPULATION = {"A1", "A2", "A3", "A4", "A5", "B1", "B2", "B3", "B4", "B5", "C1", "C2", "C3", "C4", "C5"};
    final static int[][] PREFERENCE_LIST =
            {
                    /*0*/ {6,5,7,9,8,  12,10,11,14,13},
                    /*1*/ {6,7,5,9,8,  10,11,14,12,13},
                    /*2*/ {7,5,6,8,9,  14,11,12,10,13},
                    /*3*/ {5,7,8,6,9,  11,14,12,13,10},
                    /*4*/ {5,8,9,6,7,  12,13,11,10,14},

                    /*5*/ {1,3,2,4,0,  13,12,14,11,10},
                    /*6*/ {1,4,2,0,3,  10,11,12,13,14},
                    /*7*/ {1,2,3,4,0,  12,11,13,10,14},
                    /*8*/ {0,1,2,3,4,  11,14,12,10,13},
                    /*9*/ {2,3,1,0,4,  12,10,11,13,14},

                    /*10*/ {1,3,0,2,4,  5,7,9,6,8},
                    /*11*/ {0,1,3,2,4,  6,7,5,9,8},
                    /*12*/ {1,2,0,4,3,  7,5,6,8,9},
                    /*13*/ {3,0,1,4,2,  5,6,8,7,9},
                    /*14*/ {2,3,1,0,4,  7,5,9,8,6}
            };

    public static void main(String[] args) {

        //initalize person object
        Person[] people = new Person[SIZE];
        for (int i = 0; i < SIZE; i++){
            int[] firstHalf = Arrays.copyOfRange(PREFERENCE_LIST[i], 0, PADDING);
            int[] secondHalf = Arrays.copyOfRange(PREFERENCE_LIST[i], PADDING, PADDING*2);
            people[i]= new Person(POPULATION[i], firstHalf, secondHalf );
        }

        //gale shapley lan thu nhat
        //format lại để ap dụng thuật toán cũ
        String[] setA = new String[PADDING];
        String[] setB = new String[PADDING];
        String[][] Apref = new String[PADDING][PADDING];
        String[][] Bpref = new String[PADDING][PADDING];
        for (int i = 0; i<PADDING; i++){
            setA[i]= people[i].getName();
            setB[i]= people[PADDING + i].getName();

            //vì object trả về int[] mà thuật toán cũ yêu cầu String[]
            String[] stringArrayA = new String[PADDING];
            // Vòng lặp qua từng phần tử để gán chỉ số vào mảng String
            for (int j = 0; j < PADDING; j++) {
                stringArrayA[j] = POPULATION[people[i].getPrefForFirstSet()[j]]; // Gán phần tử tương ứng
            }
            Apref[i] = stringArrayA;

            String[] stringArrayB = new String[PADDING];
            // Vòng lặp qua từng phần tử để gán chỉ số vào mảng String
            for (int j = 0; j < PADDING; j++) {
                stringArrayB[j] = POPULATION[people[i].getPrefForFirstSet()[j]]; // Gán phần tử tương ứng
            }
            Bpref[i] = stringArrayB;
        }
        GaleShapley matching = new GaleShapley(setA, setB, Apref, Bpref);
        String[][] firstMatching = matching.calcMatches() ;
        System.out.println(Arrays.deepToString(firstMatching));
    }
}
