// 
// Decompiled by Procyon v0.6.0
// 

package net.optifine.reflect;

import java.util.Iterator;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

public class ReflectorResolver
{
    private static final List<IResolvable> RESOLVABLES;
    private static boolean resolved;
    
    static {
        RESOLVABLES = Collections.synchronizedList(new ArrayList<IResolvable>());
        ReflectorResolver.resolved = false;
    }
    
    protected static void register(final IResolvable resolvable) {
        if (!ReflectorResolver.resolved) {
            ReflectorResolver.RESOLVABLES.add(resolvable);
        }
        else {
            resolvable.resolve();
        }
    }
    
    public static void resolve() {
        if (!ReflectorResolver.resolved) {
            for (final IResolvable iresolvable : ReflectorResolver.RESOLVABLES) {
                iresolvable.resolve();
            }
            ReflectorResolver.resolved = true;
        }
    }
}
