package org.example.OOP_LMP;

import javax.swing.plaf.IconUIResource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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



    // Method to get the index of an element in an array
    public static int getIndex(int[] array, int element) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == element) {
                return i; // Return the index if the element is found
            }
        }
        return -1; // Return -1 if the element is not found
    }


    //từ điểm chuyển sang preflist
    //tiêu chuẩn phải là int[][] giống như data set ban đâu nhưng trả về String[][] để áp dụng đượcc vào thuật toán cũ lun, đỡ phải format lại
    //phần tử trong mảng score có giá trị nhỏ hơn(tức là có index nhỏ trong preference list đc quy là được ưu tiên hơn
    public static String[][] scoreToPref(int[][] scores, int setOrder){
        String[][] pref = new String[PADDING][PADDING];
        for (int i=0; i < PADDING; i++){
            int[] score = scores[i];
            int n = score.length;
            int count = 0;
            for (int k = 0; k < n ; k++) {
                int minIndex = k;  // Giả định phần tử nhỏ nhất là phần tử hiện tại
                // Tìm phần tử nhỏ nhất trong đoạn còn lại của mảng
                for (int j = 0; j < n; j++) {
                    if (score[j] < score[minIndex]) {
                        minIndex = j;  // Cập nhật vị trí của phần tử nhỏ nhất
                    }
                }
                score[minIndex]=999999999;
                pref[i][count]=POPULATION[PADDING *setOrder + minIndex];
                count++;
            }
        }
        return pref;
    }
    public static void main(String[] args) {

        //initalize person object
        Person[] people = new Person[SIZE];
        for (int i = 0; i < SIZE; i++) { //assign preference list
            int[] firstHalf = Arrays.copyOfRange(PREFERENCE_LIST[i], 0, PADDING);
            int[] secondHalf = Arrays.copyOfRange(PREFERENCE_LIST[i], PADDING, PADDING * 2);
            people[i] = new Person(POPULATION[i], firstHalf, secondHalf);
        }

        //gale shapley lan thu nhat
            //format lại để ap dụng thuật toán cũ (gale shapley)
        String[] setA = new String[PADDING];
        String[] setB = new String[PADDING];
        String[] setC = new String[PADDING];
            //vì  thuật toán cũ yêu cầu String[] mà object trả về int[] nên phải kởi tạo lại preflist
        String[][] AprefB = new String[PADDING][PADDING];
        String[][] BprefA = new String[PADDING][PADDING];


        for (int i = 0; i < PADDING; i++) {
            setA[i] = people[i].getName();
            setB[i] = people[PADDING + i].getName();
            setC[i] = people[PADDING * 2+ i].getName();
            //khởi tạo lại pref list A và B
            String[] stringArrayA = new String[PADDING];
                // Vòng lặp qua từng phần tử để gán chỉ số vào mảng String
            for (int j = 0; j < PADDING; j++) {
                stringArrayA[j] = POPULATION[people[i].getPrefForFirstSet()[j]]; // Gán phần tử tương ứng
            }
            AprefB[i] = stringArrayA;

            String[] stringArrayB = new String[PADDING];
                // Vòng lặp qua từng phần tử để gán chỉ số vào mảng String
            for (int j = 0; j < PADDING; j++) {
                stringArrayB[j] = POPULATION[people[PADDING*2+i].getPrefForFirstSet()[j]]; // Gán phần tử tương ứng
            }
            BprefA[i] = stringArrayB;
        }

        //first match
        GaleShapley matching = new GaleShapley(setA, setB, AprefB, BprefA);
        String[][] firstMatching = matching.calcMatches();
            //assign first matched to B person object
        for (int i = 0; i<PADDING; i++){
            people[PADDING+i].setFirstMatch(Arrays.asList(POPULATION).indexOf(firstMatching[0][i]));  //int
        }
        /**
         * HAVEN'T DONE: assign matched to A person object
         */

        //combine preference
        //kết hợp pref list của A và B trong mỗi cặp vừa ghép để thành pref list của cặp với setC
        //trong bài này sẽ lấy phần tử B đại diện luôn cho cặp đã ghép
        //xét index của mỗi phần tử C trong 2 mảng pref của A B để tính điểm, => scores[][]
        //chuyển scores thành pref list của cặp với C (ABprefC)

            //tinh điểm
        int[][] scores = new int[PADDING][PADDING];
        for (int i = 0; i<PADDING;i++){
            int[] BprefC = people[PADDING + i].getPrefForSecondSet();
            int[] AprefC = people[people[PADDING+i].getFirstMatch()].getPrefForSecondSet();
            for (int j = 0; j<PADDING;j++){
                //tinh diem
                scores[i][j]= getIndex(BprefC, PADDING*2 + j);
                scores[i][j]+= getIndex(AprefC, PADDING*2 + j);
            }
        }

            //từ điểm chuyển sang preflist //ABprefC
        String[][] ABprefC = scoreToPref(scores, 2);

         //ket hợp pref list của C với A và B thành 1 pref list với cặp A-B đã ghép trước đo
        //xét index của A và B của cặp đã ghép trong 2 pref list của C để tính điểm => scores2[][]
        //chuyển scores2 thành pref list của C với cặp ( CprefAB )
            //calculate score
        int[][] scores2 = new int[PADDING][PADDING];
        for (int i = 0; i<PADDING;i++){
            int[] CprefA = people[PADDING*2 + i].prefForFirstSet;
            int[] CprefB = people[PADDING*2 + i].prefForSecondSet;
            int[] score2 = scores2[i];
            for (int j = 0; j<PADDING;j++){
                //tinh diem
                score2[j]= getIndex(CprefB, PADDING + j);
                score2[j]+= getIndex(CprefA, people[PADDING + j].getFirstMatch());
            }
        }
            //score to preference
        String[][] CprefAB  = scoreToPref(scores2,1);
            //actually B now will represent the first match A-B,
            // so it can be think like CprefB-new

        //second match
        GaleShapley matching2 = new GaleShapley(setC, setB, CprefAB, ABprefC);
        String[][] seccondMatching = matching2.calcMatches();
        //assign to set B
        for (int i = 0; i<PADDING; i++) {
            people[PADDING + i].setSecondMatch(Arrays.asList(POPULATION).indexOf(seccondMatching[0][i]));  //int
        }

        /**
         * In ra kết quả thui nè !!!!!!!
         */
        String[][] tripletMatchResult = new String[PADDING][3];
        for (int i = 0; i<PADDING; i++){
            String[] tripletMatch = tripletMatchResult[i];
            tripletMatch[0]= POPULATION[people[PADDING+i].getFirstMatch()];
            tripletMatch[1]= POPULATION[PADDING+i];
            tripletMatch[2]= POPULATION[people[PADDING+i].getSecondMatch()];
            System.out.println(Arrays.toString(tripletMatch));
        }
    }
}
