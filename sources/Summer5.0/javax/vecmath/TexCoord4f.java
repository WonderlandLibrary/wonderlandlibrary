// 
// Decompiled by Procyon v0.5.36
// 

package javax.vecmath;

import java.io.Serializable;

public class TexCoord4f extends Tuple4f implements Serializable
{
    static final long serialVersionUID = -3517736544731446513L;
    
    public TexCoord4f(final float x, final float y, final float z, final float w) {
        super(x, y, z, w);
    }
    
    public TexCoord4f(final float[] v) {
        super(v);
    }
    
    public TexCoord4f(final TexCoord4f v1) {
        super(v1);
    }
    
    public TexCoord4f(final Tuple4f t1) {
        super(t1);
    }
    
    public TexCoord4f(final Tuple4d t1) {
        super(t1);
    }
    
    public TexCoord4f() {
    }
}
