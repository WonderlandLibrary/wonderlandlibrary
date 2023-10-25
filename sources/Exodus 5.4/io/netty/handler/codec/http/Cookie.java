/*
 * Decompiled with CFR 0.152.
 */
package io.netty.handler.codec.http;

import java.util.Set;

public interface Cookie
extends Comparable<Cookie> {
    public void setDomain(String var1);

    public boolean isDiscard();

    public void setPorts(int ... var1);

    public void setPorts(Iterable<Integer> var1);

    public String getPath();

    public boolean isHttpOnly();

    public long getMaxAge();

    public void setMaxAge(long var1);

    public int getVersion();

    public String getName();

    public boolean isSecure();

    public String getComment();

    public Set<Integer> getPorts();

    public void setDiscard(boolean var1);

    public String getDomain();

    public void setVersion(int var1);

    public void setValue(String var1);

    public void setSecure(boolean var1);

    public void setComment(String var1);

    public void setHttpOnly(boolean var1);

    public String getCommentUrl();

    public void setPath(String var1);

    public void setCommentUrl(String var1);

    public String getValue();
}

