// 
// Decompiled by Procyon v0.5.36
// 

package javax.vecmath;

import java.io.Serializable;

public class TexCoord3f extends Tuple3f implements Serializable
{
    static final long serialVersionUID = -3517736544731446513L;
    
    public TexCoord3f(final float x, final float y, final float z) {
        super(x, y, z);
    }
    
    public TexCoord3f(final float[] v) {
        super(v);
    }
    
    public TexCoord3f(final TexCoord3f v1) {
        super(v1);
    }
    
    public TexCoord3f(final Tuple3f t1) {
        super(t1);
    }
    
    public TexCoord3f(final Tuple3d t1) {
        super(t1);
    }
    
    public TexCoord3f() {
    }
}
