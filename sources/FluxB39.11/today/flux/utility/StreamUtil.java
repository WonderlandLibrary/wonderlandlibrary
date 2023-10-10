package today.flux.utility;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;

/**
 * Created by John on 2017/07/21.
 */
public class StreamUtil {
    public static <T> Collector<T, List<T>, List<T>> inReverse() {
        return Collector.of(
                ArrayList::new,
                (l, t) -> l.add(t),
                (l, r) -> {
                    l.addAll(r);
                    return l;
                },
                Lists::<T>reverse);
    }
}
