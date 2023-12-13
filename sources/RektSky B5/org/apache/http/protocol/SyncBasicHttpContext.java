/*
 * Decompiled with CFR 0.152.
 */
package org.apache.http.protocol;

import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

@Deprecated
public class SyncBasicHttpContext
extends BasicHttpContext {
    public SyncBasicHttpContext(HttpContext parentContext) {
        super(parentContext);
    }

    public SyncBasicHttpContext() {
    }

    @Override
    public synchronized Object getAttribute(String id) {
        return super.getAttribute(id);
    }

    @Override
    public synchronized void setAttribute(String id, Object obj) {
        super.setAttribute(id, obj);
    }

    @Override
    public synchronized Object removeAttribute(String id) {
        return super.removeAttribute(id);
    }

    @Override
    public synchronized void clear() {
        super.clear();
    }
}

