/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.util;

public class Vec4b {
    private byte field_176114_d;
    private byte field_176115_b;
    private byte field_176116_c;
    private byte field_176117_a;

    public Vec4b(Vec4b vec4b) {
        this.field_176117_a = vec4b.field_176117_a;
        this.field_176115_b = vec4b.field_176115_b;
        this.field_176116_c = vec4b.field_176116_c;
        this.field_176114_d = vec4b.field_176114_d;
    }

    public Vec4b(byte by, byte by2, byte by3, byte by4) {
        this.field_176117_a = by;
        this.field_176115_b = by2;
        this.field_176116_c = by3;
        this.field_176114_d = by4;
    }

    public byte func_176110_a() {
        return this.field_176117_a;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Vec4b)) {
            return false;
        }
        Vec4b vec4b = (Vec4b)object;
        return this.field_176117_a != vec4b.field_176117_a ? false : (this.field_176114_d != vec4b.field_176114_d ? false : (this.field_176115_b != vec4b.field_176115_b ? false : this.field_176116_c == vec4b.field_176116_c));
    }

    public int hashCode() {
        int n = this.field_176117_a;
        n = 31 * n + this.field_176115_b;
        n = 31 * n + this.field_176116_c;
        n = 31 * n + this.field_176114_d;
        return n;
    }

    public byte func_176112_b() {
        return this.field_176115_b;
    }

    public byte func_176113_c() {
        return this.field_176116_c;
    }

    public byte func_176111_d() {
        return this.field_176114_d;
    }
}

