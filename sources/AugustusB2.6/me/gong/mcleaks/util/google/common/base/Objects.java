// 
// Decompiled by Procyon v0.5.36
// 

package me.gong.mcleaks.util.google.common.base;

import java.util.Arrays;
import javax.annotation.Nullable;
import me.gong.mcleaks.util.google.common.annotations.GwtCompatible;

@GwtCompatible
public final class Objects extends ExtraObjectsMethodsForWeb
{
    private Objects() {
    }
    
    public static boolean equal(@Nullable final Object a, @Nullable final Object b) {
        return a == b || (a != null && a.equals(b));
    }
    
    public static int hashCode(@Nullable final Object... objects) {
        return Arrays.hashCode(objects);
    }
}
