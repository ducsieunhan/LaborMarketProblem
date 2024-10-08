package org.example.LMPV5;

import java.util.*;

 class PriorityMerger {

    // Hàm này gộp danh sách ưu tiên của bệnh nhân và bác sĩ
    public static List<String> mergePreferences(String[] patientPreferences, String[] doctorPreferences) {
        // Map để lưu tổng điểm của mỗi bệnh viện
        Map<String, Integer> hospitalScores = new HashMap<>();

        // Tính điểm dựa trên danh sách ưu tiên của bệnh nhân
        for (int i = 0; i < patientPreferences.length; i++) {
            String hospital = patientPreferences[i];
            hospitalScores.put(hospital, i + 1);  // Gán vị trí ưu tiên (bắt đầu từ 1)
        }

        // Tính điểm dựa trên danh sách ưu tiên của bác sĩ
        for (int i = 0; i < doctorPreferences.length; i++) {
            String hospital = doctorPreferences[i];
            // Cộng thêm điểm từ danh sách ưu tiên của bác sĩ
            hospitalScores.put(hospital, hospitalScores.getOrDefault(hospital, 0) + (i + 1));
        }

        // Sắp xếp các bệnh viện dựa trên tổng điểm (điểm thấp hơn sẽ có thứ hạng cao hơn)
        List<String> mergedList = new ArrayList<>(hospitalScores.keySet());
        mergedList.sort(Comparator.comparingInt(hospitalScores::get));

        return mergedList;
    }

    public static void main(String[] args) {
        // Danh sách ưu tiên của bệnh nhân và bác sĩ
        String[] patientPreferences = {"2", "3", "1", "4","5"};
        String[] doctorPreferences = {"5", "3", "2", "1", "4"};

        // Gộp hai danh sách ưu tiên
        List<String> mergedPreferences = mergePreferences(patientPreferences, doctorPreferences);

        // In kết quả danh sách ưu tiên chung
        System.out.println("Danh sách ưu tiên chung: " + mergedPreferences);
    }
}

