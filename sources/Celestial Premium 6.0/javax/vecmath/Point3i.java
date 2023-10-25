/*
 * Decompiled with CFR 0.150.
 */
package javax.vecmath;

import java.io.Serializable;
import javax.vecmath.Tuple3i;

public class Point3i
extends Tuple3i
implements Serializable {
    static final long serialVersionUID = 6149289077348153921L;

    public Point3i(int x, int y, int z) {
        super(x, y, z);
    }

    public Point3i(int[] t) {
        super(t);
    }

    public Point3i(Tuple3i t1) {
        super(t1);
    }

    public Point3i() {
    }
}

