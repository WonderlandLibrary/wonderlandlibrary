/*
 * Decompiled with CFR 0.152.
 */
package jaco.mp3.a;

import jaco.mp3.a.f;

public final class g
implements Cloneable {
    private f a = new f();

    public final Object clone() {
        try {
            return super.clone();
        }
        catch (CloneNotSupportedException cloneNotSupportedException) {
            throw new InternalError(this + ": " + cloneNotSupportedException);
        }
    }

    public final f a() {
        return this.a;
    }
}

