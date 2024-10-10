package org.example.LMPV6;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class checkcheck {
    private final String[] competitor = {"Competitor1", "Competitor2", "Competitor3", "Competitor4", "Competitor5"};
    private Map<String, Set<String>> tripletList = new HashMap<>();

    public void initializeMap() {
        for (String comp : competitor) {
            // Gán từng đối thủ làm key và khởi tạo Set<String> rỗng làm value
            tripletList.put(comp, new HashSet<>());
        }
    }

    public static void main(String[] args) {
        checkcheck example = new checkcheck();
        example.initializeMap();

        // Kiểm tra map
        for (String key : example.tripletList.keySet()) {
            System.out.println("Key: " + key + ", Value: " + example.tripletList.get(key));
        }
    }
}
