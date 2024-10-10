package org.example;

import org.example.LMPV2.GaleShapley;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TestGaleShapley {
    public static void main(String[] args) {
        String[] employee = { "E1", "E2","E3", "E4","E5","E6", "E7", "E8", "E9", "E10"};

        final String[] company = {"C1", "C2", "C3", "C4", "C5","C6", "C7",  "C8", "C9", "C10"};

//        String[][] employeePref = {
//                {"C9", "C2", "C5", "C10", "C3", "C6", "C8", "C1", "C4", "C7"},
//                {"C5", "C3", "C7", "C1", "C2", "C9", "C10", "C6", "C4", "C8"},
//                {"C4", "C8", "C1", "C7", "C3", "C10", "C2", "C9", "C6", "C5"},
//                {"C3", "C1", "C4", "C5", "C8", "C9", "C7", "C6", "C10", "C2"},
//                {"C7", "C9", "C6", "C2", "C4", "C1", "C8", "C10", "C3", "C5"},
//                {"C6", "C2", "C9", "C5", "C3", "C4", "C1", "C7", "C10", "C8"},
//                {"C10", "C4", "C2", "C6", "C9", "C8", "C5", "C3", "C7", "C1"},
//                {"C8", "C6", "C5", "C3", "C2", "C7", "C4", "C1", "C10", "C9"},
//                {"C1", "C5", "C8", "C10", "C7", "C9", "C6", "C2", "C4", "C3"},
//                {"C2", "C10", "C3", "C6", "C5", "C4", "C1", "C9", "C7", "C8"}
//        };
        String[][] employeePref = {
                {"C9", "C2", "C5", "C10", "C3", "C6", "C8", "C1", "C4", "C7"},
                {"C9", "C2", "C5", "C10", "C3", "C6", "C8", "C1", "C4", "C7"},
                {"C9", "C2", "C5", "C10", "C3", "C6", "C8", "C1", "C4", "C7"},
                {"C9", "C2", "C5", "C10", "C3", "C6", "C8", "C1", "C4", "C7"},
                {"C9", "C2", "C5", "C10", "C3", "C6", "C8", "C1", "C4", "C7"},
                {"C9", "C2", "C5", "C10", "C3", "C6", "C8", "C1", "C4", "C7"},
                {"C9", "C2", "C5", "C10", "C3", "C6", "C8", "C1", "C4", "C7"},
                {"C9", "C2", "C5", "C10", "C3", "C6", "C8", "C1", "C4", "C7"},
                {"C9", "C2", "C5", "C10", "C3", "C6", "C8", "C1", "C4", "C7"},
                {"C9", "C2", "C5", "C10", "C3", "C6", "C8", "C1", "C4", "C7"}
        };

        final String[][] companyPref = {
                {"E5", "E1", "E3", "E8", "E7", "E2", "E10", "E4", "E9", "E6"},
                {"E5", "E1", "E3", "E8", "E7", "E2", "E10", "E4", "E9", "E6"},
                {"E5", "E1", "E3", "E8", "E7", "E2", "E10", "E4", "E9", "E6"},
                {"E5", "E1", "E3", "E8", "E7", "E2", "E10", "E4", "E9", "E6"},
                {"E5", "E1", "E3", "E8", "E7", "E2", "E10", "E4", "E9", "E6"},
                {"E5", "E1", "E3", "E8", "E7", "E2", "E10", "E4", "E9", "E6"},
                {"E5", "E1", "E3", "E8", "E7", "E2", "E10", "E4", "E9", "E6"},
                {"E5", "E1", "E3", "E8", "E7", "E2", "E10", "E4", "E9", "E6"},
                {"E5", "E1", "E3", "E8", "E7", "E2", "E10", "E4", "E9", "E6"},
                {"E5", "E1", "E3", "E8", "E7", "E2", "E10", "E4", "E9", "E6"}
        };
//        final String[][] companyPref = {
//                {"E5", "E1", "E3", "E8", "E7", "E2", "E10", "E4", "E9", "E6"},
//                {"E2", "E10", "E9", "E6", "E4", "E3", "E1", "E5", "E8", "E7"},
//                {"E6", "E8", "E4", "E7", "E10", "E1", "E3", "E9", "E2", "E5"},
//                {"E3", "E2", "E5", "E4", "E1", "E6", "E9", "E8", "E10", "E7"},
//                {"E7", "E9", "E6", "E10", "E8", "E2", "E5", "E3", "E1", "E4"},
//                {"E8", "E3", "E10", "E2", "E5", "E4", "E6", "E1", "E7", "E9"},
//                {"E9", "E4", "E1", "E5", "E3", "E10", "E2", "E8", "E6", "E7"},
//                {"E10", "E6", "E7", "E3", "E1", "E9", "E5", "E2", "E4", "E8"},
//                {"E1", "E4", "E2", "E8", "E9", "E3", "E10", "E6", "E7", "E5"},
//                {"E4", "E7", "E2", "E9", "E5", "E8", "E1", "E10", "E3", "E6"}
//        };
        // Tạo một mảng chỉ số cho hàng
        Integer[] indices = new Integer[employee.length];
        for (int i = 0; i < indices.length; i++) {
            indices[i] = i;
        }

        // Xáo trộn mảng chỉ số
        List<Integer> indexList = Arrays.asList(indices);
        Collections.shuffle(indexList);

        // Tạo các mảng mới dựa trên chỉ số đã xáo trộn
        String[] shuffledArray1 = new String[employee.length];
        String[][] shuffledEmployeePref = new String[employeePref.length][employeePref[0].length];

        for (int i = 0; i < indices.length; i++) {
            shuffledArray1[i] = employee[indexList.get(i)];
            shuffledEmployeePref[i] = employeePref[indexList.get(i)];
        }
        employee = shuffledArray1;
        employeePref = shuffledEmployeePref;

        // Xuất mảng đã xáo trộn
        System.out.println("Shuffled array1: " + Arrays.toString(shuffledArray1));
        System.out.println("Shuffled employeePref (rows shuffled):");
        for (String[] row : shuffledEmployeePref) {
            System.out.println(Arrays.toString(row));
        }


        GaleShapley matching = new GaleShapley(employee, company, employeePref, companyPref);
        // truyền vào danh sách nhân viên cùng vơí prefer list đã được hoán vị
        String[][] finalMatch = matching.calcMatches();
        System.out.println("result");
        System.out.println(Arrays.toString(finalMatch[0]));
        System.out.println(Arrays.toString(finalMatch[1]));
    }
}
