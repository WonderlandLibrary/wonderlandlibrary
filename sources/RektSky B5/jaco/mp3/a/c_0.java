/*
 * Decompiled with CFR 0.152.
 */
package jaco.mp3.a;

/*
 * Renamed from jaco.mp3.a.C
 */
public abstract class c_0 {
    public abstract void a(int var1, short var2);

    public void a(int n2, float[] fArray) {
        int n3 = 0;
        while (n3 < 32) {
            float f2;
            float f3 = fArray[n3++];
            short s2 = f2 > 32767.0f ? (short)Short.MAX_VALUE : (f3 < -32768.0f ? (short)Short.MIN_VALUE : (short)f3);
            this.a(n2, s2);
        }
    }

    public abstract void c();
}

