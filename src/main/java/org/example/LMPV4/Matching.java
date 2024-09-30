package org.example.LMPV4;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Matching {

    private int N, AppliedCount;  // Số lượng bệnh nhân, bác sĩ, bệnh viện và số nhóm đã ghép

    // Danh sách ưu tiên của bệnh nhân
    private String[][] patientToDoc;  // Danh sách ưu tiên của bệnh nhân với bác sĩ
    private String[][] patientToHos;  // Danh sách  ưu tiên của bệnh nhân với bệnh viện

    // Danh sách ưu tiên của bác sĩ
    private String[][] docToPa;  // Danh sách ưu tiên của bác sĩ với bệnh nhân
    private String[][] docToHos; // Danh sách ưu tiên của bác sĩ với bệnh viện

    // Danh sách ưu tiên của bệnh viện
    private String[][] hosToPa;  // Danh sách ưu tiên của bệnh viện với bệnh nhân
    private String[][] hosToDoc; // Danh sách ưu tiên của bệnh viện với bác sĩ

    // Danh sách bệnh nhân, bác sĩ, và bệnh viện
    private String[] patients;
    private String[] doctors;
    private String[] hospitals;

    // Trạng thái ghép nối
    private String[] doctorAccepter;  // Bác sĩ hiện đang ghép nối với bệnh nhân nào
    private String[] hospitalAccepter;  // Bệnh viện hiện đang ghép nối với bệnh nhân - bác sĩ nào
    private boolean[] patientMatched;  // Kiểm tra xem bệnh nhân đã được ghép hay chưa
    private boolean[] doctorMatched;   // Kiểm tra xem bác sĩ đã được ghép hay chưa
    private boolean[] hospitalMatched; // Kiểm tra xem bệnh viện đã được ghép hay chưa

    private List<Set<String>> tripletList ;
    /** Constructor **/
    public Matching(String[] patientList, String[] doctorList, String[] hospitalList,
                    String[][] patientToDocPref, String[][] patientToHosPref,
                    String[][] docToPaPref, String[][] docToHosPref,
                    String[][] hosToPaPref, String[][] hosToDocPref) {

        N = patientToDocPref.length;  // Số lượng bệnh nhân, bác sĩ, bệnh viện
        AppliedCount = 0;  // Số nhóm đã ghép

        // Gán giá trị cho các danh sách và ưu tiên
        patients = patientList;
        doctors = doctorList;
        hospitals = hospitalList;

        patientToDoc = patientToDocPref;
        patientToHos = patientToHosPref;
        docToPa = docToPaPref;
        docToHos = docToHosPref;
        hosToPa = hosToPaPref;
        hosToDoc = hosToDocPref;

        patientMatched = new boolean[N];
        doctorMatched = new boolean[N];
        hospitalMatched = new boolean[N];
        doctorAccepter = new String[N];
        hospitalAccepter = new String[N];
    }

    // Tính toán ghép triplet giữa bệnh nhân - bác sĩ - bệnh viện


    // Kiểm tra ưu tiên giữa hai bệnh nhân đối với một bác sĩ
    private boolean morePreferenceDoc(String curPatient, String newPatient, int index) {
        for (int i = 0; i < N; i++) {
            if (docToPa[index][i].equals(newPatient))
                return true;  // Bệnh nhân mới ưu tiên hơn
            if (docToPa[index][i].equals(curPatient))
                return false;  // Bệnh nhân hiện tại ưu tiên hơn
        }
        return false;
    }

    // Kiểm tra ưu tiên giữa hai triplet (bệnh nhân - bác sĩ) đối với một bệnh viện
    private boolean morePreferenceHos(String prevPa, String prevDoc, String newPa, String newDoc, int indexOfHos) {
        // Lấy index của bệnh nhân và bác sĩ cũ trong danh sách ưu tiên của bệnh viện
        int indexPrevPa = getIndex(hosToPa[indexOfHos], prevPa);
        int indexPrevDoc = getIndex(hosToDoc[indexOfHos], prevDoc);

        // Lấy index của bệnh nhân và bác sĩ mới trong danh sách ưu tiên của bệnh viện
        int indexNewPa = getIndex(hosToPa[indexOfHos], newPa);
        int indexNewDoc = getIndex(hosToDoc[indexOfHos], newDoc);

        // So sánh tổng chỉ số
        int totalPrev = indexPrevPa + indexPrevDoc;
        int totalNew = indexNewPa + indexNewDoc;

        // Nếu tổng của cặp cũ lớn hơn cặp mới, trả về true (nghĩa là ưu tiên cặp mới hơn)
        return totalPrev >= totalNew;    // 4   2
    }

    private static int getIndex(String[] preferenceList, String element) {
        for (int i = 0; i < preferenceList.length; i++) {
            if (preferenceList[i].equals(element)) {
                return i;
            }
        }
        return Integer.MAX_VALUE;  // Nếu không tìm thấy, trả về giá trị lớn để không ưu tiên
    }


    //    private final String[][] hosPref = {    // bệnh viện đang xét là 3
    //            {"E2", "E3", "E5", "E1", "E4"},
    //            {"E2", "E4", "E3", "E1", "E5"},
    //            {"E3", "E1", "E5", "E2", "E4"},
    //            {"E1", "E2", "E3", "E5", "E4"},
    //            {"E1", "E4", "E2", "E5", "E3"}
    //    };

    // Tìm chỉ số trong danh sách đối tượng dựa trên tên
    private int getIndexInList(String str, String[] listObjects) {
        for (int i = 0; i < N; i++)
            if (listObjects[i].equals(str))
                return i;
        return -1;
    }

    private boolean deletePreviousMatch(String currentNode) {
        for(Set a : tripletList){
            if (a.contains(currentNode)){
                tripletList.remove(a);
                return true ;
            }
        }
        return false  ;
    }
    private Set getPrevSetByObjectName(String name) {
        for(Set a : tripletList){
            if (a.contains(name)){
                tripletList.remove(a);
                return a ;
            }
        }
        return null ;
    }



    public void calcMatches() {
        while (AppliedCount < N) {
            // Tìm bệnh nhân chưa được ghép
            int freePatient = -1;
            for (int i = 0; i < N; i++) {
                if (!patientMatched[i]) {
                    freePatient = i;
                    break;
                }
            }

            if (freePatient == -1) {
                break;  // Tất cả bệnh nhân đã được ghép
            }

            // Bệnh nhân tự do duyệt qua danh sách ưu tiên của mình để tìm bác sĩ
            for (int i = 0; i < N && !patientMatched[freePatient]; i++) {
                String preferredDoc = patientToDoc[freePatient][i];
                int docIndex = getIndexInList(preferredDoc, doctors);

                // Kiểm tra nếu bác sĩ chưa ghép với ai
                if (doctorAccepter[docIndex] == null) {
                    doctorAccepter[docIndex] = patients[freePatient];  // Ghép bác sĩ với bệnh nhân
                    patientMatched[freePatient] = true;
                    doctorMatched[docIndex] = true;

                    // Gọi phương thức assignHospital để ghép bác sĩ với bệnh viện
                    assignHospital(freePatient, docIndex);
                } else {
                    // Nếu bác sĩ đã có bệnh nhân, kiểm tra xem bệnh nhân mới có ưu tiên hơn không
                    String currentPa = doctorAccepter[docIndex];
                    if (morePreferenceDoc(currentPa, patients[freePatient], docIndex)) {
                        // Xóa ghép cũ
                        patientMatched[getIndexInList(currentPa, patients)] = false;

                        // Cập nhật cặp mới
                        doctorAccepter[docIndex] = patients[freePatient];
                        patientMatched[freePatient] = true;

                        // Xóa cặp triplet cũ nếu có
                        deletePreviousMatch(currentPa);

                        // Gọi phương thức assignHospital để ghép bác sĩ với bệnh viện
                        assignHospital(freePatient, docIndex);
                    }
                }
            }
        }
    }


    private boolean assignHospital(int freePatient, int docIndex) {
        for (int j = 0; j < N && !hospitalMatched[docIndex]; j++) {
            String preferredHos = docToHos[docIndex][j];  // Bệnh viện mà bác sĩ đang muốn ghép
            int hosIndex = getIndexInList(preferredHos, hospitals);

            // Nếu bệnh viện chưa ghép với ai
            if (hospitalAccepter[hosIndex] == null) {
                Set<String> newTriplet = Set.of(patients[freePatient], doctors[docIndex], hospitals[hosIndex]);
                hospitalAccepter[hosIndex] = patients[freePatient] + "-" + doctors[docIndex];
                hospitalMatched[docIndex] = true;  // Đánh dấu bác sĩ đã ghép với bệnh viện
                tripletList.add(newTriplet);  // Thêm cặp triplet vào danh sách
                AppliedCount++;
                return true;  // Ghép thành công, thoát ra
            } else {
                // Nếu bệnh viện đã có một bộ ba ghép trước đó, kiểm tra ưu tiên
                Set<String> prevPair = getPrevSetByObjectName(preferredHos);
                String prevPa = null;
                String prevDoc = null;
                int c = 0;
                for (String element : prevPair) {
                    if (c == 0) {
                        prevPa = element;  // Bệnh nhân cũ đã được ghép
                    } else if (c == 1) {
                        prevDoc = element;  // Bác sĩ cũ đã được ghép
                        break;  // Dừng lại sau khi lấy được bệnh nhân và bác sĩ
                    }
                    c++;
                }

                // Kiểm tra xem bệnh viện có ưu tiên cặp mới hơn không
                if (morePreferenceHos(prevPa, prevDoc, patients[freePatient], doctors[docIndex], hosIndex)) {
                    // Xóa ghép cũ, đặt lại trạng thái cho đối tượng cũ
                    deletePreviousMatch(prevPa);
                    patientMatched[getIndexInList(prevPa, patients)] = false;
                    doctorMatched[getIndexInList(prevDoc, doctors)] = false;
                    doctorAccepter[getIndexInList(prevDoc, doctors)] = null;
                    AppliedCount--;

                    // Ghép cặp mới
                    Set<String> newTriplet = Set.of(patients[freePatient], doctors[docIndex], hospitals[hosIndex]);
                    hospitalAccepter[hosIndex] = patients[freePatient] + "-" + doctors[docIndex];
                    tripletList.add(newTriplet);  // Thêm cặp triplet mới vào danh sách
                    hospitalMatched[docIndex] = true;
                    AppliedCount++;
                    return true;  // Ghép thành công, thoát ra
                }
            }
        }
        return false;  // Không ghép được bệnh viện
    }


    public static void main(String[] args) {
        // Dữ liệu danh sách bệnh nhân, bác sĩ và bệnh viện
        String[] patients = {"P1", "P2", "P3"};
        String[] doctors = {"D1", "D2", "D3"};
        String[] hospitals = {"H1", "H2", "H3"};

        // Danh sách ưu tiên của bệnh nhân đối với bác sĩ
        String[][] patientToDocPref = {
                {"D1", "D2", "D3"},  // P1 ưu tiên D1, sau đó D2, cuối cùng là D3
                {"D2", "D1", "D3"},  // P2 ưu tiên D2, sau đó D1, cuối cùng là D3
                {"D3", "D1", "D2"}   // P3 ưu tiên D3, sau đó D1, cuối cùng là D2
        };

        // Danh sách ưu tiên của bệnh nhân đối với bệnh viện
        String[][] patientToHosPref = {
                {"H1", "H2", "H3"},  // P1 ưu tiên H1, sau đó H2, cuối cùng là H3
                {"H2", "H1", "H3"},  // P2 ưu tiên H2, sau đó H1, cuối cùng là H3
                {"H3", "H1", "H2"}   // P3 ưu tiên H3, sau đó H1, cuối cùng là H2
        };

        // Danh sách ưu tiên của bác sĩ đối với bệnh nhân
        String[][] docToPaPref = {
                {"P1", "P2", "P3"},  // D1 ưu tiên P1, sau đó P2, cuối cùng là P3
                {"P2", "P1", "P3"},  // D2 ưu tiên P2, sau đó P1, cuối cùng là P3
                {"P3", "P1", "P2"}   // D3 ưu tiên P3, sau đó P1, cuối cùng là P2
        };

        // Danh sách ưu tiên của bác sĩ đối với bệnh viện
        String[][] docToHosPref = {
                {"H1", "H2", "H3"},  // D1 ưu tiên H1, sau đó H2, cuối cùng là H3
                {"H2", "H1", "H3"},  // D2 ưu tiên H2, sau đó H1, cuối cùng là H3
                {"H3", "H1", "H2"}   // D3 ưu tiên H3, sau đó H1, cuối cùng là H2
        };

        // Danh sách ưu tiên của bệnh viện đối với bệnh nhân
        String[][] hosToPaPref = {
                {"P1", "P2", "P3"},  // H1 ưu tiên P1, sau đó P2, cuối cùng là P3
                {"P2", "P1", "P3"},  // H2 ưu tiên P2, sau đó P1, cuối cùng là P3
                {"P3", "P1", "P2"}   // H3 ưu tiên P3, sau đó P1, cuối cùng là P2
        };

        // Danh sách ưu tiên của bệnh viện đối với bác sĩ
        String[][] hosToDocPref = {
                {"D1", "D2", "D3"},  // H1 ưu tiên D1, sau đó D2, cuối cùng là D3
                {"D2", "D1", "D3"},  // H2 ưu tiên D2, sau đó D1, cuối cùng là D3
                {"D3", "D1", "D2"}   // H3 ưu tiên D3, sau đó D1, cuối cùng là D2
        };

        // Tạo đối tượng Matching với dữ liệu trên
        Matching matching = new Matching(patients, doctors, hospitals,
                patientToDocPref, patientToHosPref, docToPaPref, docToHosPref, hosToPaPref, hosToDocPref);

        // Khởi tạo danh sách triplet (bệnh nhân - bác sĩ - bệnh viện) là một danh sách rỗng
        matching.tripletList = new ArrayList<>();

        // Thực hiện tính toán ghép nối triplet giữa bệnh nhân - bác sĩ - bệnh viện
        matching.calcMatches();

        // In ra kết quả các cặp triplet đã ghép thành công
        System.out.println("Kết quả các cặp triplet (bệnh nhân - bác sĩ - bệnh viện) đã ghép:");
        for (Set<String> triplet : matching.tripletList) {
            System.out.println(triplet);
        }
    }
}
