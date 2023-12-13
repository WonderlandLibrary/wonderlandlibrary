/*
 * Decompiled with CFR 0.152.
 */
package javassist.bytecode;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Map;
import javassist.bytecode.AttributeInfo;
import javassist.bytecode.ByteArray;
import javassist.bytecode.ConstPool;
import javassist.bytecode.Descriptor;

public class LocalVariableAttribute
extends AttributeInfo {
    public static final String tag = "LocalVariableTable";
    public static final String typeTag = "LocalVariableTypeTable";

    public LocalVariableAttribute(ConstPool cp) {
        super(cp, tag, new byte[2]);
        ByteArray.write16bit(0, this.info, 0);
    }

    @Deprecated
    public LocalVariableAttribute(ConstPool cp, String name) {
        super(cp, name, new byte[2]);
        ByteArray.write16bit(0, this.info, 0);
    }

    LocalVariableAttribute(ConstPool cp, int n2, DataInputStream in) throws IOException {
        super(cp, n2, in);
    }

    LocalVariableAttribute(ConstPool cp, String name, byte[] i2) {
        super(cp, name, i2);
    }

    public void addEntry(int startPc, int length, int nameIndex, int descriptorIndex, int index) {
        int size = this.info.length;
        byte[] newInfo = new byte[size + 10];
        ByteArray.write16bit(this.tableLength() + 1, newInfo, 0);
        for (int i2 = 2; i2 < size; ++i2) {
            newInfo[i2] = this.info[i2];
        }
        ByteArray.write16bit(startPc, newInfo, size);
        ByteArray.write16bit(length, newInfo, size + 2);
        ByteArray.write16bit(nameIndex, newInfo, size + 4);
        ByteArray.write16bit(descriptorIndex, newInfo, size + 6);
        ByteArray.write16bit(index, newInfo, size + 8);
        this.info = newInfo;
    }

    @Override
    void renameClass(String oldname, String newname) {
        ConstPool cp = this.getConstPool();
        int n2 = this.tableLength();
        for (int i2 = 0; i2 < n2; ++i2) {
            int pos = i2 * 10 + 2;
            int index = ByteArray.readU16bit(this.info, pos + 6);
            if (index == 0) continue;
            String desc = cp.getUtf8Info(index);
            desc = this.renameEntry(desc, oldname, newname);
            ByteArray.write16bit(cp.addUtf8Info(desc), this.info, pos + 6);
        }
    }

    String renameEntry(String desc, String oldname, String newname) {
        return Descriptor.rename(desc, oldname, newname);
    }

    @Override
    void renameClass(Map<String, String> classnames) {
        ConstPool cp = this.getConstPool();
        int n2 = this.tableLength();
        for (int i2 = 0; i2 < n2; ++i2) {
            int pos = i2 * 10 + 2;
            int index = ByteArray.readU16bit(this.info, pos + 6);
            if (index == 0) continue;
            String desc = cp.getUtf8Info(index);
            desc = this.renameEntry(desc, classnames);
            ByteArray.write16bit(cp.addUtf8Info(desc), this.info, pos + 6);
        }
    }

    String renameEntry(String desc, Map<String, String> classnames) {
        return Descriptor.rename(desc, classnames);
    }

    public void shiftIndex(int lessThan, int delta) {
        int size = this.info.length;
        for (int i2 = 2; i2 < size; i2 += 10) {
            int org = ByteArray.readU16bit(this.info, i2 + 8);
            if (org < lessThan) continue;
            ByteArray.write16bit(org + delta, this.info, i2 + 8);
        }
    }

    public int tableLength() {
        return ByteArray.readU16bit(this.info, 0);
    }

    public int startPc(int i2) {
        return ByteArray.readU16bit(this.info, i2 * 10 + 2);
    }

    public int codeLength(int i2) {
        return ByteArray.readU16bit(this.info, i2 * 10 + 4);
    }

    void shiftPc(int where, int gapLength, boolean exclusive) {
        int n2 = this.tableLength();
        for (int i2 = 0; i2 < n2; ++i2) {
            int pos = i2 * 10 + 2;
            int pc = ByteArray.readU16bit(this.info, pos);
            int len = ByteArray.readU16bit(this.info, pos + 2);
            if (pc > where || exclusive && pc == where && pc != 0) {
                ByteArray.write16bit(pc + gapLength, this.info, pos);
                continue;
            }
            if (pc + len <= where && (!exclusive || pc + len != where)) continue;
            ByteArray.write16bit(len + gapLength, this.info, pos + 2);
        }
    }

    public int nameIndex(int i2) {
        return ByteArray.readU16bit(this.info, i2 * 10 + 6);
    }

    public String variableName(int i2) {
        return this.getConstPool().getUtf8Info(this.nameIndex(i2));
    }

    public int descriptorIndex(int i2) {
        return ByteArray.readU16bit(this.info, i2 * 10 + 8);
    }

    public int signatureIndex(int i2) {
        return this.descriptorIndex(i2);
    }

    public String descriptor(int i2) {
        return this.getConstPool().getUtf8Info(this.descriptorIndex(i2));
    }

    public String signature(int i2) {
        return this.descriptor(i2);
    }

    public int index(int i2) {
        return ByteArray.readU16bit(this.info, i2 * 10 + 10);
    }

    @Override
    public AttributeInfo copy(ConstPool newCp, Map<String, String> classnames) {
        byte[] src = this.get();
        byte[] dest = new byte[src.length];
        ConstPool cp = this.getConstPool();
        LocalVariableAttribute attr = this.makeThisAttr(newCp, dest);
        int n2 = ByteArray.readU16bit(src, 0);
        ByteArray.write16bit(n2, dest, 0);
        int j2 = 2;
        for (int i2 = 0; i2 < n2; ++i2) {
            int start = ByteArray.readU16bit(src, j2);
            int len = ByteArray.readU16bit(src, j2 + 2);
            int name = ByteArray.readU16bit(src, j2 + 4);
            int type = ByteArray.readU16bit(src, j2 + 6);
            int index = ByteArray.readU16bit(src, j2 + 8);
            ByteArray.write16bit(start, dest, j2);
            ByteArray.write16bit(len, dest, j2 + 2);
            if (name != 0) {
                name = cp.copy(name, newCp, null);
            }
            ByteArray.write16bit(name, dest, j2 + 4);
            if (type != 0) {
                String sig = cp.getUtf8Info(type);
                sig = Descriptor.rename(sig, classnames);
                type = newCp.addUtf8Info(sig);
            }
            ByteArray.write16bit(type, dest, j2 + 6);
            ByteArray.write16bit(index, dest, j2 + 8);
            j2 += 10;
        }
        return attr;
    }

    LocalVariableAttribute makeThisAttr(ConstPool cp, byte[] dest) {
        return new LocalVariableAttribute(cp, tag, dest);
    }
}

