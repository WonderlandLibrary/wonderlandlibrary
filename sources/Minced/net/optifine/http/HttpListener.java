// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.http;

public interface HttpListener
{
    void finished(final HttpRequest p0, final HttpResponse p1);
    
    void failed(final HttpRequest p0, final Exception p1);
}
