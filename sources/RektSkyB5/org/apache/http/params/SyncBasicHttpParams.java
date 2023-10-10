/*
 * Decompiled with CFR 0.152.
 */
package org.apache.http.params;

import org.apache.http.annotation.Contract;
import org.apache.http.annotation.ThreadingBehavior;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

@Deprecated
@Contract(threading=ThreadingBehavior.SAFE)
public class SyncBasicHttpParams
extends BasicHttpParams {
    private static final long serialVersionUID = 5387834869062660642L;

    @Override
    public synchronized boolean removeParameter(String name) {
        return super.removeParameter(name);
    }

    @Override
    public synchronized HttpParams setParameter(String name, Object value) {
        return super.setParameter(name, value);
    }

    @Override
    public synchronized Object getParameter(String name) {
        return super.getParameter(name);
    }

    @Override
    public synchronized boolean isParameterSet(String name) {
        return super.isParameterSet(name);
    }

    @Override
    public synchronized boolean isParameterSetLocally(String name) {
        return super.isParameterSetLocally(name);
    }

    @Override
    public synchronized void setParameters(String[] names, Object value) {
        super.setParameters(names, value);
    }

    @Override
    public synchronized void clear() {
        super.clear();
    }

    @Override
    public synchronized Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}

