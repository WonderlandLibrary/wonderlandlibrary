package tech.atani.client.utility.java;

import java.security.SecureRandom;
import java.util.Random;

public class ArrayUtils {

    public static <T> T getRandomItem(T[] array) {
        if (array == null || array.length == 0) {
            throw new IllegalArgumentException("Array is empty or null");
        }

        Random random = new SecureRandom();
        int randomIndex = random.nextInt(array.length);
        return array[randomIndex];
    }

}
