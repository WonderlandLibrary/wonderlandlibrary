/*
 * Decompiled with CFR 0.152.
 */
package javassist.tools;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.PrintWriter;
import javassist.bytecode.ClassFile;
import javassist.bytecode.ClassFilePrinter;

public class Dump {
    private Dump() {
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("Usage: java Dump <class file name>");
            return;
        }
        DataInputStream in = new DataInputStream(new FileInputStream(args[0]));
        ClassFile w2 = new ClassFile(in);
        PrintWriter out = new PrintWriter(System.out, true);
        out.println("*** constant pool ***");
        w2.getConstPool().print(out);
        out.println();
        out.println("*** members ***");
        ClassFilePrinter.print(w2, out);
    }
}

