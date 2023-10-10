/*
 * Decompiled with CFR 0.152.
 */
package javassist.bytecode;

import javassist.bytecode.ConstInfo;

final class LongVector {
    static final int ASIZE = 128;
    static final int ABITS = 7;
    static final int VSIZE = 8;
    private ConstInfo[][] objects;
    private int elements;

    public LongVector() {
        this.objects = new ConstInfo[8][];
        this.elements = 0;
    }

    public LongVector(int initialSize) {
        int vsize = (initialSize >> 7 & 0xFFFFFFF8) + 8;
        this.objects = new ConstInfo[vsize][];
        this.elements = 0;
    }

    public int size() {
        return this.elements;
    }

    public int capacity() {
        return this.objects.length * 128;
    }

    public ConstInfo elementAt(int i2) {
        if (i2 < 0 || this.elements <= i2) {
            return null;
        }
        return this.objects[i2 >> 7][i2 & 0x7F];
    }

    public void addElement(ConstInfo value) {
        int nth = this.elements >> 7;
        int offset = this.elements & 0x7F;
        int len = this.objects.length;
        if (nth >= len) {
            ConstInfo[][] newObj = new ConstInfo[len + 8][];
            System.arraycopy(this.objects, 0, newObj, 0, len);
            this.objects = newObj;
        }
        if (this.objects[nth] == null) {
            this.objects[nth] = new ConstInfo[128];
        }
        this.objects[nth][offset] = value;
        ++this.elements;
    }
}

