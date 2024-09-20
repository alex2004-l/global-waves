package app.user.wrapped;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

public class WrappedInterface {

    private static final Integer FIVE = 5;

    /**
     * Increments value from hashmap
     * @param entry -> the name of the entry
     * @param map -> the hashmap
     */
     void increment(final String entry, final HashMap<String, Integer> map) {
        if (map.get(entry) != null) {
            int currentCount = map.get(entry);
            currentCount = currentCount + 1;
            map.put(entry, currentCount);
        } else {
            map.put(entry, 1);
        }
    }

    // chat gpt

    /**
     * Gets top 5 pairs (key, value) from a hashmap
     * @param map -> the hashmap
     * @return -> the pairs as a treemap
     */
     TreeMap<String, Integer> top(final HashMap<String, Integer> map) {
        TreeMap<String, Integer> sortedMap = new TreeMap<>(
                (key1, key2) -> {
                    int valueComparison = Integer.compare(map.get(key2), map.get(key1));
                    return (valueComparison == 0) ? key1.compareTo(key2) : valueComparison;
                });
        sortedMap.putAll(map);

        List<String> sortedKeys = new ArrayList<>(sortedMap.keySet());
        List<Integer> sortedValues = new ArrayList<>(sortedMap.values());

        sortedMap.clear();
        for (int i = 0; i < FIVE && i < sortedKeys.size(); i++) {
            sortedMap.put(sortedKeys.get(i), sortedValues.get(i));
        }
        return sortedMap;
    }

    /**
     * Checks if wrapped is valid
     * @return -> true, if it is, false otherwise
     */
    public boolean validWrapped() {
         return false;
    }
}
