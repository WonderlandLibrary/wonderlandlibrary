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

public class BootstrapMethodsAttribute
extends AttributeInfo {
    public static final String tag = "BootstrapMethods";

    BootstrapMethodsAttribute(ConstPool cp, int n2, DataInputStream in) throws IOException {
        super(cp, n2, in);
    }

    public BootstrapMethodsAttribute(ConstPool cp, BootstrapMethod[] methods) {
        super(cp, tag);
        int size = 2;
        for (int i2 = 0; i2 < methods.length; ++i2) {
            size += 4 + methods[i2].arguments.length * 2;
        }
        byte[] data = new byte[size];
        ByteArray.write16bit(methods.length, data, 0);
        int pos = 2;
        for (int i3 = 0; i3 < methods.length; ++i3) {
            ByteArray.write16bit(methods[i3].methodRef, data, pos);
            ByteArray.write16bit(methods[i3].arguments.length, data, pos + 2);
            int[] args = methods[i3].arguments;
            pos += 4;
            for (int k2 = 0; k2 < args.length; ++k2) {
                ByteArray.write16bit(args[k2], data, pos);
                pos += 2;
            }
        }
        this.set(data);
    }

    public BootstrapMethod[] getMethods() {
        byte[] data = this.get();
        int num = ByteArray.readU16bit(data, 0);
        BootstrapMethod[] methods = new BootstrapMethod[num];
        int pos = 2;
        for (int i2 = 0; i2 < num; ++i2) {
            int ref = ByteArray.readU16bit(data, pos);
            int len = ByteArray.readU16bit(data, pos + 2);
            int[] args = new int[len];
            pos += 4;
            for (int k2 = 0; k2 < len; ++k2) {
                args[k2] = ByteArray.readU16bit(data, pos);
                pos += 2;
            }
            methods[i2] = new BootstrapMethod(ref, args);
        }
        return methods;
    }

    @Override
    public AttributeInfo copy(ConstPool newCp, Map<String, String> classnames) {
        BootstrapMethod[] methods = this.getMethods();
        ConstPool thisCp = this.getConstPool();
        for (int i2 = 0; i2 < methods.length; ++i2) {
            BootstrapMethod m2 = methods[i2];
            m2.methodRef = thisCp.copy(m2.methodRef, newCp, classnames);
            for (int k2 = 0; k2 < m2.arguments.length; ++k2) {
                m2.arguments[k2] = thisCp.copy(m2.arguments[k2], newCp, classnames);
            }
        }
        return new BootstrapMethodsAttribute(newCp, methods);
    }

    public static class BootstrapMethod {
        public int methodRef;
        public int[] arguments;

        public BootstrapMethod(int method, int[] args) {
            this.methodRef = method;
            this.arguments = args;
        }
    }
}

