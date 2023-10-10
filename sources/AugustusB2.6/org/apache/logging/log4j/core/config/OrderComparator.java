// 
// Decompiled by Procyon v0.5.36
// 

package org.apache.logging.log4j.core.config;

import java.util.Objects;
import java.io.Serializable;
import java.util.Comparator;

public class OrderComparator implements Comparator<Class<?>>, Serializable
{
    private static final long serialVersionUID = 1L;
    private static final Comparator<Class<?>> INSTANCE;
    
    public static Comparator<Class<?>> getInstance() {
        return OrderComparator.INSTANCE;
    }
    
    @Override
    public int compare(final Class<?> lhs, final Class<?> rhs) {
        final Order lhsOrder = Objects.requireNonNull(lhs, "lhs").getAnnotation(Order.class);
        final Order rhsOrder = Objects.requireNonNull(rhs, "rhs").getAnnotation(Order.class);
        if (lhsOrder == null && rhsOrder == null) {
            return 0;
        }
        if (rhsOrder == null) {
            return -1;
        }
        if (lhsOrder == null) {
            return 1;
        }
        return Integer.signum(rhsOrder.value() - lhsOrder.value());
    }
    
    static {
        INSTANCE = new OrderComparator();
    }
}
