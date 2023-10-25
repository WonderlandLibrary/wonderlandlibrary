/*
 * Decompiled with CFR 0.150.
 */
package org.apache.http.params;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.params.AbstractHttpParams;
import org.apache.http.params.HttpParams;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
@Deprecated
@ThreadSafe
public class BasicHttpParams
extends AbstractHttpParams
implements Serializable,
Cloneable {
    private static final long serialVersionUID = -7086398485908701455L;
    private final Map<String, Object> parameters = new ConcurrentHashMap<String, Object>();

    @Override
    public Object getParameter(String name) {
        return this.parameters.get(name);
    }

    @Override
    public HttpParams setParameter(String name, Object value) {
        if (name == null) {
            return this;
        }
        if (value != null) {
            this.parameters.put(name, value);
        } else {
            this.parameters.remove(name);
        }
        return this;
    }

    @Override
    public boolean removeParameter(String name) {
        if (this.parameters.containsKey(name)) {
            this.parameters.remove(name);
            return true;
        }
        return false;
    }

    public void setParameters(String[] names, Object value) {
        for (String name : names) {
            this.setParameter(name, value);
        }
    }

    public boolean isParameterSet(String name) {
        return this.getParameter(name) != null;
    }

    public boolean isParameterSetLocally(String name) {
        return this.parameters.get(name) != null;
    }

    public void clear() {
        this.parameters.clear();
    }

    @Override
    public HttpParams copy() {
        try {
            return (HttpParams)this.clone();
        }
        catch (CloneNotSupportedException ex) {
            throw new UnsupportedOperationException("Cloning not supported");
        }
    }

    public Object clone() throws CloneNotSupportedException {
        BasicHttpParams clone = (BasicHttpParams)super.clone();
        this.copyParams(clone);
        return clone;
    }

    public void copyParams(HttpParams target) {
        for (Map.Entry<String, Object> me : this.parameters.entrySet()) {
            target.setParameter(me.getKey(), me.getValue());
        }
    }

    @Override
    public Set<String> getNames() {
        return new HashSet<String>(this.parameters.keySet());
    }
}

