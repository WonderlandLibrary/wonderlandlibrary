// 
// Decompiled by Procyon v0.5.36
// 

package javax.vecmath;

import java.io.Serializable;

public class Point2i extends Tuple2i implements Serializable
{
    static final long serialVersionUID = 9208072376494084954L;
    
    public Point2i(final int x, final int y) {
        super(x, y);
    }
    
    public Point2i(final int[] t) {
        super(t);
    }
    
    public Point2i(final Tuple2i t1) {
        super(t1);
    }
    
    public Point2i() {
    }
}
