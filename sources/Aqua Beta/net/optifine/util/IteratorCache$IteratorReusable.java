package net.optifine.util;

import java.util.Iterator;
import java.util.List;

public static interface IteratorCache.IteratorReusable<E>
extends Iterator<E> {
    public void setList(List<E> var1);
}
