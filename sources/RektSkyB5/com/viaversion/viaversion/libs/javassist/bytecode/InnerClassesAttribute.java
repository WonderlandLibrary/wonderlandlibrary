/*
 * Decompiled with CFR 0.152.
 */
package com.viaversion.viaversion.libs.javassist.bytecode;

import com.viaversion.viaversion.libs.javassist.bytecode.AttributeInfo;
import com.viaversion.viaversion.libs.javassist.bytecode.ByteArray;
import com.viaversion.viaversion.libs.javassist.bytecode.ConstPool;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Map;

public class InnerClassesAttribute
extends AttributeInfo {
    public static final String tag = "InnerClasses";

    InnerClassesAttribute(ConstPool cp, int n2, DataInputStream in) throws IOException {
        super(cp, n2, in);
    }

    private InnerClassesAttribute(ConstPool cp, byte[] info) {
        super(cp, tag, info);
    }

    public InnerClassesAttribute(ConstPool cp) {
        super(cp, tag, new byte[2]);
        ByteArray.write16bit(0, this.get(), 0);
    }

    public int tableLength() {
        return ByteArray.readU16bit(this.get(), 0);
    }

    public int innerClassIndex(int nth) {
        return ByteArray.readU16bit(this.get(), nth * 8 + 2);
    }

    public String innerClass(int nth) {
        int i2 = this.innerClassIndex(nth);
        if (i2 == 0) {
            return null;
        }
        return this.constPool.getClassInfo(i2);
    }

    public void setInnerClassIndex(int nth, int index) {
        ByteArray.write16bit(index, this.get(), nth * 8 + 2);
    }

    public int outerClassIndex(int nth) {
        return ByteArray.readU16bit(this.get(), nth * 8 + 4);
    }

    public String outerClass(int nth) {
        int i2 = this.outerClassIndex(nth);
        if (i2 == 0) {
            return null;
        }
        return this.constPool.getClassInfo(i2);
    }

    public void setOuterClassIndex(int nth, int index) {
        ByteArray.write16bit(index, this.get(), nth * 8 + 4);
    }

    public int innerNameIndex(int nth) {
        return ByteArray.readU16bit(this.get(), nth * 8 + 6);
    }

    public String innerName(int nth) {
        int i2 = this.innerNameIndex(nth);
        if (i2 == 0) {
            return null;
        }
        return this.constPool.getUtf8Info(i2);
    }

    public void setInnerNameIndex(int nth, int index) {
        ByteArray.write16bit(index, this.get(), nth * 8 + 6);
    }

    public int accessFlags(int nth) {
        return ByteArray.readU16bit(this.get(), nth * 8 + 8);
    }

    public void setAccessFlags(int nth, int flags) {
        ByteArray.write16bit(flags, this.get(), nth * 8 + 8);
    }

    public int find(String name) {
        int n2 = this.tableLength();
        for (int i2 = 0; i2 < n2; ++i2) {
            if (!name.equals(this.innerClass(i2))) continue;
            return i2;
        }
        return -1;
    }

    public void append(String inner, String outer, String name, int flags) {
        int i2 = this.constPool.addClassInfo(inner);
        int o2 = this.constPool.addClassInfo(outer);
        int n2 = this.constPool.addUtf8Info(name);
        this.append(i2, o2, n2, flags);
    }

    public void append(int inner, int outer, int name, int flags) {
        byte[] data = this.get();
        int len = data.length;
        byte[] newData = new byte[len + 8];
        for (int i2 = 2; i2 < len; ++i2) {
            newData[i2] = data[i2];
        }
        int n2 = ByteArray.readU16bit(data, 0);
        ByteArray.write16bit(n2 + 1, newData, 0);
        ByteArray.write16bit(inner, newData, len);
        ByteArray.write16bit(outer, newData, len + 2);
        ByteArray.write16bit(name, newData, len + 4);
        ByteArray.write16bit(flags, newData, len + 6);
        this.set(newData);
    }

    public int remove(int nth) {
        byte[] data = this.get();
        int len = data.length;
        if (len < 10) {
            return 0;
        }
        int n2 = ByteArray.readU16bit(data, 0);
        int nthPos = 2 + nth * 8;
        if (n2 <= nth) {
            return n2;
        }
        byte[] newData = new byte[len - 8];
        ByteArray.write16bit(n2 - 1, newData, 0);
        int i2 = 2;
        int j2 = 2;
        while (i2 < len) {
            if (i2 == nthPos) {
                i2 += 8;
                continue;
            }
            newData[j2++] = data[i2++];
        }
        this.set(newData);
        return n2 - 1;
    }

    @Override
    public AttributeInfo copy(ConstPool newCp, Map<String, String> classnames) {
        byte[] src = this.get();
        byte[] dest = new byte[src.length];
        ConstPool cp = this.getConstPool();
        InnerClassesAttribute attr = new InnerClassesAttribute(newCp, dest);
        int n2 = ByteArray.readU16bit(src, 0);
        ByteArray.write16bit(n2, dest, 0);
        int j2 = 2;
        for (int i2 = 0; i2 < n2; ++i2) {
            int innerClass = ByteArray.readU16bit(src, j2);
            int outerClass = ByteArray.readU16bit(src, j2 + 2);
            int innerName = ByteArray.readU16bit(src, j2 + 4);
            int innerAccess = ByteArray.readU16bit(src, j2 + 6);
            if (innerClass != 0) {
                innerClass = cp.copy(innerClass, newCp, classnames);
            }
            ByteArray.write16bit(innerClass, dest, j2);
            if (outerClass != 0) {
                outerClass = cp.copy(outerClass, newCp, classnames);
            }
            ByteArray.write16bit(outerClass, dest, j2 + 2);
            if (innerName != 0) {
                innerName = cp.copy(innerName, newCp, classnames);
            }
            ByteArray.write16bit(innerName, dest, j2 + 4);
            ByteArray.write16bit(innerAccess, dest, j2 + 6);
            j2 += 8;
        }
        return attr;
    }
}

