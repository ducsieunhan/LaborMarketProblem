package org.example.LMPV5;

import java.util.*;

public class Matching {

    private final int N;  // Number of patients, doctors, and hospitals
    private int appliedCount;  // Count of matched triplets

    // Preference lists as a 3D array: [type][index][preference]
    private final String[][][] preferences;

    // Entities
    private final String[] patients;
    private final String[] doctors;
    private final String[] hospitals;

    // Current match states
    private final String[] doctorAccepter;
    private final String[] hospitalAccepter;
    private final boolean[] patientMatched;
    private final boolean[] doctorMatched;
    private final boolean[] hospitalMatched;

    private final List<Set<String>> tripletList;

    public Matching(String[] patientList, String[] doctorList, String[] hospitalList,
                    String[][] patientToDocPref, String[][] patientToHosPref,
                    String[][] docToPaPref, String[][] docToHosPref,
                    String[][] hosToPaPref, String[][] hosToDocPref) {
        this.N = patientToDocPref.length;  // Total number of patients
        this.appliedCount = 0;

        // Assign input values
        this.patients = patientList;
        this.doctors = doctorList;
        this.hospitals = hospitalList;

        // Initialize preference lists as a 3D array
        preferences = new String[6][][];  // 0: patientToDoc, 1: patientToHos, 2: docToPa, 3: docToHos, 4: hosToPa, 5: hosToDoc
        preferences[0] = patientToDocPref;
        preferences[1] = patientToHosPref;
        preferences[2] = docToPaPref;
        preferences[3] = docToHosPref;
        preferences[4] = hosToPaPref;
        preferences[5] = hosToDocPref;

        this.patientMatched = new boolean[N];
        this.doctorMatched = new boolean[N];
        this.hospitalMatched = new boolean[N];

        this.doctorAccepter = new String[N];
        this.hospitalAccepter = new String[N];
        this.tripletList = new ArrayList<>();

        // Initialize empty matches
        for (int i = 0; i < N; i++) {
            doctorAccepter[i] = null;
            hospitalAccepter[i] = null;
        }
    }

    // Calculates matches among patients, doctors, and hospitals
    public void calcMatches() {
        while (appliedCount < N) {
            int freePatient = findFreePatient();
            if (freePatient == -1) {
                break;  // All patients matched
            }
            proposeToDoctors(freePatient);
        }
    }

    private int findFreePatient() {
        for (int i = 0; i < N; i++) {
            if (!patientMatched[i]) {
                return i;  // Found a free patient
            }
        }
        return -1;  // No free patients
    }


    // gale shapley 1
    private void proposeToDoctors(int freePatient) {
        for (String preferredDoc : preferences[0][freePatient]) {  // Access patientToDoc preferences
            int docIndex = getIndexInList(preferredDoc, doctors);
            if (docIndex == -1) continue;  // Ignore if doctor not found

            if (doctorAccepter[docIndex] == null) {
                matchDoctorWithPatient(freePatient, docIndex);
                assignHospital(freePatient, docIndex);
                return;  // Stop proposing after a match
            } else {
                String currentPa = doctorAccepter[docIndex];
                if (morePreferenceDoc(currentPa, patients[freePatient], docIndex)) {
                    int currentPaIndex = getIndexInList(currentPa, patients);
                    patientMatched[currentPaIndex] = false;  // Unmatch current patient
                    doctorAccepter[docIndex] = patients[freePatient];  // Match new patient
                    patientMatched[freePatient] = true;
                    deletePreviousMatch(currentPa);
                    assignHospital(freePatient, docIndex);
                    return;  // Stop proposing after a match
                }
            }
        }
    }

    private void matchDoctorWithPatient(int freePatient, int docIndex) {
        doctorAccepter[docIndex] = patients[freePatient];
        patientMatched[freePatient] = true;
        doctorMatched[docIndex] = true;
    }


    // gale-shapley 2
    private boolean assignHospital(int freePatient, int docIndex) {
        String[] patientPreferences = preferences[1][freePatient]; // Lấy danh sách ưu tiên của bệnh nhân về bệnh viện
        String[] doctorPreferences = preferences[3][docIndex]; // Lấy danh sách ưu tiên của bác sĩ về bệnh viện

        //   1 2 3 4
        //   4 3 2 1       -->  2 3 1 4
        // Tìm các bệnh viện mà cả bệnh nhân và bác sĩ đều ưu tiên
        List<String> commonHospitals = new ArrayList<>();
        for (String hos : patientPreferences) {
            if (Arrays.asList(doctorPreferences).contains(hos)) {
                commonHospitals.add(hos);
            }
        }

        if (commonHospitals.isEmpty()) {
            return false; // Không có bệnh viện chung được ưu tiên
        }

        // Xếp hạng các bệnh viện dựa trên sở thích của cả bệnh nhân và bác sĩ
        commonHospitals.sort((h1, h2) -> {
            int patientRank1 = getIndexInList(h1, patientPreferences);
            int patientRank2 = getIndexInList(h2, patientPreferences);
            int doctorRank1 = getIndexInList(h1, doctorPreferences);
            int doctorRank2 = getIndexInList(h2, doctorPreferences);
            int score1 = patientRank1 + doctorRank1;
            int score2 = patientRank2 + doctorRank2;
            return Integer.compare(score1, score2); // Sắp xếp tăng dần dựa trên tổng điểm
        });

        // Chọn bệnh viện ưu tiên nhất
        String selectedHospital = commonHospitals.get(0);
        int hosIndex = getIndexInList(selectedHospital, hospitals);

        // Kiểm tra xem bệnh viện đã có cặp nào chưa
        if (hospitalAccepter[hosIndex] != null) {
            String[] previousPair = hospitalAccepter[hosIndex].split("-");
            String prevPatient = previousPair[0];
            String prevDoctor = previousPair[1];

            // Kiểm tra xem bệnh viện có ưu tiên cặp mới hơn không
            if (morePreferenceHos(prevPatient, prevDoctor, freePatient, docIndex, hosIndex)) {
                // Hủy ghép cặp trước
                updatePreviousMatch(prevPatient, prevDoctor);

                // Ghép cặp mới
                addTriplet(freePatient, docIndex, hosIndex);
                return true;
            }
        } else {
            // Nếu bệnh viện chưa có cặp nào, ghép cặp mới
            addTriplet(freePatient, docIndex, hosIndex);
            return true;
        }

        return false;
    }

    private boolean morePreferenceHos(String prevPa, String prevDoc, int newPa, int newDoc, int hosIndex) {
        // So sánh thứ hạng của cặp hiện tại và cặp mới trong danh sách ưu tiên của bệnh viện
        int currentRank = getIndexInList(prevPa, preferences[4][hosIndex]) + getIndexInList(prevDoc, preferences[5][hosIndex]);
        int newRank = getIndexInList(patients[newPa], preferences[4][hosIndex]) + getIndexInList(doctors[newDoc], preferences[5][hosIndex]);
        return newRank < currentRank; // Cặp mới ưu tiên hơn nếu tổng điểm nhỏ hơn
    }



    private void addTriplet(int freePatient, int docIndex, int hosIndex) {
        Set<String> newTriplet = new HashSet<>();
        newTriplet.add(patients[freePatient]);
        newTriplet.add(doctors[docIndex]);
        newTriplet.add(hospitals[hosIndex]);
        tripletList.add(newTriplet);  // Add new triplet
        hospitalAccepter[hosIndex] = patients[freePatient] + "-" + doctors[docIndex];
        appliedCount++;
    }

    private void updatePreviousMatch(String prevPa, String prevDoc) {
        deletePreviousMatch(prevPa);
        patientMatched[getIndexInList(prevPa, patients)] = false;
        doctorMatched[getIndexInList(prevDoc, doctors)] = false;
        doctorAccepter[getIndexInList(prevDoc, doctors)] = null;
        appliedCount--;
    }

    private String[] getPreviousPair(Set<String> triplet) {
        List<String> elements = new ArrayList<>(triplet);
        return new String[]{elements.get(0), elements.get(1)};  // Return patient and doctor
    }

    private int getIndexInList(String str, String[] listObjects) {
        for (int i = 0; i < N; i++) {
            if (listObjects[i].equals(str)) {
                return i;
            }
        }
        return -1;  // Not found
    }

    private boolean deletePreviousMatch(String currentNode) {
        return tripletList.removeIf(triplet -> triplet.contains(currentNode));
    }

    private Set<String> getPrevSetByObjectName(String name) {
        for (Set<String> triplet : tripletList) {
            if (triplet.contains(name)) {
                return triplet;
            }
        }
        return null;  // Not found
    }
    private void printTriplet() {

        for (Set<String> triplet : tripletList) {
            System.out.println();
            for(String s1 : triplet){
                System.out.print(s1 + " ");
            }
        }
    }

    // Check preference between two patients for a doctor
    private boolean morePreferenceDoc(String curPatient, String newPatient, int index) {
        for (String patient : preferences[2][index]) {  // Access docToPa preferences
            if (patient.equals(newPatient)) return true;  // New patient preferred
            if (patient.equals(curPatient)) return false;  // Current patient preferred
        }
        return false;  // No preference found
    }

    // Check preference between two triplets for a hospital
    private boolean morePreferenceHos(String prevPa, String prevDoc, String newPa, String newDoc, int indexOfHos) {
        return getIndex(preferences[4][indexOfHos], prevPa) + getIndex(preferences[5][indexOfHos], prevDoc) >=
                getIndex(preferences[4][indexOfHos], newPa) + getIndex(preferences[5][indexOfHos], newDoc);
    }

    private static int getIndex(String[] preferenceList, String element) {
        for (int i = 0; i < preferenceList.length; i++) {
            if (preferenceList[i].equals(element)) {
                return i;
            }
        }
        return Integer.MAX_VALUE;  // Not found
    }

    public static void main(String[] args) {
        // Sample data
        String[] patients = {"P1", "P2", "P3", "P4", "P5"};
        String[] doctors = {"D1", "D2", "D3", "D4", "D5"};
        String[] hospitals = {"H1", "H2", "H3", "H4", "H5"};

        // Patient preferences for doctors
        String[][] patientToDocPref = {
                {"D1", "D2", "D3", "D4", "D5"},
                {"D2", "D1", "D3", "D5", "D4"},
                {"D3", "D1", "D2", "D4", "D5"},
                {"D5", "D3", "D1", "D2", "D4"},
                {"D4", "D5", "D2", "D3", "D1"}
        };

        // Patient preferences for hospitals
        String[][] patientToHosPref = {
                {"H1", "H2", "H3", "H4", "H5"},
                {"H3", "H2", "H1", "H5", "H4"},
                {"H2", "H1", "H3", "H4", "H5"},
                {"H5", "H3", "H1", "H2", "H4"},
                {"H4", "H5", "H2", "H3", "H1"}
        };

        // Doctor preferences for patients
        String[][] docToPaPref = {
                {"P1", "P2", "P3", "P4", "P5"},
                {"P2", "P1", "P3", "P5", "P4"},
                {"P3", "P1", "P2", "P4", "P5"},
                {"P4", "P5", "P1", "P2", "P3"},
                {"P5", "P4", "P2", "P3", "P1"}
        };

        // Doctor preferences for hospitals
        String[][] docToHosPref = {
                {"H1", "H2", "H3", "H4", "H5"},
                {"H2", "H3", "H1", "H5", "H4"},
                {"H3", "H1", "H2", "H4", "H5"},
                {"H4", "H5", "H1", "H2", "H3"},
                {"H5", "H4", "H2", "H3", "H1"}
        };

        // Hospital preferences for patients
        String[][] hosToPaPref = {
                {"P1", "P2", "P3", "P4", "P5"},
                {"P2", "P3", "P1", "P5", "P4"},
                {"P3", "P1", "P2", "P4", "P5"},
                {"P4", "P5", "P1", "P2", "P3"},
                {"P5", "P4", "P2", "P3", "P1"}
        };

        // Hospital preferences for doctors
        String[][] hosToDocPref = {
                {"D1", "D2", "D3", "D4", "D5"},
                {"D2", "D3", "D1", "D5", "D4"},
                {"D3", "D1", "D2", "D4", "D5"},
                {"D5", "D4", "D2", "D3", "D1"},
                {"D4", "D5", "D2", "D3", "D1"}
        };

        // Create Matching instance
        Matching matching = new Matching(patients, doctors, hospitals,
                patientToDocPref, patientToHosPref, docToPaPref,
                docToHosPref, hosToPaPref, hosToDocPref);

        System.out.println();
        // Calculate matches
        matching.calcMatches();
        matching.printTriplet();

    }


}