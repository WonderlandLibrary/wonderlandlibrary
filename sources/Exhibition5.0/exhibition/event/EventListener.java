// 
// Decompiled by Procyon v0.5.30
// 

package exhibition.event;

public interface EventListener<E extends Event>
{
    void onEvent(final E p0);
}
