package org.example;

import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.Permutation;
import org.moeaframework.problem.AbstractProblem;

public class LaborMarketV2 extends AbstractProblem {


    private final String[] employee = {"E1", "E2", "E3", "E4", "E5"};

     private final String[] company = {"C1", "C2", "C3", "C4", "C5"};

    private final String[][] employeePref = {
            {"C1", "C4", "C5", "C2", "C3"},
            {"C4", "C1", "C2", "C3", "C5"},
            {"C2", "C3", "C5", "C1", "C4"},
            {"C5", "C4", "C2", "C1", "C3"},
            {"C1", "C3", "C4", "C2", "C5"}
    };

    private final String[][] companyPref = {
            {"E2", "E3", "E5", "E1", "E4"},
            {"E2", "E4", "E3", "E1", "E5"},
            {"E3", "E1", "E5", "E2", "E4"},
            {"E1", "E2", "E3", "E5", "E4"},
            {"E1", "E4", "E2", "E5", "E3"}
    };


    private final int N = employee.length ;

    public LaborMarketV2()
    {
        super(1,1);
    }


    @Override
    public void evaluate(Solution solution) {
        Permutation permutation = (Permutation) solution.getVariable(0);
        String[] randomizedEmployee = new String[N];
        String[][] randomizedEmployeePref = new String[N][N];
        for (int i = 0; i < N; i++) {
            randomizedEmployee[i] = employee[permutation.get(i)];
            int originalIndex = permutation.get(i);
//            System.out.println(permutation.get(i));

            randomizedEmployeePref[i] = employeePref[originalIndex];  // hoán vị danh sách ưu tiên
        }
        GaleShapley matching = new GaleShapley(randomizedEmployee, company, randomizedEmployeePref, companyPref);
                                                // truyền vào danh sách nhân viên cùng vơí prefer list đã được hoán vị
        String[][] finalMatch = matching.calcMatches() ;    // lấy ra cặp ghép nhân viên công ty

        double totalStatisfy = 0 ;


        for(int i = 0 ; i < N ; i++){
            String employeeName = finalMatch[0][i] ;
            String companyName = finalMatch[1][i] ;

            int employeeIndex = matching.employIndexOf(employeeName) ;
            for(int j = 0 ; j < N ; j++){                   // tính tổng hài lòng của nhân viên
                if (randomizedEmployeePref[employeeIndex][j].equals(companyName)){
                    totalStatisfy  += (N - j) ;
                    break ;
                }
            }
            int companyIndex = matching.companyIndexOf(companyName) ;
            for(int j = 0 ; j < N ; j++){                        // cộng với tổng hài lòng của công ty
                if (companyPref[companyIndex][j].equals(employeeName)){
                    totalStatisfy  += (N - j) ;
                    break;
                }
            }
        }

        solution.setObjective(0, -totalStatisfy);        // tối ưu hóa độ hài lòng
        //
        System.out.println();
        System.out.println("----------------------------------");
        System.out.println("Kết quả ghép cặp: ");
        for (int i = 0 ; i < N ; i++){
            System.out.println(finalMatch[0][i] + " vào công ty " + finalMatch[1][i]);
        }
    }

    @Override
    public Solution newSolution() {
        Solution solution = new Solution(1, 1);
        Permutation permutation = new Permutation(employee.length);
        permutation.randomize(); // Ngẫu nhiên thứ tự hoán vị
        solution.setVariable(0, permutation);
        return solution;
    }
}
