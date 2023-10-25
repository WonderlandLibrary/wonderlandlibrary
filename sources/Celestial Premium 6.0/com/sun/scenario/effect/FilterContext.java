/*
 * Decompiled with CFR 0.150.
 */
package com.sun.scenario.effect;

public class FilterContext {
    private Object referent;

    protected FilterContext(Object object) {
        if (object == null) {
            throw new IllegalArgumentException("Referent must be non-null");
        }
        this.referent = object;
    }

    public final Object getReferent() {
        return this.referent;
    }

    public int hashCode() {
        return this.referent.hashCode();
    }

    public boolean equals(Object object) {
        if (!(object instanceof FilterContext)) {
            return false;
        }
        FilterContext filterContext = (FilterContext)object;
        return this.referent.equals(filterContext.referent);
    }
}

