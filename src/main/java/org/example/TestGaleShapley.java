package org.example;

import org.example.LMPV2.GaleShapley;

import java.util.Arrays;

public class TestGaleShapley {
    public static void main(String[] args) {
        final String[] employee = { "E1", "E2","E3", "E4","E5"};

        final String[] company = {"C1", "C2", "C3", "C4", "C5"};

        final String[][] employeePref = {

                {"C1", "C4", "C5", "C2", "C3"},

                {"C1", "C3", "C4", "C2", "C5"},
                {"C2", "C3", "C5", "C1", "C4"},
                {"C5", "C4", "C2", "C1", "C3"},
                {"C4", "C1", "C2", "C3", "C5"},
        };

        final String[][] companyPref = {

                {"E3", "E1", "E5", "E2", "E4"},
                {"E2", "E4", "E3", "E1", "E5"},
                {"E2", "E3", "E5", "E1", "E4"},

                {"E1", "E2", "E3", "E5", "E4"},
                {"E1", "E4", "E2", "E5", "E3"}
        };
        GaleShapley matching = new GaleShapley(employee, company, employeePref, companyPref);
        // truyền vào danh sách nhân viên cùng vơí prefer list đã được hoán vị
        String[][] finalMatch = matching.calcMatches();
        System.out.println(Arrays.toString(finalMatch[0]));
        System.out.println(Arrays.toString(finalMatch[1]));
    }
}
