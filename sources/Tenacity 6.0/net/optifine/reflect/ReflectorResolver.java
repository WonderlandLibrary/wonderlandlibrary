// 
// Decompiled by Procyon v0.5.36
// 

package net.optifine.reflect;

import java.util.Collections;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ReflectorResolver
{
    private static final List<IResolvable> RESOLVABLES;
    private static boolean resolved;
    
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
    
    static {
        RESOLVABLES = Collections.synchronizedList(new ArrayList<IResolvable>());
        ReflectorResolver.resolved = false;
    }
}
