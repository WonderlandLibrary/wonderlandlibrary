package tech.atani.client.utility.java;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MapUtil {

    public static <K, V> K getKeyAtIndex(Map<K, V> map, int index) {
        int currentIndex = 0;

        for (K key : map.keySet()) {
            if (currentIndex == index) {
                return key;
            }
            currentIndex++;
        }

        return null;
    }

    public static <K, V> List<K> convertMapKeysToList(Map<K, V> map) {
        return new ArrayList<>(map.keySet());
    }

}
