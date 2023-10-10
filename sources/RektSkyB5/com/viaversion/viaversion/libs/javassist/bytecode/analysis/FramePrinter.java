/*
 * Decompiled with CFR 0.152.
 */
package com.viaversion.viaversion.libs.javassist.bytecode.analysis;

import com.viaversion.viaversion.libs.javassist.CtClass;
import com.viaversion.viaversion.libs.javassist.CtMethod;
import com.viaversion.viaversion.libs.javassist.Modifier;
import com.viaversion.viaversion.libs.javassist.NotFoundException;
import com.viaversion.viaversion.libs.javassist.bytecode.BadBytecode;
import com.viaversion.viaversion.libs.javassist.bytecode.CodeAttribute;
import com.viaversion.viaversion.libs.javassist.bytecode.CodeIterator;
import com.viaversion.viaversion.libs.javassist.bytecode.ConstPool;
import com.viaversion.viaversion.libs.javassist.bytecode.Descriptor;
import com.viaversion.viaversion.libs.javassist.bytecode.InstructionPrinter;
import com.viaversion.viaversion.libs.javassist.bytecode.MethodInfo;
import com.viaversion.viaversion.libs.javassist.bytecode.analysis.Analyzer;
import com.viaversion.viaversion.libs.javassist.bytecode.analysis.Frame;
import com.viaversion.viaversion.libs.javassist.bytecode.analysis.Type;
import java.io.PrintStream;

public final class FramePrinter {
    private final PrintStream stream;

    public FramePrinter(PrintStream stream) {
        this.stream = stream;
    }

    public static void print(CtClass clazz, PrintStream stream) {
        new FramePrinter(stream).print(clazz);
    }

    public void print(CtClass clazz) {
        CtMethod[] methods = clazz.getDeclaredMethods();
        for (int i2 = 0; i2 < methods.length; ++i2) {
            this.print(methods[i2]);
        }
    }

    private String getMethodString(CtMethod method) {
        try {
            return Modifier.toString(method.getModifiers()) + " " + method.getReturnType().getName() + " " + method.getName() + Descriptor.toString(method.getSignature()) + ";";
        }
        catch (NotFoundException e2) {
            throw new RuntimeException(e2);
        }
    }

    public void print(CtMethod method) {
        Frame[] frames;
        this.stream.println("\n" + this.getMethodString(method));
        MethodInfo info = method.getMethodInfo2();
        ConstPool pool = info.getConstPool();
        CodeAttribute code = info.getCodeAttribute();
        if (code == null) {
            return;
        }
        try {
            frames = new Analyzer().analyze(method.getDeclaringClass(), info);
        }
        catch (BadBytecode e2) {
            throw new RuntimeException(e2);
        }
        int spacing = String.valueOf(code.getCodeLength()).length();
        CodeIterator iterator = code.iterator();
        while (iterator.hasNext()) {
            int pos;
            try {
                pos = iterator.next();
            }
            catch (BadBytecode e3) {
                throw new RuntimeException(e3);
            }
            this.stream.println(pos + ": " + InstructionPrinter.instructionString(iterator, pos, pool));
            this.addSpacing(spacing + 3);
            Frame frame = frames[pos];
            if (frame == null) {
                this.stream.println("--DEAD CODE--");
                continue;
            }
            this.printStack(frame);
            this.addSpacing(spacing + 3);
            this.printLocals(frame);
        }
    }

    private void printStack(Frame frame) {
        this.stream.print("stack [");
        int top = frame.getTopIndex();
        for (int i2 = 0; i2 <= top; ++i2) {
            if (i2 > 0) {
                this.stream.print(", ");
            }
            Type type = frame.getStack(i2);
            this.stream.print(type);
        }
        this.stream.println("]");
    }

    private void printLocals(Frame frame) {
        this.stream.print("locals [");
        int length = frame.localsLength();
        for (int i2 = 0; i2 < length; ++i2) {
            Type type;
            if (i2 > 0) {
                this.stream.print(", ");
            }
            this.stream.print((type = frame.getLocal(i2)) == null ? "empty" : type.toString());
        }
        this.stream.println("]");
    }

    private void addSpacing(int count) {
        while (count-- > 0) {
            this.stream.print(' ');
        }
    }
}

