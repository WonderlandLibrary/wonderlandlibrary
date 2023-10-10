/*
 * Decompiled with CFR 0.152.
 */
package javassist.bytecode;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import javassist.bytecode.ConstInfo;
import javassist.bytecode.ConstPool;

class ConstInfoPadding
extends ConstInfo {
    public ConstInfoPadding(int i2) {
        super(i2);
    }

    @Override
    public int getTag() {
        return 0;
    }

    @Override
    public int copy(ConstPool src, ConstPool dest, Map<String, String> map) {
        return dest.addConstInfoPadding();
    }

    @Override
    public void write(DataOutputStream out) throws IOException {
    }

    @Override
    public void print(PrintWriter out) {
        out.println("padding");
    }
}

