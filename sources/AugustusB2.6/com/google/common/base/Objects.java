// 
// Decompiled by Procyon v0.5.36
// 

package com.google.common.base;

import java.util.Arrays;
import javax.annotation.CheckForNull;
import com.google.common.annotations.GwtCompatible;

@ElementTypesAreNonnullByDefault
@GwtCompatible
public final class Objects extends ExtraObjectsMethodsForWeb
{
    private Objects() {
    }
    
    public static boolean equal(@CheckForNull final Object a, @CheckForNull final Object b) {
        return a == b || (a != null && a.equals(b));
    }
    
    public static int hashCode(@CheckForNull final Object... objects) {
        return Arrays.hashCode(objects);
    }
}
