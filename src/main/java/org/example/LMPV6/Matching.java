package org.example.LMPV6;

import java.util.*;

public class Matching {
    private final int N;  // Number of patients, doctors, and hospitals
    private int appliedCount;  // Count of matched triplets

    private final String[] competitorAccepter;
    // Preference lists as a 3D array: [type][index][preference]
    private final boolean[] competitorMatched ;
    private final String[][] preferences;

    private final String[] competitor ;

    private Map<String, Set<String>> tripletList ;

    public Matching(  String[][] preferences, String[] competitor) {
        N = competitor.length;
        this.competitorAccepter =  new String[N];
        this.preferences = preferences;
        this.competitor = competitor;
        this.competitorMatched = new boolean[N];
        this.tripletList = new HashMap<>();
        initializeTriplet();    // tạo danh sách triplet chứa key cho mỗi biến // ví dụ 1 ghép với 2 3 , gọi 1 sẽ lấy đc 2 3, gọi 2 sẽ lấy được 1 3 ,..
    }
    public void initializeTriplet() {    // khởi tạo danh sách triplet
        for (String comp : competitor) {
            tripletList.put(comp, new HashSet<>());
        }
    }

    public boolean removeTriplet(String competitor1, String competitor2, String competitor3) {  // xóa 1 cặp triplet
        // Xóa ghép cặp cho bệnh nhân
        if (tripletList.containsKey(competitor1)) {
            tripletList.get(competitor1).remove(competitor2);
            tripletList.get(competitor1).remove(competitor3);
            return true;
        }
        return false;
    }
    public boolean removeAllPrevTriplet(String competitor1){   // xóa cả 3 cặp triplet đã ghép trước đó

        Set<String> values = tripletList.get(competitor1);
            Iterator<String> iterator = values.iterator();
            String competitor2 = iterator.next();
            String competitor3 = iterator.next();

        return removeTriplet(competitor1, competitor2,competitor3)
                && removeTriplet(competitor2,competitor1,competitor3)
                && removeTriplet(competitor3, competitor1,competitor2);
    }

    public boolean addTriplet(String competitor, String competitorToAdd1, String competitorToAdd2){   // thêm 1 cặp triplet
        if(tripletList.containsKey(competitor)){
            Set<String> competitorsSet = tripletList.get(competitor);
            competitorsSet.add(competitorToAdd1);
            competitorsSet.add(competitorToAdd2);
            return true ;
        }
        return false ;
    }
    public boolean addAllTriplet(String competitor1, String competitor2, String competitor3){       // thêm cả 3 cặp triplet vừa ghép
        return addTriplet(competitor1, competitor2,competitor3)
                && addTriplet(competitor2,competitor1,competitor3)
                && addTriplet(competitor3, competitor1,competitor2);
    }

    private int findFreePatient() {     // chọn ra bệnh nhân đầu tiên để ghép
        for (int i = 0; i < (N / 3); i++) {
            if (!competitorMatched[i]) {
                return i;  // Found a free patient
            }
        }
        return -1;  // No free patients
    }

    private void matchDoctorWithPatient(int freePatient, int docIndex, int padding) {    // lưu ghép bệnh nhân với bác sĩ
        competitorAccepter[docIndex + padding] = competitor[freePatient];
        competitorMatched[freePatient] = true;
        competitorMatched[docIndex + padding] = true;
    }

    private int getIndexInList(String str, String[] listObjects) {  // lấy ra vị trí của competitor trong 1 mảng chứa
        for (int i = 0; i < N; i++) {
            if (listObjects[i].equals(str)) {
                return i;
            }
        }
        return -1;  // Not found
    }
    private boolean morePreferenceHos(String prevPa, String prevDoc, int newPa, int newDoc, int hosIndex) {      // kiểm tra bệnh viên ưu tiên cặp ghép cũ hay mới
        // So sánh thứ hạng của cặp hiện tại và cặp mới trong danh sách ưu tiên của bệnh viện
        int currentRank = getIndexInList(prevPa, preferences[hosIndex]) + getIndexInList(prevDoc, preferences[hosIndex]);
        int newRank = getIndexInList(competitor[newPa], preferences[hosIndex]) + getIndexInList(competitor[newDoc], preferences[hosIndex]);
        return newRank < currentRank; // Cặp mới ưu tiên hơn nếu tổng điểm nhỏ hơn
    }

    private boolean morePreferenceDoc(String curPatient, String newPatient, int index) {        // kiểm tra bác sĩ ưu tiên cặp ghép cũ hay mới
        for (int i = 0 ; i <  (preferences[index].length/3); i++) {  // vì những bác sĩ được lưu ở vị trí từ 0 đến 4 trong ds ưu tiên của bệnh nhân
            if (preferences[index][i].equals(newPatient)) return true;  // nếu bệnh nhân mới ở vị trí ưu tiên cao hơn
            if (preferences[index][i].equals(curPatient)) return false;  // ngược lại
            return true ;
        }
        return false;
    }

    private void proposeToDoctors(int freePatient) {    // ghép bệnh nhân với bác sĩ
        int doctorPadding = N / 3;  // Padding để xác định bác sĩ bắt đầu từ vị trí thứ 5 trong mảng competitor
        int i = 0 ;
        for (String preferredDoc : preferences[freePatient]) {  // Truy cập danh sách ưu tiên của bệnh nhân về bác sĩ
            int docIndex = getIndexInList(preferredDoc, competitor);  // Tìm chỉ số của bác sĩ trong mảng tổng competitor
            i++ ;
            if(i==5) break;                        // vì trong danh sách ưu tiên của bệnh nhân, bệnh viện ở ví trí từ 0 đến 4

            if (competitorAccepter[docIndex] == null) {
                matchDoctorWithPatient(freePatient, docIndex, doctorPadding);  // Ghép bệnh nhân với bác sĩ
                assignHospital(freePatient, docIndex);  // Ghép bệnh viện
                return;
            } else {
                String currentPa = competitorAccepter[docIndex];

                if (morePreferenceDoc(currentPa, competitor[freePatient], docIndex)) {
                    matchDoctorWithPatient(freePatient, docIndex, doctorPadding);  // Ghép bệnh nhân với bác sĩ
                    removeAllPrevTriplet(currentPa);  // Xóa ghép trước đó
                    assignHospital(freePatient, docIndex);  // Ghép bệnh viện
                    return;
                }
            }
        }
    }
    private List<String> getCommonHospitals(String[] patientPreferences, String[] doctorPreferences) {   // kết hợp danh sách ưu tiên của bệnh nhân và bác sĩ về bệnh viện
        List<String> commonHospitals = new ArrayList<>();                                           // tạo ra 1 danh sách ưu tiên bệnh viện chung
        for (String hos : patientPreferences) {
            if (Arrays.asList(doctorPreferences).contains(hos)) {
                commonHospitals.add(hos);
            }
        }
        commonHospitals.sort((h1, h2) -> {
            int patientRank1 = getIndexInList(h1, patientPreferences);
            int patientRank2 = getIndexInList(h2, patientPreferences);
            int doctorRank1 = getIndexInList(h1, doctorPreferences);
            int doctorRank2 = getIndexInList(h2, doctorPreferences);
            int score1 = patientRank1 + doctorRank1;
            int score2 = patientRank2 + doctorRank2;
            return Integer.compare(score1, score2);  // Sắp xếp tăng dần dựa trên tổng điểm
        });
        return commonHospitals;
    }

    private boolean assignHospital(int freePatient, int docIndex) {
        int hospitalPadding = 2 * (N / 3);  // Padding để xác định bệnh viện bắt đầu từ vị trí thứ 10 trong mảng competitor
        String[] patientPreferences = new String[N/3];
        String[] doctorPreferences = new String[N/3];
        for(int i = 0 ; i < (N/3) ; i++){       //  vì vị trí ưu tiên cho bệnh viện của cả 2 bệnh nhân bác sĩ đề từ vị trí 5
            patientPreferences[i] = preferences[freePatient][N/3];
            patientPreferences[i] = preferences[docIndex][N/3];
        }

        // Lấy danh sách bệnh viện chung giữa bệnh nhân và bác sĩ
        List<String> commonHospitals = getCommonHospitals(patientPreferences, doctorPreferences);

        if (commonHospitals.isEmpty()) {
            return false;  // Không có bệnh viện chung được ưu tiên
        }


        // Chọn bệnh viện ưu tiên nhất
        String selectedHospital = commonHospitals.get(0);
        int hosIndex = getIndexInList(selectedHospital, competitor);  // Tìm vị trí bệnh viện trong mảng competitor


        // Kiểm tra xem bệnh viện đã có cặp nào chưa
        if (competitorAccepter[hosIndex] != null) {
            String[] previousPair = competitorAccepter[hosIndex].split("-");
            String prevPatient = previousPair[0];
            String prevDoctor = previousPair[1];

            // Kiểm tra xem bệnh viện có ưu tiên cặp mới hơn không
            if (morePreferenceHos(prevPatient, prevDoctor, freePatient, docIndex, hosIndex - hospitalPadding)) {
                // Hủy ghép cặp trước
                removeAllPrevTriplet(selectedHospital);

                // Ghép cặp mới
                addAllTriplet(competitor[freePatient], competitor[docIndex], competitor[hosIndex]);
                competitorAccepter[hosIndex] = competitor[freePatient] + "-" + competitor[docIndex];
                return true;
            }
        } else {
            // Nếu bệnh viện chưa có cặp nào, ghép cặp mới
            addAllTriplet(competitor[freePatient], competitor[docIndex], competitor[hosIndex]);
            competitorAccepter[hosIndex] = competitor[freePatient] + "-" + competitor[docIndex];

            return true;
        }

        return false;
    }

    public void calcMatches() {
        while (appliedCount < N) {
            int freePatient = findFreePatient();
            if (freePatient == -1) {
                break;  // All patients matched
            }
            proposeToDoctors(freePatient);
        }
    }

    public static void main(String[] args) {
        // Khởi tạo các competitor (5 bệnh nhân, 5 bác sĩ, 5 bệnh viện)
        String[] competitors = {
                "P1", "P2", "P3", "P4", "P5",  // Patients
                "D1", "D2", "D3", "D4", "D5",  // Doctors
                "H1", "H2", "H3", "H4", "H5"   // Hospitals
        };

        // Danh sách ưu tiên (preference) của bệnh nhân, bác sĩ, và bệnh viện
        // Mỗi bệnh nhân có danh sách ưu tiên các bác sĩ
        // Mỗi bác sĩ có danh sách ưu tiên các bệnh viện
        String[][] preferences = {
                // 5 bệnh nhân
                {"D1", "D2", "D3", "D4", "D5", "H1", "H2", "H3", "H4", "H5"},  // P1
                {"D2", "D3", "D1", "D5", "D4", "H2", "H3", "H1", "H5", "H4"},  // P2
                {"D3", "D1", "D2", "D4", "D5", "H3", "H1", "H2", "H4", "H5"},  // P3
                {"D4", "D2", "D1", "D3", "D5", "H4", "H5", "H1", "H2", "H3"},  // P4
                {"D5", "D3", "D4", "D1", "D2", "H5", "H4", "H2", "H3", "H1"},  // P5

                // 5 bác sĩ
                {"P1", "P2", "P3", "P4", "P5", "H1", "H2", "H3", "H4", "H5"},  // D1
                {"P2", "P3", "P1", "P5", "P4", "H2", "H3", "H1", "H5", "H4"},  // D2
                {"P3", "P1", "P2", "P4", "P5", "H3", "H1", "H2", "H4", "H5"},  // D3
                {"P4", "P5", "P1", "P2", "P3", "H4", "H5", "H1", "H2", "H3"},  // D4
                {"P5", "P4", "P2", "P3", "P1", "H5", "H4", "H2", "H3", "H1"},  // D5

                // 5 bệnh viện
                {"P1", "P2", "P3", "P4", "P5", "D1", "D2", "D3", "D4", "D5"},  // H1
                {"P2", "P3", "P1", "P5", "P4", "D2", "D3", "D1", "D5", "D4"},  // H2
                {"P3", "P1", "P2", "P4", "P5", "D3", "D1", "D2", "D4", "D5"},  // H3
                {"P4", "P5", "P1", "P2", "P3", "D4", "D5", "D1", "D2", "D3"},  // H4
                {"P5", "P4", "P2", "P3", "P1", "D5", "D4", "D2", "D3", "D1"}   // H5
        };

        // Khởi tạo đối tượng Matching với dữ liệu đã tạo
        Matching matching = new Matching(preferences, competitors);

        // Tính toán các cặp ghép
        matching.calcMatches();

        // In ra kết quả ghép cặp cuối cùng (tripletList)
        System.out.println("Kết quả ghép cặp cuối cùng:");
        for (Map.Entry<String, Set<String>> entry : matching.tripletList.entrySet()) {
            System.out.println("Competitor: " + entry.getKey() + " - Cặp với: " + entry.getValue());
        }
    }

}
