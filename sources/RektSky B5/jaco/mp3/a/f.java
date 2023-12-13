/*
 * Decompiled with CFR 0.152.
 */
package jaco.mp3.a;

public final class f {
    private final float[] a = new float[32];

    static {
        new f();
    }

    /*
     * Unable to fully structure code
     * Could not resolve type clashes
     */
    public final void a(f var1_1) {
        block6: {
            if (var1_1 == this) break block6;
            var1_1 = var1_1.a;
            var4_2 = this;
            var2_4 = 0;
            while (var2_4 < 32) {
                var4_2.a[var2_4] = 0.0f;
                ++var2_4;
            }
            var2_4 = ((Object)var1_1).length > 32 ? 32 : ((Object)var1_1).length;
            var3_5 = 0;
            while (var3_5 < var2_4) {
                var4_3 = var1_1[var3_5];
                if (var4_3 == -Infinityf) ** GOTO lbl-1000
                if (var4_3 > 1.0f) {
                    v0 /* !! */  = 1.0f;
                } else if (var4_3 < -1.0f) {
                    v0 /* !! */  = -1.0f;
                } else lbl-1000:
                // 2 sources

                {
                    v0 /* !! */  = var4_3;
                }
                this.a[var3_5] = (float)v0 /* !! */ ;
                ++var3_5;
            }
        }
    }

    final float[] a() {
        float[] fArray = new float[32];
        int n2 = 0;
        while (n2 < 32) {
            float f2 = this.a[n2];
            fArray[n2] = f2 == Float.NEGATIVE_INFINITY ? 0.0f : (f2 = (float)Math.pow(2.0, f2));
            ++n2;
        }
        return fArray;
    }
}

