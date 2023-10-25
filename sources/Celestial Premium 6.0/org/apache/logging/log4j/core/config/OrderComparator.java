/*
 * Decompiled with CFR 0.150.
 */
package org.apache.logging.log4j.core.config;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Objects;
import org.apache.logging.log4j.core.config.Order;

public class OrderComparator
implements Comparator<Class<?>>,
Serializable {
    private static final long serialVersionUID = 1L;
    private static final Comparator<Class<?>> INSTANCE = new OrderComparator();

    public static Comparator<Class<?>> getInstance() {
        return INSTANCE;
    }

    @Override
    public int compare(Class<?> lhs, Class<?> rhs) {
        Order lhsOrder = Objects.requireNonNull(lhs, "lhs").getAnnotation(Order.class);
        Order rhsOrder = Objects.requireNonNull(rhs, "rhs").getAnnotation(Order.class);
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
}

