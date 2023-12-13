/*
 * Decompiled with CFR 0.152.
 */
package com.sun.jna;

import com.sun.jna.Function;
import com.sun.jna.Memory;
import com.sun.jna.NativeString;
import com.sun.jna.Pointer;
import com.sun.jna.WString;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StringArray
extends Memory
implements Function.PostCallRead {
    private boolean wide;
    private List natives = new ArrayList();
    private Object[] original;

    public StringArray(String[] strings) {
        this(strings, false);
    }

    public StringArray(String[] strings, boolean wide) {
        this((Object[])strings, wide);
    }

    public StringArray(WString[] strings) {
        this(strings, true);
    }

    private StringArray(Object[] strings, boolean wide) {
        super((strings.length + 1) * Pointer.SIZE);
        this.original = strings;
        this.wide = wide;
        for (int i2 = 0; i2 < strings.length; ++i2) {
            Pointer p2 = null;
            if (strings[i2] != null) {
                NativeString ns = new NativeString(strings[i2].toString(), wide);
                this.natives.add(ns);
                p2 = ns.getPointer();
            }
            this.setPointer(Pointer.SIZE * i2, p2);
        }
        this.setPointer(Pointer.SIZE * strings.length, null);
    }

    public void read() {
        boolean returnWide = this.original instanceof WString[];
        for (int si = 0; si < this.original.length; ++si) {
            Pointer p2 = this.getPointer(si * Pointer.SIZE);
            CharSequence s2 = null;
            if (p2 != null) {
                s2 = p2.getString(0L, this.wide);
                if (returnWide) {
                    s2 = new WString((String)s2);
                }
            }
            this.original[si] = s2;
        }
    }

    public String toString() {
        String s2 = this.wide ? "const wchar_t*[]" : "const char*[]";
        s2 = s2 + Arrays.asList(this.original);
        return s2;
    }
}

