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

public class LineNumberAttribute
extends AttributeInfo {
    public static final String tag = "LineNumberTable";

    LineNumberAttribute(ConstPool cp, int n2, DataInputStream in) throws IOException {
        super(cp, n2, in);
    }

    private LineNumberAttribute(ConstPool cp, byte[] i2) {
        super(cp, tag, i2);
    }

    public int tableLength() {
        return ByteArray.readU16bit(this.info, 0);
    }

    public int startPc(int i2) {
        return ByteArray.readU16bit(this.info, i2 * 4 + 2);
    }

    public int lineNumber(int i2) {
        return ByteArray.readU16bit(this.info, i2 * 4 + 4);
    }

    public int toLineNumber(int pc) {
        int i2;
        int n2 = this.tableLength();
        for (i2 = 0; i2 < n2; ++i2) {
            if (pc >= this.startPc(i2)) continue;
            if (i2 != 0) break;
            return this.lineNumber(0);
        }
        return this.lineNumber(i2 - 1);
    }

    public int toStartPc(int line) {
        int n2 = this.tableLength();
        for (int i2 = 0; i2 < n2; ++i2) {
            if (line != this.lineNumber(i2)) continue;
            return this.startPc(i2);
        }
        return -1;
    }

    public Pc toNearPc(int line) {
        int n2 = this.tableLength();
        int nearPc = 0;
        int distance = 0;
        if (n2 > 0) {
            distance = this.lineNumber(0) - line;
            nearPc = this.startPc(0);
        }
        for (int i2 = 1; i2 < n2; ++i2) {
            int d2 = this.lineNumber(i2) - line;
            if ((d2 >= 0 || d2 <= distance) && (d2 < 0 || d2 >= distance && distance >= 0)) continue;
            distance = d2;
            nearPc = this.startPc(i2);
        }
        Pc res = new Pc();
        res.index = nearPc;
        res.line = line + distance;
        return res;
    }

    @Override
    public AttributeInfo copy(ConstPool newCp, Map<String, String> classnames) {
        byte[] src = this.info;
        int num = src.length;
        byte[] dest = new byte[num];
        for (int i2 = 0; i2 < num; ++i2) {
            dest[i2] = src[i2];
        }
        LineNumberAttribute attr = new LineNumberAttribute(newCp, dest);
        return attr;
    }

    void shiftPc(int where, int gapLength, boolean exclusive) {
        int n2 = this.tableLength();
        for (int i2 = 0; i2 < n2; ++i2) {
            int pos = i2 * 4 + 2;
            int pc = ByteArray.readU16bit(this.info, pos);
            if (pc <= where && (!exclusive || pc != where)) continue;
            ByteArray.write16bit(pc + gapLength, this.info, pos);
        }
    }

    public static class Pc {
        public int index;
        public int line;
    }
}

