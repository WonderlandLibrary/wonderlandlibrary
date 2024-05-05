package net.minecraft.util;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.util.Cartesian;

public class Cartesian {
    public static <T> Iterable<T[]> cartesianProduct(Class<T> clazz, Iterable<? extends Iterable<? extends T>> sets) {
        return new Product(clazz, Cartesian.toArray(Iterable.class, sets), null);
    }

    public static <T> Iterable<List<T>> cartesianProduct(Iterable<? extends Iterable<? extends T>> sets) {
        return Cartesian.arraysAsLists(Cartesian.cartesianProduct(Object.class, sets));
    }

    private static <T> Iterable<List<T>> arraysAsLists(Iterable<Object[]> arrays) {
        return Iterables.transform(arrays, (Function)new GetList(null));
    }

    private static <T> T[] toArray(Class<? super T> clazz, Iterable<? extends T> it) {
        ArrayList list = Lists.newArrayList();
        for (Object t : it) {
            list.add(t);
        }
        return list.toArray((Object[])Cartesian.createArray(clazz, list.size()));
    }

    private static <T> T[] createArray(Class<? super T> p_179319_0_, int p_179319_1_) {
        return (Object[])Array.newInstance(p_179319_0_, (int)p_179319_1_);
    }

    static /* synthetic */ Object[] access$200(Class x0, int x1) {
        return Cartesian.createArray(x0, x1);
    }
}
