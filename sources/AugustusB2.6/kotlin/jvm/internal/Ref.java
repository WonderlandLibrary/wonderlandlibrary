// 
// Decompiled by Procyon v0.5.36
// 

package kotlin.jvm.internal;

import java.io.Serializable;

public class Ref
{
    public static final class IntRef implements Serializable
    {
        public int element;
        
        @Override
        public final String toString() {
            return String.valueOf(this.element);
        }
    }
    
    public static final class LongRef implements Serializable
    {
        public long element;
        
        @Override
        public final String toString() {
            return String.valueOf(this.element);
        }
    }
}
