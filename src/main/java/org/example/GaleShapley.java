package org.example;

import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.Permutation;
import org.moeaframework.problem.AbstractProblem;

public class GaleShapley {

    private int N, AppliedCount;  // đếm số nhân viên được nhận
    private String[][] employeePref;  // danh sách ưu tiên của nhân viên dựa trên tiền lương của mỗi công ty
    private String[][] companyPref; // danh sách ưu tiên của công ty dựa trên kĩ năng của nhân viên
    private String[] employee;    // danh sách nhân viên
    private String[] company;  // danh sách công ty
    private String[] companyAccepter;  // người nhân viên đang được công ty xem xét để nhận vào
    private boolean[] employApply ; // kiểm tra xem nhân viên đã được nhân vào chưa

    /** Constructor **/
    public GaleShapley(String[] employeePermuted, String[] c, String[][] ep, String[][] cp)  // mp: men preference , wp: women preference
    {
        N = ep.length;      // số lượng nhân viên và công ty
        AppliedCount = 0;    // số nhân viên được nhận
        employee = employeePermuted;
        company = c;
        employeePref = ep;
        companyPref = cp;
        employApply = new boolean[N];
        companyAccepter = new String[N];
    }

    // ghép cặp nhân viên với công ty
    public String[][] calcMatches()
    {
        while (AppliedCount < N)  // số cặp nhân viên - công ty đã ghép thành công
        {
            int free; // nhân viên nào đã được ghép cặp chưa
            for (free = 0; free < N; free++)
                if (!employApply[free])
                    break;


            for (int i = 0; i < N && !employApply[free]; i++)
            {
                int index = companyIndexOf(employeePref[free][i]);  // lấy ra vị trí của công ty trong mảng
                if (companyAccepter[index] == null)   // kiếm tra xem công ty đó đã có ai ứng tuyển chưa
                {
                    companyAccepter[index] = employee[free];
                    employApply[free] = true;  // nhân viên ở vị trí free đã ứng tuyển
                    AppliedCount++;
                }
                else
                {
                    String currentPartner = companyAccepter[index];
                    if (morePreference(currentPartner, employee[free], index)) // kiếm tra nếu nhân viên khác ở ds ưu tiên cao hơn
                    {
                        companyAccepter[index] = employee[free];
                        employApply[free] = true;
                        employApply[employIndexOf(currentPartner)] = false;  // nhân viên ở danh sách ưu tiên thấp hơn
                                                                            // trở về không ứng tuyển thành công
                    }
                }
            }
        }
          String[][] finalMatching = new String[2][N] ;
            for(int i = 0 ; i < N;  i++){
                finalMatching[0][i] = companyAccepter[i];
                finalMatching[1][i] = company[i] ;
            }
            return  finalMatching ;  // lấy ra danh sách ứng tuyển
    }
    private boolean morePreference(String curEmployee, String newEmployee, int index)  // hàm kiểm tra nhân viên ở vị trí ưu tiên
                                                                                        // cao hơn
    {
        for (int i = 0; i < N; i++)
        {
            if (companyPref[index][i].equals(newEmployee)) // nếu nhân viên ứng tuyển mới ở vị trí ưu tiên cao hơn
                return true;                              // trả về true
            if (companyPref[index][i].equals(curEmployee))
                return false;                             // thấp hơn trả về false
        }
        return false;
    }
    public int employIndexOf(String str)   // lấy ra vị trí của nhân viên trong mảng nhân viên
    {
        for (int i = 0; i < N; i++)
            if (employee[i].equals(str))
                return i;
        return -5;
    }
    public int companyIndexOf(String str)   // lấy ra vị trí của công ty trong mảng công ty
    {
        for (int i = 0; i < N; i++)
            if (company[i].equals(str))
                return i;
        return -5;
    }
}
